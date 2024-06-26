package com.example.iTIME.validation;

import com.example.iTIME.Exception.CommonException;
import com.example.iTIME.Exception.MisMatchException;
import com.example.iTIME.util.AppConstant;
import com.example.iTIME.util.DateTimeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DateTimeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BasicValidationTest {
    @InjectMocks
    BasicValidation basicValidation;

    @Test
    void checkEmpIdAndDateBasicValidationTest1(){
        String empId = "1m";
        String date = "20240304";
        assertThrows(CommonException.class,()->{
            basicValidation.checkEmpIdAndDateBasicValidation(empId,date);
        });
    }
    @Test
    void checkEmpIdAndDateBasicValidationTest2(){
        String empId = "1";
        String date = "2024030";
        assertThrows(CommonException.class,()->{
            basicValidation.checkEmpIdAndDateBasicValidation(empId,date);
        });
    }

    @Test
    void checkEmpIdAndDateBasicValidationTest3(){
        String empId = "";
        String date = "20240304";
        assertThrows(NullPointerException.class,()->{
            basicValidation.checkEmpIdAndDateBasicValidation(empId,date);
        });
    }

    @Test
    void checkEmpIdAndDateBasicValidationTest4(){
        String empId = "1";
        String date = "00000000";
        assertThrows(MisMatchException.class,()->{
            basicValidation.checkEmpIdAndDateBasicValidation(empId,date);
        });
    }

    @Test
    void checkEmpIdAndDateBasicValidationTest5(){
        String empId = "1";
        String date = "20241512";
        assertThrows(MisMatchException.class,()->{
            basicValidation.checkEmpIdAndDateBasicValidation(empId,date);
        });
    }

    @Test
    void checkEmpIdAndDateBasicValidationTest6() {
        String empId = "1";
        String date = "20240412";
        assertDoesNotThrow(() -> basicValidation.checkEmpIdAndDateBasicValidation(empId,date));

    }


    @Test
    void checkEmpIdAndPunchType1(){
        String empId = "1m";
        String punchType = "IN";
        assertThrows(CommonException.class,()->{
            basicValidation.checkEmpIdAndPunchType(empId,punchType);
        });
    }

    @Test
    void checkEmpIdAndPunchType2(){
        String empId = "1";
        String punchType = "mmm";
        assertThrows(CommonException.class,()->{
            basicValidation.checkEmpIdAndPunchType(empId,punchType);
        });
    }

    @Test
    void checkEmpIdAndPunchType3(){
        String empId = "";
        String punchType = "IN";
        assertThrows(NullPointerException.class,()->{
            basicValidation.checkEmpIdAndPunchType(empId,punchType);
        });
    }

    @Test
    void checkEmpIdAndPunchType4(){
        String empId = "1";
        String punchType = "";
        assertThrows(NullPointerException.class,()->{
            basicValidation.checkEmpIdAndPunchType(empId,punchType);
        });
    }

    @Test
    void checkEmpIdTest(){
        String empId = null;
        assertThrows(NullPointerException.class,()->{
            basicValidation.checkEmpId(empId);
        });
    }
}
