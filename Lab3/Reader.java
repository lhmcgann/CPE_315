import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Arrays;

/**
* Reads through all of the lines in a given File, processing each line - as
*  specified by sublass - as it goes.
*/
public abstract class Reader {

   protected Scanner s;
   protected int lineCount; // only counts lines of actual code; the COUNT not NUM
   protected boolean eof;
   protected static final String WHITESPACE = "\\s+";

   public Reader(File file) {
      try {
         s = new Scanner(file);
      }
      catch (FileNotFoundException e) {
         System.out.println("The asm file was not found.");
         System.exit(1);
      }
      lineCount = 0;
      eof = false;
   }

   public int getLineCnt() {
      return lineCount;
   }

   /**
   * Try reading the next line from this Reader's file.
   * @return - the next line if there is one; an empty string if eof
   */
   public String readNextLine() {
      String result = "";
      try {
         result = s.nextLine();
      }
      catch (NoSuchElementException e) {
         eof = true;
      }
      return result;
   }

   public void readThroughLines() {
      String line;
      // read all lines in the file
      do {
         line = readNextLine();
         // process this line, whatever it may be
         processLine(line);
      } while (!eof);
   }

   /**
   * Executes the desired action on any given line.
   * Postcondition: line counter must be incremented if "valid" line found
   *  - the definition of "valid" depends on the subclass
   */
   public abstract void processLine(String line);

   protected boolean isEmptyLine(String line) {
      return line.length() == 0;
   }

   /**
   * Cleanup for this Reader: close the Scanner (and henceforth its open file).
   */
   public void terminate() {
      s.close();
   }
}
