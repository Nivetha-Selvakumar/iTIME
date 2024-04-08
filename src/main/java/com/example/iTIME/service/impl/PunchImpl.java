package com.example.iTIME.service.impl;

import com.example.iTIME.DTO.PunchTypeDTO;
import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.Exception.NotFoundException;
import com.example.iTIME.Exception.LastPunchException;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import com.example.iTIME.mapper.PunchTypeMapper;
import com.example.iTIME.repository.PunchTypeRepo;
import com.example.iTIME.service.PunchService;
import com.example.iTIME.util.AppConstant;
import com.example.iTIME.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class PunchImpl implements PunchService {

    @Autowired
    Validation validation;

    @Autowired
    PunchTypeMapper punchTypeMapper;


    @Autowired
    PunchTypeRepo punchTypeRepo;

    @Override
    public String addPunch(Integer empId, PunchType punchType) throws CommonException {
        if(validation.checkEmployeeId(empId)){
                EmployeeEntity employeeEntity = new EmployeeEntity();
                PunchTypeDTO punchTypeDTO = new PunchTypeDTO();
                employeeEntity.setId(empId);
                PunchTypeEntity punchTypeEntity = punchTypeMapper.punchModelToEntity(punchTypeDTO,punchType,employeeEntity);
                punchTypeRepo.save(punchTypeEntity);
                return punchType.toString();
        }else{
            throw new NotFoundException(AppConstant.EMPLOYEE_NOT_FOUND);
        }
    }
}
