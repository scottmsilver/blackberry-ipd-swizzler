package com.silverware;

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
