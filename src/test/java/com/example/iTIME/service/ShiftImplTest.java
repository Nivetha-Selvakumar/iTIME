package com.example.iTIME.service;

import com.example.iTIME.DTO.ResponseWorkHrsDTO;
import com.example.iTIME.DTO.ShiftDTO;
import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.Enum.ShiftType;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import com.example.iTIME.entity.ShiftEntity;
import com.example.iTIME.repository.EmployeeRepo;
import com.example.iTIME.repository.PunchTypeRepo;
import com.example.iTIME.service.impl.ShiftImpl;
import com.example.iTIME.util.AppConstant;
import com.example.iTIME.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShiftImplTest {
    @InjectMocks
    ShiftImpl shiftImpl;

    @Mock
    PunchTypeRepo punchTypeRepo;

    @Mock
    EmployeeRepo employeeRepo;

    @Mock
    DateTimeUtil dateTimeUtil;


    EmployeeEntity employeeEntity = new EmployeeEntity();

    ResponseWorkHrsDTO responseWorkHrsDTO = new ResponseWorkHrsDTO();

    List<PunchTypeEntity> punches = new ArrayList<>();

    PunchTypeEntity lastPunchTypeEntity = new PunchTypeEntity();


    ShiftDTO shiftDTO = new ShiftDTO();

    ShiftEntity shiftEntity = new ShiftEntity();

    PunchTypeEntity punchTypeEntity = new PunchTypeEntity();


    @BeforeEach
    void init(){
        employeeEntity.setId(1);
        punchTypeEntity.setId(1);

        shiftEntity.setId(1);
        responseWorkHrsDTO.setLastPunchType(String.valueOf(PunchType.IN));
        responseWorkHrsDTO.setPunchIn("11:30");
        responseWorkHrsDTO.setPunchOut("17:00");
    }

    @Test
    void calculateWorkingHoursTest1(){
        when(employeeRepo.findById(1)).thenReturn(Optional.empty());
        CommonException commonException = assertThrows(CommonException.class,()->{
            shiftImpl.calculateWorkingHours("1","20240405");
        });
        String expected = AppConstant.EMPLOYEE_NOT_FOUND;
        String actual = commonException.getMessage();
        assertEquals(expected,actual);
    }

    @Test
    void calculateWorkingHoursTest2() throws CommonException {
        shiftEntity.setShiftType(ShiftType.DAY);
        shiftDTO.setShiftType(ShiftType.DAY);
        employeeEntity.setShiftId(shiftEntity);

        shiftEntity.setStartTime(Time.valueOf("08:00:00"));
        shiftEntity.setEndTime(Time.valueOf("17:00:00"));

        shiftEntity.setStartFrom("2");
        shiftEntity.setEndAt("2");

        responseWorkHrsDTO.setLastPunchType(String.valueOf(PunchType.OUT));

        when(dateTimeUtil.dateTimeFormatter(any())).thenReturn(LocalDate.now());
        when(employeeRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(employeeEntity));
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(any(), any(), any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(any(), any(), any())).thenReturn(lastPunchTypeEntity);

        ResponseWorkHrsDTO actual = shiftImpl.calculateWorkingHours("1","20240405");
        assertEquals("--:--",actual.getWorkingHours());

    }

    @Test
    void calculateWorkingHoursTest3() throws CommonException {
        shiftEntity.setShiftType(ShiftType.DAY);
        shiftEntity.setStartTime(Time.valueOf("06:00:00"));
        shiftEntity.setEndTime(Time.valueOf("19:00:00"));
        shiftEntity.setStartFrom("2");
        shiftEntity.setEndAt("2");

        shiftDTO.setShiftType(ShiftType.DAY);

        employeeEntity.setShiftId(shiftEntity);

        punchTypeEntity.setPunchTime(Timestamp.valueOf("2024-04-09 06:00:00"));
        punchTypeEntity.setPunchType(PunchType.IN);

        punches.add(punchTypeEntity);
        punchTypeEntity = new PunchTypeEntity();
        punchTypeEntity.setPunchTime(Timestamp.valueOf("2024-04-09 19:00:00"));
        punchTypeEntity.setPunchType(PunchType.OUT);
        punches.add(punchTypeEntity);

        when(dateTimeUtil.dateTimeFormatter(any())).thenReturn(LocalDate.now());
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employeeEntity));
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(employeeEntity,Timestamp.valueOf("2024-04-09 04:00:00.0"),Timestamp.valueOf("2024-04-09 21:00:00.0"))).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(employeeEntity,Timestamp.valueOf("2024-04-09 04:00:00.0"),Timestamp.valueOf("2024-04-09 21:00:00.0"))).thenReturn(punchTypeEntity);
        punchTypeEntity.setPunchType(PunchType.OUT);
        ResponseWorkHrsDTO actual = shiftImpl.calculateWorkingHours("1","20240409");
        assertEquals(responseWorkHrsDTO.getWorkingHours(), actual.getPunchIn());

    }

    @Test
    void calculateWorkingHoursTest4() throws CommonException {
        employeeEntity.setId(2);

        shiftEntity.setShiftType(ShiftType.NIGHT);
        shiftDTO.setShiftType(ShiftType.NIGHT);
        employeeEntity.setShiftId(shiftEntity);

        shiftEntity.setStartTime(Time.valueOf("17:00:00"));
        shiftEntity.setEndTime(Time.valueOf("04:00:00"));

        shiftEntity.setStartFrom("2");
        shiftEntity.setEndAt("2");

        punchTypeEntity.setPunchTime(Timestamp.valueOf("2024-04-09 17:00:00.0"));
        punchTypeEntity.setPunchType(PunchType.IN);

        punches.add(punchTypeEntity);

        punchTypeEntity = new PunchTypeEntity();
        punchTypeEntity.setPunchTime(Timestamp.valueOf("2024-04-10 04:00:00"));
        punchTypeEntity.setPunchType(PunchType.OUT);
        punches.add(punchTypeEntity);

        responseWorkHrsDTO.setLastPunchType(String.valueOf(PunchType.OUT));


        when(employeeRepo.findById(2)).thenReturn(Optional.of(employeeEntity));
        when(dateTimeUtil.dateTimeFormatter(any())).thenReturn(LocalDate.now());
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(employeeEntity,Timestamp.valueOf("2024-04-08 15:00:00.0"),Timestamp.valueOf("2024-04-10 06:00:00.0"))).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(employeeEntity,Timestamp.valueOf("2024-04-08 15:00:00.0"),Timestamp.valueOf("2024-04-10 06:00:00.0"))).thenReturn(punchTypeEntity);
        punchTypeEntity.setPunchType(PunchType.OUT);
        ResponseWorkHrsDTO actual = shiftImpl.calculateWorkingHours("2","20240409");
        assertEquals(responseWorkHrsDTO.getWorkingHours(), actual.getPunchIn());

    }




}
