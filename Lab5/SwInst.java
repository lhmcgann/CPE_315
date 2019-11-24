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
      e.DM[getMemAdr(e)] = e.RF.get(rt);
      // TODO: I think the problem has to do with a) DM indices go up by one by MIPS offsets by 4's
      //    and b) direction of +- when changing $sp (see end of MIPS Calling Convention slides)
      //    a sub note to be: MIPS code should be correct for all systems and my Emulator should go based off of that, not v.v.
      //       i.e. follow notes (decr $sp, pos offset for sw's) and change Em implementation; don't change MIPS use
   }

}
