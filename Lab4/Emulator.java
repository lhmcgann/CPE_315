import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

/**
* Emulates the execution of MIPS instructions.
*/
public class Emulator {

   private final String HELP = "\nh = show help\nd = dump register state\n" +
      "p = show pipeline registers\n" +
      "s = step through a single clock cycle step (i.e. simulate 1 cycle and stop)"
      + "\ns num = step through num clock cycles"
      + "\nr = run until the program ends and display timing summary"
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

   private Simulator sim;
   public int hold;
   private boolean checkUAL;

   public Emulator(Simulator sim) {
      this.sim = sim;
      PC = 0;
      hold = 0;
      checkUAL = false;
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
      DM = new int[DM_LEN];
      IM = new ArrayList<Inst>();

      // TODO: fix this! when to print this? edit actual execute fnxn so don't
      //    need special thing here; this will also run b4 a.secondPass(e);
      System.out.print("mips> ");
   }

   public void addInst(Inst inst) {
      IM.add(inst);
   }

   /**
   * If not on hold, increment the PC, then emulate the next instruction.
   */
   private void emulateInstruction() {
      // if Em on hold (i.e. bc br taken), don't emulate next insts
      if (hold > 0) {
         hold--;
      }
      else { // else emulate as normal
         // increment PC first so any cmds that modify PC aren't affected
         PC++;
         Inst currInst = IM.get(PC - 1);

         // if lw, must check use_after_load
         if (currInst instanceof LwInst) {
            LwInst lw = (LwInst) currInst;
            if (useAfterLoad(lw)) {
               hold = 1; // don't emualate the next inst yet
               lw.markUAL();
            }
         }

         currInst.emulate(this);
         if (lab4.DEBUG) {
            System.out.println("\tEmulated " + IM.get(PC-1).getName());
         }
      }

      if (lab4.DEBUG) {
         System.out.println("\tEm PC: "+PC);
      }

      // run CPU sim; pass in IM so sim can access next Inst using its own PC
      sim.runOneCC(IM);
   }

   private boolean useAfterLoad(LwInst lw) {
      Inst nextInst;
      // currInst = lw if get to here; check rest of conditions if next inst exists
      if (PC < IM.size()) {
         nextInst = IM.get(PC);
         // check to see if uses lw's dest register
         return nextInst.usesReg(lw.rt);
      }

      // conditions not met
      return false;
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
         case "p": // show what insts in what pipeline regs
            p();
            break;
         case "s": // step through 1 or n clock cycles
            if (args[0] == -1)
               s(1);
            else
               s(args[0]);
            break;
         case "r": // run until prog end
            r();
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
      if (lab4.DEBUG) {
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

   private void p() {
      sim.dumpPRegs();
   }

   /**
   * @param arg - the number of instructions to step through; must be valid #
   */
   private void s(int arg) {
      for (int i = 0; i < arg; i++)
         emulateInstruction();
      p(); // dump pipeline regs at the end, regardless of # steps
   }

   /**
   * Run until program end
   */
   private void r() {
      while(PC < IM.size())
         emulateInstruction(); // increments PC

      sim.flushCPU(IM); // to push through all of the last insts

      if (lab4.DEBUG) {
         p();
      }
      // Print the Program complete message
      System.out.println("\nProgram complete");
      System.out.printf("CPI = %.2f\tCycles = %d\tInstructions = %d\n",
         sim.getCPI(), sim.getCCCount(), sim.numInsts());
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
      PC = 0;
      // reset all regs in Reg File
      for (Integer key : RF.keySet())
         RF.put(key, 0);
      // reset Data Mem
      for (int i = 0; i < DM_LEN; i++)
         DM[i] = 0;
      sim.resetSim();
      System.out.println("\tSimulator reset");
   }

   /**
   * Quit: exit the program
   */
   private void q() {
      System.exit(0);
   }

}
