public class Stall extends Inst {

   /**
   * An empty Inst representing a Stall
   */
   public Stall() {
      super("stall", -1);
   }

   public void emulate(Emulator e) {}

   protected boolean validRegs(String[] line) { return true; }
   protected void computeArgs(String[] line) {}

   public String getBinInstruction() { return null; }
}
