public class BCmd extends ICmd implements NeedsLabelAdr {

   private int labelAdr;

   /**
   * A Cmd with an initially known string cmd representation and associated opcode.
   */
   public BCmd(String cmd, int code) {
      super(cmd, code);
      rtI = 2;
      rsI = 1;

      labelAdr = -1;
   }

   /**
   * For branch immediate-format cmds, line[imdI] is the label used to calc offset
   */
   @Override
   protected void calcImd(String[] line) {
      // imd is the offset calculated from lineNum and line num the label pts to
      // TODO: figure this out!!!
      imd = labelAdr - (lineNum + 1);
   }

   @Override
   public void setLabelAdr(int labelAdr) {
      this.labelAdr = labelAdr;
   }

}
