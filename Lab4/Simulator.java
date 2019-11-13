import java.util.ArrayList;

/**
* Simulates a 5-stage pipelined CPU.
*/
public class Simulator {

   private final String EMPTY = "empty";
   private final String STALL = "stall";
   private final String SQUASH = "squash";

   private Inst if_id;
   private Inst id_ex;
   private Inst ex_mem;
   private Inst mem_wb;
   private int PC; // Simulator has it's own PC bc implementing Sim as separate from actual execution (Emulator)
   private int ccCount;
   private int numInsts;
   private boolean stalled;
   private boolean squashed;

   /**
   * Completely resets this Simulator: pipeline regs, PC, everything.
   */
   public void resetSim() {
      if_id = id_ex = ex_mem = mem_wb = new Blank(EMPTY);
      PC = ccCount = numInsts = 0;
      stalled = squashed = false;
   }

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
      fetch(getNextInst(IM));

      if (lab4.DEBUG)
         System.out.println("\tSim PC: "+PC+'\n');
   }

   /**
   * Call when reached end of the program to push last insts through CPU.
   */
   public void flushCPU(ArrayList<Inst> IM) {
      while (!mem_wb.getName().equals(EMPTY))
         runOneCC(IM);
   }

   /**
   * Runs the write back stage. Data leaves pipeline. Instruciton completes.
   */
   private void writeBack() {
      // incr inst count if an actual instruciton completes in this cycle
      if (!(mem_wb instanceof Blank)) {
         numInsts++;
         if (lab4.DEBUG) {
            System.out.println("\tNum Insts: " + numInsts);
         }
      }
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
         if_id = id_ex = ex_mem = new Blank(SQUASH);
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
         id_ex = new Blank(STALL);
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
         // squashed = true; // don't need to do this bc no other stages need to know; the work is done here
         if_id = new Blank(SQUASH);
         PC = adr;
      }
      // now see if next inst goes into pipeline
      else if (!(stalled || squashed)) {
         PC++;
         if_id = nextInst;
      }
      else {
         // PC, if_id don't change; either way (squash, stall, both), all now false
         stalled = false; // stall taken care of
         squashed = false; //squashing taken care of
      }

      ccCount++; // no matter what, another clock cycle has completed
      if (lab4.DEBUG) {
         System.out.println("\tCC: " + ccCount);
      }
   }

   /**
   * @param IM - Instruction Memory from which to get the next inst
   * @return - the next Inst to put in the pipeline; an "empty" Inst if reached
   *  end of program
   */
   private Inst getNextInst(ArrayList<Inst> IM) {
      if (PC < IM.size())
         return IM.get(PC);
      else return new Blank(EMPTY);
   }

   /**
   * @return - if taken, the adr to jump to; -1 if not a br or br not taken
   */
   private int branchTaken() {
      if (!(mem_wb instanceof BInst))
         return -1;
      // downcast so can check taken
      BInst br = (BInst) mem_wb;

      if (lab4.DEBUG)
         System.out.println("\t\tBNE " + br + " should branch? " + br.taken);

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
      if ((id_ex instanceof LwInst)) {
         LwInst lw = (LwInst) id_ex;
         return lw.isUAL();
      }
      return false;
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
      System.out.println("\npc\tif/id\tid/exe\texe/mem\tmem/wb");
      System.out.println(PC + " \t" + printPReg(if_id) + '\t' + printPReg(id_ex)
         + '\t' + printPReg(ex_mem) + '\t' + printPReg(mem_wb));
   }

   /**
   * @param pReg - the pipeline reg whose inst we want the name of
   * @return - the String representation of the inst in a given pReg
   */
   private String printPReg(Inst pReg) {
      return pReg.getName();
   }

}
