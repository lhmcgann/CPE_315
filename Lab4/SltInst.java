public class SltInst extends RInst {

   /**
   * Set Less Than inst
   */
   public SltInst(String inst, int code) {
      super(inst, code);
   }

   @Override
   public void emulate(Emulator e) {
      if (e.RF.get(rs) < e.RF.get(rt))
         e.RF.put(rd, 1);
      else
         e.RF.put(rd, 0);
   }

}
