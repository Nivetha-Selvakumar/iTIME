package com.example.iTIME.mapper;

import com.example.iTIME.DTO.PunchTypeDTO;
import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;


@ExtendWith(MockitoExtension.class)
class PunchTypeMapperTest {
    @InjectMocks
    PunchTypeMapper punchTypeMapper;

    PunchTypeEntity punchTypeEntity = new PunchTypeEntity();

    EmployeeEntity employeeEntity = new EmployeeEntity();

    PunchTypeDTO punchTypeDTO = new PunchTypeDTO();

    PunchType punchType;

    @BeforeEach
    void init(){
        employeeEntity.setId(1);

        punchTypeDTO.setPunchType(PunchType.IN);
        punchTypeDTO.setPunchTime(Timestamp.valueOf("2024-04-02 00:00:00"));

        punchTypeEntity.setPunchType(PunchType.IN);
        punchTypeEntity.setEmpId(employeeEntity);
        punchTypeEntity.setId(1);

    }
    @Test
    void punchModelToEntityTest(){
        PunchTypeEntity punchTypeEntity1 = punchTypeMapper.punchModelToEntity(PunchType.IN,employeeEntity);
        Assertions.assertEquals(punchTypeEntity.getEmpId(), punchTypeEntity1.getEmpId());
        Assertions.assertEquals(punchTypeEntity.getPunchType(),punchTypeEntity1.getPunchType());

    }

}
