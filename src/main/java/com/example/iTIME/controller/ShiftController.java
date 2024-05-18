package com.example.iTIME.controller;

import com.example.iTIME.DTO.ResponseWorkingHrsDTO;
import com.example.iTIME.DTO.ShiftRosterDTO;
import com.example.iTIME.DTO.WorkHoursResponseDTO;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.service.ShiftService;
import com.example.iTIME.util.AppConstant;
import com.example.iTIME.validation.BasicValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/webPunch")
public class ShiftController {
    @Autowired
    ShiftService shiftService;
    @Autowired
    BasicValidation basicValidation;

    @GetMapping(path = "/calculateWorkingHours")
    public ResponseEntity<ResponseWorkingHrsDTO> viewShiftWorking(@RequestHeader String empId, @RequestParam String date)
            throws CommonException {
        basicValidation.checkEmpIdAndDateBasicValidation(empId, date);
        ResponseWorkingHrsDTO response = shiftService.calculateWorkingHours(empId, date);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/shiftWorkHours")
    public ResponseEntity<WorkHoursResponseDTO>  viewWorkHours(@RequestHeader String empId, @RequestParam String date)
            throws CommonException{
        basicValidation.checkEmpIdAndDateBasicValidation(empId, date);
        WorkHoursResponseDTO response = shiftService.calculateActualWorkHours(empId, date);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(path = "/shiftRoaster/create")
    public String createShiftRoaster(@RequestHeader String empId, @RequestBody ShiftRosterDTO shiftRosterDTO) throws CommonException {
        basicValidation.checkEmpId(empId);
        shiftService.shiftAssign(empId,shiftRosterDTO);
        return AppConstant.SUCCESSFULLY_REGISTERED;
    }

}
