public class JInst extends Inst implements NeedsLabelAdr {

   protected final int IMD_SIZE = 26; // bit width

   protected int imdI;
   protected int imd;

   protected int labelAdr;

   /**
   * A JInst with an initially known string inst representation and associated opcode.
   */
   public JInst(String inst, int code) {
      super(inst, code);
      imdI = 1;
      imd = -1;

      labelAdr = -1;
   }

   protected boolean validRegs(String[] line) {
      return true; // no regs to validate
   }

   /**
   * Assumed that validRegs has been called prior and all args are valid.
   * Calculates the imd based on the label in line
   */
   protected void computeArgs(String[] line) {
      // imd is the actual adr the label pts to
      imd = labelAdr;
   }

   public String getBinInstruction() {
      return intToBin(op, OP_SIZE) + " " + intToBin(imd, IMD_SIZE);
   }

   public boolean usesReg(int regNum) {
      return false;
   }

   @Override
   public void setLabelAdr(int labelAdr) {
      this.labelAdr = labelAdr;
   }

   @Override
   public int getLabelAdr() {
      return labelAdr;
   }

   @Override
   public void emulate(Emulator e) {
      e.PC = labelAdr;
      e.hold = 1;
   }

}
