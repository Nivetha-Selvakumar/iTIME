package com.example.iTIME.DTO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class ShiftRosterDTOTest {

    @InjectMocks
    ShiftRosterDTO shiftRosterDTO;

    @Test
    void  shiftRosterTest(){
        shiftRosterDTO.setEmployeeList(new ArrayList<>());
        shiftRosterDTO.setShiftType("General");
        shiftRosterDTO.setAssignShiftType("MONTHLY");
        shiftRosterDTO.setStartDate("2024-04-04");
        shiftRosterDTO.setEndDate("2024-04-30");
        shiftRosterDTO.setMonth("5");
        shiftRosterDTO.setYear("2024");
        shiftRosterDTO.setWeekOff1(new ArrayList<>());
        shiftRosterDTO.setWeekOff2(new ArrayList<>());
        shiftRosterDTO.setWeekOff3(new ArrayList<>());
        shiftRosterDTO.setWeekOff4(new ArrayList<>());
        shiftRosterDTO.setWeekOff5(new ArrayList<>());
        shiftRosterDTO.setWeekOff6(new ArrayList<>());

        List<Integer> empList = shiftRosterDTO.getEmployeeList();
        String shift = shiftRosterDTO.getShiftType();
        String assign = shiftRosterDTO.getAssignShiftType();
        String startDate = shiftRosterDTO.getStartDate();
        String endDate = shiftRosterDTO.getEndDate();
        String month = shiftRosterDTO.getMonth();
        String year = shiftRosterDTO.getYear();
        List<String> weekOff1 = shiftRosterDTO.getWeekOff1();
        List<String> weekOff2 = shiftRosterDTO.getWeekOff2();
        List<String> weekOff3 = shiftRosterDTO.getWeekOff3();
        List<String> weekOff4 = shiftRosterDTO.getWeekOff4();
        List<String> weekOff5 = shiftRosterDTO.getWeekOff5();
        List<String> weekOff6 = shiftRosterDTO.getWeekOff6();

        Assertions.assertEquals(empList,shiftRosterDTO.getEmployeeList());
        Assertions.assertEquals(shift,shiftRosterDTO.getShiftType());
        Assertions.assertEquals(assign,shiftRosterDTO.getAssignShiftType());
        Assertions.assertEquals(startDate,shiftRosterDTO.getStartDate());
        Assertions.assertEquals(endDate,shiftRosterDTO.getEndDate());
        Assertions.assertEquals(month,shiftRosterDTO.getMonth());
        Assertions.assertEquals(year,shiftRosterDTO.getYear());
        Assertions.assertEquals(weekOff1,shiftRosterDTO.getWeekOff1());
        Assertions.assertEquals(weekOff2,shiftRosterDTO.getWeekOff2());
        Assertions.assertEquals(weekOff3,shiftRosterDTO.getWeekOff3());
        Assertions.assertEquals(weekOff4,shiftRosterDTO.getWeekOff4());
        Assertions.assertEquals(weekOff5,shiftRosterDTO.getWeekOff5());
        Assertions.assertEquals(weekOff6,shiftRosterDTO.getWeekOff6());

        ShiftRosterDTO shiftRosterDTO1 = new ShiftRosterDTO();

        Assertions.assertNotNull(shiftRosterDTO1);


    }
}
