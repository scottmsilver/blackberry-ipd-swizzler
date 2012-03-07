package com.silverware;

import java.util.HashMap;
import java.util.Iterator;

public class InteractivePagerBackup {
  HashMap<Integer, Database> databases = new HashMap<Integer, Database>();
  HashMap<String, Integer> databaseNameToDatabaseIdMap = new HashMap<String, Integer>();
  
  public void addDatabase(int databaseId, String databaseName) {
    Database database = new Database(databaseId, databaseName);
    assert(!databases.containsKey(new Integer(databaseId)));
    databases.put(new Integer(databaseId), database);
    databaseNameToDatabaseIdMap.put(databaseName, new Integer(databaseId));
  }

  public int getDatabaseCount() {
    return databases.size();
  }

  public Iterator<Database> getDatabases() {
    return databases.values().iterator();
  }
  
  public Database getDatabase(String databaseName) {
    return getDatabase(databaseNameToDatabaseIdMap.get(databaseName).intValue());
  }
  
  public Database getDatabase(int databaseId) {
    return databases.get(new Integer(databaseId));
  }

  @Override
  public String toString() {
    return "InteractivePagerBackup [databases=" + databases + "]";
  }

  public Memos getMemos() {
    return new Memos(getDatabase("Memos"));
  }
}
