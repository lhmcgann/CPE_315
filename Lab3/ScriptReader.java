import java.util.Scanner;
import java.io.File;
import java.util.NoSuchElementException;
import java.util.Arrays;

/**
* Reads through all of the lines in a given File, processing each line - as
*  specified by sublass - as it goes.
*/
public class ScriptReader extends Reader {

   private final int MAX_ARGS = 2;
   protected Emulator e;

   public ScriptReader(File file, Emulator e) {
      super(file);
      this.e = e;
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
   * Gathers script cmd arguments if applicable.
   * Postcondition: line counter must be incremented if "valid" line found
   *  - the definition of "valid" depends on the subclass
   */
   @Override
   public void processLine(String line) {
      int[] args = new int[MAX_ARGS]; // autofill with 0's
      Arrays.fill(args, -1); // fill with -1's (so will indicate no args)
      String[] elements = line.split(WHITESPACE);
      // get array of int representations of any cmd args
      for (int i = 0; i < elements.length - 1; i++)
         args[i] = Integer.parseInt(elements[i+1]);
      // execute the script command
      e.executeScriptCmd(elements[0], args);
      lineCount++;
   }
}
