package com.silverware;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A simple tuple of type, length and binary data.
 */
public class DatabaseRecordField {
  private byte type;
  private byte[] data;
  private int length;
 
  public DatabaseRecordField(int fieldLength, byte fieldType, byte[] fieldData) {
    length = fieldLength;
    type = fieldType;
    data = fieldData;
  }

  @Override
  public String toString() {
    return "DatabaseRecordField [type=" + type + "\n"
        + Utility.hexToString(data, 0) + "\n";
  }

  public void dump(PrintWriter printWriter) {
    printWriter.printf("    %2x (%4x)", type, length);
    printWriter.print(Utility.hexToString(data, 4));
  }

  String getDataAsNullTerminateString() {
    BinaryReadHelper helper = new BinaryReadHelper(new ByteArrayInputStream(data));
    
    try {
      return helper.getNullTerminatedString("fieldValue", length);
    } catch (IOException e) {
      return null;
    }
  }
  
  public byte[] getData() {
    return data;
  }

  public int getLength() {
    return length;
  }
}
