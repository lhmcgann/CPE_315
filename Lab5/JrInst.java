public class JrInst extends RInst {
   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public JrInst(String inst, int code) {
      super(inst, code);

      // overwrite rXI values; leave all others bc don't use anyway
      rsI = 1;
   }

   @Override
   protected boolean validRegs(String[] line) {
      return Assembler.REGS.containsKey(line[rsI]);
   }

   @Override
   protected void computeArgs(String[] line) {
      // others not used so don't need to change from 0 in constructor
      rs = Assembler.REGS.get(line[rsI]);
   }

   @Override
   public void emulate(Emulator e) {
      e.PC = e.RF.get(rs);
   }

}
