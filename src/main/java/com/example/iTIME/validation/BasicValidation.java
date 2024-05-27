package com.example.iTIME.validation;

import com.example.iTIME.DTO.ShiftRosterDTO;
import com.example.iTIME.Enum.EnumDayOfWeek;
import com.example.iTIME.Enum.ShiftRoasterType;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.Exception.MisMatchException;
import com.example.iTIME.util.AppConstant;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

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


    public void checkEmpId(String empId) throws CommonException {
        employeeIdBasicValidation(empId);
    }

    private void punchTypeBasicValidation(String punchType) throws CommonException {
        if (punchType == null || punchType.isEmpty()){
            throw new NullPointerException(AppConstant.NO_PUNCH);
        }else{
            if(!punchType.matches(AppConstant.IN) || punchType.matches(AppConstant.OUT)){
                throw new MisMatchException(AppConstant.PUNCHTYPE_MISMATCH);
            }
        }
    }

    private void dateBasicValidation(String date) throws CommonException {

        if(!date.matches(AppConstant.DATE_REGEX)){
            throw new MisMatchException(AppConstant.INVALID_DATE);
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


    public void checkShiftRosterDTO(ShiftRosterDTO shiftRosterDTO) throws CommonException {

        validateAssignShiftType(shiftRosterDTO.getAssignShiftType());

        if (!shiftRosterDTO.getStartDate().matches(AppConstant.DATE_VALIDATION_REGEX)) {
            throw new MisMatchException(AppConstant.INVALID_START_DATE);
        }

        if (!shiftRosterDTO.getEndDate().matches(AppConstant.DATE_VALIDATION_REGEX) ) {
            throw new MisMatchException(AppConstant.INVALID_END_DATE);
        }

        LocalDate startDate = LocalDate.parse(shiftRosterDTO.getStartDate());
        LocalDate endDate = LocalDate.parse(shiftRosterDTO.getEndDate());

        if (endDate.isBefore(startDate)){
            throw new MisMatchException(AppConstant.END_DATE_AFTER_START_DATE);
        }

        if (!shiftRosterDTO.getMonth().matches(AppConstant.MONTH_VALIDATION_REGEX)) {
            throw new MisMatchException(AppConstant.INVALID_MONTH);
        }

        String startDateMonth = shiftRosterDTO.getStartDate().substring(5, 7);

        if (!shiftRosterDTO.getMonth().equals(startDateMonth)) {
            throw new MisMatchException(AppConstant.MONTH_DOES_NOT_MATCHES_DATE);
        }


        if (!shiftRosterDTO.getYear().matches(AppConstant.YEAR_VALIDATION_REGEX)) {
            throw new MisMatchException(AppConstant.INVALID_YEAR);
        }

        if (!shiftRosterDTO.getYear().equals(shiftRosterDTO.getStartDate().substring(0, 4))) {
            throw new MisMatchException(AppConstant.YEAR_DOES_NOT_MATCHES_DATE);
        }

        validateWeekOff(shiftRosterDTO.getWeekOff1());
        validateWeekOff(shiftRosterDTO.getWeekOff2());
        validateWeekOff(shiftRosterDTO.getWeekOff3());
        validateWeekOff(shiftRosterDTO.getWeekOff4());
        validateWeekOff(shiftRosterDTO.getWeekOff5());
        validateWeekOff(shiftRosterDTO.getWeekOff6());
    }

    private void validateWeekOff(List<String> weekOff) throws CommonException {
        for (String days : weekOff){
            boolean found = false;
            for(EnumDayOfWeek day : EnumDayOfWeek.values()){
                if(day.name().equals(days)){
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new MisMatchException(AppConstant.WEEK_OFF_DAY_INVALID);
            }
        }
    }

    private void validateAssignShiftType(String assignShiftType) throws CommonException {
        boolean found = false;
        for (ShiftRoasterType type : ShiftRoasterType.values()) {
            if (type.name().equals(assignShiftType)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new MisMatchException(AppConstant.ASSIGN_SHIFT_NOT_FOUND);
        }
    }
}
