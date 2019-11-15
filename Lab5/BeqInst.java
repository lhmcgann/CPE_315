public class BeqInst extends BInst {

   /**
   * Branch on Equal
   */
   public BeqInst(String inst, int code) {
      super(inst, code);
   }

   protected boolean shouldBranch(Emulator e) {
      return e.RF.get(rs) != e.RF.get(rt);
   }

}
