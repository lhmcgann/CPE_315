public class BeqInst extends BInst {

   /**
   * Branch on Equal
   */
   public BeqInst(String inst, int code) {
      super(inst, code);
   }

   public void execute(Emulator e) {
      if (rs == rt) {
         e.PC = labelAdr;
      }
      else
         e.PC++;
   }

}
