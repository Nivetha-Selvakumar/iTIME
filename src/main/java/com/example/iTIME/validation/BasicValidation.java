package com.example.iTIME.validation;

import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.Exception.MisMatchException;
import com.example.iTIME.util.AppConstant;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
            throw new NullPointerException(AppConstant.NO_PUNCH);
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

            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(4, 6));
            int day = Integer.parseInt(date.substring(6, 8));

            if (year < 1 || month < 1 || month > 12 || day < 1 || day > 31) {
                throw new MisMatchException(AppConstant.INVALID_DATE);
            }
            // Check if the parsed values form a valid date
            LocalDate parsedDate = LocalDate.of(year, month, day);

            String parsedDateString = parsedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            if (!parsedDateString.equals(date)) {
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
