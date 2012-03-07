package com.silverware;

import java.util.Iterator;

/**
 * This represents all the Memos in a BlackberyIPD file.
 * Memos provides a single method {@link #getAll()} which build a wrapper
 * Iterator around the Iterator provided by a Database over the DatabaseRecords.
 * The wrapper builds the wrapper Memos around the DatabaseRecords so they are
 * useful!
 * 
 * @author scottmsilver@gmail.com
 *
 */
public class Memos {
  Database database;

  public Memos(Database theDatabase) {
    database = theDatabase;
  }

  public Iterator<Memo> getAll() {
    return new Iterator<Memo>() {
      Iterator<DatabaseRecord> recordIterator = database.getRecords().values()
          .iterator();

      @Override
      public boolean hasNext() {
        return recordIterator.hasNext();
      }

      @Override
      public Memo next() {
        DatabaseRecord memoRecord = recordIterator.next();
        
        return new Memo(memoRecord);
      }

      @Override
      public void remove() {
        recordIterator.remove();
      }
    };
  }

}
