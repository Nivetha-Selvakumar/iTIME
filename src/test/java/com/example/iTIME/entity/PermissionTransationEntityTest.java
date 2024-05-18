package com.example.iTIME.entity;

import com.example.iTIME.Enum.PermissionStatus;
import org.assertj.core.api.OptionalAssert;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
 class PermissionTransationEntityTest {
    @InjectMocks
    PermissionTransactionEntity permissionTransactionEntity;

    @Test
    void permissionTransactionTest(){
        permissionTransactionEntity.setId(1);
        permissionTransactionEntity.setEmpId(new EmployeeEntity());
        permissionTransactionEntity.setPermissionTypeId(new PermissionTypeEntity());
        permissionTransactionEntity.setStartTime(Time.valueOf("06:00:00"));
        permissionTransactionEntity.setEndTime(Time.valueOf("09:00:00"));
        permissionTransactionEntity.setApprovalStatus(PermissionStatus.REJECTED);
        permissionTransactionEntity.setPermissionDate(Timestamp.valueOf("2024-09-09 00:00:00"));

        int id = permissionTransactionEntity.getId();
        EmployeeEntity employeeEntity = permissionTransactionEntity.getEmpId();
        PermissionTypeEntity permissionTypeEntity = permissionTransactionEntity.getPermissionTypeId();
        Time startTime = permissionTransactionEntity.getStartTime();
        Time endTime = permissionTransactionEntity.getEndTime();
        PermissionStatus approval = permissionTransactionEntity.getApprovalStatus();
        Timestamp permissionDate = permissionTransactionEntity.getPermissionDate();

        Assertions.assertEquals(Optional.of(id), Optional.of(permissionTransactionEntity.getId()));
        Assertions.assertEquals(employeeEntity,permissionTransactionEntity.getEmpId());
        Assertions.assertEquals(permissionTypeEntity, permissionTransactionEntity.getPermissionTypeId());
        Assertions.assertEquals(startTime, permissionTransactionEntity.getStartTime());
        Assertions.assertEquals(endTime, permissionTransactionEntity.getEndTime());
        Assertions.assertEquals(approval, permissionTransactionEntity.getApprovalStatus());
        Assertions.assertEquals(permissionDate,permissionTransactionEntity.getPermissionDate());

        PermissionTransactionEntity permissionTransactionEntity1 = new PermissionTransactionEntity();
        Assertions.assertNotNull(permissionTransactionEntity1);

    }
}
