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
public class ResponseWorkingHrsDTO {

    @NotBlank(message = "First punch time is blank")
    private String punchIn;

    @NotBlank(message = "Last punch time is blank")
    private String punchOut;

    @NotBlank(message = "Working hours is blank")
    private String workingHours;

    @NotBlank(message = "Last punch type is blank")
    private String lastPunchType;

    private String permissionHours;

    private String actualShiftHours;

}
