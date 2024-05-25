package com.example.iTIME.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AppConstant {

    public static final String PUNCH_SUCCESSFULLY = "Successfully Punched ";

    public static final String EMPLOYEE_NOT_FOUND = "Employee Not Found";

    public static final String NO_EMPLOYEE = "Employee ID cannot be null or empty.";

    public static final String INVALID_START_DATE = "Invalid Start Date. Enter the date in YYYY-MM-DD format";

    public static final String INVALID_END_DATE = "Invalid End Date. Enter the date in YYYY-MM-DD format";

    public static final String EMPLOYEE_MISMATCH = "Employee Id should contain only numbers";

    public static final String PUNCHTYPE_MISMATCH = "Punch Type should be IN or OUT";

    public static final String NO_PUNCH = "Punch Type cannot be null or empty";
    
    public static final String INVALID_DATE = "Invalid date. Enter correct date";

    public static final String SUCCESSFULLY_REGISTERED = "Successfully Registered";

    public static final  String NULL_FORMAT = "--:--";

    public static final  String ZERO_FORMAT = "00:00";

    public static final int FIRST_WEEK = 1;

    public static final int SECOND_WEEK = 2;

    public static final int THIRD_WEEK = 3;

    public static final int FOURTH_WEEK = 4;

    public static final int FIFTH_WEEK = 5;

    public static final int SIXTH_WEEK = 6;

    public static final String SHIFT_NOT_FOUND = "Shift Not Found";

    public static final String ILLEGAL_ASSIGNING_SHIFT = "Assigning Shift Type should only be MONTHLY or ANNUAL.  ";

    public static final String INVALID_EMPLOYEE_IN_LIST = "One of the Employee is Not Found";

    public static final String IN = "IN";

    public static final String OUT = "OUT";

    public static final String DATE_REGEX = "^(?:19|20)\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])$";

    public static final String ASSIGN_SHIFT_NOT_FOUND = "Assign Shift Type should only be MONTHLY or ANNUAL";

    public static final String INVALID_MONTH = "Invalid Month.Enter month in MM format";

    public static final String INVALID_YEAR = "Invalid Year. Enter year in YYYY format";

    public static final String YEAR_DOES_NOT_MATCHES_DATE = "Year does not matches with the date.";

    public static final String END_DATE_AFTER_START_DATE = "End Date is before start date.";

    public static final String MONTH_DOES_NOT_MATCHES_DATE = "Month does not matches with the date." ;

    public static final String WEEK_OFF_DAY_INVALID = "Entered week off invalid. Days should be SUN,MON,TUE,WED,THU,FRI,SAT";

    public static final String DATE_VALIDATION_REGEX = "^(?:19|20)\\d{2}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\\d|3[01])$";

    public static final String MONTH_VALIDATION_REGEX = "^(?:0[1-9]|1[0-2])$" ;

    public static final String YEAR_VALIDATION_REGEX = "^(?:19|20)\\d{2}$";
}
