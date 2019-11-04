public class SwInst extends LSInst {

   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public SwInst(String inst, int code) {
      super(inst, code);
   }

   @Override
   public void execute(Emulator e) {
      // put rt -> M[rs+imd]
      e.DM[e.RF.get(rs) + imd] = e.RF.get(rt);
   }

}
