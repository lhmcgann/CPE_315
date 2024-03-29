public abstract class RInst extends Inst {

   // rXI: the index in a given String[] line where the value of rX can be found
   protected int rsI;
   protected int rtI;
   protected int rdI;

   // the number representation of the desired register
   protected int rs;
   protected int rt;
   protected int rd;
   protected int shamt;
   protected int funct;

   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public RInst(String inst, int code) {
      super(inst, code);
      // bc funct only used for R-Format instructions, & opcode 0 if funct used
      funct = code;
      op = 0;

      // values for most cases
      rsI = 2;
      rtI = 3;
      rdI = 1;

      // default values
      rt = rs = rd = shamt = 0;
   }

   protected boolean validRegs(String[] line) {
      return (Assembler.REGS.containsKey(line[rsI]) &&
            Assembler.REGS.containsKey(line[rdI]) &&
            Assembler.REGS.containsKey(line[rtI]));
   }

   protected void computeArgs(String[] line) {
      rs = Assembler.REGS.get(line[rsI]);
      rd = Assembler.REGS.get(line[rdI]);
      rt = Assembler.REGS.get(line[rtI]);
      // shamt not used so don't need to change from 0 in constructor
   }

   public String getBinInstruction() {
      return intToBin(op, OP_SIZE) + " " + intToBin(rs, REG_SIZE) + " " +
         intToBin(rt, REG_SIZE) + " " + intToBin(rd, REG_SIZE) + " " +
         intToBin(shamt, REG_SIZE) + " " + intToBin(funct, OP_SIZE);
   }

}
