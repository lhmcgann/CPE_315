import java.util.Hashtable;
import java.util.ArrayList;

public class Assembler {

   private static final ArrayList<String> CMDS = {"and", "or", "add", "addi",
      "sll", "sub", "slt", "beq", "bne", "lw", "sw", "j", "jr", "jal"};
   public static final Hashtable<String, Integer> CMD_TO_OP = ;

   private Hastable<String, Integer> symbolTable;

   public Assembler(File file) {
      symbolTable = new Hashtable<String, Integer>;
   }

   /**
    * Call this function after a label has been found.
    * Find the address of the line the label refers to.
    * Add the label and corresponding address to the symbolTable.
    * @param elements - an array of the elements in a current line
    */
   public void addToSymbolTable(String[] elements) {
      String label = elements[0];
      label = label.substring(0, label.length() - 1); // remove the ':'
      // if only the label on this line, find adr of line its pointing to
      if (elements.length == 1) {
         sca.findNextLine();
      }
      else
         symbolTable.put(label, lineNumber);
   }

   /**
   * @param elements - an array of the elements in a current line
   */
   public boolean lineHasLabel(String[] elements) {
      return elements.length > 1;
   }

   public void iterateThroughLines() {
      // iterate through all lines
      // TODO
      String line;
      // loop until read EOF
      while (!(line = r.findNextActualLine()).equals("")) {
         parseLine(line);
      }
      // if went through file at least once, you know it's no longer 1st pass
      firstPass = false;
   }

   /**
   * Process a single line in the file this Assembler is reading.
   * If first pass, check for labels, else translate code into assembly.
   * @param line - the line to parse
   */
   public void parseLine(String line) {
      String[] elements = line.split("\\s+");
      // only search for labels on the first pass
      if (firstPass) {
         if (lineHasLabel(elements))
            addToSymbolTable(elements);
      }
      else {
         // TODO: translate assembly into binary; skip newlines
      }

   }

   public void terminate() {
      s.close();
   }

}
