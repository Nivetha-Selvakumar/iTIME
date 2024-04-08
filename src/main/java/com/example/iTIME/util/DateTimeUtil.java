package com.example.iTIME.util;

import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Configuration
public class DateTimeUtil {
    public LocalDate dateTimeFormatter(String date1) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date=LocalDate.parse(date1,dateFormatter);
        return date;
    }

    public String convertTimeStampToTime(Timestamp timestamp){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(timestamp);
    }

    public String convertHours(Long hours, Long minutes){
        String format = String.format("%02d:%02d",hours,minutes);
        return format;
    }


}
