package com.example.iTIME.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="tbl_employee")
public class EmployeeEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "emp_name", nullable = false, length = 25 )
    private String empName;

    @Column(name = "emp_code", nullable = false, length = 10)
    private String empCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", referencedColumnName = "Id", nullable = false)
    private ShiftEntity shiftId;

    @OneToMany(mappedBy = "empId")
    List<PunchTypeEntity> punchTypeEntityList;

    @OneToMany(mappedBy = "empId")
    List<PermissionTransactionEntity> permissionTransactionEntityList;

    @OneToMany(mappedBy = "empId")
    List<ShiftRoasterEntity> shiftRoasterEntityList;
}
