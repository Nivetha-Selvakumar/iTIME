package com.example.iTIME.DTO;

import com.example.iTIME.Enum.ShiftType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;

@ExtendWith(MockitoExtension.class)
class ShiftDTOTest {

    @InjectMocks
    ShiftDTO shiftDTO;

    @Test
    void shiftfTest(){
        shiftDTO.setShiftType(ShiftType.DAY);
        shiftDTO.setShiftName("ABC");
        shiftDTO.setEndAt("22:00");
        shiftDTO.setStartFrom("08:00");
        shiftDTO.setEndTime(Time.valueOf("15:00:00"));
        shiftDTO.setStartTime(Time.valueOf("08:00:00"));

        ShiftType type = shiftDTO.getShiftType();
        String shiftName = shiftDTO.getShiftName();
        String endAt = shiftDTO.getEndAt();
        String startFrom = shiftDTO.getStartFrom();
        Time startTime = shiftDTO.getStartTime();
        Time endTime = shiftDTO.getEndTime();

        Assertions.assertEquals(type,shiftDTO.getShiftType());
        Assertions.assertEquals(shiftName,shiftDTO.getShiftName());
        Assertions.assertEquals(endAt,shiftDTO.getEndAt());
        Assertions.assertEquals(startFrom,shiftDTO.getStartFrom());
        Assertions.assertEquals(startTime,shiftDTO.getStartTime());
        Assertions.assertEquals(endTime,shiftDTO.getEndTime());

        ShiftDTO shiftDTO1 = new ShiftDTO();

        Assertions.assertNotNull(shiftDTO1);

    }
}
