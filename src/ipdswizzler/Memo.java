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
package ipdswizzler;

import java.util.Arrays;

/**
 * A Memo represents a BlackBerry Memo from an IPD file.
 * It's implemented as a wrapper around a DatabaseRecord. Each
 * field in the DatabaseRecord corresponds to an attribute of the Memo.
 * At the moment I make no attempt to cache the results of any conversion process.
 * 
 * @author scottmsilver@gmail.com
 */
public class Memo {
  DatabaseRecord record;
  
  public Memo(DatabaseRecord memoRecord) {
    record = memoRecord;
  }

  public String getTitle() {
    DatabaseRecordField field = record.getField((byte) 1);
    
    if (field != null) {
      return field.getDataAsNullTerminateString();
    } else {
      return null;
    }  
  }

  public String[] getTags() {
    DatabaseRecordField field = record.getField((byte) 4);
    if (field != null) {
      return field.getDataAsNullTerminateString().split(",");
    } else {
      return null;
    }
  }

  @Override
  public String toString() {
    return String.format("title=%s, tags=%s, content=%s", getTitle(), Arrays.toString(getTags()), getContent());
  }

  public String getContent() {
   DatabaseRecordField field = record.getField((byte) 2);
   if (field == null) {
     return null;
   } else {
     return field.getDataAsNullTerminateString();
   }
  }
}
