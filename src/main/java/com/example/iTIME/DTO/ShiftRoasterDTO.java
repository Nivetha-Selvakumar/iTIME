package com.example.iTIME.DTO;


import com.example.iTIME.Enum.ShiftRoasterType;
import lombok.*;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class ShiftRoasterDTO {


    private  List <Integer> employeeList;

    private  String shiftType;//general shift

    private ShiftRoasterType assignShiftType;//monthly yearly

    private String startDate;

    private  String endDate;

    private String month;

    private String year;

    private  List <String> weekOff1;

    private  List <String> weekOff2;

    private  List <String> weekOff3;

    private  List <String> weekOff4;

    private  List <String> weekOff5;

    private  List <String> weekOff6;

}
