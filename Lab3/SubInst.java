public class SubInst extends RInst {

   /**
   * Sub inst
   */
   public SubInst(String inst, int code) {
      super(inst, code);
   }

   @Override
   public void execute(Emulator e) {
      e.RF.put(rd, e.RF.get(rs) - e.RF.get(rt));
   }

}
