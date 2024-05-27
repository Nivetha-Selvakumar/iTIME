package com.example.iTIME.DTO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResponseWorkingHrsDTOTest {

    @InjectMocks
    ResponseWorkingHrsDTO responseWorkingHrsDTO;

    @Test
    void responseTest(){
        responseWorkingHrsDTO.setWorkingHours("8");
        responseWorkingHrsDTO.setPermissionHours("2");
        responseWorkingHrsDTO.setPunchIn("07:00");
        responseWorkingHrsDTO.setPunchOut("15:00");
        responseWorkingHrsDTO.setLastPunchType("IN");
        responseWorkingHrsDTO.setActualShiftHours("7");

        String workHrs= responseWorkingHrsDTO.getWorkingHours();
        String permissionHrs = responseWorkingHrsDTO.getPermissionHours();
        String punchIn = responseWorkingHrsDTO.getPunchIn();
        String punchOut = responseWorkingHrsDTO.getPunchOut();
        String lastPunch = responseWorkingHrsDTO.getLastPunchType();
        String actualHrs = responseWorkingHrsDTO.getWorkingHours();

        Assertions.assertEquals(workHrs,responseWorkingHrsDTO.getWorkingHours());
        Assertions.assertEquals(permissionHrs,responseWorkingHrsDTO.getPermissionHours());
        Assertions.assertEquals(punchIn,responseWorkingHrsDTO.getPunchIn());
        Assertions.assertEquals(punchOut,responseWorkingHrsDTO.getPunchOut());
        Assertions.assertEquals(lastPunch,responseWorkingHrsDTO.getLastPunchType());
        Assertions.assertEquals(actualHrs,responseWorkingHrsDTO.getWorkingHours());

        ResponseWorkingHrsDTO responseWorkingHrsDTO1 = new ResponseWorkingHrsDTO();
        Assertions.assertNotNull(responseWorkingHrsDTO1);

    }

}
