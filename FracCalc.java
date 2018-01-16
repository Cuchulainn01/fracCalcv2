import java.util.*;
 
public class FracCalc { //el main
   public static void main(String[] args) {
      Scanner scanny = new Scanner(System.in);
      System.out.println("Welcome to FracCalc! To exit, please type 'quit'");
      System.out.println("What's new with the update: Fixed fatal bug with multiple character input. Added test cases.");
      boolean userIsDone = false;
      while (userIsDone == false) {
         String userin = scanny.nextLine();
         if (userin.equals("test")) {
            System.out.println(test());
         }
         else if (userin.equals("quit")) {
            System.out.println("Goodbye!");
            userIsDone = true;
         }
         else {
            System.out.println(produceAnswer(userin));
         }
      }
   }
   public static String produceAnswer(String input) { //actually calls the input processing you'll see further down, also calls the math functions, and
      String[] in = input.split("\\s");               //calls a method that handles the negatives.
      String preNeg = "";
      if (in.length % 2 == 0) {
         return "You seem to have an extra operator without an operand, or maybe two operands without an operator in beween.";
      }
      String processedString = processInput(input);
      String[] processedArray = processedString.split(" ");
      if (in[1].contains("+")) {
         preNeg = add(processedString, processedArray);
         preNeg = sanitizeOutput(preNeg);
      }
      if (in[1].contains("-")) {
         preNeg = subtract(processedString, processedArray);
         preNeg = sanitizeOutput(preNeg);
      }
      if (in[1].contains("*")) {
         preNeg = multiply(processedString, processedArray);
         preNeg = sanitizeOutput(preNeg);
      }
      if (in[1].contains("/")) {
         preNeg = divide(processedString, processedArray);
         preNeg = sanitizeOutput(preNeg);
      }
      return preNeg;
   }
   public static String sanitizeOutput(String preNeg) { //handles negatives so that the gcd function doesn't have to.
      String[] split = preNeg.split("/");
      String ans = "";
      if ((split[0].contains("-"))&&(split[1].contains("-"))) {
         preNeg = preNeg.replaceAll("-", "");
         ans = reduce(preNeg);
         return ans;
      }
      if (preNeg.contains("-")) {
         preNeg = preNeg.replaceAll("-", "");
         ans = reduce(preNeg);
         ans = "-" + ans;
         return ans;
      }
      else {
         ans = reduce(preNeg);
         return ans;
      }
   }
   public static String reduce(String preReduc) { //using the gcd found in the gcd method to actually reduce the fraction
      String[] reduction = preReduc.split("/");
      String ans = "";
      int whole = 0;
      int gcd;
      int numer = Integer.parseInt(reduction[0]);
      int denomer = Integer.parseInt(reduction[1]);
      gcd = gcd(numer, denomer);
      reduction[0] = "" + (numer / gcd);
      reduction[1] = "" + (denomer / gcd);
      ans = reduction[0] + "/" + reduction[1];
 
      if (reduction[0].equals(reduction[1])) {
         ans = "1";
      }
      if (reduction[1].equals("1")) {
         ans = reduction[0];
      }
      else {
         ans = reduction[0] + "/" + reduction[1];
      } //everything below here is turning an improper fraction into "whole_numerator/denominator", or "whole"
      while (Integer.parseInt(reduction[0]) >= Integer.parseInt(reduction[1])) {
         reduction[0] = Integer.parseInt(reduction[0]) - Integer.parseInt(reduction[1]) + "";
         whole ++;
      }
      if (whole > 0) {
         ans = whole + "_" + reduction[0] + "/" + reduction[1];
      }
      if (reduction[0].equals("0")) {
         ans = whole + "";
      }
      return ans;
   }
   public static int gcd(int a, int b) {//recursive algorithm finding the gcd
      if (a == 0 || b == 0) {
         return Math.max(Math.abs(a), Math.abs(b));
      } 
      else {
         return gcd(b, a % b);
      }
   }
   public static String add(String input, String[] inputSplit) { //parses the now processed fraction, adds. Has sister methods for the other operations
      String[] FirstFrac = inputSplit[0].split("/");             //specifically, the add finds a common denominator, and then adds the numerators.
      int num1 = Integer.parseInt(FirstFrac[0]);
      int den1 = Integer.parseInt(FirstFrac[1]);
      String[] SecondFrac = inputSplit[2].split("/");
      int num2 = Integer.parseInt(SecondFrac[0]);
      int den2 = Integer.parseInt(SecondFrac[1]);
      num2 *= den1;
      num1 *= den2;
      den2 *= den1;
      int numanswer = num1 + num2;
      int denanswer = den2;
      return numanswer + "/" + denanswer;
   }
   public static String subtract(String input, String[] inputSplit) {//subtraction works the same as additon, except with subtraction. Common denom is the same
      String[] FirstFrac = inputSplit[0].split("/");
      int num1 = Integer.parseInt(FirstFrac[0]);
      int den1 = Integer.parseInt(FirstFrac[1]);
      String[] SecondFrac = inputSplit[2].split("/");
      int num2 = Integer.parseInt(SecondFrac[0]);
      int den2 = Integer.parseInt(SecondFrac[1]);
      num2 *= den1;
      num1 *= den2;
      den2 *= den1;
      int numanswer = num1 - num2;
      int denanswer = den2;
      return numanswer + "/" + denanswer;
   }
   public static String multiply(String input, String[] inputSplit) { //Easiest of the 4 functions. Numerator times numerator, denominator times denominator
      String[] FirstFrac = inputSplit[0].split("/");
      int num1 = Integer.parseInt(FirstFrac[0]);
      int den1 = Integer.parseInt(FirstFrac[1]);
      String[] SecondFrac = inputSplit[2].split("/");
      int num2 = Integer.parseInt(SecondFrac[0]);
      int den2 = Integer.parseInt(SecondFrac[1]);
      int numanswer = num1 * num2;
      int denanswer = den1 * den2;
      return numanswer + "/" + denanswer;
   }
   public static String divide(String input, String[] inputSplit) {//dividing a fraction is the same as multiplying by the reciprical. Thats what this does.
      String[] FirstFrac = inputSplit[0].split("/");
      int num1 = Integer.parseInt(FirstFrac[0]);
      int den1 = Integer.parseInt(FirstFrac[1]);
      String[] SecondFrac = inputSplit[2].split("/");
      int num2 = Integer.parseInt(SecondFrac[0]);
      int den2 = Integer.parseInt(SecondFrac[1]);
      int numanswer = num1 * den2;
      int denanswer = den1 * num2;
      return numanswer + "/" + denanswer;
   }
   public static String processInput(String ogIn) { //takes mixed fractions and turns them into improper fractions. Makes everything else easier.
      String update = "";
      String[] in = ogIn.split(" ");
      for(int arrayIndex = 0; arrayIndex <= in.length - 1; arrayIndex += 2) {
         if (in[arrayIndex].contains("_")) {
            String[] mixAndFraction = in[arrayIndex].split("_");
            int whole = Integer.parseInt(mixAndFraction[0]);
            String[] justFraction = mixAndFraction[1].split("/");
            int numerator = Integer.parseInt(justFraction[0]);
            int denominator = Integer.parseInt(justFraction[1]);
            int temp = whole * denominator;
            if (temp < 0) {
               temp = Math.abs(temp);
               numerator += temp;
               numerator = -1 * numerator;
            }
            else {
               numerator += temp;
            }
            in[arrayIndex] = numerator + "/" + denominator;
         }
         else if (!(in[arrayIndex].contains("/"))) {
            in[arrayIndex] = in[arrayIndex] + "/1";
         }
      }
      update = in[0] + " " + in[1] + " " + in[2];
      return update;
   }
   public static String test() {
      String[] cases = {"10/4 + 2/2", "3 + 4", "2_2/3 - 1_1/3", "3/2 * -2/4", "3/2 / -2/4", "-7/5 / 5/-7", "-1_1/2 + 0", "-38_3/72 + -4_82/37", "2/9456 - 3_435/43556", "2/9456 * 3_435/43556", "2/9456 / 3_435/43556", "2134 + 1/78", "-1_1/2 + -1_1/2", "556_235/205 - 706_302/5461"}; //first set of tests
      String[] answers = {"3_1/2", "7", "1_1/3", "-3/4", "-3", "1_24/25", "-1_1/2", "-44_229/888", "-3_503281/51483192", "43701/68644256", "10889/154963746", "2134_1/78", "-3", "-148_203517/223901"}; //answer key
      String[] attempt = new String[cases.length];
      for (int i = 0; i < cases.length; i++) {
         attempt[i] = produceAnswer(cases[i]);
         System.out.println(produceAnswer(cases[i]) + " = " + answers[i]);
      }
      if (Arrays.equals(answers, attempt)) {
         return "Kein problem Kumpel!"; //no problem buddy!
      }
      else {
         return "Ein (paar) problem(e)..."; //a (few) problem(s)
      }
   }
}