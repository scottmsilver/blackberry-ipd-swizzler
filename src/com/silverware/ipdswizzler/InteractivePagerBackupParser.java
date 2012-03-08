/**
 * Copyright 2012 Google, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package com.silverware.ipdswizzler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class InteractivePagerBackupParser {
  public InteractivePagerBackup parse(String fileName) throws IOException {
    InteractivePagerBackup pagerBackup = new InteractivePagerBackup();
    BinaryReadHelper expect = new BinaryReadHelper(
        new FileInputStream(fileName));

    // Read the header.
    expect.matchString("Inter@ctive Pager Backup/Restore File", "header");
    expect.matchByte((byte) 0xa, "linefeed");
    expect.matchByte((byte) 0x2, "databaseVersion");

    int databaseCount = expect.getInt16(true, "databaseCount");

    expect.matchByte((byte) 0, "databaseNameSectionSeparator");

    // Read the database names section.
    slurpDatabaseNames(pagerBackup, expect, databaseCount);

    // Read the individual databases until there are no more bytes to read.
    while (expect.getBytesRead() < new File(fileName).length()) {
      slurpDatabase(pagerBackup, expect);
    }
    
    return pagerBackup;
  }

  private void slurpDatabaseNames(InteractivePagerBackup pagerBackup,
      BinaryReadHelper expect, int databaseCount) throws IOException {
    for (int databaseNameIndex = 0; databaseNameIndex < databaseCount; databaseNameIndex++) {
      int databaseNameLength = expect.getInt16(false, "databaseNameLength");

      String databaseName = expect.getNullTerminatedString("databaseName", databaseNameLength);
      pagerBackup.addDatabase(databaseNameIndex, databaseName);
    }
    assert (pagerBackup.getDatabaseCount() == databaseCount);
  }

  /*
   * And each database data block is of the form:
   * 
   * <Database ID> 2 bytes Zero-based position in the list of database name
   * blocks <Record length> 4 bytes <Database version> 1 byte
   * <DatabaseRecordHandle> 2 bytes <Record unique ID> 4 bytes <Field length #1>
   * 2 bytes <Field type #1> 1 byte <Field data #1> As long as the field length
   * <Field length #m> 2 bytes <Field type #m> 1 byte <Field data #m> As long as
   * the field length
   */
  private void slurpDatabase(InteractivePagerBackup pagerBackup,
      BinaryReadHelper expect) throws IOException {
    int databaseId = expect.getInt16(false, "databaseId");
    Database database = pagerBackup.getDatabase(databaseId);

    int recordLength = expect.getInt32(false, "recordLength");
    expect.startCountingBytes();
    expect.getByte("databaseVersion");

    int databaseRecordHandle = expect.getInt16(false, "databaseRecordHandle");
    int databaseRecordUniqueId = expect.getInt32(false,
        "databaseRecordUniqueId");

    DatabaseRecord record = database.addRecord(databaseRecordHandle,
        databaseRecordUniqueId);

    int bytesRead = expect.measureBytesRead();
    while (bytesRead < recordLength) {
      int fieldLength = expect.getInt16(false, "fieldLength");
      byte fieldType = expect.getByte("fieldType");
      byte[] fieldData = expect.getBytes(fieldLength, "fieldData");
      bytesRead = expect.measureBytesRead();

      record.addField(fieldLength, fieldType, fieldData);

      assert (bytesRead <= recordLength);
    }

    bytesRead = expect.stopCountingBytes();
  }
}