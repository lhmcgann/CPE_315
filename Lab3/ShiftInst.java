import java.lang.NumberFormatException;

public class ShiftInst extends RInst {

   protected int shamtI;

   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public ShiftInst(String inst, int code) {
      super(inst, code);

      // overwrite rXI values; leave rsI bc don't use anyway
      rtI = 2;
      rdI = 1;
      shamtI = 3;
   }

   @Override
   protected boolean validRegs(String[] line) {
      return (Assembler.REGS.containsKey(line[rdI]) &&
            Assembler.REGS.containsKey(line[rtI]));
   }

   @Override
   protected void computeArgs(String[] line) {
      // rs not used so don't need to change from 0 in constructor
      rd = Assembler.REGS.get(line[rdI]);
      rt = Assembler.REGS.get(line[rtI]);
      try {
         shamt = Integer.parseInt(line[shamtI]);
      }
      catch (NumberFormatException e) {
         System.out.println("Invalid shift immediate of " + line[shamtI]);
         System.exit(1);
      }
   }

}
