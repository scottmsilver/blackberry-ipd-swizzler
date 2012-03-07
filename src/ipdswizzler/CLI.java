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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

public class CLI {

  /**
   * @param args
   */
  public static void main(String[] args) {
    InteractivePagerBackupParser parser = new InteractivePagerBackupParser();
    String command = args[1];
    String fileName = args[0];

    try {
      InteractivePagerBackup backup = parser.parse(fileName);

      if (command.equals("ls")) {
        listDatabases(backup);
      } else if (command.equals("printdbname")) {
        printDatabase(args[2], backup.getDatabase(args[2]));
      } else if (command.equals("printdbid")) {
        printDatabase(args[2], backup.getDatabase(Integer.parseInt(args[2])));
      } else if (command.equals("evernote-export")) {
        String exportFile = args[2];
        new EvernoteExporter(backup.getMemos()).export(exportFile);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void listDatabases(InteractivePagerBackup backup) {
    for (Iterator<Database> databases = backup.getDatabases(); databases
        .hasNext();) {
      Database database = databases.next();

      System.out.println(database.getId() + ": " + database.getName());
    }
  }

  private static void printDatabase(String arg, Database database) {
    if (database == null) {
      System.out.println("no db '" + arg + "'");
    } else {
      System.out.println(database.getId() + ": " + database.getName());
      database.dump(new PrintWriter(System.out));
    }
  }

}
