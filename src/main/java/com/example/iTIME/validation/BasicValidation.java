package com.example.iTIME.validation;

import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.Exception.MisMatchException;
import com.example.iTIME.util.AppConstant;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicValidation {


    public void checkEmpIdAndDateBasicValidation(String empId, String date) throws CommonException {
        employeeIdBasicValidation(empId);
        dateBasicValidation(date);
    }

    public void checkEmpIdAndPunchType(String empId, String punchType) throws CommonException {
        employeeIdBasicValidation(empId);
        punchTypeBasicValidation(punchType);

    }

    private void punchTypeBasicValidation(String punchType) throws CommonException {
        if (punchType == null || punchType.isEmpty()){
            throw new NullPointerException(AppConstant.NO_DATE);
        }else{
            if(!punchType.matches("IN") || punchType.matches("OUT")){
                throw new MisMatchException(AppConstant.PUNCHTYPE_MISMATCH);
            }
        }
    }

    private void dateBasicValidation(String date) throws CommonException {
        if (date == null || date.isEmpty()){
            throw new NullPointerException(AppConstant.NO_DATE);
        }else{
            if (!date.matches("\\d{8}")){
                throw new MisMatchException(AppConstant.DATE_MISMATCH);
            }
        }
    }

    private void employeeIdBasicValidation(String empId) throws CommonException {
        if(empId == null || empId.isEmpty()){
            throw new NullPointerException(AppConstant.NO_EMPLOYEE);
        }else{
            if (!empId.matches("\\d+")){
                throw new MisMatchException(AppConstant.EMPLOYEE_MISMATCH);
            }
        }
    }


}
