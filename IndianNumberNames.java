package algorithms;

import java.util.ArrayList;

/**
 * Utility class to convert numbers into their word representation 
 * based on the Indian numbering system (e.g., thousand, lakh, crore).
 * 
 * Supports values up to and including 1 crore.
 */
public class NumberNames {

    // Names of single-digit numbers (1–9)
    private String singleDigitNames[] = {
        "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"
    };

    // Names of two-digit numbers from 10 to 19
    private String teens[] = {
        "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen",
        "sixteen", "seventeen", "eighteen", "nineteen"
    };

    // Names for tens place (20, 30, ..., 90)
    private String tensPlaceNames[] = {
        "", "", "twenty", "thirty", "forty", "fifty",
        "sixty", "seventy", "eighty", "ninety"
    };

    /**
     * Positional labels based on digit place.
     * Note: The index in this array maps to the digit position from right (units) to left.
     * Positions: [units, tens, hundreds, thousands, ten-thousands, lakhs, ten-lakhs, crores]
     */
    private String digitPlaceNames[] = {
        "", "", "hundred", "thousand", "thousand", "lakh", "lakh", "crore", ""
    };

    /**
     * Converts a number to its equivalent name in the Indian numbering format.
     * 
     * @param number The numeric value to be converted.
     * @return The number in words.
     */
    public String numberToNumberName(long number) {
        ArrayList<Integer> digits = new ArrayList<>();

        long n = number;

        // Break number into digits and store in reverse order (units digit first)
        while (n > 0) {
            digits.add((int) n % 10);
            n = n / 10;
        }

        System.out.println(digits); // For debugging
        return getNumberNames(digits, digits.size());
    }

    /**
     * Recursive method to convert digits to their word form with appropriate positions.
     * 
     * @param digits       List of digits in reverse order (units first)
     * @param digitLength  Total number of digits
     * @return             String representation in words
     */
    private String getNumberNames(ArrayList<Integer> digits, int digitLength) {

        String numName = "";

        // Handle single-digit numbers
        if (digits.size() >= 1)
            numName = singleDigitNames[digits.get(0)] + numName;

        // Handle two-digit numbers less than 20 (teens)
        if (digits.size() >= 2 && digits.get(1) == 1)
            numName = teens[digits.get(0)];

        // Handle two-digit numbers 20 and above
        else if (digits.size() >= 2 && digits.get(1) > 1)
            numName = tensPlaceNames[digits.get(1)] + numName;


        // Append positional label (e.g., hundred, thousand, lakh)
        if (digits.size() >= 1 && digits.get(0) != 0)
            numName = numName + digitPlaceNames[digitLength - digits.size()];

        // Recursive call for numbers with more than 2 digits
        if (digits.size() >= 3) {

            // Create sublist: First group of 3 digits, then 2 digits thereafter
            ArrayList<Integer> subarr = new ArrayList<>(
                digits.subList(
//                		catch2 - handle the places that 3 for 100s place and then 2 till a crore then pattern repeats
                    digits.size() == digitLength ? 3 : 2, 
                    digits.size()
                )
            );

            System.out.println(subarr); // For debugging

            numName = getNumberNames(subarr, digitLength) +
                    ((digits.size() == digitLength && digits.get(2) != 0)
                        ? singleDigitNames[digits.get(2)] + digitPlaceNames[2]
                        : "")
                    + numName;
        }

        return numName;
    }

    public static void main(String[] args) {
        long number = 121325;
        NumberNames n = new NumberNames();
        System.out.println("In Indian Currency Words: " + n.numberToNumberName(number));
    }
}
