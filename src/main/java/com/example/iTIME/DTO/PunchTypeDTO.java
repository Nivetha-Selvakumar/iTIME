package com.example.iTIME.DTO;

import com.example.iTIME.Enum.PunchType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Component
public class PunchTypeDTO {

    private Timestamp punchTime;

    @NotBlank(message = "Enter Punch Type")
    private PunchType punchType;

}

