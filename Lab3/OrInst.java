public class OrInst extends RInst {

   /**
   * Add inst
   */
   public OrInst(String inst, int code) {
      super(inst, code);
   }

   public void execute(Emulator e) {
      e.RF.put(rd) = e.RF.get(rs) | e.RF.get(rt);
   }

}
