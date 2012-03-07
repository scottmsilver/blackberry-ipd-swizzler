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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Iterator;

import org.apache.commons.lang3.StringEscapeUtils;

/*
 
 <?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE en-export SYSTEM "http://xml.evernote.com/pub/evernote-export2.dtd">
<en-export>
<note>
  <title>Amiodarone</title>
  <content><![CDATA[<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE en-note SYSTEM "http://xml.evernote.com/pub/enml2.dtd"><en-note>Test Me<p/>Test Me2<p/></en-note>]]></content>
  <created>20120226T080000Z</created>
  <tag>medical</tag></note>
</en-export>

 */
/**
 * Helper class to consume a BlackBerry Memo database and output an Evernote import file called enex.
 */
public class EvernoteExporter {
  Memos memos;
  
  public EvernoteExporter(Memos theMemos) {
    this.memos = theMemos;
  }

  public void export(String exportFile) {
    PrintWriter printWriter = new PrintWriter(System.out);
    
    printWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    printWriter.println("<!DOCTYPE en-export SYSTEM \"http://xml.evernote.com/pub/evernote-export2.dtd\">");
    printWriter.println("<en-export>");
    
    int index = 0;
    for (Iterator<Memo> memoIterator = memos.getAll(); memoIterator.hasNext();) {
      Memo memo = memoIterator.next();
      printMemo(printWriter, memo, index++);      
    }
    
    printWriter.println("</en-export>");

    printWriter.flush();
  }

  /**
   * Print a single memo.
   * 
   * @param printWriter
   * @param memo
   * @param index
   */
  private void printMemo(PrintWriter printWriter, Memo memo, int index) {
    printWriter.println("<note>"); 

    String title = memo.getTitle();
    String content = memo.getContent();
    String[] tags = memo.getTags();
    
    if (title != null) {
      printWriter.printf("<title>%s</title>\n", StringEscapeUtils.escapeHtml4(title), index); 
    }

    printWriter.print("<content><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\"><en-note>");
    
    if (content != null) {
      printContentAsHtml(printWriter, content);
    }
    
    printWriter.println("</en-note>]]></content>");

    if (tags != null ) {
      for (int tagIndex = 0; tagIndex < tags.length; tagIndex++) {
        printWriter.printf("<tag>%s</tag>\n", StringEscapeUtils.escapeHtml4(tags[tagIndex]));
      }
    }
    
    printWriter.println("</note>"); 
  }

  private void printContentAsHtml(PrintWriter printWriter, String content) {
    BufferedReader reader = new BufferedReader(new StringReader(content));
    try {
      for (String line = reader.readLine(); line != null;  line = reader.readLine()) {
        printWriter.print(StringEscapeUtils.escapeHtml4(line));
        printWriter.print("<p/>");
      }
    } catch (IOException e) {
      // FIXME(ssilver): Do something with this.
      return;
    }
  }
}
