package com.example.iTIME.service;

import com.example.iTIME.DTO.ResponseWorkHrsDTO;
import com.example.iTIME.Exception.CommonException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ShiftService {



     ResponseWorkHrsDTO calculateWorkingHours(String empId, String date) throws CommonException;
}
