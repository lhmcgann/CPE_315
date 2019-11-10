import java.util.ArrayList;

/**
* Simulates a 5-stage pipelined CPU.
*/
public class Simulator {

   private final String EMPTY = "empty";

   private Inst if_id;
   private Inst id_ex;
   private Inst ex_mem;
   private Inst mem_wb;
   private int PC; // Simulator has it's own PC bc implementing Sim as separate from actual execution (Emulator)
   private int ccCount;
   private int numInsts;
   private int numStalls;
   private int numSquashed;
   private boolean stalled;
   private boolean squashed;

   public Simulator() {
      resetSim();
   }

   /**
   * Advance the CPU through a single clock cycle.
   * @param IM - the Inst Mem mapping lineNum to the actual Inst; so sim can
   *  access next Inst using its own PC, not the Emulator's
   */
   public void runOneCC(ArrayList<Inst> IM) {
      writeBack();
      memory();
      execute();
      decode();
      fetch(IM.get(PC));
      if (lab4.DEBUG) {
         System.out.println("\tSim PC: " + PC);
      }
   }

   /**
   * Runs the write back stage. Data leaves pipeline. Instruciton completes.
   */
   private void writeBack() {
      // incr inst count if an actual instruciton completes in this cycle
      if (!(mem_wb == null || (mem_wb instanceof Stall) || (mem_wb instanceof Squash)))
         numInsts++;
   }
   /**
   * Runs the memory stage. Data moves from ex_mem to mem_wb.
   */
   private void memory() {
      mem_wb = ex_mem; // happens regardless of squash, but this is where squash happens
      int adr;
      if ((adr = branchTaken()) != -1) {
         squashed = true;
         // prev 3 PRegs get squashed
         if_id = id_ex = ex_mem = new Squash();
         PC = adr; // this will not be overwritten later
      }
   }
   /**
   * Runs the execute stage. Data moves from id_ex to ex_mem.
   */
   private void execute() {
      if (!squashed)
         ex_mem = id_ex; // this happens regardless of stall
   }
   /**
   * Runs the decode/read RF stage. Data moves from if_id to id_ex.
   */
   private void decode() {
      // if use after load detected, insert a stall
      if (useAfterLoad()) {
         stalled = true;
         id_ex = new Stall();
      }
      // otherwise, pass insts through pipeline as normal (if not already squashed)
      else if (!squashed)
         id_ex = if_id;
   }
   /**
   * Runs the instruction fetch stage. Data moves from "PC" to if_id.
   * @param nextInst - the next Inst to enter the pipeline (is at the current PC)
   */
   private void fetch(Inst nextInst) {
      // if jump, squash one inst
      int adr;
      if ((adr = jump()) != -1) {
         squashed = true;
         if_id = new Squash();
         PC = adr;
      }
      // now see if next inst goes into pipeline
      if (!(stalled || squashed)) {
         PC++;
         if_id = nextInst;
      }
      else {
         // PC, if_id don't change; either way (squash, stall, both), all now false
         stalled = false; // stall taken care of
         squashed = false; //squashing taken care of
      }

      ccCount++; // no matter what, another clock cycle has completed
   }

   /**
   * @return - if taken, the adr to jump to; -1 if not a br or br not taken
   */
   private int branchTaken() {
      if (!(mem_wb instanceof BInst))
         return -1;
      // downcast so can check taken
      BInst br = (BInst) mem_wb;

      // return next adr if taken, -1 if not
      if (br.taken)
         return br.getLabelAdr();
      return -1;
   }

   /**
   * @return - true if a lw is followed by an inst that uses the lw's dest reg
   */
   private boolean useAfterLoad() {
      // only proceed if there's actually a lw inst
      if (!(id_ex instanceof LwInst))
         return false;
      // only proceed if following inst is an RInst or IInst bc will have rs, rt
      if (!(if_id instanceof RegInst))
         return false;

      // downcast so can access regs
      LwInst lw = (LwInst) id_ex;
      RegInst nextInst = (RegInst) if_id;

      // check to see if any regs match
      return (lw.rt == nextInst.rs || lw.rt == nextInst.rt);
   }

   /**
   * @return - the address to jump to, -1 if not a jump
   */
   private int jump() {
      // if j or jal, return the label adr
      if (if_id instanceof JInst) {
         JInst j = (JInst) if_id;
         return j.getLabelAdr();
      }
      // if jr, use that get adr fnxn
      if (if_id instanceof JrInst) {
         JrInst jr = (JrInst) if_id;
         return jr.getAdr();
      }
      return -1; // not any kind of jump
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
      System.out.println(PC + " \t" + printPReg(if_id) + '\t' + printPReg(id_ex)
         + '\t' + printPReg(ex_mem) + '\t' + printPReg(mem_wb));
   }

   /**
   * @param pReg - the pipeline reg whose inst we want the name of
   * @return - the String representation of the inst in a given pReg
   */
   private String printPReg(Inst pReg) {
      if (pReg == null)
         return EMPTY;
      else return pReg.getName();
   }

   /**
   * Completely resets this Simulator: pipeline regs, PC, everything.
   */
   public void resetSim() {
      if_id = id_ex = ex_mem = mem_wb = null;
      PC = ccCount = numInsts = numStalls = numSquashed = 0;
      stalled = squashed = false;
   }

}
