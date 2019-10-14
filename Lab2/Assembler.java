import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Arrays;

public class Assembler {

   private static final ArrayList<String> CMDS =
      new ArrayList<String>(Arrays.asList("and", "or", "add", "addi", "sll",
      "sub", "slt", "beq", "bne", "lw", "sw", "j", "jr", "jal"));
   // TODO: ask abt opcodes (pg1 or pg2? which column(s)?)
   // TODO: ask abt register numbers: just exactly the decimal num from slides?
   private static final ArrayList<Integer> OPS =
      new ArrayList<Integer>(Arrays.asList(0x24, 0x25, 0x20, 0x8, 0x00,
      0x22, 0x2a, 0x4, 0x5, 0x23, 0x2b, 0x2, 0x08, 0x3));
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
      if (lab2.DEBUG) {
         System.out.println("Label '" + openLabel + "' closed with lineNum = " + lineNum);
      }
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
   * Convert a line of assembly code into binary and output the binary to the
   *  screen.
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

   public void printSymbolTable() {
      System.out.println("SYMBOL TABLE");
      for (String label : symbolTable.keySet()) {
         System.out.println(label + " : " + symbolTable.get(label));
      }
   }

}
