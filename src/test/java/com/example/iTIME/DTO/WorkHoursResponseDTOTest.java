package com.example.iTIME.DTO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkHoursResponseDTOTest {
    @InjectMocks
    WorkHoursResponseDTO workHoursResponseDTO;

    @Test
    void workHrsResponseTest(){
        workHoursResponseDTO.setAssignedWorkHours("8");
        workHoursResponseDTO.setPermissionHours("2");
        workHoursResponseDTO.setPunchIn("07:00");
        workHoursResponseDTO.setPunchOut("15:00");
        workHoursResponseDTO.setActualWorkHours("7");
        workHoursResponseDTO.setShortFall("00:10");
        workHoursResponseDTO.setExtraHours(null);

        String extraHrs = workHoursResponseDTO.getExtraHours();
        String assHrs = workHoursResponseDTO.getAssignedWorkHours();
        String punchIn = workHoursResponseDTO.getPunchIn();
        String punchOut = workHoursResponseDTO.getPunchOut();
        String actualWork = workHoursResponseDTO.getActualWorkHours();
        String shortFall = workHoursResponseDTO.getShortFall();
        String perm = workHoursResponseDTO.getPermissionHours();

        Assertions.assertEquals(extraHrs,workHoursResponseDTO.getExtraHours());
        Assertions.assertEquals(assHrs,workHoursResponseDTO.getAssignedWorkHours());
        Assertions.assertEquals(punchIn,workHoursResponseDTO.getPunchIn());
        Assertions.assertEquals(punchOut,workHoursResponseDTO.getPunchOut());
        Assertions.assertEquals(actualWork,workHoursResponseDTO.getActualWorkHours());
        Assertions.assertEquals(shortFall,workHoursResponseDTO.getShortFall());
        Assertions.assertEquals(perm,workHoursResponseDTO.getPermissionHours());

        WorkHoursResponseDTO workHoursResponseDTO1 = new WorkHoursResponseDTO();

        Assertions.assertNotNull(workHoursResponseDTO1);



    }
}
