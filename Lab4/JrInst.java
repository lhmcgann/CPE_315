public class JrInst extends RInst {

   protected int jAdr;

   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public JrInst(String inst, int code) {
      super(inst, code);

      // overwrite rXI values; leave all others bc don't use anyway
      rsI = 1;

      // init to invalid adr so if call getAdr too early, will know not set yet
      jAdr = -1;
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
      jAdr = e.RF.get(rs);
      e.PC = jAdr;
      e.hold = 1;
   }

   /**
   * @return - the address this jr inst jumps to; -1 if hasn't been computed yet
   */
   public int getAdr() {
      return jAdr;
   }

}
