package com.example.iTIME.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
class AppConstantsTest {

    @InjectMocks
    AppConstant appConstant;

    @Test
    void punchSuccessfullyTest(){
        Assertions.assertEquals("Successfully Punched ", AppConstant.PUNCH_SUCCESSFULLY);
    }
}
