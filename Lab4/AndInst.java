public class AndInst extends RInst {

   /**
   * And inst
   */
   public AndInst(String inst, int code) {
      super(inst, code);
   }

   @Override
   public void emulate(Emulator e) {
      e.RF.put(rd, e.RF.get(rs) & e.RF.get(rt));
   }

}
