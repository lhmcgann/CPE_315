public class JalInst extends JInst {

   private final int RA = 31;
   /**
   * A JalInst with an initially known string inst representation and associated opcode.
   */
   public JalInst(String inst, int code) {
      super(inst, code);
   }

   @Override
   public void emulate(Emulator e) {
      e.RF.put(RA, e.PC); // store PC (already incremented) before jumping
      e.PC = labelAdr; // super(e);
   }

}
