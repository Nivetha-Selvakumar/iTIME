package com.example.iTIME.entity;


import com.example.iTIME.Enum.PunchType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="tbl_punch_type")
public class PunchTypeEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "punch_time", nullable = false)
    @CreationTimestamp
    private Timestamp punchTime;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "punch_type", nullable = false, length = 10)
    private PunchType punchType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", referencedColumnName = "Id", nullable = false)
    private EmployeeEntity empId;

}
