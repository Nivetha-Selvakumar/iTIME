package com.example.iTIME.validation;

import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import com.example.iTIME.repository.EmployeeRepo;
import com.example.iTIME.repository.PunchTypeRepo;
import com.example.iTIME.util.AppConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationTest {
    @InjectMocks
    Validation validation;

    @Mock
    EmployeeRepo employeeRepo;

    @Mock
    PunchTypeRepo punchTypeRepo;

    EmployeeEntity employeeEntity = new EmployeeEntity();

    PunchTypeEntity punchTypeEntity = new PunchTypeEntity();

    List<EmployeeEntity> employeeEntityList = new ArrayList<>();

    List<Integer> employeeIdList = new ArrayList<>();

    @BeforeEach
    void init(){

        employeeEntity.setId(1);
        employeeEntityList.add(employeeEntity);
        employeeEntity.setId(2);
        employeeEntityList.add(employeeEntity);

        employeeIdList.add(1);
        employeeIdList.add(2);

    }

    @Test
    void checkEmployeeIdTest(){
        when(employeeRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(employeeEntity));
        assertTrue(validation.checkEmployeeId(1));
    }

    @Test
    void checkPunchTest(){
        when(punchTypeRepo.findTopByEmpIdOrderByPunchTimeDesc(Mockito.any())).thenReturn(punchTypeEntity);
        assertTrue(validation.checkPunch(1,PunchType.IN));
    }

    @Test
    void checkEmployeeListEmptyTest(){
        when(employeeRepo.findById(Mockito.any())).thenReturn(Optional.empty());
        CommonException commonException = assertThrows(CommonException.class,()->{
           validation.checkEmployeeList(employeeIdList);
        });
        assertEquals(AppConstant.INVALID_EMPLOYEE_IN_LIST,commonException.getMessage());
    }

    @Test
    void checkEmployeeListTrueTest() throws CommonException {
        when(employeeRepo.findById(Mockito.any())).thenReturn(Optional.of(employeeEntity));
        validation.checkEmployeeList(employeeIdList);
        verify(employeeRepo,Mockito.atLeastOnce()).findById(1);
    }
}
