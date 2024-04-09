package com.example.iTIME.entity;


import com.example.iTIME.Enum.ShiftType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="tbl_shift")
public class ShiftEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "shift_name", nullable = false, length = 25 )
    private String shiftName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "shiftType", nullable = false, length = 10)
    private ShiftType shiftType;

    @Column(name = "start_time", nullable = false)
    private Time startTime;

    @Column(name = "end_time", nullable = false)
    private Time endTime;

    @Column(name = "start_from", nullable = false, length = 15)
    private String startFrom;

    @Column(name = "end_at", nullable = false, length = 15)
    private String endAt;

    @OneToMany(mappedBy = "shiftId")
    List<EmployeeEntity> employeeEntityList;

}
