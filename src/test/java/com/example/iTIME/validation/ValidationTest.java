package com.example.iTIME.validation;

import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import com.example.iTIME.repository.EmployeeRepo;
import com.example.iTIME.repository.PunchTypeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
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

    @BeforeEach
    void init(){
        employeeEntity.setId(1);
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

}
