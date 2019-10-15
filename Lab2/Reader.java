import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Arrays;

public class  Reader {

   private Scanner s;
   public Assembler a;
   private int lineCount; // only counts lines of actual code; the COUNT not NUM
   private boolean eof;
   private static final String WHITESPACE = "\\s+";

   public Reader(String filename) {
      try {
         s = new Scanner(new File(filename));
      }
      catch (FileNotFoundException e) {
         System.out.println("The file " + filename + " was not found.");
         System.exit(1);
      }
      a = new Assembler();
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

   public void firstPass() {
      String line;
      // read all lines in the file
      do {
         line = readNextLine();
         // process this line, whatever it may be
         processLine(line);
      } while (!eof);
   }

   /**
   * For first pass only.
   */
   public void processLine(String line) {
      // remove comments from the line
      int commentI;
      if ((commentI = line.indexOf('#')) != -1)
         line = line.substring(0, commentI);
      line = line.trim(); // remove leading and trailing whitespace

      // if (now) empty or eof (i.e. line.length() == 0), do nothing
      if (!isEmptyLine(line)) {
         // TODO: DON'T split like this bc whitespace NOT guaranteed!!! --> split properly

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
            a.addInst(prepareInstLine(line));
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
   * @param line - the initial instruction line; first char is first char of
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
      // int p1 = line.indexOf('(');
      // String rs;
      // if (p1 != -1)
      //
      //    rs =
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

   private boolean isEmptyLine(String line) {
      return line.length() == 0;
   }

   /**
   * @return - the end range for the label substring, -1 if no label is found
   */
   private int getLabelEnd(String line) {
      return line.indexOf(":");
   }

   /**
   * Cleanup for this Reader: close the Scanner (and henceforth its open file).
   */
   public void terminate() {
      s.close();
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
