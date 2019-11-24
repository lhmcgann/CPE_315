public class LwInst extends LSInst {

   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public LwInst(String inst, int code) {
      super(inst, code);
   }

   @Override
   public void emulate(Emulator e) {
      // put M[rs+imd] -> rt
      e.RF.put(rt, e.DM[getMemAdr(e)]);
   }

}
