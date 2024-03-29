import java.io.InputStream;
import java.util.Arrays;

/**
* Reads through all of the lines in a given File, processing each line - as
*  specified by sublass - as it goes.
*/
public class AsmReader extends Reader {

   protected Assembler a;

   public AsmReader(InputStream in, Assembler a) {
      super(in);
      this.a = a;
   }

   public int getLineCnt() {
      return lineCount;
   }

   /**
   * Conducts first pass tasks on the given line:
   *  locates, stores, and assigns labels
   *  adds trimmed/prepped instructions to the Assembler's IM
   *  increments line count for lines of assembly code
   */
   @Override
   public void processLine(String line) {
      // remove comments from the line
      int commentI;
      if ((commentI = line.indexOf('#')) != -1)
         line = line.substring(0, commentI);
      line = line.trim(); // remove leading and trailing whitespace

      // if (now) empty or eof (i.e. line.length() == 0), do nothing
      if (!isEmptyLine(line)) {
         // if label (i.e. first element has ':')
         int labelEnd;
         if ((labelEnd = getLabelEnd(line)) != -1) {
            // mark "open" label
            a.openLabel(line.substring(0, labelEnd));
            // recursive call with substring of line (i.e. process after label)
            processLine(line.substring(labelEnd + 1));
         }
         // if get here, line must be code, so following options assume code
         else {
            // add this line to instMem
            a.addInstLine(prepareInstLine(line));
            // if "open" label, add this lineNum to table w/ "open" label
            if (a.isOpenLabel())
               a.addSymbol(lineCount);
            // inc lineCount after adding to symbol tbl so add lineNUM not CNT
            lineCount++;
         }
      }
   }

   /**
   * Identifies the instruction string.
   * Spaces the instruction itself and all of its aguments by nothing more than a ','
   * Included here in order to have all String manipulation in this class.
   * @param line - the initial instruction line; first int is first int of
   *  the instruction; no comments after the assembly code
   * @return - a String array of the inst itself followed by its arguments
   */
   private String[] prepareInstLine(String line) {
      String inst;
      int endRange;
      // only 2 cases: (1) 1st arg is a register (look for $)
      if ((endRange = line.indexOf('$')) != -1)
         inst = line.substring(0, endRange).trim(); // trim WS btwn inst and $
      // or (2) 1st arg label (for j and jal only --> look for whitespace)
      else
         inst = line.split(WHITESPACE)[0];

      // handle lw/sw instructions
      line = line.replace('(', ',');
      line = line.replace(')', ' ');

      // put a comma btwn inst and params (for split by "," later)
      String newLine = inst + "," + line.substring(inst.length(), line.length());

      String[] args = newLine.split(",");
      // trim any whitespace off indv args
      for (int i = 0; i < args.length; i++)
         args[i] = args[i].trim();

      return args;
   }

   /**
   * @return - the end range for the label substring, -1 if no label is found
   */
   private int getLabelEnd(String line) {
      return line.indexOf(":");
   }

   /**
   * Print the symbol table. - USED FOR TESTING
   */
   public void printLabels() {
      System.out.println("SYMBOL TABLE");
      for (String label : a.getSymbolTable().keySet()) {
         System.out.println(label + " : " + a.getSymbolTable().get(label));
      }
   }

   /**
   * Print the instruction memory. - USED FOR TESTING
   */
   public void printInstLines() {
      System.out.println("INST MEM");
      for (int i = 0; i < a.getInstMem().size(); i++) {
         System.out.println(i + "  " + Arrays.toString(a.getInstMem().get(i)));
      }
   }
}
