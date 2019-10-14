import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Arrays;

public class Assembler {

   private static final ArrayList<String> CMDS =
      new ArrayList<String>(Arrays.asList("and", "or", "add", "addi", "sll",
      "sub", "slt", "beq", "bne", "lw", "sw", "j", "jr", "jal"));
   private static final Hashtable<String, Integer> CMD_TO_OP = new Hashtable<String, Integer>();

   private Hashtable<String, Integer> symbolTable;
   private String openLabel;

   public Assembler() {
      symbolTable = new Hashtable<String, Integer>();
      openLabel = null;
   }

   /**
    * Call this function after a label has been found and the corresponding
    *    referenced line of code has been found.
    * Add the label and corresponding address to the symbolTable.
    * "Close" the "open" label.
    * @param lineNum - the line number the open label refers to
    */
   public void addSymbol(int lineNum) {
      symbolTable.put(openLabel, lineNum);
      openLabel = null; // "close"
   }

   /**
   * @return - true if there is a label waiting for its reference lineNum to be
   *     found, false if all labels have been matched to a lineNum and added
   */
   public boolean isOpenLabel() {
      return openLabel != null;
   }

   /**
   * Opens up a new label waiting for a corresponding reference lineNum to be
   *  found.
   * @param label - the new "open" label
   */
   public void openLabel(String label) {
      openLabel = label;
   }

   /**
   * @param cmd - the assembly command to verify
   *     the first term in the String[] elements of the current line
   *     will never be an empty, comment, or label line
   * @return - true if the command is supported, false if not
   */
   private boolean isSupportedCmd(String cmd) {
      return CMD_TO_OP.containsKey(cmd);
   }

   /**
   * Convert a line of assembly code into binary and output the binary to an
   *  an output file.
   * If an invalid command is given, exit after printing an error message.
   * @param elements - an array of all the terms/items in the line of assembly
   *  code to translate; may include trailing in-line comments
   */
   public void translate(String[] elements) {
      if (isSupportedCmd(elements[0]))
         // TODO: translating things here
         System.out.println("TODO: translate to binary; make data struct/class");
      // else unsupported cmd error
      else {
         System.out.println("The cmd " + elements[0] + " is unsupported.");
         System.exit(1);
      }
   }

}
