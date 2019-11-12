public class LwInst extends LSInst {

   private boolean ual;

   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public LwInst(String inst, int code) {
      super(inst, code);
      ual = false;
   }

   @Override
   public void emulate(Emulator e) {
      // put M[rs+imd] -> rt
      e.RF.put(rt, e.DM[e.RF.get(rs) + imd]);
   }

   public void markUAL() {
      ual = true;
   }

   public boolean isUAL() {
      return ual;
   }

}
