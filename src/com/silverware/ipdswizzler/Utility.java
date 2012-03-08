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
