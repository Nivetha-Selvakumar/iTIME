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

    private EnumDayOfWeek(String day, String value) {
        this.day = day;
        this.value =value;

    }
    public String getValue() {
        return value;
    }

    public String getDay(){
        return day;
    }

    public static EnumDayOfWeek valuesOf(String value){
        for (EnumDayOfWeek d :values()){
            if(d.value.equals(value)){
                return d;
            }
        }
        return null;
    }
    public static EnumDayOfWeek getByDay(String day){
        for (EnumDayOfWeek d :values()){
            if(d.day.equals(day)){
                return d;
            }
        }
        return null;
    }
}
