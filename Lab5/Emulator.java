import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

public class Emulator {

   private final String HELP = "\nh = show help\nd = dump register state\n" +
      "s = single step through the program (i.e. execute 1 instruction and stop)"
      + "\ns num = step through num instructions of the program"
      + "\nr = run until the program ends\n" +
      "o = output a comma separated listing of the x,y coordinates to a file called coordinates.csv"
      + "\nb = output the branch predictor accuracy"
      + "\nm num1 num2 = display data memory from location num1 to num2"
      + "\nc = clear all registers, memory, and the program counter to 0"
      + "\nq = exit the program";
   private final String CMD_PROMPT = "\nmips> ";
   private final int REGS_PER_LINE = 4;
   private final int DM_LEN = 8192;
   public int PC;
   public Map<Integer, Integer> RF;
   public int[] DM;
   private ArrayList<Inst> IM;
   private char ghrSize;
   private char ghr; // char bc will only ever be 8-bits
   private int ghrMask;
   private char[] predArray; // array of 2-bit predictions
   private final char T = 3;
   private final char NT = 0;
   private final char WT = 2;
   private final char WNT = 1;
   private int numBrs;
   private int correctBrs;

   public Emulator(char ghrSize) {
      PC = numBrs = correctBrs = 0;
      // maps number representations of all supported registers to reg's value
      RF = new HashMap<Integer,
         Integer>() {{
            put(0, 0);
            put(2, 0);
            put(3, 0);
            put(4, 0);
            put(5, 0);
            put(6, 0);
            put(7, 0);
            put(8, 0);
            put(9, 0);
            put(10, 0);
            put(11, 0);
            put(12, 0);
            put(13, 0);
            put(14, 0);
            put(15, 0);
            put(16, 0);
            put(17, 0);
            put(18, 0);
            put(19, 0);
            put(20, 0);
            put(21, 0);
            put(22, 0);
            put(23, 0);
            put(24, 0);
            put(25, 0);
            put(29, 0);
            put(31, 0);
         }};
      DM = new int[DM_LEN]; // fill DM with 0's
      for (int i = 0; i < DM.length; i++)
         DM[i] = 0;
      IM = new ArrayList<Inst>();

      // ghr setup
      this.ghrSize = ghrSize;
      ghrMask = ghrSize + (ghrSize - 1); // to fill up to ghrSize-bit with 1's
      ghr = 0;
      predArray = new char[(int)Math.pow(2, ghrSize)]; // fill predArray with 0's
      for (int i = 0; i < predArray.length; i++)
         predArray[i] = 0;

      System.out.print(CMD_PROMPT);
   }

   public void addInst(Inst inst) {
      IM.add(inst);
   }

   /**
   * Increment the PC, THEN emulate the next instruction.
   */
   private void emulateInstruction() {
      PC++; // increment PC first so any cmds that modify PC aren't affected
      IM.get(PC-1).emulate(this);
   }

   /**
   * Use to determine which path to take before know actual br result (for CPU sim).
   * @return - a boolean indicating if predicted taken (true) or not taken (false)
   */
   private boolean takeBranch() {
      char val = predArray[getPredIndex()];
      return val > WNT; // if >1 (true), WT or ST. if <=1 (false), SNT or WNT.
   }

   /**
   * ALWAYS use thi method instead of using the ghr to index directly into the
   *  prediction array.
   * @return - the index to use into the prediciton array
   */
   private char getPredIndex() {
      return (char) (ghr & ghrMask); // so ignores shifted bits if size < 8
   }

