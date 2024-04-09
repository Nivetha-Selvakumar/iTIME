package com.example.iTIME.service;

import com.example.iTIME.DTO.PunchTypeDTO;
import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import com.example.iTIME.mapper.PunchTypeMapper;
import com.example.iTIME.repository.PunchTypeRepo;
import com.example.iTIME.service.impl.PunchImpl;
import com.example.iTIME.util.AppConstant;
import com.example.iTIME.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PunchImplTest {
    @InjectMocks
    PunchImpl punchImpl;

    @Mock
    Validation validation;

    @Mock
    PunchTypeRepo punchTypeRepo;

    @Mock
    PunchTypeMapper punchTypeMapper;
    EmployeeEntity employeeEntity = new EmployeeEntity();

    PunchTypeDTO punchTypeDTO = new PunchTypeDTO();

    PunchTypeEntity punchTypeEntity = new PunchTypeEntity();


    @BeforeEach
    void init(){
        employeeEntity.setId(1);

        punchTypeEntity.setPunchType(PunchType.OUT);
        punchTypeEntity.setId(1);
        punchTypeEntity.setEmpId(employeeEntity);

        punchTypeDTO.setPunchType(PunchType.OUT);

    }

    @Test
    void addPunchTest1() throws CommonException {
        when(validation.checkEmployeeId(1)).thenReturn(true);
        punchTypeRepo.save(punchTypeEntity);
        String expected = String.valueOf(PunchType.OUT);
        String actual = punchImpl.addPunch("1","OUT");
        assertEquals(expected,actual);
    }

    @Test
    void addPunchTest2(){
        when(validation.checkEmployeeId(1)).thenReturn(false);
        CommonException commonException = assertThrows(CommonException.class,()->{
           punchImpl.addPunch("1","IN");
        });
        String expected = AppConstant.EMPLOYEE_NOT_FOUND;
        String actual = commonException.getMessage();
        assertEquals(expected,actual);
    }






}
