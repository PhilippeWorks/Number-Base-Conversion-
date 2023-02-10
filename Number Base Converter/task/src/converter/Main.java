package converter;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;

/* goal convert fractions
 * 1. split input into integer and fractional ex: 1.1 to 1 and 0.1
 * 2. convert each independently to target base
 * 3. join() both numbers to get result
 * */

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        String[] values;
        StringBuilder result;
        int sourceBase;
        int targetBase;
        boolean keepGoing;

        while (true) {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            input = scanner.nextLine();

            if (input.equals("/exit")) {
                break;
            }

            keepGoing = true;

            try {
                values = input.split(" ");
                sourceBase = Integer.parseInt(values[0]);
                System.out.println("sourcebase: " + sourceBase);
                targetBase = Integer.parseInt(values[1]);
                System.out.println("targetBase: " + targetBase);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            do {
                System.out.println("Enter number in base " +
                        sourceBase + " to convert to base " + targetBase +
                        " (To go back type /back) ");

                input = scanner.nextLine();

                if (input.equals("/back")) {
                    keepGoing = false;
                } else if (!input.contains(".")){
                    result = new StringBuilder("Conversion result: ");
                    result.append(new BigInteger(input, sourceBase).toString(targetBase));
                    System.out.println(result + "\n");
                } else {
                    String[] inputValues = input.split("\\.");
                    String decimalPart = new BigInteger(inputValues[0], sourceBase).toString(targetBase);
                    String fractionalPart = "";

                    //fractional to base 10 conversion
                    char[] fractionCharArr = inputValues[1].toCharArray();
                    double charSum = 0.0;
                    int j = - 1;
                    for (int i = 0; i < fractionCharArr.length; i++) {
                        charSum = charSum + (Double.parseDouble(String.valueOf(Character.getNumericValue(fractionCharArr[i]))) * Math.pow(sourceBase, j));
                        j--;
                    }

                    //base10 to target base conversion
                    BigDecimal base10number = new BigDecimal(charSum, new MathContext(5, RoundingMode.HALF_DOWN));
                    BigDecimal remainderResult = base10number.multiply(new BigDecimal(targetBase));

                    while (fractionalPart.length() < 5) {
                        int value = 0;
                        if (!(charSum == 0.0)) {
                            value = Integer.parseInt(remainderResult.toPlainString().substring(0, remainderResult.toPlainString().indexOf(".")));
                        }
                        if (String.valueOf(value).length() > 1) {
                            fractionalPart = fractionalPart.concat(new BigInteger(String.valueOf(value)).toString(targetBase));
                        } else {
                            fractionalPart = fractionalPart.concat(String.valueOf(value));
                        }
                        base10number = remainderResult.subtract(new BigDecimal(value));
                        remainderResult = base10number.multiply(new BigDecimal(targetBase));
                    }


                    result = new StringBuilder(
                            "Conversion result: " +
                                    decimalPart +
                                    "." +
                                    fractionalPart);

                    System.out.println(result + "\n");
                }

            } while (keepGoing);
        }
    }
}