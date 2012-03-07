package com.silverware;

public class Utility {
  static final int BYTES_PER_ROW = 64;
  
  public static String hexToString(byte[] data, int indent) {
    StringBuffer buffer = new StringBuffer(data.length * 3);
    
    for (int dataIndex = 0; dataIndex < data.length; dataIndex += BYTES_PER_ROW) {
      int bytesToPrint = dataIndex + BYTES_PER_ROW > data.length ? data.length - dataIndex : BYTES_PER_ROW;
      StringBuffer asHexBuffer = new StringBuffer(BYTES_PER_ROW * 2);
      StringBuffer asAsciiBuffer = new StringBuffer(BYTES_PER_ROW);
      for (int printIndex = dataIndex; printIndex < dataIndex + bytesToPrint; printIndex++) {
        byte toPrint = data[printIndex];
        asHexBuffer.append(String.format("%02x", toPrint));
        if ((toPrint >= 32) && (toPrint < 127)) {
          asAsciiBuffer.append((char) toPrint);
        } else {
          asAsciiBuffer.append('.');
        }
      }
      
      for (int indentIndex = 0; indentIndex < indent; indentIndex++) {
        buffer.append(' ');
      }
      
      buffer.append(asHexBuffer);
      buffer.append(" ");
      buffer.append(asAsciiBuffer);
      buffer.append("\n");
    }
    
    return buffer.toString();
  }
}
