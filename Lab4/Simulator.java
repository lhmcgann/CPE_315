/**
* Simulates a 5-stage pipelined CPU.
*/
public class Simulator {

   private final String EMPTY = "empty";
   private final String STALL = "stall";

   private String if_id;
   private String id_ex;
   private String ex_mem;
   private String mem_wb;
   private int PC; // Simulator has it's own PC bc implementing Sim as separate from actual execution (Emulator)
   private int ccCount;
   private int numInsts;
   private int numStalls;
   private int numSquashed;
   private boolean stalled;

   public Simulator() {
      resetSim();
   }

   /**
   * Advance the CPU through a single clock cycle.
   */
   public int runOneCC(String nextInst) {
      writeBack();
      memory();
      execute();
      decode();
      fetch(newInst);
      // TODO: does this work to tell Em of a stall so doesn't lose this "nextInst"?
      return PC;
   }

   /**
   * Runs the write back stage. Data leaves pipeline. Instruciton completes.
   */
   private void writeBack() {
      // incr inst count if an actual instruciton completes in this cycle
      if (!(mem_wb.equals(EMPTY) || mem_wb.equals(STALL)))
         numInsts++;
   }
   /**
   * Runs the memory stage. Data moves from ex_mem to mem_wb.
   */
   private void memory() {
      mem_wb = ex_mem;
   }
   /**
   * Runs the execute stage. Data moves from id_ex to ex_mem.
   */
   private void execute(){
      ex_mem = id_ex; // this happens regardless of stall
   }
   /**
   * Runs the decode/read RF stage. Data moves from if_id to id_ex.
   */
   private void decode() {
      // if use after load detected, insert a stall
      if (useAfterLoad()) {
         stalled = true;
         id_ex = STALL;
      }
      // otherwise, pass insts through pipeline as normal
      else
         id_ex = if_id;
   }
   /**
   * Runs the instruction fetch stage. Data moves from "PC" to if_id.
   */
   private void fetch(String nextInst) {
      // if not stalled, incr PC and get new inst; else CPU at this pt stays same
      if (!stalled) {
         PC++;
         if_id = nextInst;
      }
      else
         stalled = false; // stall has happened, resume normal functionality

      // either way, another clock cycle has completed
      ccCount++;
   }

   private boolean useAfterLoad() {
      // if lw followed by inst that uses (via rs or rt) the dest (rt) of lw
      return id_ex.name.equals("lw") && (id_ex.rt == if_id.rs ||
                                          id_ex.rt == if_id.rt);
      // TODO: figure out how to actually make this work: i.e. how rep pRegs, &
      // how to access rs, rt of if_id inst if don't know if even has/uses rs, rt
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
      if_id = id_ex = ex_mem = mem_wb = EMPTY;
      PC = ccCount = numInsts = numStalls = numSquashed = 0;
      stalled = false;
   }

}
