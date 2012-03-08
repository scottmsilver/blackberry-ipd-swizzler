package com.silverware.ipdswizzler;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A single Database in an InterativePagerBackup.
 */
public class Database {
  HashMap<Integer, DatabaseRecord> records;
  private String name;
  private int id;

  public Database(int databaseId, String databaseName) {
    id = databaseId;
    records = new HashMap<Integer, DatabaseRecord>();
    name = databaseName;
  }

  public HashMap<Integer, DatabaseRecord> getRecords() {
    return records;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public DatabaseRecord addRecord(int databaseRecordHandle,
      int databaseRecordUniqueId) {
    DatabaseRecord databaseRecord = new DatabaseRecord(databaseRecordHandle,
        databaseRecordUniqueId);

    if (records.get(databaseRecordHandle) != null) {
      System.out.println("hi");
    }
    records.put(databaseRecordHandle, databaseRecord);

    return databaseRecord;
  }

  public void dump(PrintWriter printWriter) {
    printWriter.printf("%s: [%d, records=%d])\n", name, id, records.size());
    for (Iterator<DatabaseRecord> recordIterator = records.values().iterator(); recordIterator
        .hasNext();) {
      DatabaseRecord record = recordIterator.next();

      record.dump(printWriter);
    }
  }

  @Override
  public String toString() {
    return "Database [records=" + records + ", name=" + name + ", id=" + id
        + ", count=" + records.size() + "]";
  }
}
