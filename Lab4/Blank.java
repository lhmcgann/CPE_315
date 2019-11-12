public class Blank extends Inst {

   /**
   * An empty Inst, used to represent a Stall or a Squash.
   */
   public Blank(String name) {
      super(name, -1);
   }

   public void emulate(Emulator e) {}

   protected boolean validRegs(String[] line) { return true; }
   protected void computeArgs(String[] line) {}

   public String getBinInstruction() { return null; }

   public boolean usesReg(int regNum) { return false; }
}
