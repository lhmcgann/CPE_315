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
      // assume .asm code is specific to this emulator (i.e. indices by 1, and grows low->high DM)
      int adr = e.RF.get(rs) + imd;

      if (adr >= e.DM_LEN) {
         System.out.printf("DM index %d out of bounds.\n", adr);
         System.exit(1);
      }

      return adr;
   }

}
