public abstract class LSInst extends IInst {

   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public LSInst(String inst, int code) {
      super(inst, code);

      // This is literally the only difference
      imdI = 2;
      rsI = 3;
   }

   protected int getMemAdr(Emulator e) {
      // div by 4 bc MIPS thinks these insts act on WORDS = 4 bytes but emulated DM indices incr by 1 not 4
      int adr = e.RF.get(rs) + imd/4; // TODO: div whole thing by 4 or just imd?
      // TODO: QUESTION: should our stack actually grow from high DM to low DM or just start at 0?
      // TODO: QUESTION: also, how to do first stack store? bc if do $sp + 8 and then -4, 0, will be stored at 1, 2...
      if (adr >= e.DM_LEN) {
         System.out.println("DM index %d out of bounds.");
         System.exit(1);
      }
      return adr;
   }

}
