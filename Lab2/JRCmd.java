public class JRCmd extends RCmd {
   /**
   * A Cmd with an initially known string cmd representation and associated opcode.
   */
   public JRCmd(String cmd, int code) {
      super(cmd, code);

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

}
