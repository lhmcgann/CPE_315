import java.io.InputStream;
import java.util.Arrays;

/**
* Reads through all of the lines in a given InputStream, processing each line
*  - as specified by sublass - as it goes.
*/
public class ScriptReader extends Reader {

   private final int MAX_ARGS = 2;
   protected Emulator e;
   private boolean isScript;

   public ScriptReader(InputStream in, Emulator e, boolean isScript) {
      super(in);
      this.e = e;
      this.isScript = isScript;
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

      // if it's ACTUALLY a script, print out the "user input"
      if (isScript)
         System.out.println(line);

      // execute the script command
      e.executeScriptCmd(elements[0], args);
      lineCount++;
   }
}
