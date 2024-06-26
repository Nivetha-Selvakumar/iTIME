package com.example.iTIME.DTO;

import com.example.iTIME.Enum.ShiftType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.stereotype.Component;

import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Component
public class ShiftDTO {

    @NotBlank(message = "Enter Shift Name")
    private String shiftName;

    @NotBlank(message = "Enter Shift Type")
    private ShiftType shiftType;

    @NotBlank(message = "Enter Start Time")
    private Time startTime;

    @NotBlank(message = "Enter End Time")
    private Time endTime;

    @NotBlank(message = "Enter Start From")
    private String startFrom;

    @NotBlank(message = "Enter End From")
    private String endAt;
}
