package com.example.iTIME.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
class DateTimeUtilTest {
    @InjectMocks
    DateTimeUtil dateTimeUtil;


    @Test
    void dateTimeFormatterTest(){
        String input = "20240404";
        LocalDate expected = LocalDate.of(2024,04,04);
        LocalDate formatter = dateTimeUtil.dateTimeFormatter(input);
        assertEquals(expected,formatter);
    }

    @Test
    void convertTimeStampToTime(){
        Timestamp input = Timestamp.valueOf("2024-04-09 11:32:40");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String expected = formatter.format(input);
        String actual = dateTimeUtil.convertTimeStampToTime(input);
        assertEquals(expected,actual);

    }

    @Test
    void convertHours(){
        Long hours = (long) 11.3609;
        Long mins = (long) 38.000;
        String expected = String.format("%02d:%02d",hours,mins);
        String actual = dateTimeUtil.convertHours(hours,mins);
        assertEquals(expected,actual);

    }

}
