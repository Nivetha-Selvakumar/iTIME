package com.example.iTIME.mapper;

import com.example.iTIME.DTO.PunchTypeDTO;
import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class PunchTypeMapper {

    public PunchTypeEntity punchModelToEntity(PunchTypeDTO punchTypeDTO, PunchType punchType, EmployeeEntity employeeEntity) {
        PunchTypeEntity punchTypeEntity = new PunchTypeEntity();
        punchTypeEntity.setPunchTime(punchTypeDTO.getPunchTime());
        punchTypeEntity.setPunchType(punchType);
        punchTypeEntity.setEmpId(employeeEntity);

        return punchTypeEntity;
    }
}
