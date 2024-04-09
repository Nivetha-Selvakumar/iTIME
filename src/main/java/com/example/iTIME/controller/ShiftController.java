package com.example.iTIME.controller;

import com.example.iTIME.DTO.ResponseWorkHrsDTO;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.service.ShiftService;
import com.example.iTIME.validation.BasicValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/webpunch")
public class ShiftController {
    @Autowired
    ShiftService shiftService;
    @Autowired
    BasicValidation basicValidation;

    @GetMapping(path = "/calculateWorkHours")
    public ResponseEntity<ResponseWorkHrsDTO> viewShiftWorking(@RequestHeader String empId, @RequestParam String date)
            throws CommonException {
        basicValidation.checkEmpIdAndDateBasicValidation(empId, date);
        ResponseWorkHrsDTO response = shiftService.calculateWorkingHours(empId, date);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
