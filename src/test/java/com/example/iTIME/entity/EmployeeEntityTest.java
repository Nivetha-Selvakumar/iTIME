package com.example.iTIME.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
class EmployeeEntityTest {
    @InjectMocks
    EmployeeEntity employeeEntity;

    @Mock
    ShiftEntity shiftEntity;

    @Test
    void testEmployee(){
        shiftEntity.setId(1);

        employeeEntity.setId(1);
        employeeEntity.setShiftId(shiftEntity);
        employeeEntity.setEmpCode("01");
        employeeEntity.setPunchTypeEntityList(new ArrayList<>());
        employeeEntity.setPermissionTransactionEntityList(new ArrayList<>());
        employeeEntity.setPunchTypeEntityList(new ArrayList<>());
        employeeEntity.setEmpName("Nive");

        int id = employeeEntity.getId();
        String empName = employeeEntity.getEmpName();
        String empCode = employeeEntity.getEmpCode();
        ShiftEntity shift = employeeEntity.getShiftId();
        List<PunchTypeEntity> punchTypeEntityList = employeeEntity.getPunchTypeEntityList();
        List<ShiftRosterEntity> shiftRosterEntityList = employeeEntity.getShiftRosterEntityList();
        List<PermissionTransactionEntity> permissionTransactionEntityList = employeeEntity.getPermissionTransactionEntityList();

        Assertions.assertEquals(Optional.of(id), Optional.of(employeeEntity.getId()));
        Assertions.assertEquals(empName,employeeEntity.getEmpName());
        Assertions.assertEquals(empCode,employeeEntity.getEmpCode());
        Assertions.assertEquals(shift,employeeEntity.getShiftId());
        Assertions.assertEquals(punchTypeEntityList,employeeEntity.getPunchTypeEntityList());
        Assertions.assertEquals(shiftRosterEntityList,employeeEntity.getShiftRosterEntityList());
        Assertions.assertEquals(permissionTransactionEntityList,employeeEntity.getPermissionTransactionEntityList());
    }

    @Test
    void testNoArgsConstructorEmployeeEntity(){
        EmployeeEntity employeeEntity1 = new EmployeeEntity();
        assertNotNull(employeeEntity1);

    }

}
