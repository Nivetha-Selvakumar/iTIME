package com.example.iTIME.entity;

import com.example.iTIME.Enum.PunchType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PunchTypeEntityTest {
    @InjectMocks
    PunchTypeEntity punchTypeEntity;

    @Test
    void testPunchType(){
        punchTypeEntity.setId(1);
        punchTypeEntity.setPunchTime(Timestamp.valueOf("2024-04-04 00:00:00"));
        punchTypeEntity.setPunchType(PunchType.OUT);
        punchTypeEntity.setEmpId(new EmployeeEntity());

        int id = punchTypeEntity.getId();
        Timestamp time = punchTypeEntity.getPunchTime();
        PunchType punchType= punchTypeEntity.getPunchType();
        EmployeeEntity employeeEntity = punchTypeEntity.getEmpId();

        Assertions.assertEquals(Optional.of(id), Optional.of(punchTypeEntity.getId()));
        Assertions.assertEquals(time,punchTypeEntity.getPunchTime());
        Assertions.assertEquals(punchType,punchTypeEntity.getPunchType());
        Assertions.assertEquals(employeeEntity,punchTypeEntity.getEmpId());

        PunchTypeEntity punchTypeEntity1 = new PunchTypeEntity();
        Assertions.assertNotNull(punchTypeEntity1);

    }
}
