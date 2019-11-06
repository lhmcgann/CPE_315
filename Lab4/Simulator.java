/**
* Simulates a 5-stage pipelined CPU.
*/
public class Simulator {

   private String if_id;
   private String id_ex;
   private String ex_mem;
   private String mem_wb;
   private int PC; // Simulator has it's own PC bc implementing Sim as separate from actual execution (Emulator)

   public Simulator() {
      if_id = id_ex = ex_mem = mem_wb = "empty";
      PC = 0;
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
   }

   /**
   * Print out what instruction is in each pipeline register, "empty" if none.
   */
   public void dumpPRegs() {
      System.out.println("pc\tif/id\tid/exe\texe/mem\tmem/wb");
      System.out.println(PC + " \t" + if_id + '\t' + id_ex + '\t' + ex_mem
         + '\t' + mem_wb);
   }

   public void resetSim() {
      PC = 0;
      // TODO: should c cmd clear pipeline regs to empty or leave them?
      if_id = id_ex = ex_mem = mem_wb = "empty";
   }

}
