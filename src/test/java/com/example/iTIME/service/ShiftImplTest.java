package com.example.iTIME.service;

import com.example.iTIME.DTO.ResponseWorkingHrsDTO;
import com.example.iTIME.DTO.ShiftDTO;
import com.example.iTIME.DTO.WorkHoursResponseDTO;
import com.example.iTIME.Enum.PermissionStatus;
import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.Enum.ShiftType;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PermissionTransactionEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import com.example.iTIME.entity.ShiftEntity;
import com.example.iTIME.repository.EmployeeRepo;
import com.example.iTIME.repository.PermissionTransactionRepo;
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
import java.time.LocalTime;
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
    PermissionTransactionRepo permissionTransactionRepo;


    EmployeeEntity employeeEntity = new EmployeeEntity();

    ResponseWorkingHrsDTO responseWorkingHrsDTO = new ResponseWorkingHrsDTO();

    List<PunchTypeEntity> punches = new ArrayList<>();

    PunchTypeEntity lastPunchTypeEntity = new PunchTypeEntity();

    PermissionTransactionEntity permissionTransactionEntity = new PermissionTransactionEntity();

    ShiftDTO shiftDTO = new ShiftDTO();

    ShiftEntity shiftEntity = new ShiftEntity();

    PunchTypeEntity punchTypeEntity = new PunchTypeEntity();

    WorkHoursResponseDTO workHoursResponseDTO = new WorkHoursResponseDTO();




    @BeforeEach
    void init(){
        employeeEntity.setId(1);
        punchTypeEntity.setId(1);

        shiftEntity.setId(1);
        responseWorkingHrsDTO.setLastPunchType(String.valueOf(PunchType.IN));
        responseWorkingHrsDTO.setPunchIn("11:30");
        responseWorkingHrsDTO.setPunchOut("17:00");

        workHoursResponseDTO.setPermissionHours("02:00");
        workHoursResponseDTO.setPunchIn(responseWorkingHrsDTO.getPunchIn());
        workHoursResponseDTO.setPunchOut(responseWorkingHrsDTO.getPunchOut());
        workHoursResponseDTO.setAssignedWorkHours("08:00");
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

        permissionTransactionEntity.setStartTime(Time.valueOf("08:16:00"));
        permissionTransactionEntity.setEndTime(Time.valueOf("10:15:00"));

        responseWorkingHrsDTO.setLastPunchType(String.valueOf(PunchType.OUT));

        String permissionDurationString = "00:00";

//        when(DateTimeUtil.dateTimeFormatter(any())).thenReturn(LocalDate.now());
        when(employeeRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(employeeEntity));
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(any(), any(), any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(any(), any(), any())).thenReturn(lastPunchTypeEntity);
        when(permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(any(),any(),any(),any())).thenReturn(permissionTransactionEntity);
//        when(dateTimeUtil.convertHoursAndMinutes(any())).thenReturn(permissionDurationString);
        ResponseWorkingHrsDTO actual = shiftImpl.calculateWorkingHours("1","20240409");
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
        permissionTransactionEntity.setStartTime(Time.valueOf("08:16:00"));
        permissionTransactionEntity.setEndTime(Time.valueOf("10:15:00"));

        responseWorkingHrsDTO.setWorkingHours("11:01");

//        when(dateTimeUtil.dateTimeFormatter(any())).thenReturn(LocalDate.now());
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employeeEntity));
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punchTypeEntity);
        when(permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(permissionTransactionEntity);
//        when(dateTimeUtil.convertHoursAndMinutes(any())).thenReturn("02:00");
        punchTypeEntity.setPunchType(PunchType.OUT);
        ResponseWorkingHrsDTO actual = shiftImpl.calculateWorkingHours("1","20240409");
        assertEquals(responseWorkingHrsDTO.getWorkingHours(), actual.getWorkingHours());

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
        permissionTransactionEntity.setStartTime(Time.valueOf("08:16:00"));
        permissionTransactionEntity.setEndTime(Time.valueOf("10:15:00"));

        punchTypeEntity = new PunchTypeEntity();
        punchTypeEntity.setPunchTime(Timestamp.valueOf("2024-04-10 04:00:00"));
        punchTypeEntity.setPunchType(PunchType.OUT);
        punches.add(punchTypeEntity);

        responseWorkingHrsDTO.setWorkingHours("09:01");

        responseWorkingHrsDTO.setLastPunchType(String.valueOf(PunchType.OUT));

        when(employeeRepo.findById(2)).thenReturn(Optional.of(employeeEntity));
//        when(dateTimeUtil.dateTimeFormatter(any())).thenReturn(LocalDate.now());
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punchTypeEntity);
        when(permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(permissionTransactionEntity);
//        when(dateTimeUtil.convertHoursAndMinutes(any())).thenReturn("02:00");
        punchTypeEntity.setPunchType(PunchType.OUT);
        ResponseWorkingHrsDTO actual = shiftImpl.calculateWorkingHours("2","20240409");
        assertEquals(responseWorkingHrsDTO.getWorkingHours(), actual.getWorkingHours());

    }

    @Test
    void calculateActualWorkHoursTest1(){
        when(employeeRepo.findById(1)).thenReturn(Optional.empty());
        CommonException commonException = assertThrows(CommonException.class,()->{
            shiftImpl.calculateActualWorkHours("1","20240405");
        });
        String expected = AppConstant.EMPLOYEE_NOT_FOUND;
        String actual = commonException.getMessage();
        assertEquals(expected,actual);
    }

    @Test
    void calculateActualWorkHoursTest2() throws CommonException {
        shiftEntity.setShiftType(ShiftType.DAY);
        shiftEntity.setStartTime(Time.valueOf("06:00:00"));
        shiftEntity.setEndTime(Time.valueOf("19:00:00"));
        shiftEntity.setStartFrom("2");
        shiftEntity.setEndAt("2");

        shiftDTO.setShiftType(ShiftType.DAY);

        employeeEntity.setShiftId(shiftEntity);

        workHoursResponseDTO.setPunchIn("--:--");
        workHoursResponseDTO.setPunchOut("--:--");
        workHoursResponseDTO.setAssignedWorkHours("08:00");
        workHoursResponseDTO.setActualWorkHours("--:--");

        String permissionHours = "02:00";

        responseWorkingHrsDTO.setPunchIn("08:00");
        responseWorkingHrsDTO.setPunchOut("17:00");
        responseWorkingHrsDTO.setWorkingHours("08:00");
        responseWorkingHrsDTO.setPermissionHours(permissionHours);

        permissionTransactionEntity.setStartTime(Time.valueOf("07:00:00"));
        permissionTransactionEntity.setEndTime(Time.valueOf("16:00:00"));

        String permissionHrs = "02:00";
        String assignedHrs = "08:00";

        when(employeeRepo.findById(any())).thenReturn(Optional.of(employeeEntity));
//        when(dateTimeUtil.dateTimeFormatter(any())).thenReturn(LocalDate.now());
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(any(),any(),any())).thenReturn(punchTypeEntity);
        when(permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(permissionTransactionEntity);
//        when(dateTimeUtil.convertHoursAndMinutes(any())).thenReturn(permissionHours);
        punchTypeEntity.setPunchType(PunchType.OUT);
        workHoursResponseDTO.setPermissionHours(permissionHours);
        workHoursResponseDTO.setAssignedWorkHours(assignedHrs);
        WorkHoursResponseDTO actual= shiftImpl.calculateActualWorkHours("1","20240409");
        assertEquals(workHoursResponseDTO.getPunchIn(),actual.getPunchIn());
    }

    @Test
    void calculateActualWorkHoursTest3() throws CommonException {
        shiftEntity.setShiftType(ShiftType.DAY);
        shiftEntity.setStartTime(Time.valueOf("06:00:00"));
        shiftEntity.setEndTime(Time.valueOf("19:00:00"));
        shiftEntity.setStartFrom("2");
        shiftEntity.setEndAt("2");

        shiftDTO.setShiftType(ShiftType.DAY);

        employeeEntity.setShiftId(shiftEntity);
        punchTypeEntity.setPunchTime(Timestamp.valueOf("2024-04-09 08:00:00.0"));
        punchTypeEntity.setPunchType(PunchType.IN);

        punches.add(punchTypeEntity);
        permissionTransactionEntity.setStartTime(Time.valueOf("08:16:00"));
        permissionTransactionEntity.setEndTime(Time.valueOf("10:15:00"));

        punchTypeEntity = new PunchTypeEntity();
        punchTypeEntity.setPunchTime(Timestamp.valueOf("2024-04-09 17:00:00"));
        punchTypeEntity.setPunchType(PunchType.OUT);
        punches.add(punchTypeEntity);

        workHoursResponseDTO.setPunchIn("08:00");
        workHoursResponseDTO.setPunchOut("14:00");
        workHoursResponseDTO.setAssignedWorkHours("08:00");
        workHoursResponseDTO.setActualWorkHours("06:00");

        String permissionHours = "02:00";

        responseWorkingHrsDTO.setPunchIn("08:00");
        responseWorkingHrsDTO.setPunchOut("17:00");
        responseWorkingHrsDTO.setWorkingHours("08:00");
        responseWorkingHrsDTO.setPermissionHours(permissionHours);

        permissionTransactionEntity.setStartTime(Time.valueOf("07:00:00"));
        permissionTransactionEntity.setEndTime(Time.valueOf("16:00:00"));

        String permissionHrs = "02:00";
        String assignedHrs = "08:00";

        punchTypeEntity.setPunchType(PunchType.IN);

        when(employeeRepo.findById(any())).thenReturn(Optional.of(employeeEntity));
        when(employeeRepo.findById(any())).thenReturn(Optional.of(employeeEntity));
//        when(dateTimeUtil.dateTimeFormatter(any())).thenReturn(LocalDate.now());
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(any(),any(),any())).thenReturn(punchTypeEntity);
        when(permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(permissionTransactionEntity);
//        when(dateTimeUtil.convertHoursAndMinutes(any())).thenReturn(permissionHours);
        punchTypeEntity.setPunchType(PunchType.OUT);
        workHoursResponseDTO.setPermissionHours(permissionHours);
        workHoursResponseDTO.setAssignedWorkHours(assignedHrs);
        WorkHoursResponseDTO actual= shiftImpl.calculateActualWorkHours("1","20240409");
        assertEquals(workHoursResponseDTO.getPunchIn(),actual.getPunchIn());
    }


}
