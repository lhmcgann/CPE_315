import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

public class  Reader {

   private Scanner s;
   public Assembler a;
   private int lineCount; // only counts lines of actual code; the COUNT not NUM
   private boolean eof;
   private boolean firstPass;
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
      firstPass = true;
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

   public void iterateThroughLines() {
      String line;
      // read all lines in the file
      do {
         line = readNextLine();
         line = line.trim(); // remove leading and trailing whitespace
         // process this line, whatever it may be
         processLine(line);
      } while (!eof);
      // by now, file has been read at least once, so not the first pass
      firstPass = false;
   }

   public void processLine(String line) {
      // remove comments from the line
      int commentI;
      if ((commentI = line.indexOf('#')) != -1)
         line = line.substring(0, commentI);

      // if (now) empty or eof (i.e. line.length() == 0), do nothing
      if (!isEmptyLine(line)) {
         // TODO: DON'T split like this bc whitespace NOT guaranteed!!!
         // remove commas so split elements are terms only
         // line = line.replace(",", " ");
         // String[] elements = line.split(WHITESPACE);

         // if label (i.e. first element has ':') and firstPass (so we care)
         int labelEnd;
         if ((labelEnd = getLabelEnd(line)) != -1 && firstPass) {
            // mark "open" label
            a.openLabel(line.substring(0, labelEnd));
            // recursive call with substring of line (i.e. process after label)
            processLine(line.substring(labelEnd + 1));
         }
         // if get here, line must be code, so following options assume code
         // if firstPass, incr lineCount
         else if (firstPass) {
            // add this line to instMem
            a.addInst(line);
            // if "open" label, add this lineNum to table w/ "open" label
            if (a.isOpenLabel())
               a.addSymbol(lineCount);
            // inc lineCount after adding to symbol tbl so add lineNUM not CNT
            lineCount++;
         }
         // else, translate cmd to binary
         // else
         //    a.translate(elements);
      }
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
}
