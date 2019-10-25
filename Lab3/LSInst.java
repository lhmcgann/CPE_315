public abstract class LSInst extends IInst {

   /**
   * A Inst with an initially known string inst representation and associated opcode.
   */
   public LSInst(String inst, int code) {
      super(inst, code);

      // This is literally the only difference
      imdI = 2;
      rsI = 3;
   }

}
