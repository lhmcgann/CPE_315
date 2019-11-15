public class AddiInst extends IInst {

   /**
   * Addi inst
   */
   public AddiInst(String inst, int code) {
      super(inst, code);
   }

   @Override
   public void emulate(Emulator e) {
      e.RF.put(rt, e.RF.get(rs) + imd);
   }

}
