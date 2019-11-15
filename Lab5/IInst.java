import java.lang.NumberFormatException;

public abstract class IInst extends Inst {

   protected final int IMD_SIZE = 16;

   // rXI: the index in a given String[] line where the value of rX can be found
   protected int rsI;
   protected int rtI;
   protected int imdI;

   protected int rt;
   protected int rs;
   protected int imd;

   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public IInst(String inst, int code) {
      super(inst, code);

      rtI = 1;
      rsI = 2;
      imdI = 3;

      rt = rs = imd = -1;
   }

   protected boolean validRegs(String[] line) {
      return (Assembler.REGS.containsKey(line[rsI]) &&
            Assembler.REGS.containsKey(line[rtI]));
   }

   protected void computeArgs(String[] line) {
      rs = Assembler.REGS.get(line[rsI]);
      rt = Assembler.REGS.get(line[rtI]);

      calcImd(line);
   }

   /**
   * For normal immediate-format inst, line[imdI] is the numeric imd itself
   */
   protected void calcImd(String[] line) {
      try {
         imd = Integer.parseInt(line[imdI]);
      }
      catch (NumberFormatException e) {
         System.out.println("Invalid immediate of " + line[imdI]);
         System.exit(1);
      }
   }

   public String getBinInstruction() {
      return intToBin(op, OP_SIZE) + " " + intToBin(rs, REG_SIZE) + " " +
         intToBin(rt, REG_SIZE) + " " + intToBin(imd, IMD_SIZE);
   }

}
