import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class Assembler {

   private static final ArrayList<String> NAMES =
      new ArrayList<String>(Arrays.asList("and", "or", "add", "addi", "sll",
      "sub", "slt", "beq", "bne", "lw", "sw", "j", "jr", "jal"));
   // TODO: ask abt opcodes (pg1 or pg2? which column(s)?)
   // TODO: ask abt register numbers: just exactly the decimal num from slides?
   private static final ArrayList<Integer> CODES =
      new ArrayList<Integer>(Arrays.asList(0x24, 0x25, 0x20, 0x8, 0x00,
      0x22, 0x2a, 0x4, 0x5, 0x23, 0x2b, 0x2, 0x08, 0x3));
   private static final ArrayList<Cmd> CMDS = new ArrayList<Cmd>() {{
      add(new Cmd("and", 0x24, Cmd.Format.R));
      add(new Cmd("or", 0x25, Cmd.Format.R));
      add(new Cmd("add", 0x20, Cmd.Format.R));
      add(new Cmd("addi", 0x8, Cmd.Format.I));
      add(new Cmd("sll", 0x00, Cmd.Format.R));
      add(new Cmd("sub", 0x22, Cmd.Format.R));
      add(new Cmd("slt", 0x2a, Cmd.Format.R));
      add(new Cmd("beq", 0x4, Cmd.Format.I));
      add(new Cmd("bne", 0x5, Cmd.Format.I));
      add(new Cmd("lw", 0x23, Cmd.Format.I));
      add(new Cmd("sw", 0x2b, Cmd.Format.I));
      add(new Cmd("j", 0x2, Cmd.Format.J));
      add(new Cmd("jr", 0x08, Cmd.Format.R));
      add(new Cmd("jal", 0x3, Cmd.Format.J));
   }};
   private static final HashMap<String, Cmd> STR_TO_CMD = new HashMap<String, Cmd>();

   private static final HashMap<String, Integer> REGS = new HashMap<String,
      Integer>() {{
         put("zero", 0);
         put("0", 0);
         put("v0", 2);
         put("v1", 3);
         put("a0", 4);
         put("a1", 5);
         put("a2", 6);
         put("a2", 7);
         put("t0", 8);
         put("t1", 9);
         put("t2", 10);
         put("t3", 11);
         put("t4", 12);
         put("t5", 13);
         put("t6", 14);
         put("t7", 15);
         put("s0", 16);
         put("s1", 17);
         put("s2", 18);
         put("s3", 19);
         put("s4", 20);
         put("s5", 21);
         put("s6", 22);
         put("s7", 23);
         put("t8", 24);
         put("t9", 25);
         put("sp", 29);
         put("ra", 31);
      }};

   private HashMap<String, Integer> symbolTable;
   private ArrayList<String> instMem;
   private String openLabel;

   public Assembler() {
      symbolTable = new HashMap<String, Integer>();
      instMem = new ArrayList<String>();
      openLabel = null;
      // initialize STR_TO_CMD as a HashMap from cmd name to a cmd object
      for (int i = 0; i < NAMES.size(); i++)
         STR_TO_CMD.put(NAMES.get(i), CMDS.get(i));
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
      return STR_TO_CMD.containsKey(cmd);
   }

   /**
   * Adds an instruction to the stored instruction memory at the given lineNum
   * Adding is sequential and only code lines are added, so the index is the
   *  line number.
   */
   public void addInst(String line) {
      instMem.add(line);
   }

   /**
   * Translate this Assembler's instMem to binary.
   */
   public void translate() {
      for (String line : instMem) {
         // parse instrxn itself (check validity)
         Cmd cmd = identifyInst(line);
         // split rest into args
         // assign args
         // convert args to binary
      }
   }

   public Cmd identifyInst(String line) {
      String str = "";
      if (isSupportedCmd(str))
         return STR_TO_CMD.get(str);
      // else unsupported cmd error
      else {
         System.out.println("invalid instruction: " + str);
         System.exit(1);
      }
      return null;
   }

   public void printSymbolTable() {
      System.out.println("SYMBOL TABLE");
      for (String label : symbolTable.keySet()) {
         System.out.println(label + " : " + symbolTable.get(label));
      }
   }

   public void printInstMem() {
      System.out.println("INST MEM");
      for (int i = 0; i < instMem.size(); i++) {
         System.out.println(i + "  " + instMem.get(i));
      }
   }

}
