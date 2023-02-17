package com.amazon.railwaycrossingapp.Exceptions;

import java.util.regex.Pattern;

public class InpValidator {
    public boolean validateSelection(int choice, int range) throws BadInpException {
        if (0 < choice && choice <= range) {
            return true;
        } else {
            throw new BadInpException("Invalid option selected/Option not in the given range!!");
        }
    }

    public void validateEmail(String email) throws BadEmailException {
        String regexPattern = "^(.+)@(\\S+)$";

        if (Pattern.compile(regexPattern)
                .matcher(email)
                .matches()) {
        } else {
            throw new BadEmailException("Invalid email entered");
        }
    }

    public void validate24Hours(String time) throws BadTimeException {
        if (time.length() > 2) {
            throw new BadTimeException("Please enter time in integer hours between(0-23)");
        }
        int timeInt = Integer.parseInt(time);
        if (0 > timeInt || 24 <= timeInt) {
            throw new BadTimeException("Please enter time in hours between(0-23)");
        }    }


}