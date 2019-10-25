import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class Assembler {

   private static final ArrayList<String> NAMES =
      new ArrayList<String>(Arrays.asList("and", "or", "add", "addi", "sll",
      "sub", "slt", "beq", "bne", "lw", "sw", "j", "jr", "jal"));
   private static final ArrayList<Integer> CODES =
      new ArrayList<Integer>(Arrays.asList(0x24, 0x25, 0x20, 0x8, 0x00,
      0x22, 0x2a, 0x4, 0x5, 0x23, 0x2b, 0x2, 0x08, 0x3));
   private static final ArrayList<Inst> INSTS = new ArrayList<Inst>() {{
      add(new AndInst("and", 0x24));
      add(new OrInst("or", 0x25));
      add(new AddInst("add", 0x20));
      add(new AddiInst("addi", 0x8));
      add(new ShiftInst("sll", 0x00));
      add(new SubInst("sub", 0x22));
      add(new SltInst("slt", 0x2a));
      add(new BeqInst("beq", 0x4));
      add(new BneInst("bne", 0x5));
      add(new LwInst("lw", 0x23));
      add(new SwInst("sw", 0x2b));
      add(new JInst("j", 0x2));
      add(new JrInst("jr", 0x08));
      add(new JalInst("jal", 0x3));
   }};
   // maps the string representation of any instruction to a template Inst object
   private static final HashMap<String, Inst> STR_TO_INST = new HashMap<String, Inst>();

   public static final HashMap<String, Integer> REGS = new HashMap<String,
      Integer>() {{
         put("$zero", 0);
         put("$0", 0);
         put("$v0", 2);
         put("$v1", 3);
         put("$a0", 4);
         put("$a1", 5);
         put("$a2", 6);
         put("$a3", 7);
         put("$t0", 8);
         put("$t1", 9);
         put("$t2", 10);
         put("$t3", 11);
         put("$t4", 12);
         put("$t5", 13);
         put("$t6", 14);
         put("$t7", 15);
         put("$s0", 16);
         put("$s1", 17);
         put("$s2", 18);
         put("$s3", 19);
         put("$s4", 20);
         put("$s5", 21);
         put("$s6", 22);
         put("$s7", 23);
         put("$t8", 24);
         put("$t9", 25);
         put("$sp", 29);
         put("$ra", 31);
      }};

   private HashMap<String, Integer> symbolTable;
   private ArrayList<String[]> instMem;
   private String openLabel;

   public Assembler() {
      symbolTable = new HashMap<String, Integer>();
      instMem = new ArrayList<String[]>();
      openLabel = null;
      // initialize STR_TO_INST as a HashMap from inst name to a inst object
      for (int i = 0; i < NAMES.size(); i++)
         STR_TO_INST.put(NAMES.get(i), INSTS.get(i));
   }

   /**
    * Call this function after a label has been found and the corresponding
    *    referenced line of code has been found.
    * Add the label and corresponding address to the symbolTable.
    * "Close" the "open" label.
    * @param lineNum - the line number the open label refers to
    */
   public void addSymbol(int lineNum) {
      if (lab3.DEBUG) {
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
   * @param inst - the assembly command to verify
   *     the first term in the String[] elements of the current line
   *     will never be an empty, comment, or label line
   * @return - true if the command is supported, false if not
   */
   private boolean isSupportedInst(String inst) {
      return STR_TO_INST.containsKey(inst);
   }

   /**
   * Adds an instruction to the stored instruction memory at the given lineNum
   * Adding is sequential and only code lines are added, so the index is the
   *  line number.
   */
   public void addInst(String[] line) {
      instMem.add(line);
   }

   /**
   * Translate this Assembler's instMem to binary.
   * Output the translation of each instruction to the screen.
   */
   public void translate() {
      for (int i = 0; i < instMem.size(); i++) {
         String[] line = instMem.get(i);
         // get full Inst (check instrxn validity)
         Inst inst = identifyInst(line[0]);
         inst.setLineNum(i);
         /**
            if BInst or JInst:
               get label (last element in line, ASSUMING valid # args)
               set label-mapped-adr
         */
         if (inst instanceof NeedsLabelAdr) {
            NeedsLabelAdr n = (NeedsLabelAdr) inst;
            n.setLabelAdr(symbolTable.get(line[line.length - 1]));
         }
         // assign textual args to correct rs/rt/rd, etc
         inst.processArgs(line);
         // get full binary representation of inst
            // calc label offset if needed
         // output binary
         System.out.println(inst.getBinInstruction());
      }
   }

   /**
   * Map the instruction to its full Inst object if it's a valid instruction.
   * If invalid, print error message and exit.
   * @param inst - the instruction
   * @return - the corresponding Inst object, null if invalid
   */
   public Inst identifyInst(String inst) {
      // check inst validity
      if (isSupportedInst(inst))
         return STR_TO_INST.get(inst);
      else {
         System.out.println("invalid instruction: " + inst);
         System.exit(1);
      }
      return null;
   }

   public HashMap<String, Integer> getSymbolTable() {
      return symbolTable;
   }

   public ArrayList<String[]> getInstMem() {
      return instMem;
   }

}
