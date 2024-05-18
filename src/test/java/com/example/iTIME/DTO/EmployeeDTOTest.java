package com.example.iTIME.DTO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeDTOTest {
    @InjectMocks
    EmployeeDTO employeeDTO;

    @Test
    void empployeeDTOTEst(){
        employeeDTO.setShiftId("1");
        employeeDTO.setEmpName("Nive");
        employeeDTO.setEmpCode("123");

        String shift= employeeDTO.getShiftId();
        String name = employeeDTO.getEmpName();
        String empCode = employeeDTO.getEmpCode();

        Assertions.assertEquals(shift,employeeDTO.getShiftId());
        Assertions.assertEquals(name,employeeDTO.getEmpName());
        Assertions.assertEquals(empCode,employeeDTO.getEmpCode());

        EmployeeDTO employeeDTO1 = new EmployeeDTO();

        Assertions.assertNotNull(employeeDTO1);
    }
}
