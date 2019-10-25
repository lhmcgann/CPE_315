public abstract class BInst extends IInst implements NeedsLabelAdr {

   protected int labelAdr;

   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public BInst(String inst, int code) {
      super(inst, code);
      rtI = 2;
      rsI = 1;

      labelAdr = -1;
   }

   @Override
   public void execute(Emulator e) {
      if (shouldBranch())
         e.PC = labelAdr - 1; // the -1 is bc PC will be incremented elsewhere
   }

   /**
   * @return - evaluation of the branch condition
   */
   protected abstract boolean shouldBranch();

   /**
   * For branch immediate-format insts, line[imdI] is the label used to calc offset
   */
   @Override
   protected void calcImd(String[] line) {
      // imd is the offset calculated from lineNum and line num the label pts to
      imd = labelAdr - (lineNum + 1);
   }

   @Override
   public void setLabelAdr(int labelAdr) {
      this.labelAdr = labelAdr;
   }

}
