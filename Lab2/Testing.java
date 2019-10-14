import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Arrays;

public class Testing {
   public static void main(String[] args) {
      String line = "label: cmd      $30, blah       , hi   #   comment me!!!";
      line = line.trim();
      String[] elements = line.split("\\s+");
      int len = elements[0].length();
      System.out.println("Elements: " + Arrays.toString(elements));
      System.out.println("E[0].sub(0, len-1): " + elements[0].substring(0, len - 1));
      System.out.println("line.sub(len):" + line.substring(len));
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

//    my substringing with the labels works
//    TODO: if code, replace all ',' with whitepsace before splitting
