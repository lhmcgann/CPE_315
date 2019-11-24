public class SwInst extends LSInst {

   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public SwInst(String inst, int code) {
      super(inst, code);
   }

   @Override
   public void emulate(Emulator e) {
   if (lab5.DEBUG) {
      System.out.println("index into DM = " + (e.RF.get(rs) + imd));
      System.out.println("value to str = " + (e.RF.get(rt)));
   }
      // put rt -> M[rs+imd]
      e.DM[e.RF.get(rs) + imd] = e.RF.get(rt);

   }

}
