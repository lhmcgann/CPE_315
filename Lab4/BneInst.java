public class BneInst extends BInst {

   /**
   * Branch on Equal
   */
   public BneInst(String inst, int code) {
      super(inst, code);
   }

   protected boolean shouldBranch(Emulator e) {
      if (lab4.DEBUG) {
         System.out.println("\t\tBNE " + this + " taken: " + taken);
         System.out.println("\t\tBNE " + this + " should branch? " + (e.RF.get(rs) != e.RF.get(rt)));
      }
      return e.RF.get(rs) != e.RF.get(rt);
   }

}
