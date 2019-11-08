public class PipelineReg {

   private Inst inst;
   private String name;
   private int op;
   private int funct;
   private int rs;
   private int rt;
   private int rd;
   private int imd;
   private int shamt;

   public PipelineReg (Inst inst) {
      String bin = inst.getBinInstruction();
   }

}
