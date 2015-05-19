package hu.exclusive.crm.report;

import java.text.DecimalFormat;

public class HungarianNumbers {

    private static final String[] tensNames = { "", "tizen", "huszon", "harminc", "negyven", "ötven", "hatvan", "hetven",
            "nyolcvan", "kilencven" };

    private static final String[] numNames = { "", "egy", "kettő", "három", "négy", "öt", "hat", "hét", "nyolc", "kilenc", "tíz" };

    private HungarianNumbers() {
    }

    private static String convertLessThanOneThousand(int number) {
        String soFar;

        if (number % 100 < 10) {
            soFar = numNames[number % 100];
            number /= 100;
        } else {
            soFar = numNames[number % 10];
            number /= 10;

            soFar = tensNames[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0)
            return soFar;

        return number == 1 ? "száz" + soFar : numNames[number] + "száz" + soFar;
    }

    public static String convert(long number) {
        // 0 to 999 999 999 999
        if (number == 0) {
            return "zéró";
        }

        String snumber = Long.toString(number);

        // pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        // XXXnnnnnnnnn
        int billions = Integer.parseInt(snumber.substring(0, 3));
        // nnnXXXnnnnnn
        int millions = Integer.parseInt(snumber.substring(3, 6));
        // nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9, 12));

        String tradBillions;
        switch (billions) {
        case 0:
            tradBillions = "";
            break;
        case 1:
            tradBillions = convertLessThanOneThousand(billions) + " milliárd ";
            break;
        default:
            tradBillions = convertLessThanOneThousand(billions) + " milliárd ";
        }
        String result = tradBillions;

        String tradMillions;
        switch (millions) {
        case 0:
            tradMillions = "";
            break;
        case 1:
            tradMillions = convertLessThanOneThousand(millions) + " millió ";
            break;
        default:
            tradMillions = convertLessThanOneThousand(millions) + " millió ";
        }
        result = result + tradMillions;

        String tradHundredThousands;
        switch (hundredThousands) {
        case 0:
            tradHundredThousands = "";
            break;
        case 1:
            tradHundredThousands = "ezer-";
            break;
        default:
            tradHundredThousands = convertLessThanOneThousand(hundredThousands) + "ezer-";
        }
        result = result + tradHundredThousands;

        String tradThousand;
        tradThousand = convertLessThanOneThousand(thousands);
        result = result + tradThousand;

        // remove extra spaces!
        return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }

    /**
     * testing
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("*** " + HungarianNumbers.convert(0));
        System.out.println("*** " + HungarianNumbers.convert(1));
        System.out.println("*** " + HungarianNumbers.convert(16));
        System.out.println("*** " + HungarianNumbers.convert(100));
        System.out.println("*** " + HungarianNumbers.convert(800));
        System.out.println("*** " + HungarianNumbers.convert(80133));
        System.out.println("*** " + HungarianNumbers.convert(131688));

        /*
         * ** zero
         * ** one
         * ** sixteen
         * ** one hundred
         * ** one hundred eighteen
         * ** two hundred
         * ** two hundred nineteen
         * ** eight hundred
         * ** eight hundred one
         * ** one thousand three hundred sixteen
         * ** one million
         * ** two millions
         * ** three millions two hundred
         * ** seven hundred thousand
         * ** nine millions
         * ** nine millions one thousand
         * ** one hundred twenty three millions four hundred
         * * fifty six thousand seven hundred eighty nine
         * ** two billion one hundred forty seven millions
         * * four hundred eighty three thousand six hundred forty seven
         * ** three billion ten
         */
    }
}