   public void adjustPred(boolean taken) {
      char nextVal;
      char i = getPredIndex();

      if (lab5.DEBUG) {
         System.out.println("pred index = " + Integer.toBinaryString(i));
         System.out.println("pred = " + Integer.toBinaryString(predArray[i]));
      }

      // update the prediction in the predArray at the old ghr spot
      if (taken) {
         predArray[i] = (char) Math.min(predArray[i]+1, T);
         nextVal = 1; // ghr gets a 1 if taken
      }
      else {
         predArray[i] = (char) Math.max(predArray[i]-1, NT);
         nextVal = 0; // ghr gets a 0 if NOT taken
      }
      // predArray[getPredIndex()] += Math.signum(realVal - predArray[getPredIndex()]);

      // shift ghr and add the T/NT value of the most recent branch
      if (lab5.DEBUG)
         System.out.println("ghr = " + Integer.toBinaryString(ghr));
      ghr = (char) (ghr << 1);
      if (lab5.DEBUG)
         System.out.println("ghr = " + Integer.toBinaryString(ghr));
      ghr += nextVal;
      if (lab5.DEBUG)
         System.out.println("ghr = " + Integer.toBinaryString(ghr));

      // update counts for br accuracy
      numBrs++; // increment regardless of outcome
      if ((takeBranch() && taken) || (!takeBranch() && !taken)) {
         correctBrs++; // incr if matching T-T or NT-NT
         if (lab5.DEBUG)
            System.out.println("correct Brs = " + correctBrs);
      }
   }

   /**
   * @param cmd - the script cmd to execute
   * @param args - a 2-int long array containing any cmd arguments; filled with
   *  -1's if no cmd args
   */
   public void executeScriptCmd(String cmd, int[] args) {
      switch (cmd) {
         case "h": // show help
            h();
            break;
         case "d": // dump reg state
            d();
            break;
         case "s": // step through 1 or n insts
            if (args[0] == -1)
               s(1);
            else
               s(args[0]);
            break;
         case "r": // run until prog end
            r();
            break;
         case "b": // output br predictor accuracy
            b();
            break;
         case "o": // output to coordinates.csv
            o();
            break;
         case "m": // display data mem from n1 to n2
            m(args);
            break;
         case "c": // clear all
            c();
            break;
         case "q": // quit
            q();
            break;
         default: // do nothing (just print cmd prompt again? or invalid cmd?)
            System.out.println("Invalid cmd");
            break;
      }

      System.out.print(CMD_PROMPT);
   }

   /**
   * Show help
   */
   private void h() {
      System.out.println(HELP);
   }

   /**
   * Dump reg states
   */
   private void d() {
      System.out.println("\npc = " + PC); // print 1st buffer newline and the PC
      Object[] keys = Assembler.REGS.keySet().toArray();
      if (lab5.DEBUG) {
         System.out.println("old keys: " + Arrays.toString(keys));
      }
      // skip the first reg str bc it's $zero
      for (int r = 1; r < keys.length; r++) {
         String reg = keys[r].toString();
         int regNum = Assembler.REGS.get(reg);
         // print out each reg str and the reg's value
         System.out.print(reg + " = " + RF.get(regNum));
         // to make the table appearance
         if (r%REGS_PER_LINE == 0)
            System.out.println();
         else
            System.out.print("\t\t");
      }
      System.out.println(); // so there will be a new line under the table
   }

   /**
   * @param arg - the number of instructions to step through; must be valid #
   */
   private void s(int arg) {
      for (int i = 0; i < arg; i++)
         emulateInstruction();
      System.out.println("\t" + arg + " instruction(s) emulated");
   }

   /**
   * Run until program end
   */
   private void r() {
      while(PC < IM.size())
         emulateInstruction(); // increments PC
   }

   /**
   * Output the branch accuracy.
   */
   private void b() {
      double accuracy = ((float) correctBrs) / numBrs * 100;
      System.out.printf("\naccuracy %.2f%% (%d correct predictions, %d predictions)\n",
         accuracy, correctBrs, numBrs);
   }

   private void o() {

   }

   /**
   * Display data mem from addresses args[0] to args[1].
   * @param args - the bounds of the range of mem addresses to print; must be
   *  valid mem addresses; args must be of length 2
   */
   private void m(int[] args) {
      System.out.println();
      for (int i = args[0]; i <= args[1]; i++)
         System.out.println("[" + i + "] = " + DM[i]);
   }

   /**
   * Clear all.
   */
   private void c() {
      PC = numBrs = correctBrs = 0;
      // reset all regs in Reg File
      for (Integer key : RF.keySet())
         RF.put(key, 0);
      // reset Data Mem
      for (int i = 0; i < DM_LEN; i++)
         DM[i] = 0;

      // reset ghr stuff
      ghr = 0;
      for (int i = 0; i < predArray.length; i++)
         predArray[i] = 0;

      System.out.println("\tSimulator reset");
   }

   /**
   * Quit: exit the program
   */
   private void q() {
      System.exit(0);
   }

}
