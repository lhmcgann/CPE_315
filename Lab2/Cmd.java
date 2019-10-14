public class Cmd {

   public enum Format {
      R,
      I,
      J
   }

   private String cmd;
   private int op;
   private int rt;
   private int rs;
   private int rd;
   private int shamt;
   private int funct;
   private Format format;

   /**
   * @param code - funct if R-Format instruction, otherwise the opcode
   */
   public Cmd(String cmd, int code, Format f) {
      this.cmd = cmd;
      // bc funct only used for R-Format instructions, & opcode 0 if funct used
      if (format == Format.R) {
         funct = code;
         op = 0;
      }
      else {
         op = code;
         funct = -1;
      }
      rt = rs = rd = shamt = -1;
      format = f;
   }

   public String getCmd() {
      return cmd;
   }

   public int getOpcode() {
      return op;
   }

   public int getRt() {
      return rt;
   }

   public int setRt(int newRt) {
      rt = newRt;
   }

   public int getRs() {
      return rs;
   }

   public int setRs(int newRs) {
      rs = newRs;
   }

   public int getRd() {
      return rd;
   }

   public int setRd(int newRd) {
      rd = newRd;
   }

   public int getShamt() {
      return shamt;
   }

   public int setShamt(int newShamt) {
      shamt = newShamt;
   }

   public Format getFormat() {
      return format;
   }

}
