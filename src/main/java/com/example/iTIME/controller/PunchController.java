package com.example.iTIME.controller;

import com.example.iTIME.DTO.PunchTypeDTO;
import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.service.PunchService;
import com.example.iTIME.util.AppConstant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/webpunch")
@Controller
public class PunchController {
    @Autowired
    PunchService punchService;

    @PostMapping(path="/punch")
    public ResponseEntity<String> createPunch(@RequestParam Integer empId, @RequestParam PunchType punchType)throws CommonException {
        String response = punchService.addPunch(empId, punchType );
        return new ResponseEntity<>(AppConstant.PUNCH_SUCCESSFULLY+response,HttpStatus.CREATED);
    }


}
