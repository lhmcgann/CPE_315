public class OrInst extends RInst {

   /**
   * Or inst
   */
   public OrInst(String inst, int code) {
      super(inst, code);
   }

   @Override
   public void execute(Emulator e) {
      e.RF.put(rd, e.RF.get(rs) | e.RF.get(rt));
   }

}
