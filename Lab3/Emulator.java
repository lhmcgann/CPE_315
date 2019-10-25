import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

public class Emulator {

   private final String HELP = "\nh = show help\nd = dump register state\n" +
      "s = single step through the program (i.e. execute 1 instruction and stop)"
      + "\ns num = step through num instructions of the program"
      + "\nr = run until the program ends"
      + "\nm num1 num2 = display data memory from location num1 to num2"
      + "\nc = clear all registers, memory, and the program counter to 0"
      + "\nq = exit the program";
   private final String CMD_PROMPT = "\nmips> ";
   private final int REGS_PER_LINE = 4;
   private final int DM_LEN = 8192;
   public int PC;
   public HashMap<Integer, Integer> RF;
   public int[] DM;
   private ArrayList<Inst> IM;

   public Emulator() {
      PC = 0;
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
   }

   public void addInst(Inst inst) {
      IM.add(inst);
   }

   /**
   * Execute the next instruction, THEN increment the PC.
   */
   private void executeInstruction() {
      PC++; // increment PC first so any cmds that modify PC aren't affected
      IM.get(PC-1).execute(this);
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

      System.out.println(CMD_PROMPT);
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
      // skip the first reg str bc it's $zero
      for (int r = 1; r < keys.length; r++) {
         String reg = keys[r].toString();
         // print out each reg str and the reg's value
         System.out.print(reg + " = " + RF.get(Assembler.REGS.get(reg)));
         // to make the table appearance
         if (r%REGS_PER_LINE == 0)
            System.out.println();
         else
            System.out.print("\t\t");
      }
   }

   /**
   * @param arg - the number of instructions to step through; must be valid #
   */
   private void s(int arg) {
      for (int i = 0; i < arg; i++)
         executeInstruction();
      System.out.println("\t\t" + arg + " instruction(s) executed");
   }

   /**
   * Run until program end
   */
   private void r() {
      while(PC < IM.size())
         executeInstruction(); // increments PC
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
   }

   /**
   * Quit: exit the program
   */
   private void q() {
      System.exit(0);
   }

}
