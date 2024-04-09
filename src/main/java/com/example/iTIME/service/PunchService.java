package com.example.iTIME.service;

import com.example.iTIME.Exception.CommonException;
import org.springframework.stereotype.Service;

@Service
public interface PunchService {
    String addPunch(String empId, String punchType) throws CommonException;
}
