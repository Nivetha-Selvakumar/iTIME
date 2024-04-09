package com.example.iTIME.controller;

import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.service.PunchService;
import com.example.iTIME.util.AppConstant;
import com.example.iTIME.validation.BasicValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/webpunch")
public class PunchController {
    @Autowired
    PunchService punchService;
    @Autowired
    BasicValidation basicValidation;

    @PostMapping(path="/punch")
    public ResponseEntity<String> createPunch(@RequestParam String empId, @RequestParam String punchType) throws
            CommonException {
        basicValidation.checkEmpIdAndPunchType(empId,punchType);
        String response = punchService.addPunch(empId, punchType );
        return new ResponseEntity<>(AppConstant.PUNCH_SUCCESSFULLY + response, HttpStatus.CREATED);
    }
}
