package dB;

public class Validator {

    private static int PASSWORD_LENGTH = 8;

    public static boolean isValidNameLength(String firstName) {
        if(firstName.length() < 50){
            return true;
        }
        return false;
    }

    public static boolean isAlphabeticWithSpaceAndDots(String stringValue) {

        int alphabetCount = 0, spaceCount = 0, dotCount = 0, totalCount = 0;

        for(int i = 0; i < stringValue.length(); i++) {
            if(stringValue.charAt(i) >= '0' && stringValue.charAt(i) <= '9') {
                return false;
            }

            if(isAlphabetic(stringValue.charAt(i))) {
                ++alphabetCount;
            } else if(stringValue.charAt(i) == '.') {
                ++dotCount;
            } else if(stringValue.charAt(i) == ' ') {
                ++spaceCount;
            }
        }

        totalCount = alphabetCount + dotCount + spaceCount;

        if(totalCount == dotCount || totalCount == spaceCount || totalCount == (spaceCount+dotCount) ||
                spaceCount > alphabetCount || dotCount > alphabetCount) {
            return false;
        }
        return true;
    }

    public static boolean isAlphabetic(char ch) {
        ch = Character.toUpperCase(ch);
        return (ch >= 'A' && ch <= 'Z');
    }

    public static boolean isNumeric(char ch) {
        return (ch >= '0' && ch <= '9');
    }

    public static boolean isNumeric(String stringValue) {
        for(int i = 0; i < stringValue.length(); i++) {
            if(!isNumeric(stringValue.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidAccNoLength(long accNo) {
        return accNo <= 100000000 && accNo >= 999;
    }
    public static boolean isPositive(long number) {
        return number > 0;
    }

    public static boolean isValidPassword(String password) {

        if (password.length() < PASSWORD_LENGTH) {
            return false;
        }

        int charCount = 0;
        int numCount = 0;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);

            if (isNumeric(ch)) {
                numCount++;
            } else if (isAlphabetic(ch)) {
                charCount++;
            } else {
                return false;
            }
        }
        return (charCount >= 2 && numCount >= 2);
    }

    public static boolean arePasswordsMatching(String password, String confirmedPassword) {
        return password.equals(confirmedPassword);
    }

}
