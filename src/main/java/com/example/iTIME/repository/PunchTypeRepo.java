package com.example.iTIME.repository;

import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface PunchTypeRepo extends JpaRepository<PunchTypeEntity, Integer> {

      List<PunchTypeEntity> findByEmpIdAndPunchTimeBetween(EmployeeEntity employeeEntity, Timestamp startDateTime, Timestamp endDateTime);

      PunchTypeEntity findTopByEmpIdOrderByPunchTimeDesc(EmployeeEntity employeeEntity);

      PunchTypeEntity findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(EmployeeEntity employeeEntity, Timestamp startDateTime, Timestamp endDateTime);
}
