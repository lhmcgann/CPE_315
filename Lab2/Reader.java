public class  Reader {

   private Scanner s;
   private int lineCount; // only counts lines of actual code; the COUNT not NUM
   private boolean eof;
   private int readthroughsCompleted;
   private static final String WHITESPACE = "\\s+";
   private static final ArrayList<String> CMDS = {"and", "or", "add", "addi",
      "sll", "sub", "slt", "beq", "bne", "lw", "sw", "j", "jr", "jal"};

   public Reader(File file) {
      s = new Scanner(file);
      lineCount = 0;
      eof = false;
      readthroughsCompleted = 0;
   }

   public int getLineCnt() {
      return lineCount;
   }

   public int getReadthroughsCompleted() {
      return readthroughsCompleted;
   }

   /**
   * Try reading the next line from this Reader's file.
   * @return - the next line if there is one; an empty string if eof
   */
   public String readNextLine() {
      String result = "";
      try {
         result = s.newLine();
      }
      catch (NoSuchElementException e) {
         eof = true;
      }
      return result;
   }

   public void iterateThroughLines() {
      String line;
      // read all lines in the file
      do {
         line = readNextLine();
         line = line.trim(); // remove leading and trailing whitespace
         // process this line, whatever it may be
         processLine(line);
      } while (!eof);
      // increment count of readthroughs
      readthroughsCompleted++;
   }

   public void processLine(String line) {
      String[] elements = line.split(WHITESPACE);
      // if empty or eof (i.e. line.length() == 0), do nothing
      if (isEmptyLine(line))
         return false;
      // if comment (i.e. first element starts with #), do nothing
      else if (isComment(elements))
         return false;
      // TODO: error checking for valid code cmds
      // if code (i.e. first element is a supported cmd)
      else if (isCode(elements)) {
         // if firstPass, incr lineCount
         if (readthroughsCompleted == 0 && a.isOpenLabel())
            // if "open" label, add this lineNum to table w/ "open" label
            a.addSymbol(lineCount);
         // else translate to binary
         else
            a.translate(elements);
      }
      // if label (i.e. first element has ':')
      else if (isLabel(elements) && firstPass) {
         // if firstPass, mark "open" label
         int len = elements[0].length();
         a.openLabel(elements[0].substring(0, len - 1));
         // recursive call with substring of line (i.e. process after label)
         processLine(line.substring(len));
      }
      // else, do nothing
   }

   /**
   * Continue reading lines in this Reader's file until the next non-whitespace
   *  line. Will also stop if reads EOF. The lineCount count is incremented upon
   *  finding the next line, but not if stops by EOF.
   * @return - the next actual line (trimmed); empty string if EOF
   */
   public String findNextActualLine() {
      String line;
      // read lines until a line of code is found (i.e. skip whitepsace lines)
      do {
         line = readNextLine();
         line = line.trim("\\s+"); // remove leading and trailing whitespace
      } while (!eof && line.length() == 0); // !eof && !isCodeLine
      // only increment lineCount if stopped by line, not by EOF
      if (!eof)
          lineCount++;
      return line;
   }

   /**
   * @param line - a string as returned by readNextLine: trimmed, empty if eof
   * @return - false if empty string, comment, or just label
   */
   public boolean isCodeLine(String line) {
      if (line.length() == 0)
         return false;
      else if (is label){
         if (/*is comment*/) {
            return false;
         }
         else if (/*is label*/)
         // not comment and not just label; not just label and comment
      }
   }

   private boolean isComment(String element) {
      int index = element.indexOf('#');
      return index != -1;
   }
}
