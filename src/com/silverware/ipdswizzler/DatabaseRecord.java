/*
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
 */
package com.silverware.ipdswizzler;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A record in a Database. Each Database has a set of DatabaseRecordField
 * fields.
 */
public class DatabaseRecord {

  private int handle;
  private int uniqueId;
  HashMap<Byte, DatabaseRecordField> fields = new HashMap<Byte, DatabaseRecordField>();

  public DatabaseRecord(int databaseRecordHandle, int databaseRecordUniqueId) {
    handle = databaseRecordHandle;
    uniqueId = databaseRecordUniqueId;
  }

  public DatabaseRecordField addField(int fieldLength, byte fieldType,
      byte[] fieldData) {
    DatabaseRecordField field = new DatabaseRecordField(fieldLength, fieldType,
        fieldData);

    fields.put(fieldType, field);

    return field;
  }

  public DatabaseRecordField getField(byte fieldType) {
    return fields.get(fieldType);
  }

  @Override
  public String toString() {
    return "DatabaseRecord [handle=" + handle + ", uniqueId=" + uniqueId
        + "]\n" + fields;
  }

  public void dump(PrintWriter printWriter) {
    printWriter.printf("  Record h=%d id=%d\n", handle, uniqueId);
    for (Iterator<DatabaseRecordField> fieldIterator = fields.values()
        .iterator(); fieldIterator.hasNext();) {
      DatabaseRecordField field = fieldIterator.next();
      field.dump(printWriter);
    }
  }
}
