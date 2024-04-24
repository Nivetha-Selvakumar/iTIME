package com.example.iTIME.service.impl;

import com.example.iTIME.DTO.ResponseWorkingHrsDTO;
import com.example.iTIME.DTO.ShiftRoasterDTO;
import com.example.iTIME.DTO.WorkHoursResponseDTO;
import com.example.iTIME.Enum.*;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.Exception.NotFoundException;
import com.example.iTIME.entity.*;
import com.example.iTIME.repository.*;
import com.example.iTIME.service.ShiftService;
import com.example.iTIME.util.AppConstant;
import com.example.iTIME.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShiftImpl implements ShiftService {

    @Autowired
    PunchTypeRepo punchTypeRepo;
    @Autowired
    EmployeeRepo employeeRepo;
    @Autowired
    PermissionTransactionRepo permissionTransactionRepo;
    @Autowired
    ShiftRoasterRepo shiftRoasterRepo;
    @Autowired
    ShiftRepo shiftRepo;


    @Override
    public ResponseWorkingHrsDTO calculateWorkingHours(String empId1, String date1) throws CommonException {
            // Converting the String to Integer and Date format
            Integer empId = Integer.valueOf(empId1);
            LocalDate date = DateTimeUtil.dateTimeFormatter(date1);
            EmployeeEntity employeeEntity = employeeRepo.findById(empId).orElseThrow(
                    ()->new NotFoundException(AppConstant.EMPLOYEE_NOT_FOUND));
            // To save all the values in responseDTO
            ResponseWorkingHrsDTO responseWorkingHrsDTO = new ResponseWorkingHrsDTO();
            if(employeeEntity.getShiftId().getShiftType().equals(ShiftType.DAY)){
                dayShiftWorkHoursCalculation(employeeEntity,date, responseWorkingHrsDTO);
            }else{
                nightShiftWorkHrsCalculation(employeeEntity,date, responseWorkingHrsDTO);
            }
        return responseWorkingHrsDTO;
    }

    @Override
    public WorkHoursResponseDTO calculateActualWorkHours(String empId1, String date1) throws CommonException {
        Integer empId = Integer.valueOf(empId1);
        LocalDate date = DateTimeUtil.dateTimeFormatter(date1);
        EmployeeEntity employeeEntity = employeeRepo.findById(empId).orElseThrow(
                ()->new NotFoundException(AppConstant.EMPLOYEE_NOT_FOUND));

        WorkHoursResponseDTO workHoursResponseDTO = new WorkHoursResponseDTO();
        ResponseWorkingHrsDTO responseWorkingHrsDTO = calculateWorkingHours(empId1,date1);
        workHoursResponseDTO.setPunchIn(responseWorkingHrsDTO.getPunchIn());
        workHoursResponseDTO.setPunchOut(responseWorkingHrsDTO.getPunchOut());
        workHoursResponseDTO.setActualWorkHours(responseWorkingHrsDTO.getWorkingHours());

        LocalTime actualStartTime = employeeEntity.getShiftId().getStartTime().toLocalTime();
        LocalTime actualEndTime= employeeEntity.getShiftId().getEndTime().toLocalTime();

        Duration workHrsDuration = Duration.between(actualStartTime, actualEndTime).minusMinutes(30);

        workHoursResponseDTO.setAssignedWorkHours(DateTimeUtil.convertHoursAndMinutes(workHrsDuration));
        LocalTime workingTime = null;
        LocalTime shiftTime = null;
        if(!workHoursResponseDTO.getActualWorkHours().matches( "--:--")){
            workingTime = LocalTime.parse(workHoursResponseDTO.getActualWorkHours(),
                    DateTimeFormatter.ofPattern("HH:mm"));
        }
        if (!workHoursResponseDTO.getAssignedWorkHours().matches("--:--")){
            shiftTime = LocalTime.parse(workHoursResponseDTO.getAssignedWorkHours(),
                    DateTimeFormatter.ofPattern("HH:mm"));
        }
        if(workingTime != null && shiftTime != null){
            if(workingTime.isAfter(shiftTime)){
                ResponseWorkingHrsDTO extraHoursTime = calculateShiftWorkingHoursDifference(workingTime,shiftTime,date,responseWorkingHrsDTO,employeeEntity);
                workHoursResponseDTO.setExtraHours(extraHoursTime.getActualShiftHours());

            } else if (workingTime.isBefore(shiftTime)) {
                ResponseWorkingHrsDTO shortFallTime = calculateShiftWorkingHoursDifference(workingTime,shiftTime,date,responseWorkingHrsDTO,employeeEntity);
                workHoursResponseDTO.setShortFall(shortFallTime.getActualShiftHours());
            }else{
                workHoursResponseDTO.setExtraHours(null);
                workHoursResponseDTO.setShortFall(null);
            }
        }else{
            workHoursResponseDTO.setExtraHours(null);
            workHoursResponseDTO.setShortFall(null);
        }
        workHoursResponseDTO.setPermissionHours(responseWorkingHrsDTO.getPermissionHours());
        return workHoursResponseDTO;
    }

    @Override
    public void shiftAssign(String empId1, ShiftRoasterDTO shiftRoasterDTO) throws CommonException {
        Integer empId = Integer.valueOf(empId1);
        Integer year = Integer .valueOf(shiftRoasterDTO.getYear());

        EmployeeEntity employeeEntity = employeeRepo.findById(empId).orElseThrow(
                ()->new NotFoundException(AppConstant.EMPLOYEE_NOT_FOUND));
        ShiftEntity shiftEntity = shiftRepo.findById(Integer.valueOf(shiftRoasterDTO.getShiftType()))
                .orElseThrow(()-> new NotFoundException(AppConstant.SHIFT_NOT_FOUND));

        ShiftRoasterEntity shiftRoasterEntity = new ShiftRoasterEntity();
        EmployeeEntity employeeEntity1 = new EmployeeEntity();

        if(shiftRoasterDTO.getAssignShiftType().equals(ShiftRoasterType.MONTHLY)){
            Integer month = Integer.valueOf(shiftRoasterDTO.getMonth());
//            LocalDate startDate = LocalDate.of(year,month,1);
//            LocalDate endDate = LocalDate.of(year,month+1,1).minusDays(1);
            LocalDate startDate= LocalDate.parse(shiftRoasterDTO.getStartDate());
            LocalDate endDate = LocalDate.parse(shiftRoasterDTO.getEndDate());
            for (Integer employeeId : shiftRoasterDTO.getEmployeeList()){
                for (LocalDate date = startDate; !date.isAfter(endDate); date=date.plusDays(1)) {
                    int dayOfMonth = date.getDayOfMonth();
                    boolean isWeekOff= isWeekOff(date,shiftRoasterDTO);

                    Integer shiftValue = isWeekOff ? 0 : shiftEntity.getId();
                    setShiftValue(shiftValue, dayOfMonth,shiftRoasterEntity);
                }
                employeeEntity1.setId(employeeId);
                shiftRoasterEntity.setEmpId(employeeEntity1);
                shiftRoasterEntity.setMonth(month);
                shiftRoasterEntity.setYear(year);
                shiftRoasterEntity.setCreatedBy(employeeEntity.getEmpName()); // Set created by user
                shiftRoasterEntity.setUpdatedBy(employeeEntity.getEmpName());
                shiftRoasterRepo.save(shiftRoasterEntity);
            }
        } else if (shiftRoasterDTO.getAssignShiftType().equals(ShiftRoasterType.ANNUAL)) {
            LocalDate startDate = LocalDate.of(year, 1, 1);
            LocalDate endDate = LocalDate.of(year, 12, 31);
            for (Integer employeeId : shiftRoasterDTO.getEmployeeList()) {
                for (int setMonth = 1; setMonth <= 12; setMonth++) {
                    ShiftRoasterEntity shiftRoasterEntityForMonth = new ShiftRoasterEntity();
                    for (LocalDate date = startDate; !date.isAfter(endDate) && date.getMonth().getValue() <= endDate.getMonth().getValue(); date = date.plusDays(1)){

                        int dayOfMonth = date.getDayOfMonth();
                        boolean isWeekOffAnnual = isWeekOffAnnual(date, shiftRoasterDTO);
                        Integer shiftValue = isWeekOffAnnual ? 0 : shiftEntity.getId();
                        setShiftValue(shiftValue, dayOfMonth, shiftRoasterEntityForMonth); // Set shift value for each day
                    }
                    shiftRoasterEntityForMonth.setMonth(setMonth);
                    employeeEntity1.setId(employeeId);
                    shiftRoasterEntityForMonth.setEmpId(employeeEntity1);
                    shiftRoasterEntityForMonth.setYear(year);
                    shiftRoasterEntityForMonth.setCreatedBy(employeeEntity.getEmpName()); // Set created by user
                    shiftRoasterEntityForMonth.setUpdatedBy(employeeEntity.getEmpName());
                    shiftRoasterRepo.save(shiftRoasterEntityForMonth); // Save entity for the month
                }
            }
        }else{
            throw new NotFoundException(AppConstant.ILLEGAL_ASSIGNING_SHIFT);
        }

    }

    private boolean isWeekOffAnnual(LocalDate date, ShiftRoasterDTO shiftRoasterDTO) {
        Date date1 = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        List<String> weekOffList = shiftRoasterDTO.getWeekOff1();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String dayName = dayOfWeek.toString().substring(0,3).toUpperCase();
        for (String day : weekOffList) {
            if (day != null && EnumDayOfWeek.valueOf(day.trim()).getDay().equals(dayName)){
                return true;
            }
        }
        return false;
    }

    private boolean isWeekOff(LocalDate date, ShiftRoasterDTO shiftRoasterDTO) {
        Date date1 = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        List<String> weekOffList = findWeekOffList(weekOfMonth,shiftRoasterDTO);
        if (weekOffList != null) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            String dayName = dayOfWeek.toString().substring(0,3).toUpperCase();
            for (String day : weekOffList) {
                if (day != null && EnumDayOfWeek.valueOf(day.trim()).getDay().equals(dayName)){
                    return true;
                }
            }
        }
        return false;
    }

    private void setShiftValue(Integer value, int dayOfMonth, ShiftRoasterEntity shiftRoasterEntity) {
        Map<Integer, String> daySetterMap = createDaySetterMap();
        String setterMethodName = daySetterMap.get(dayOfMonth);
        if (setterMethodName != null) {
            try {
                shiftRoasterEntity.getClass()
                        .getMethod(setterMethodName, Integer.class)
                        .invoke(shiftRoasterEntity, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Map<Integer, String> createDaySetterMap() {
        Map<Integer, String> daySetterMap = new HashMap<>();
        for (int i = 1; i <= 31; i++) {
            daySetterMap.put(i, "setDay" + String.format("%02d", i));
        }
        return daySetterMap;
    }


    private List<String> findWeekOffList(int weekOfMonth, ShiftRoasterDTO shiftRoasterDTO) {
        if(AppConstant.FIRST_WEEK == weekOfMonth){
            return shiftRoasterDTO.getWeekOff1();
        }else if (AppConstant.SECOND_WEEK == weekOfMonth) {
            return shiftRoasterDTO.getWeekOff2();
        }else if (AppConstant.THIRD_WEEK == weekOfMonth) {
            return shiftRoasterDTO.getWeekOff3();
        }else if (AppConstant.FOURTH_WEEK == weekOfMonth) {
            return shiftRoasterDTO.getWeekOff4();
        }else if (AppConstant.FIFTH_WEEK == weekOfMonth) {
            return shiftRoasterDTO.getWeekOff5();
        }else if (AppConstant.SIXTH_WEEK == weekOfMonth) {
            return shiftRoasterDTO.getWeekOff6();
        }
        return new ArrayList<>();
    }


    private ResponseWorkingHrsDTO calculateShiftWorkingHoursDifference(LocalTime workingTime, LocalTime shiftTime, LocalDate date, ResponseWorkingHrsDTO responseWorkingHrsDTO, EmployeeEntity employeeEntity) {
        Timestamp startOfDay = Timestamp.valueOf(date.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(date.atStartOfDay().plusDays(1).minusNanos(1));

        PermissionTransactionEntity permissionTransactionEntity = permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(employeeEntity,startOfDay,endOfDay,PermissionStatus.APPROVED);

        LocalTime actualStartTime = employeeEntity.getShiftId().getStartTime().toLocalTime();
        LocalTime actualEndTime= employeeEntity.getShiftId().getEndTime().toLocalTime();

        LocalTime permissionStartTime=null; 
        LocalTime permissionEndTime=null;
        Duration permissionDuration = Duration.ZERO;
        if (permissionTransactionEntity != null){
            permissionStartTime = permissionTransactionEntity.getStartTime().toLocalTime();
            permissionEndTime = permissionTransactionEntity.getEndTime().toLocalTime();
            permissionDuration = Duration.between(permissionStartTime,permissionEndTime);
        }

        String permissionDurationString= DateTimeUtil.convertHoursAndMinutes(permissionDuration);
        if(permissionDurationString.matches("00:00")){
            responseWorkingHrsDTO.setPermissionHours("--:--");
        }else{
            responseWorkingHrsDTO.setPermissionHours(permissionDurationString);
        }

        Duration differenceWorkingHours = Duration.between(workingTime,shiftTime);

        LocalTime firstPunchInTime = LocalTime.parse(responseWorkingHrsDTO.getPunchIn());
        LocalTime lastPunchOutTime = LocalTime.parse(responseWorkingHrsDTO.getPunchOut());
        if (permissionStartTime!=null && permissionEndTime != null){
            if((permissionStartTime.isAfter(actualStartTime) && permissionEndTime.isBefore(firstPunchInTime))
                    || (permissionStartTime.isAfter(firstPunchInTime) && permissionEndTime.isBefore(lastPunchOutTime))
                    || (permissionStartTime.isAfter(lastPunchOutTime) && permissionStartTime.isBefore(actualEndTime))){
                differenceWorkingHours = differenceWorkingHours.minus(permissionDuration);
            }
        }
        responseWorkingHrsDTO.setActualShiftHours(DateTimeUtil.convertHoursAndMinutes(differenceWorkingHours));
        return responseWorkingHrsDTO;
    }

    private void dayShiftWorkHoursCalculation(EmployeeEntity employeeEntity, LocalDate date,
                                              ResponseWorkingHrsDTO responseWorkingHrsDTO) {
        // Getting the shift actual timings
        LocalTime actualStartTime = employeeEntity.getShiftId().getStartTime().toLocalTime();
        LocalTime actualEndTime = employeeEntity.getShiftId().getEndTime().toLocalTime();
        // calculating the shift with buffer timings
        LocalTime startBuffer=  actualStartTime.minusHours(Long.parseLong(employeeEntity.getShiftId().getStartFrom()));
        LocalTime endBuffer= actualEndTime.plusHours(Long.parseLong(employeeEntity.getShiftId().getEndAt()));
        //setting the given date with the shift timing
        Timestamp startDateTime = Timestamp.valueOf(LocalDateTime.of(date,startBuffer));
        Timestamp endDateTime = Timestamp.valueOf(LocalDateTime.of(date,endBuffer));

        Duration shiftHours = Duration.between(actualStartTime,actualEndTime);
        responseWorkingHrsDTO.setActualShiftHours(DateTimeUtil.convertHoursAndMinutes(shiftHours));
        //Getting list of punches for the particular date on the shift
        List<PunchTypeEntity> punches = punchTypeRepo.findByEmpIdAndPunchTimeBetween(employeeEntity, startDateTime, endDateTime);
        //Filter according to IN and OUT punchtype
        List<PunchTypeEntity> punchInList = punches.stream()
                .filter(punch -> punch.getPunchType().equals(PunchType.IN))
                .sorted(Comparator.comparing(PunchTypeEntity::getPunchTime))
                .toList();

        List<PunchTypeEntity> punchOutList = punches.stream()
                .filter(punch -> punch.getPunchType().equals(PunchType.OUT))
                .sorted(Comparator.comparing(PunchTypeEntity::getPunchTime).reversed())
                .toList();
        //To set Last punchType
        PunchTypeEntity punchTypeEntity = punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc
                (employeeEntity,startDateTime,endDateTime);
        if(punchTypeEntity != null){
            responseWorkingHrsDTO.setLastPunchType(String.valueOf(punchTypeEntity.getPunchType()));
        }else{
            responseWorkingHrsDTO.setLastPunchType(null);
        }
        // getting firstPunchIn and lastPunchOut
        PunchTypeEntity firstPunchIn = punchInList.isEmpty() ? null : punchInList.get(0);
        PunchTypeEntity lastPunchOut = punchOutList.isEmpty() ? null : punchOutList.get(0);

        responseWorkingHrsDTO.setPunchIn(firstPunchIn!=null? DateTimeUtil.convertTimeStampToTime(firstPunchIn.getPunchTime()):"--:--");
        responseWorkingHrsDTO.setPunchOut(lastPunchOut!=null? DateTimeUtil.convertTimeStampToTime(lastPunchOut.getPunchTime()):"--:--");

        //Check permission
        Timestamp startOfDay = Timestamp.valueOf(date.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(date.atStartOfDay().plusDays(1).minusNanos(1));

        PermissionTransactionEntity permissionTransactionEntity = permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(employeeEntity,startOfDay,endOfDay,PermissionStatus.APPROVED);
        LocalTime permissionStartTime=null;
        LocalTime permissionEndTime=null;
        Duration permissionDuration = Duration.ZERO;
        if (permissionTransactionEntity != null){
            permissionStartTime = permissionTransactionEntity.getStartTime().toLocalTime();
            permissionEndTime = permissionTransactionEntity.getEndTime().toLocalTime();
            permissionDuration = Duration.between(permissionStartTime,permissionEndTime);
        }

        String permissionDurationString= DateTimeUtil.convertHoursAndMinutes(permissionDuration);
        if(permissionDurationString.matches("00:00")){
            responseWorkingHrsDTO.setPermissionHours("--:--");
        }else{
            responseWorkingHrsDTO.setPermissionHours(permissionDurationString);
        }

        // Calculating working hours
        if (firstPunchIn != null && lastPunchOut != null) {

            Date punchInTime = firstPunchIn.getPunchTime();
            Date punchOutTime = lastPunchOut.getPunchTime();

            Duration workDuration = Duration.between(punchInTime.toInstant(), punchOutTime.toInstant());
            if(permissionStartTime != null && permissionEndTime !=null){
                if(permissionStartTime.isAfter(firstPunchIn.getPunchTime().toLocalDateTime().toLocalTime()) && permissionEndTime.isBefore(lastPunchOut.getPunchTime().toLocalDateTime().toLocalTime())){
                    workDuration = workDuration.minus(permissionDuration);
                }
            }
            responseWorkingHrsDTO.setWorkingHours(DateTimeUtil.convertHoursAndMinutes(workDuration));
        }else{
            responseWorkingHrsDTO.setWorkingHours("--:--");
        }

    }
    private void nightShiftWorkHrsCalculation(EmployeeEntity employeeEntity, LocalDate date, ResponseWorkingHrsDTO responseWorkingHrsDTO){

        // Getting the shift actual timings
        LocalTime actualStartTime = employeeEntity.getShiftId().getStartTime().toLocalTime();
        LocalTime actualEndTime= employeeEntity.getShiftId().getEndTime().toLocalTime();

        // calculating the shift with buffer timings
        LocalTime startBuffer=  actualStartTime.minusHours(Long.parseLong(employeeEntity.getShiftId().getStartFrom()));
        LocalTime endBuffer= actualEndTime.plusHours(Long.parseLong(employeeEntity.getShiftId().getEndAt()));

        Duration shiftHoursCalculated = Duration.between(actualStartTime,actualEndTime);
        responseWorkingHrsDTO.setActualShiftHours(DateTimeUtil.convertHoursAndMinutes(shiftHoursCalculated));

        LocalDateTime shiftEndTime = date.plusDays(1).atTime(endBuffer);
        LocalDateTime currentTime = LocalDateTime.now();

        Timestamp startDateTime;
        Timestamp endDateTime;

        if(currentTime.isBefore(shiftEndTime)){
            currentTime= currentTime.minusDays(1);
            startDateTime = Timestamp.valueOf(LocalDateTime.of(currentTime.toLocalDate(),startBuffer));
            endDateTime = Timestamp.valueOf(LocalDateTime.of(date,endBuffer));
        }else{
            startDateTime = Timestamp.valueOf(LocalDateTime.of(date,startBuffer));
            endDateTime = Timestamp.valueOf(LocalDateTime.of(date,endBuffer));
        }
        if (endBuffer.isBefore(startBuffer)) {
            endDateTime = Timestamp.valueOf(LocalDateTime.of(date.plusDays(1), endBuffer));
        }
        //Getting list of punches for the particular date on the shift
        List<PunchTypeEntity> punches = punchTypeRepo.findByEmpIdAndPunchTimeBetween(employeeEntity, startDateTime, endDateTime);
        //Filter according to IN and OUT punchType
        List<PunchTypeEntity> punchInList = punches.stream()
                .filter(punch -> punch.getPunchType().equals(PunchType.IN))
                .sorted(Comparator.comparing(PunchTypeEntity::getPunchTime))
                .toList();

        List<PunchTypeEntity> punchOutList = punches.stream()
                .filter(punch -> punch.getPunchType().equals(PunchType.OUT))
                .sorted(Comparator.comparing(PunchTypeEntity::getPunchTime).reversed())
                .toList();

        //To set Last punchType
        PunchTypeEntity punchTypeEntity = punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(employeeEntity,startDateTime,endDateTime);
        if(punchTypeEntity != null){
            responseWorkingHrsDTO.setLastPunchType(String.valueOf(punchTypeEntity.getPunchType()));
        }else{
            responseWorkingHrsDTO.setLastPunchType(null);
        }

        // getting firstPunchIn and lastPunchOut
        PunchTypeEntity firstPunchIn = punchInList.isEmpty() ? null : punchInList.get(0);
        PunchTypeEntity lastPunchOut = punchOutList.isEmpty() ? null : punchOutList.get(0);

        //Converting to timestamp format
        responseWorkingHrsDTO.setPunchIn(firstPunchIn!=null? DateTimeUtil.convertTimeStampToTime(firstPunchIn.getPunchTime()):"--:--");
        responseWorkingHrsDTO.setPunchOut(lastPunchOut!=null?DateTimeUtil.convertTimeStampToTime(lastPunchOut.getPunchTime()):"--:--");


        //Check permission
        Timestamp startOfDay = Timestamp.valueOf(date.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(date.atStartOfDay().plusDays(1).minusNanos(1));

        PermissionTransactionEntity permissionTransactionEntity = permissionTransactionRepo.findByEmpIdAndPermissionDateBetweenAndApprovalStatus(employeeEntity,startOfDay,endOfDay,PermissionStatus.APPROVED);
        LocalTime permissionStartTime=null;
        LocalTime permissionEndTime=null;
        Duration permissionDuration = Duration.ZERO;
        if (permissionTransactionEntity != null){
            permissionStartTime = permissionTransactionEntity.getStartTime().toLocalTime();
            permissionEndTime = permissionTransactionEntity.getEndTime().toLocalTime();
            permissionDuration = Duration.between(permissionStartTime,permissionEndTime);
        }
        String permissionDurationString= DateTimeUtil.convertHoursAndMinutes(permissionDuration);
        if(permissionDurationString.matches("00:00")){
            responseWorkingHrsDTO.setPermissionHours("--:--");
        }else{
            responseWorkingHrsDTO.setPermissionHours(permissionDurationString);
        }

        // Calculating working hours
        if (firstPunchIn != null && lastPunchOut != null) {

            Date punchInTime = firstPunchIn.getPunchTime();
            Date punchOutTime = lastPunchOut.getPunchTime();

            Duration workDuration = Duration.between(punchInTime.toInstant(), punchOutTime.toInstant());
            if (permissionStartTime != null && permissionEndTime != null){
                if(permissionStartTime.isAfter(firstPunchIn.getPunchTime().toLocalDateTime().toLocalTime()) && permissionEndTime.isBefore(lastPunchOut.getPunchTime().toLocalDateTime().toLocalTime())){
                    workDuration = workDuration.minus(permissionDuration);
                }
            }
            workDuration = workDuration.minus(permissionDuration);

            responseWorkingHrsDTO.setWorkingHours(DateTimeUtil.convertHoursAndMinutes( workDuration));
         }else{
            responseWorkingHrsDTO.setWorkingHours("--:--");
        }
    }
}
