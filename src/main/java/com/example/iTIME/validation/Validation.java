package com.example.iTIME.validation;

import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import com.example.iTIME.repository.EmployeeRepo;
import com.example.iTIME.repository.PunchTypeRepo;
import com.example.iTIME.repository.ShiftRoasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Validation {

    @Autowired
    EmployeeRepo employeeRepo;
    @Autowired
    PunchTypeRepo punchTypeRepo;
    @Autowired
    ShiftRoasterRepo shiftRoasterRepo;

    public boolean checkEmployeeId(Integer empId){
        boolean flag = false;
        Optional<EmployeeEntity> employeeEntity = employeeRepo.findById(empId);
        if (employeeEntity.isPresent()){
            flag = true;
        }
        return flag;
    }

    public boolean checkPunch(Integer empId, PunchType punchType) {
        boolean flag = false;
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setId(empId);
        PunchTypeEntity punchTypeEntity= punchTypeRepo.findTopByEmpIdOrderByPunchTimeDesc(employeeEntity);
        if(punchTypeEntity.getPunchType() != punchType){
            flag= true;
        }
        return flag;
    }

}
