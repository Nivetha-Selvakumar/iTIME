package com.example.iTIME.util;

import com.example.iTIME.Exception.CommonException;
import org.springdoc.api.ErrorMessage;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Configuration
public class DateTimeUtil {

    DateTimeUtil() {
    }

    public static LocalDate dateTimeFormatter(String date1) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(date1,dateFormatter);
    }

    public static String convertTimeStampToTime(Timestamp timestamp){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(timestamp);
    }

    public static String convertHours(Long hours, Long minutes){
        return  String.format("%02d:%02d",hours,minutes);
    }

    public static String convertHoursAndMinutes(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return  String.format("%02d:%02d",hours,minutes);
    }

    public static Timestamp convertToTimeStamp(String date) throws CommonException {
        try{
            return new Timestamp(new SimpleDateFormat(AppConstant.DATEONLY_FORMAT).parse(date).getTime());
        }catch (final ParseException e){
            throw new CommonException(String.format(AppConstant.INVALID_DATE,date));

        }
    }
}
