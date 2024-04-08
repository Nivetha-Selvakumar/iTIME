package com.example.iTIME.service;

import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.Exception.CommonException;
import org.springframework.stereotype.Service;

@Service
public interface PunchService {
    String addPunch(Integer empId, PunchType punchType) throws CommonException;
}
