import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Arrays;

public class Testing {
   public static void main(String[] args) throws FileNotFoundException {

      // get filename from args[1] (args[0] is this program itself)
      File file = new File(args[0]);
      Scanner s = new Scanner(file);
      String line = s.nextLine();
      System.out.println("Length of line: " + line.length());
      System.out.println("Line: " + line);
      line = line.trim();
      System.out.println("Length of trimmed line: " + line.length());
      System.out.println("Trimmed Line: " + line);
      String[] elements = line.split("\\s+");
      System.out.println("Num elements: " + elements.length);
      System.out.println("Elements: " + Arrays.toString(elements));
      try {
         line = s.nextLine();
         System.out.println("Not EOF yet!\n");
      }
      catch (NoSuchElementException e) {
         line = "";
         System.out.println("EOF found!\n");
      }
      System.out.println("Length of line: " + line.length());
      System.out.println("Line: " + line);
      line = line.trim();
      System.out.println("Length of trimmed line: " + line.length());
      System.out.println("Trimmed Line: " + line);
      elements = line.split("\\s+");
      System.out.println("Num elements: " + elements.length);
      System.out.println("Elements: " + Arrays.toString(elements));
      s.close();
   }
}

// Results:
//    scanner.nextLine() will return an empty string if the line is just a newline
//    scanner.nextLine() will get the whole line of whitespace even if just that and \n
//    a line with only whitespace --> empty array when splitting by "\\s+"
//    leading whitespace on line w/ other text: split by "\\s+" -> 1st element = ""
//    trailing whitespace has no effect when splitting by "\\s+"
//    for leading whitespace: use trim before split to remove "" from resulting array
//    just a new line: split returns array of length 1 w/ element ""
//    for whitespace lines, trim (line length now = 0) then split("\\s+") -> [""] bc only newline char (see prev note)
