import java.util.Scanner;
import java.io.File;
import java.util.NoSuchElementException;
import java.util.Arrays;

/**
* Reads through all of the lines in a given File, processing each line - as
*  specified by sublass - as it goes.
*/
public class ScriptReader extends Reader {

   public ScriptReader(File file) {
      super(file);
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

   /**
   * Executes the desired action on any given line.
   * Postcondition: line counter must be incremented if "valid" line found
   *  - the definition of "valid" depends on the subclass
   */
   @Override
   public void processLine(String line) {

   }
}
