package com.example.iTIME.DTO;

import com.example.iTIME.Enum.PunchType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

@ExtendWith(MockitoExtension.class)
class PunchTypeDTOTest {
    @InjectMocks
    PunchTypeDTO punchTypeDTO;
    @Test
    void punchDTOTest(){
        punchTypeDTO.setPunchType(PunchType.IN);
        punchTypeDTO.setPunchTime(Timestamp.valueOf("2024-04-04 00:00:00"));

        PunchType punchType= punchTypeDTO.getPunchType();
        Timestamp punchTime = punchTypeDTO.getPunchTime();

        Assertions.assertEquals(punchTime,punchTypeDTO.getPunchTime());
        Assertions.assertEquals(punchType,punchTypeDTO.getPunchType());

        PunchTypeDTO punchTypeDTO1 = new PunchTypeDTO();
        Assertions.assertNotNull(punchTypeDTO1);
    }
}
