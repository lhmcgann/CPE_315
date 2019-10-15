import java.lang.NumberFormatException;

public class ICmd extends Cmd {

   protected final int IMD_SIZE = 16;

   // rXI: the index in a given String[] line where the value of rX can be found
   protected int rsI;
   protected int rtI;
   protected int imdI;

   protected int rt;
   protected int rs;
   protected int imd;

   /**
   * A Cmd with an initially known string cmd representation and associated opcode.
   */
   public ICmd(String cmd, int code) {
      super(cmd, code);

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
   * For normal immediate-format cmd, line[imdI] is the numeric imd itself
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
