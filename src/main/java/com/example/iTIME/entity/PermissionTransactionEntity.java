package com.example.iTIME.entity;

import com.example.iTIME.Enum.PermissionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="tbl_permission_transaction")
public class PermissionTransactionEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", referencedColumnName = "Id", nullable = false)
    private EmployeeEntity empId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_type_id", referencedColumnName = "Id", nullable = false)
    private PermissionTypeEntity permissionTypeId;

    @Column(name = "start_time", nullable = false)
    private Time startTime;

    @Column(name = "end_time", nullable = false)
    private Time endTime;

    @Enumerated(value = EnumType.STRING)
    @Column(name="approval_status",nullable = false,length = 10)
    private PermissionStatus approvalStatus;

    @Column(name = "permission_date", nullable = false)
    private Timestamp permissionDate;

}
