package com.example.iTIME.controller;

import com.example.iTIME.DTO.ResponseWorkHrsDTO;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/webpunch")
@Controller
public class ShiftController {

    @Autowired
    ShiftService shiftService;

    @GetMapping(path = "/calculateWorkHours")
    public ResponseEntity<ResponseWorkHrsDTO> viewShiftWorking(@RequestHeader String empId, @RequestParam String date)throws CommonException {
        ResponseWorkHrsDTO response = shiftService.calculateWorkingHours(empId, date );
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }
}
