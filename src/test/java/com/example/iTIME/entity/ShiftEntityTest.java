package com.example.iTIME.entity;

import com.example.iTIME.Enum.ShiftType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.util.ArrayList;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class ShiftEntityTest {
    @InjectMocks
    ShiftEntity shiftEntity;

    @Test
    void shiftEntityTest(){
        shiftEntity.setId(1);
        shiftEntity.setShiftType(ShiftType.DAY);
        shiftEntity.setEndAt("0");
        shiftEntity.setStartFrom("0");
        shiftEntity.setStartTime(Time.valueOf("00:00:00"));
        shiftEntity.setEndTime(Time.valueOf("00:00:00"));
        shiftEntity.setEmployeeEntityList(new ArrayList<>());
        shiftEntity.setShiftName("ABC");

        int id = shiftEntity.getId();
        ShiftType shiftType= shiftEntity.getShiftType();
        String endAt = shiftEntity.getEndAt();
        String startFrom = shiftEntity.getStartFrom();
        Time startTime= shiftEntity.getStartTime();
        Time endTime= shiftEntity.getEndTime();
        List<EmployeeEntity> employeeEntityList= shiftEntity.getEmployeeEntityList();
        String name = shiftEntity.getShiftName();

        Assertions.assertEquals(id,shiftEntity.getId());
        Assertions.assertEquals(shiftType,shiftEntity.getShiftType());
        Assertions.assertEquals(endAt,shiftEntity.getEndAt());
        Assertions.assertEquals(startFrom,shiftEntity.getEndAt());
        Assertions.assertEquals(startTime,shiftEntity.getStartTime());
        Assertions.assertEquals(endTime,shiftEntity.getEndTime());
        Assertions.assertEquals(employeeEntityList,shiftEntity.getEmployeeEntityList());
        Assertions.assertEquals(name,shiftEntity.getShiftName());

        ShiftEntity shiftEntity1 = new ShiftEntity();

        Assertions.assertNotNull(shiftEntity1);

    }
}
