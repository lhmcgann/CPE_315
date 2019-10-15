import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Arrays;

public class Testing {
   public static void main(String[] args) {
      int val = ~1;
      int bitWidth = 16;
      String bin = String.format("%"+bitWidth+"s", Integer.toBinaryString(val)).replace(' ', '0');
      int num = Integer.parseInt(bin);
      System.out.println("bin: " + bin + ", int: " + num + ", bin int: " + Integer.toBinaryString(num));

      System.out.println(~0 + ", " + ~1);
      System.out.println(Integer.toBinaryString(~1 & 0b11111));
      System.out.println(Integer.toBinaryString(1 & 0b111));
      System.out.println(Integer.toBinaryString(~(~1 & 0b111)));
      System.out.println(Integer.toBinaryString(~0) + ", " + Integer.toBinaryString(~1));
      System.out.println(Integer.toBinaryString(0b10101 | 0b01010));
      System.out.println(Integer.toBinaryString(0b0101 | 0b01010));
      System.out.println(Integer.toBinaryString(0b11101 & 0b1010));
      System.out.println(Integer.toBinaryString(~1 & 0));
      System.out.println(Integer.toBinaryString(~1 & 0b0));
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
//    if code, replace all ',' with whitepsace before splitting
