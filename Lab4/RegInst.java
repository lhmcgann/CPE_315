public abstract class RegInst extends Inst {

   // rXI: the index in a given String[] line where the value of rX can be found
   protected int rsI;
   protected int rtI;

   // the number representation of the desired register
   public int rs;
   public int rt;

   public RegInst(String name, int code) {
      super(name, code);
      rs = rt = -1;
   }

}
