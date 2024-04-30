package com.example.iTIME.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AppConstant {

    public static final String PUNCH_SUCCESSFULLY = "Successfully Punched ";

    public static final String EMPLOYEE_NOT_FOUND = "Employee Not Found";

    public static final String NO_EMPLOYEE = "Employee ID cannot be null or empty.";

    public static final String NO_DATE = "Date cannot be null or empty";

    public static final String EMPLOYEE_MISMATCH = "Employee Id should contain only numbers";

    public static final String DATE_MISMATCH = "Date should contain only numbers and is in format YYYYMMDD";

    public static final String PUNCHTYPE_MISMATCH = "Punch Type should be IN or OUT";

    public static final String NO_PUNCH = "Punch Type cannot be null or empty";
    
    public static final String INVALID_DATE = "Invalid date. Enter correct date";

    public static final String SUCCESSFULLY_REGISTERED = "Successfully Registered";

    public static final String DATEONLY_FORMAT = "yyyyMMdd";

    public static final int FIRST_WEEK = 1;

    public static final int SECOND_WEEK = 2;

    public static final int THIRD_WEEK = 3;

    public static final int FOURTH_WEEK = 4;

    public static final int FIFTH_WEEK = 5;

    public static final int SIXTH_WEEK = 6;
    public static final String SHIFT_NOT_FOUND = "Shift Not Found";
    public static final String ILLEGAL_ASSIGNING_SHIFT = "Assigning Shift Type should only be MONTHLY or ANNUAL.  ";
}
