package com.example.iTIME.service;

import com.example.iTIME.DTO.ResponseWorkingHrsDTO;
import com.example.iTIME.DTO.ShiftDTO;
import com.example.iTIME.DTO.ShiftRosterDTO;
import com.example.iTIME.DTO.WorkHoursResponseDTO;
import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.Enum.ShiftRoasterType;
import com.example.iTIME.Enum.ShiftType;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.entity.*;
import com.example.iTIME.repository.*;
import com.example.iTIME.service.impl.ShiftImpl;
import com.example.iTIME.util.AppConstant;
import com.example.iTIME.validation.Validation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
    ShiftRepo shiftRepo;

    @Mock
    ShiftRoasterRepo shiftRoasterRepo;

    @Mock
    PermissionTransactionRepo permissionTransactionRepo;

    @Mock
    Validation validation;

    EmployeeEntity employeeEntity = new EmployeeEntity();

    List<EmployeeEntity> employeeEntityList = new ArrayList<>();

    ResponseWorkingHrsDTO responseWorkingHrsDTO = new ResponseWorkingHrsDTO();

    List<PunchTypeEntity> punches = new ArrayList<>();

    PunchTypeEntity lastPunchTypeEntity = new PunchTypeEntity();

    PermissionTransactionEntity permissionTransactionEntity = new PermissionTransactionEntity();

    ShiftDTO shiftDTO = new ShiftDTO();

    ShiftEntity shiftEntity = new ShiftEntity();

    PunchTypeEntity punchTypeEntity = new PunchTypeEntity();

    WorkHoursResponseDTO workHoursResponseDTO = new WorkHoursResponseDTO();

    ShiftRosterDTO shiftRoasterDTO = new ShiftRosterDTO();

    ShiftRosterEntity shiftRosterEntity = new ShiftRosterEntity();

    List<Integer> employeeIds= new ArrayList<>();

    List<String> weekOff = new ArrayList<>();

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

        weekOff.add("SUN");
        weekOff.add("SAT");

        employeeIds.add(1);
        employeeIds.add(2);

        employeeEntity.setId(1);
        employeeEntityList.add(employeeEntity);
        employeeEntity.setId(2);
        employeeEntityList.add(employeeEntity);

        shiftRoasterDTO.setYear("2024");
        shiftRoasterDTO.setShiftType("1");
        shiftRoasterDTO.setStartDate("2024-09-01");
        shiftRoasterDTO.setEndDate("2024-09-30");
        shiftRoasterDTO.setEmployeeList(employeeIds);
        shiftRoasterDTO.setWeekOff1(weekOff);

        shiftEntity.setShiftType(ShiftType.DAY);
        shiftEntity.setStartTime(Time.valueOf("06:00:00"));
        shiftEntity.setEndTime(Time.valueOf("19:00:00"));
        shiftEntity.setStartFrom("2");
        shiftEntity.setEndAt("2");

        shiftDTO.setShiftType(ShiftType.DAY);

        employeeEntity.setShiftId(shiftEntity);

    }

    @Test
    void calculateWorkingHoursTest1(){
        when(employeeRepo.findById(1)).thenReturn(Optional.empty());
        CommonException commonException = assertThrows(CommonException.class,()->{
            shiftImpl.calculateWorkingHours("1","20240405");
        });
        String expected = AppConstant.EMPLOYEE_NOT_FOUND;
        String actual = commonException.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void calculateWorkingHoursTest2() throws CommonException {
        shiftEntity.setStartTime(Time.valueOf("08:00:00"));
        shiftEntity.setEndTime(Time.valueOf("17:00:00"));

        permissionTransactionEntity.setStartTime(Time.valueOf("08:16:00"));
        permissionTransactionEntity.setEndTime(Time.valueOf("10:15:00"));

        responseWorkingHrsDTO.setLastPunchType(String.valueOf(PunchType.OUT));

        when(employeeRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(employeeEntity));
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(any(), any(), any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(any(), any(), any())).thenReturn(lastPunchTypeEntity);
        when(permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(any(),any(),any(),any())).thenReturn(permissionTransactionEntity);
        ResponseWorkingHrsDTO actual = shiftImpl.calculateWorkingHours("1","20240409");
        Assertions.assertEquals("--:--", actual.getWorkingHours());

    }

    @Test
    void calculateWorkingHoursTest3() throws CommonException {

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

        when(employeeRepo.findById(1)).thenReturn(Optional.of(employeeEntity));
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punchTypeEntity);
        when(permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(permissionTransactionEntity);
        punchTypeEntity.setPunchType(PunchType.OUT);
        ResponseWorkingHrsDTO actual = shiftImpl.calculateWorkingHours("1","20240409");
        Assertions.assertEquals(responseWorkingHrsDTO.getWorkingHours(), actual.getWorkingHours());

    }

    @Test
    void calculateWorkingHoursTest4() throws CommonException {
        shiftEntity.setShiftType(ShiftType.NIGHT);
        shiftDTO.setShiftType(ShiftType.NIGHT);

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
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punchTypeEntity);
        when(permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(permissionTransactionEntity);
        punchTypeEntity.setPunchType(PunchType.OUT);
        ResponseWorkingHrsDTO actual = shiftImpl.calculateWorkingHours("2","20240409");
        Assertions.assertEquals(responseWorkingHrsDTO.getWorkingHours(), actual.getWorkingHours());

    }

    @Test
    void calculateActualWorkHoursTest1(){
        when(employeeRepo.findById(1)).thenReturn(Optional.empty());
        CommonException commonException = assertThrows(CommonException.class,()->{
            shiftImpl.calculateActualWorkHours("1","20240405");
        });
        String expected = AppConstant.EMPLOYEE_NOT_FOUND;
        String actual = commonException.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void calculateActualWorkHoursTest2() throws CommonException {

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

        String assignedHrs = "08:00";

        when(employeeRepo.findById(any())).thenReturn(Optional.of(employeeEntity));
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(any(),any(),any())).thenReturn(punchTypeEntity);
        when(permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(permissionTransactionEntity);
        punchTypeEntity.setPunchType(PunchType.OUT);
        workHoursResponseDTO.setPermissionHours(permissionHours);
        workHoursResponseDTO.setAssignedWorkHours(assignedHrs);
        WorkHoursResponseDTO actual= shiftImpl.calculateActualWorkHours("1","20240409");
        Assertions.assertEquals(workHoursResponseDTO.getPunchIn(), actual.getPunchIn());
    }

    @Test
    void calculateActualWorkHoursTest3() throws CommonException {

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

        String assignedHrs = "08:00";

        punchTypeEntity.setPunchType(PunchType.IN);

        when(employeeRepo.findById(any())).thenReturn(Optional.of(employeeEntity));
        when(employeeRepo.findById(any())).thenReturn(Optional.of(employeeEntity));
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(any(),any(),any())).thenReturn(punchTypeEntity);
        when(permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(permissionTransactionEntity);
        punchTypeEntity.setPunchType(PunchType.OUT);
        workHoursResponseDTO.setPermissionHours(permissionHours);
        workHoursResponseDTO.setAssignedWorkHours(assignedHrs);
        WorkHoursResponseDTO actual= shiftImpl.calculateActualWorkHours("1","20240409");
        Assertions.assertEquals(workHoursResponseDTO.getPunchIn(), actual.getPunchIn());
    }

    @Test
    void calculateActualWorkHoursTest4() throws CommonException {
        employeeEntity.setShiftId(shiftEntity);
        punchTypeEntity.setPunchTime(Timestamp.valueOf("2024-04-09 07:00:00.0"));
        punchTypeEntity.setPunchType(PunchType.IN);

        punches.add(punchTypeEntity);

        punchTypeEntity = new PunchTypeEntity();
        punchTypeEntity.setPunchTime(Timestamp.valueOf("2024-04-09 15:00:00"));
        punchTypeEntity.setPunchType(PunchType.OUT);
        punches.add(punchTypeEntity);

        workHoursResponseDTO.setPunchIn("07:00");
        workHoursResponseDTO.setPunchOut("20:00");

        String permissionHours = "00:00";

        responseWorkingHrsDTO.setPunchIn("07:00");
        responseWorkingHrsDTO.setPunchOut("20:00");
        responseWorkingHrsDTO.setWorkingHours("08:00");
        responseWorkingHrsDTO.setPermissionHours(permissionHours);

        permissionTransactionEntity.setStartTime(Time.valueOf("10:00:00"));
        permissionTransactionEntity.setEndTime(Time.valueOf("02:00:00"));

        shiftEntity.setStartFrom("0");
        shiftEntity.setEndAt("0");

        String assignedHrs = "08:00";

        punchTypeEntity.setPunchType(PunchType.IN);

        when(employeeRepo.findById(any())).thenReturn(Optional.of(employeeEntity));
        when(employeeRepo.findById(any())).thenReturn(Optional.of(employeeEntity));
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(any(),any(),any())).thenReturn(punchTypeEntity);
        when(permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(permissionTransactionEntity);
        punchTypeEntity.setPunchType(PunchType.OUT);
        workHoursResponseDTO.setPermissionHours(permissionHours);
        workHoursResponseDTO.setAssignedWorkHours(assignedHrs);
        WorkHoursResponseDTO actual= shiftImpl.calculateActualWorkHours("1","20240409");
        Assertions.assertEquals(workHoursResponseDTO.getPunchIn(), actual.getPunchIn());
    }

    @Test
    void calculateActualWorkHoursTest5() throws CommonException {
        employeeEntity.setShiftId(shiftEntity);
        punchTypeEntity.setPunchTime(Timestamp.valueOf("2024-04-09 07:00:00.0"));
        punchTypeEntity.setPunchType(PunchType.IN);

        punches.add(punchTypeEntity);

        punchTypeEntity = new PunchTypeEntity();
        punchTypeEntity.setPunchTime(Timestamp.valueOf("2024-04-09 15:00:00"));
        punchTypeEntity.setPunchType(PunchType.OUT);
        punches.add(punchTypeEntity);

        workHoursResponseDTO.setPunchIn("07:00");
        workHoursResponseDTO.setPunchOut("17:00");
        workHoursResponseDTO.setAssignedWorkHours("08:00");

        String permissionHours = "00:00";

        responseWorkingHrsDTO.setPunchIn("10:00");
        responseWorkingHrsDTO.setPunchOut("17:00");
        responseWorkingHrsDTO.setWorkingHours("08:00");
        responseWorkingHrsDTO.setPermissionHours(permissionHours);

        permissionTransactionEntity.setStartTime(Time.valueOf("07:00:00"));
        permissionTransactionEntity.setEndTime(Time.valueOf("08:00:00"));

        String assignedHrs = "04:00";

        punchTypeEntity.setPunchType(PunchType.IN);

        when(employeeRepo.findById(any())).thenReturn(Optional.of(employeeEntity));
        when(employeeRepo.findById(any())).thenReturn(Optional.of(employeeEntity));
        when(punchTypeRepo.findByEmpIdAndPunchTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(punches);
        when(punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(any(),any(),any())).thenReturn(punchTypeEntity);
        when(permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(permissionTransactionEntity);
        punchTypeEntity.setPunchType(PunchType.OUT);
        workHoursResponseDTO.setAssignedWorkHours(assignedHrs);
        WorkHoursResponseDTO actual= shiftImpl.calculateActualWorkHours("1","20240409");
        Assertions.assertEquals(workHoursResponseDTO.getPunchIn(), actual.getPunchIn());
    }


    @Test
    void shiftAssignEmployeeTest(){
        shiftRoasterDTO.setYear("2024");
        when(employeeRepo.findById(Mockito.any())).thenReturn(Optional.empty());
        CommonException commonException = assertThrows(CommonException.class,()->{
            shiftImpl.shiftAssign("1",shiftRoasterDTO);
        });
        Assertions.assertEquals(AppConstant.EMPLOYEE_NOT_FOUND, commonException.getMessage());
    }

    @Test
    void shiftAssignShiftEntityTest(){
        shiftRoasterDTO.setYear("2024");
        shiftRoasterDTO.setShiftType("1");
        when(employeeRepo.findById(Mockito.any())).thenReturn(Optional.of(employeeEntity));
        when(shiftRepo.findById(Mockito.any())).thenReturn(Optional.empty());
        CommonException commonException = assertThrows(CommonException.class,()->{
            shiftImpl.shiftAssign("1",shiftRoasterDTO);
        });
        Assertions.assertEquals(AppConstant.SHIFT_NOT_FOUND, commonException.getMessage());
    }

    @Test
    void shiftAssignMonthlyTest() throws CommonException {

        shiftRoasterDTO.setAssignShiftType(String.valueOf(ShiftRoasterType.MONTHLY));
        shiftRoasterDTO.setMonth("9");

        when(employeeRepo.findById(Mockito.any())).thenReturn(Optional.of(employeeEntity));
        when(shiftRepo.findById(Mockito.any())).thenReturn(Optional.of(shiftEntity));
        validation.checkEmployeeList(Mockito.anyList());
        when(shiftRoasterRepo.findByEmpIdAndMonthAndYear(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(Optional.of(shiftRosterEntity));
        shiftImpl.shiftAssign("1",shiftRoasterDTO);
        verify(shiftRoasterRepo,Mockito.atLeastOnce()).save(shiftRosterEntity);
    }

    @Test
    void shiftAssignAnnualTest() throws CommonException {
        shiftRoasterDTO.setAssignShiftType(String.valueOf(ShiftRoasterType.ANNUAL));
        when(employeeRepo.findById(Mockito.any())).thenReturn(Optional.of(employeeEntity));
        when(shiftRepo.findById(Mockito.any())).thenReturn(Optional.of(shiftEntity));
        validation.checkEmployeeList(Mockito.anyList());
        when(shiftRoasterRepo.findByEmpIdAndMonthAndYear(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(Optional.of(shiftRosterEntity));
        shiftImpl.shiftAssign("1",shiftRoasterDTO);
        verify(shiftRoasterRepo,Mockito.atLeastOnce()).save(shiftRosterEntity);
    }

    @Test
    void shiftAssignNoMonthlyAnnualTest() throws CommonException {
        shiftRoasterDTO.setAssignShiftType("monthly");
        when(employeeRepo.findById(Mockito.any())).thenReturn(Optional.of(employeeEntity));
        when(shiftRepo.findById(Mockito.any())).thenReturn(Optional.of(shiftEntity));
        validation.checkEmployeeList(Mockito.anyList());
        CommonException commonException = assertThrows(CommonException.class,()->
                shiftImpl.shiftAssign("1",shiftRoasterDTO));
        String expected = AppConstant.ILLEGAL_ASSIGNING_SHIFT;
        assertEquals(expected,commonException.getMessage());
    }

    @Test
    void shiftAssignShiftRosterEntityMonthlyNullTest() throws CommonException {

        shiftRoasterDTO.setAssignShiftType(String.valueOf(ShiftRoasterType.MONTHLY));
        shiftRoasterDTO.setMonth("9");
        when(employeeRepo.findById(Mockito.any())).thenReturn(Optional.of(employeeEntity));
        when(shiftRepo.findById(Mockito.any())).thenReturn(Optional.of(shiftEntity));
        validation.checkEmployeeList(Mockito.anyList());
        when(shiftRoasterRepo.findByEmpIdAndMonthAndYear(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(Optional.empty());
        shiftImpl.shiftAssign("1",shiftRoasterDTO);
        verify(shiftRoasterRepo,Mockito.atLeastOnce()).findByEmpIdAndMonthAndYear(Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    void shiftAssignShiftRosterEntityAnnualNullTest() throws CommonException {

        shiftRoasterDTO.setAssignShiftType(String.valueOf(ShiftRoasterType.ANNUAL));
        when(employeeRepo.findById(Mockito.any())).thenReturn(Optional.of(employeeEntity));
        when(shiftRepo.findById(Mockito.any())).thenReturn(Optional.of(shiftEntity));
        validation.checkEmployeeList(Mockito.anyList());
        when(shiftRoasterRepo.findByEmpIdAndMonthAndYear(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(Optional.empty());
        shiftImpl.shiftAssign("1",shiftRoasterDTO);
        verify(shiftRoasterRepo,Mockito.atLeastOnce()).findByEmpIdAndMonthAndYear(Mockito.any(),Mockito.any(),Mockito.any());
    }



}

