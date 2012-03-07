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
