public abstract class Cmd {

   protected final int INST_I = 0;
   protected final int OP_SIZE = 6; // bit width
   protected final int REG_SIZE = 5; // bit width

   protected String cmd;
   protected int op;
   protected int lineNum;

   /**
   * A Cmd with an initially known string cmd representation and associated opcode.
   */
   public Cmd(String cmd, int code) {
      this.cmd = cmd;
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

   protected abstract boolean validRegs(String[] line);
   protected abstract void computeArgs(String[] line);

   public abstract String getBinInstruction();

   protected String intToBin(int val, int bitWidth) {
      
      return String.format("%"+bitWidth+"s", Integer.toBinaryString(val)).replace(' ', '0');
   }

   public String getCmd() {
      return cmd;
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
