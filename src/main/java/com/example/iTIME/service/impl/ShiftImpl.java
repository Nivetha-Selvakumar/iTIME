package com.example.iTIME.service.impl;

import com.example.iTIME.DTO.ResponseWorkHrsDTO;
import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.Enum.ShiftType;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.Exception.NotFoundException;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import com.example.iTIME.repository.EmployeeRepo;
import com.example.iTIME.repository.PunchTypeRepo;
import com.example.iTIME.repository.ShiftRepo;
import com.example.iTIME.service.ShiftService;
import com.example.iTIME.util.AppConstant;
import com.example.iTIME.util.DateTimeUtil;
import com.example.iTIME.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class ShiftImpl implements ShiftService {

    @Autowired
    Validation validation;

    @Autowired
    ShiftRepo shiftRepo;

    @Autowired
    PunchTypeRepo punchTypeRepo;

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    DateTimeUtil dateTimeUtil;


    @Override
    public ResponseWorkHrsDTO calculateWorkingHours(String empId1, String date1) throws CommonException {
            // Converting the String to Integer and Date format
            Integer empId = Integer.valueOf(empId1);
            LocalDate date = dateTimeUtil.dateTimeFormatter(date1);
            EmployeeEntity employeeEntity = employeeRepo.findById(empId).orElseThrow(()->new NotFoundException(AppConstant.EMPLOYEE_NOT_FOUND));
            // To save all the values in responseDTO
            ResponseWorkHrsDTO responseWorkHrsDTO = new ResponseWorkHrsDTO();

            if(employeeEntity.getShiftId().getShiftType().equals(ShiftType.DAY)){
                // Getting the shift actual timings
                LocalTime actualStartTime = employeeEntity.getShiftId().getStartTime().toLocalTime();
                LocalTime actualEndTime= employeeEntity.getShiftId().getEndTime().toLocalTime();
                // calculating the shift with buffer timings
                LocalTime startBuffer=  actualStartTime.minusHours(Long.parseLong(employeeEntity.getShiftId().getStartFrom()));
                LocalTime endBuffer= actualEndTime.plusHours(Long.parseLong(employeeEntity.getShiftId().getEndAt()));
                //setting the given date with the shift timing
                Timestamp startDateTime = Timestamp.valueOf(LocalDateTime.of(date,startBuffer));
                Timestamp endDateTime = Timestamp.valueOf(LocalDateTime.of(date,endBuffer));
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
                PunchTypeEntity punchTypeEntity = punchTypeRepo.findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(employeeEntity,startDateTime,endDateTime);
                if(punchTypeEntity != null){
                    responseWorkHrsDTO.setLastPunchType(String.valueOf(punchTypeEntity.getPunchType()));
                }else{
                    responseWorkHrsDTO.setLastPunchType(null);
                }

                // getting firstPunchIn and lastPunchOut
                PunchTypeEntity firstPunchIn = punchInList.isEmpty() ? null : punchInList.get(0);
                PunchTypeEntity lastPunchOut = punchOutList.isEmpty() ? null : punchOutList.get(0);

                responseWorkHrsDTO.setPunchIn(firstPunchIn!=null? dateTimeUtil.convertTimeStampToTime(firstPunchIn.getPunchTime()):"--:--");
                responseWorkHrsDTO.setPunchOut(lastPunchOut!=null?dateTimeUtil.convertTimeStampToTime(lastPunchOut.getPunchTime()):"--:--");
                // Calculating working hours
                if (firstPunchIn != null && lastPunchOut != null) {

                    Date punchInTime = firstPunchIn.getPunchTime();
                    Date punchOutTime = lastPunchOut.getPunchTime();

                    Duration workDuration = Duration.between(punchInTime.toInstant(), punchOutTime.toInstant());

                    long hours = workDuration.toHours();
                    long minutes = workDuration.minusHours(hours).toMinutes();


                    responseWorkHrsDTO.setWorkingHours(dateTimeUtil.convertHours( hours, minutes));
                }else{
                    responseWorkHrsDTO.setWorkingHours("--:--");
                }
            }else{
                // Getting the shift actual timings
                LocalTime actualStartTime = employeeEntity.getShiftId().getStartTime().toLocalTime();
                LocalTime actualEndTime= employeeEntity.getShiftId().getEndTime().toLocalTime();

                // calculating the shift with buffer timings
                LocalTime startBuffer=  actualStartTime.minusHours(Long.parseLong(employeeEntity.getShiftId().getStartFrom()));
                LocalTime endBuffer= actualEndTime.plusHours(Long.parseLong(employeeEntity.getShiftId().getEndAt()));

                LocalDateTime shiftStartTime = date.atTime(startBuffer);
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
                    responseWorkHrsDTO.setLastPunchType(String.valueOf(punchTypeEntity.getPunchType()));
                }else{
                    responseWorkHrsDTO.setLastPunchType(null);
                }

                // getting firstPunchIn and lastPunchOut
                PunchTypeEntity firstPunchIn = punchInList.isEmpty() ? null : punchInList.get(0);
                PunchTypeEntity lastPunchOut = punchOutList.isEmpty() ? null : punchOutList.get(0);

                //Converting to timestamp format
                responseWorkHrsDTO.setPunchIn(firstPunchIn!=null? dateTimeUtil.convertTimeStampToTime(firstPunchIn.getPunchTime()):"--:--");
                responseWorkHrsDTO.setPunchOut(lastPunchOut!=null?dateTimeUtil.convertTimeStampToTime(lastPunchOut.getPunchTime()):"--:--");

                // Calculating working hours
                if (firstPunchIn != null && lastPunchOut != null) {

                    Date punchInTime = firstPunchIn.getPunchTime();
                    Date punchOutTime = lastPunchOut.getPunchTime();

                    Duration workDuration = Duration.between(punchInTime.toInstant(), punchOutTime.toInstant());

                    long hours = workDuration.toHours();
                    long minutes = workDuration.minusHours(hours).toMinutes();

                    responseWorkHrsDTO.setWorkingHours(dateTimeUtil.convertHours( hours, minutes));
                }else{
                    responseWorkHrsDTO.setWorkingHours("--:--");
                }
            }
        return  responseWorkHrsDTO;
    }
}
