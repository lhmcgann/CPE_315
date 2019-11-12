public abstract class Inst {

   protected final int INST_I = 0; // i of str representat'n of inst in instLine
   protected final int OP_SIZE = 6; // bit width
   protected final int REG_SIZE = 5; // bit width

   protected String name;
   protected int op;
   protected int lineNum;

   /**
   * A Inst with an initially known string representation and associated opcode
   */
   public Inst(String inst, int code) {
      name = inst;
      op = code;
      lineNum = 0;
   }

   /**
   * If valid arguments (i.e. #/format of registers/immediates) given for this
   *  inst, deduce/set the values of this inst's args (i.e. rs, rt, imd, etc)
   * Validation (rn) is only for registers given (checks if is indeed a reg and
   *  not just some random string)
   */
   public void processArgs(String[] line) {
      if (!validRegs(line)) {
         System.out.println("Invalid register input for instruction " + line[INST_I]);
         System.exit(1);
      }
      computeArgs(line);
   }

   public abstract void emulate(Emulator e);

   protected abstract boolean validRegs(String[] line);
   protected abstract void computeArgs(String[] line);

   public abstract String getBinInstruction();

   protected String intToBin(int val, int bitWidth) {
      String bin = String.format("%"+bitWidth+"s", Integer.toBinaryString(val)).replace(' ', '0');
      return bin.substring(bin.length() - bitWidth, bin.length());
   }

   public abstract boolean usesReg(int regNum);

   public String getName() {
      return name;
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
