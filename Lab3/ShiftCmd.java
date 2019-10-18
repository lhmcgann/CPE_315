import java.lang.NumberFormatException;

public class ShiftCmd extends RCmd {

   protected int shamtI;

   /**
   * A Cmd with an initially known string cmd representation and associated opcode.
   */
   public ShiftCmd(String cmd, int code) {
      super(cmd, code);

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
