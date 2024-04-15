package com.example.iTIME.controller;

import com.example.iTIME.DTO.ResponseWorkingHrsDTO;
import com.example.iTIME.DTO.WorkHoursResponseDTO;
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

    ResponseWorkingHrsDTO responseWorkingHrsDTO = new ResponseWorkingHrsDTO();

    WorkHoursResponseDTO workHoursResponseDTO = new WorkHoursResponseDTO();

    @Test
    void viewShiftWorkingHoursTest() throws CommonException {
        basicValidation.checkEmpIdAndDateBasicValidation("1","20240402");
        when(shiftService.calculateWorkingHours(Mockito.any(),Mockito.anyString())).thenReturn(responseWorkingHrsDTO);
        ResponseWorkingHrsDTO actual = shiftController.viewShiftWorking("1","20240405").getBody();
        assertEquals(responseWorkingHrsDTO,actual);

    }
    @Test
    void viewWorkHoursTest() throws CommonException {
        basicValidation.checkEmpIdAndDateBasicValidation("1","20240402");
        when(shiftService.calculateActualWorkHours(Mockito.any(),Mockito.anyString())).thenReturn(workHoursResponseDTO);
        WorkHoursResponseDTO actual = shiftController.viewWorkHours("1","20240405").getBody();
        assertEquals(workHoursResponseDTO,actual);

    }
}
