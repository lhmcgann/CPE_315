public class lab2 {

   public static void main(String[] args) {

      // make sure a file was given
      if (!validArgs(args)) {
         System.out.println("Usage: java lab2 [filename]");
         System.exit(1);
      }

      // get filename from args[1] (args[0] is this program itself)
      Reader r = new Reader(args[1]);
      // FUNCTION: passThrough(file, actionFunction)
      // axnFnxn1: findLabel(line)
      // axnFnxn2: processCmd(line)
      r.terminate();
   }

   public static boolean validArgs(String[] args) {
      return args.length == 1;
   }
   
}
