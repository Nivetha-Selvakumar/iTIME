package com.example.iTIME.controller;

import com.example.iTIME.DTO.ResponseWorkHrsDTO;
import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.service.ShiftService;
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
class ShiftControllerTest {
    @InjectMocks
    ShiftController shiftController;

    @Mock
    ShiftService shiftService;
    @Mock
    BasicValidation basicValidation;

    ResponseWorkHrsDTO responseWorkHrsDTO = new ResponseWorkHrsDTO();

    @Test
    void viewShiftWorkingHoursTest() throws CommonException {
        basicValidation.checkEmpIdAndDateBasicValidation("1","20240402");
        when(shiftService.calculateWorkingHours(Mockito.any(),Mockito.anyString())).thenReturn(responseWorkHrsDTO);
        ResponseWorkHrsDTO actual = shiftController.viewShiftWorking("1","20240405").getBody();
        assertEquals(responseWorkHrsDTO,actual);

    }
}
