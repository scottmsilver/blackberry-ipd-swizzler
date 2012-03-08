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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

class BinaryReadHelper {
  DataInputStream stream;

  /*
   * Number of bytes read since last call to startCountingBytes or since
   * construction.
   */
  private int counter;

  /*
   * Number of bytes read from this instance.
   */
  private int offset;

  public BinaryReadHelper(InputStream input) {
    stream = new DataInputStream(input);
  }

  void matchString(String matchValue, String fieldName) throws IOException {
    byte[] matchValueBytes = matchValue.getBytes();
    byte[] fieldValue = new byte[matchValueBytes.length];

    stream.read(fieldValue, 0, fieldValue.length);

    assert (Arrays.equals(matchValueBytes, fieldValue));

    counter += fieldValue.length;
    offset += fieldValue.length;
  }

  void matchByte(byte match, String fieldName) throws IOException {
    byte fieldByte = stream.readByte();
    assert (match == fieldByte);

    counter += 1;
    offset += 1;
  }

  // length includes \0
  String getNullTerminatedString(String fieldName, int length)
      throws IOException {
    byte[] field = new byte[length];

    int bytesRead = stream.read(field, 0, length);
    assert (bytesRead == length);

    counter += length;
    offset += length;

    // FIXME(ssilver): Wrong charset
    String string = new String(field, 0, length - 1, Charset.defaultCharset());
    debugOutput(fieldName, string, length);

    return string;
  }

  private void debugOutput(String fieldName, String fieldValue, int length) {
    // System.out.format("field: %s at %x: %s\n", fieldName, offset -
    // length, fieldValue);
  }

  int getInt16(boolean bigEndian, String fieldName) throws IOException {
    int field = stream.readUnsignedShort();
    int returnField = field;

    if (!bigEndian) {
      returnField = ((field & 0xff) << 8) | (field >> 8);
    }

    counter += 2;
    offset += 2;
    debugOutput(fieldName, Integer.toString(returnField), 2);
    return returnField;
  }

  public int getInt32(boolean bigEndian, String fieldName) throws IOException {
    int field = stream.readInt();
    int returnField = field;

    if (!bigEndian) {
      returnField = (field >>> 24) | (field << 24) | ((field << 8) & 0xff0000)
          | ((field >> 8) & 0xff00);
    }

    counter += 4;
    offset += 4;
    debugOutput(fieldName, Integer.toString(returnField), 4);

    return returnField;
  }

  public byte getByte(String fieldName) throws IOException {
    counter += 1;
    offset += 1;

    byte returnValue = stream.readByte();

    debugOutput(fieldName, Byte.toString(returnValue), 1);
    return returnValue;
  }

  public void startCountingBytes() {
    assert (counter == 0);
    counter = 0;
  }

  public int stopCountingBytes() {
    counter = 0;
    return counter;
  }

  public int measureBytesRead() {
    return counter;
  }

  public byte[] getBytes(int fieldLength, String fieldName) throws IOException {
    byte[] fieldBytes = new byte[fieldLength];

    int bytesRead = stream.read(fieldBytes, 0, fieldLength);
    assert (bytesRead == fieldLength);

    counter += fieldLength;
    offset += fieldLength;
    debugOutput(fieldName, Utility.hexToString(fieldBytes, 0), fieldLength);

    return fieldBytes;
  }

  public long getBytesRead() {
    return offset;
  }
}