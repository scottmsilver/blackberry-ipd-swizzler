package com.silverware;

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
