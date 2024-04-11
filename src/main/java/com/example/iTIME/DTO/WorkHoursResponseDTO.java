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
public class WorkHoursResponseDTO {
    @NotBlank(message = "First punch time is blank")
    private String punchIn;

    @NotBlank(message = "Last punch time is blank")
    private String punchOut;

    @NotBlank(message = "Actual working hours is blank")
    private String actualWorkHours;

    @NotBlank(message = "Assigned work hours is blank")
    private String assignedWorkHours;

    private String shortFall;

    private String extraHours;

    private String permissionHours;


}
