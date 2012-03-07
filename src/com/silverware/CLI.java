package com.silverware;

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
