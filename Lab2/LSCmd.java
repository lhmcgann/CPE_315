import java.lang.NumberFormatException;

public class LSCmd extends ICmd {

   /**
   * A Cmd with an initially known string cmd representation and associated opcode.
   */
   public LSCmd(String cmd, int code) {
      super(cmd, code);

      // This is literally the only difference
      imdI = 2;
      rsI = 3;
   }

}
