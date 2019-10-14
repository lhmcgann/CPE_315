import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

public class  Reader {

   private Scanner s;
   private Assembler a;
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
      // if empty or eof (i.e. line.length() == 0), do nothing
      // if comment (i.e. first element starts with #), do nothing
      if (!isEmptyLine(line) && !isComment(line)) {
         // remove commas so split elements are terms only
         line = line.replace(",", " ");
         String[] elements = line.split(WHITESPACE);
         // if label (i.e. first element has ':') and firstPass (so we care)
         if (isLabel(elements) && firstPass) {
            int len = elements[0].length();
            // mark "open" label
            a.openLabel(elements[0].substring(0, len - 1));
            // recursive call with substring of line (i.e. process after label)
            processLine(line.substring(len));
         }
         // if get here, line must be code, so following options assume code
         // if firstPass, incr lineCount
         else if (firstPass) {
            // if "open" label, add this lineNum to table w/ "open" label
            if (a.isOpenLabel())
               a.addSymbol(lineCount);
            // inc lineCount after adding to symbol tbl so add lineNUM not CNT
            lineCount++;
         }
         // else, translate cmd to binary
         else
            a.translate(elements);
      }
   }

   private boolean isEmptyLine(String line) {
      return line.length() == 0;
   }

   private boolean isComment(String line) {
      return line.charAt(0) == '#';
   }

   private boolean isLabel(String[] elements) {
      return elements[0].contains(":");
   }

   /**
   * Cleanup for this Reader: close the Scanner (and henceforth its open file).
   */
   public void terminate() {
      s.close();
   }
}
