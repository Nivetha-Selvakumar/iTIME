package com.example.iTIME.service;

import com.example.iTIME.DTO.ResponseWorkingHrsDTO;
import com.example.iTIME.DTO.WorkHoursResponseDTO;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.Exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface ShiftService {
     ResponseWorkingHrsDTO calculateWorkingHours(String empId, String date) throws CommonException;

     WorkHoursResponseDTO calculateActualWorkHours(String empId, String date) throws CommonException;
}
