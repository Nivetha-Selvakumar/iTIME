package com.example.iTIME.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Component
public class  EmployeeDTO {
    private  Integer id;

    @NotBlank(message = "Enter Employee Name")
    private String empName;

    @NotBlank(message = "Enter Employee Code")
    private String empCode;

    @NotBlank(message = "Enter Shift Id")
    private String shiftId;
}
