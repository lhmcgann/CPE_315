public class Emulator {

   private final String CMD_PROMPT = "\nmips> ";
   private final int DM_LEN = 8192;
   public int PC;
   private HashMap<Integer, Integer> RF;
   private int[] DM;
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
   * Execute the next instruction. Increment the PC.
   */
   private void executeInstruction() {
      IM[PC].execute(this);
      PC++;
   }

   public void executeScriptCmd(Str cmd, int[] args) {
      switch (cmd) {
         case "h": // show help
         case "d": // dump reg state
         case "s": // step through 1 or n insts
         case "r": // run until prog end
         case "m": // display data mem from n1 to n2
         case "c": // clear all
         case "q": // quit
         default: // do nothing (just print cmd prompt again?)
      }

      System.out.println(CMD_PROMPT);
   }

   /**
   * Show help
   */
   private void h() {

   }

   /**
   * Dump reg state
   */
   private void d() {

   }

   /**
   * Step through 1 or args[0] steps
   * @param args - the number of instructions to step through; must be valid #
   */
   private void s(int args) {
      // int instNum;
      // // determine if 1 step or n steps
      // if (args.length == 0)
      //    instNum = 1;
      // else
      //    instNum = args[0];
      // execute # steps
      for (int i = 0; i < arg; i++)
         executeInstruction();
   }

   /**
   * Run until program end
   */
   private void r() {

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
