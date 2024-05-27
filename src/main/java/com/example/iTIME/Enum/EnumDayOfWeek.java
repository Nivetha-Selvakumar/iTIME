package com.example.iTIME.Enum;

public enum EnumDayOfWeek {
    SUN("SUN", "SUNDAY"),
    MON("MON","MONDAY"),
    TUE("TUE","TUESDAY"),

    WED("WED","WEDNESDAY"),

    THU("THU", "THURSDAY"),

    FRI("FRI","FRIDAY"),

    SAT("SAT","SATURDAY");

    String value;
    String day;

    EnumDayOfWeek(String day, String value) {
        this.day = day;
        this.value =value;

    }
    public String getValue() {
        return value;
    }

    public String getDay(){
        return day;
    }

}
