public abstract class Inst {

   protected final int INST_I = 0;
   protected final int OP_SIZE = 6; // bit width
   protected final int REG_SIZE = 5; // bit width

   protected String inst;
   protected int op;
   protected int lineNum;

   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public Inst(String inst, int code) {
      this.inst = inst;
      op = code;
      lineNum = 0;
   }

   public void processArgs(String[] line) {
      if (!validRegs(line)) {
         System.out.println("Invalid register input given for instruction " + line[INST_I]);
         System.exit(1);
      }
      computeArgs(line);
   }

   public abstract void execute(Emulator e);

   protected abstract boolean validRegs(String[] line);
   protected abstract void computeArgs(String[] line);

   public abstract String getBinInstruction();

   protected String intToBin(int val, int bitWidth) {
      String bin = String.format("%"+bitWidth+"s", Integer.toBinaryString(val)).replace(' ', '0');
      return bin.substring(bin.length() - bitWidth, bin.length());
   }

   public String getInst() {
      return inst;
   }

   public int getOpcode() {
      return op;
   }

   public int getLineNum() {
      return lineNum;
   }

   public void setLineNum(int lineNum) {
      this.lineNum = lineNum;
   }

}
