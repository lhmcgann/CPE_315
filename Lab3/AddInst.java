public class AddInst extends RInst {

   /**
   * Add inst
   */
   public AddInst(String inst, int code) {
      super(inst, code);
   }

   public void execute(Emulator e) {
      e.RF.put(rd, e.RF.get(rs) + e.RF.get(rt));
   }

}
