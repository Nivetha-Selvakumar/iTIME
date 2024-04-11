package com.example.iTIME.repository;

import com.example.iTIME.Enum.PermissionStatus;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PermissionTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@EnableJpaRepositories
@Repository
public interface PermissionTransactionRepo extends JpaRepository<PermissionTransactionEntity, Integer> {

    PermissionTransactionEntity findByEmpIdAndPermissionDateBetweenAndApprovalStatus(EmployeeEntity employeeEntity, Timestamp startOfDay, Timestamp endOfDay, PermissionStatus permissionStatus);

}
