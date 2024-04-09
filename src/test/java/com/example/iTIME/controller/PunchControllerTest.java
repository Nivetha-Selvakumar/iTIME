package com.example.iTIME.controller;

import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.service.PunchService;
import com.example.iTIME.util.AppConstant;
import com.example.iTIME.validation.BasicValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PunchControllerTest {

    @InjectMocks
    PunchController punchController;

    @Mock
    PunchService punchService;
    @Mock
    BasicValidation basicValidation;

    @Test
    void createPunchTest() throws CommonException {
        basicValidation.checkEmpIdAndPunchType("1","IN");
        when(punchService.addPunch(Mockito.any(),Mockito.any())).thenReturn(String.valueOf(PunchType.IN));
        String actual = punchController.createPunch("1","IN").getBody();
        assertEquals(AppConstant.PUNCH_SUCCESSFULLY+PunchType.IN,actual);


    }



}
