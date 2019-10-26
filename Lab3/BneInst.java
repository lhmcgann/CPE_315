public class BneInst extends BInst {

   /**
   * Branch on Equal
   */
   public BneInst(String inst, int code) {
      super(inst, code);
   }

   protected boolean shouldBranch(Emulator e) {
      return e.RF.get(rs) != e.RF.get(rt);
   }

}
