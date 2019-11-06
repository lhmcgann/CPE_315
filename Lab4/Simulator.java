/**
* Simulates a 5-stage pipelined CPU.
*/
public class Simulator {

   private String if_id;
   private String id_ex;
   private String ex_mem;
   private String mem_wb;
   private int PC; // Simulator has it's own PC bc implementing Sim as separate from actual execution (Emulator)
   private int ccCount;
   private int numInsts;
   private int numStalls;
   private int numSquashed;

   public Simulator() {
      resetSim();
   }

   /**
   * Advance the CPU through a single clock cycle.
   */
   public void runOneCC(String nextInst) {
      PC++; // increment the PC first, just as you would in a real CPU
      mem_wb = ex_mem;
      ex_mem = id_ex;
      id_ex = if_id;
      if_id = nextInst;
      ccCount++; // increase the count of instruction cycles no matter what

      // the max and -4 is bc it takes 4cc's for the first inst to complete
      numInsts = Math.max(ccCount - 4 - numStalls - numSquashed, 0);
   }

   /**
   * @return - the number of clock cycles run up to this point
   */
   public int getCCCount() {
      return ccCount;
   }

   /**
   * @return - the number of insts successfully completed up to this point
   */
   public int numInsts() {
      return numInsts;
   }

   /**
   * @return - the CPI (cycles per instruction) at this point in time
   */
   public double getCPI() {
      return ((float) ccCount) / ((float) numInsts);
   }

   /**
   * Print out what instruction is in each pipeline register, "empty" if none.
   */
   public void dumpPRegs() {
      System.out.println("pc\tif/id\tid/exe\texe/mem\tmem/wb");
      System.out.println(PC + " \t" + if_id + '\t' + id_ex + '\t' + ex_mem
         + '\t' + mem_wb);
   }

   /**
   * Completely resets this Simulator: pipeline regs, PC, everything.
   */
   public void resetSim() {
      if_id = id_ex = ex_mem = mem_wb = "empty";
      PC = ccCount = numInsts = numStalls = numSquashed = 0;
   }

}
