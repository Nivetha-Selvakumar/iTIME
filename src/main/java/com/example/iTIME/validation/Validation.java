package com.example.iTIME.validation;

import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.Exception.NotFoundException;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import com.example.iTIME.repository.EmployeeRepo;
import com.example.iTIME.repository.PunchTypeRepo;
import com.example.iTIME.repository.ShiftRoasterRepo;
import com.example.iTIME.util.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public void checkEmployeeList(List<Integer> employeeList) throws CommonException {
        for (Integer empId : employeeList) {
            Optional<EmployeeEntity> employeeEntity = employeeRepo.findById(empId);
            if (employeeEntity.isEmpty()) {
                throw new NotFoundException(AppConstant.INVALID_EMPLOYEE_IN_LIST);
            }
        }
    }

//    public void checkShiftWeekOffValidation(LocalDate date, int lastWeekOfMonth, boolean isWeekOff) {
//        Date date1 = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date1);
//        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
//        if(weekOfMonth == 1){
//            LocalDate previousMonthLastWeekStartDate = date.minusMonths(1).withDayOfMonth(1)
//                    .with(WeekFields.ISO.dayOfWeek(), 7);
//            LocalDate previousMonthLastWeekEndDate = previousMonthLastWeekStartDate.plusDays(6);
//            checkShiftAssignmentsForWeek(previousMonthLastWeekStartDate, previousMonthLastWeekEndDate,isWeekOff);
//
//        }else if (weekOfMonth == lastWeekOfMonth){
//            LocalDate nextMonthFirstWeekStartDate = date.plusMonths(1).withDayOfMonth(1);
//            LocalDate nextMonthFirstWeekEndDate = nextMonthFirstWeekStartDate.plusDays(6);
//
//            checkShiftAssignmentsForWeek(nextMonthFirstWeekStartDate, nextMonthFirstWeekEndDate, isWeekOff);
//
//        }else {
//            LocalDate weekStartDate = date.with(WeekFields.ISO.dayOfWeek(), 1); // Start of the week
//            LocalDate weekEndDate = weekStartDate.plusDays(6); // End of the week
//
//            checkShiftAssignmentsForWeek(weekStartDate, weekEndDate, isWeekOff);
//
//        }
//    }
//    private void checkShiftAssignmentsForWeek(LocalDate weekStartDate, LocalDate weekEndDate, boolean isWeekOff) {
//        int workingDays = 0;
//        int weekOffs = 0;
//
//        // Iterate over each day in the week
//        for (LocalDate date = weekStartDate; !date.isAfter(weekEndDate); date = date.plusDays(1)) {
//            if (isWeekOff) {
//                weekOffs++;
//            } else {
//                workingDays++;
//            }
//        }
//
//        if (workingDays < 5 || workingDays > 6 || weekOffs < 1 || weekOffs > 2) {
//            throw new ValidationException("Invalid shift assignment for the week starting from: " + weekStartDate +
//                    ". Must have 5-6 working days and 1-2 week offs.");
//        }
//    }
}
