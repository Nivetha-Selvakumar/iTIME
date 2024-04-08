package com.example.iTIME.repository;

import com.example.iTIME.Enum.PunchType;
import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.PunchTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface PunchTypeRepo extends JpaRepository<PunchTypeEntity, Integer> {
//      Optional<PunchTypeEntity> findTopByPunchTypeAndEmpIdOrderByPunchTimeDesc(PunchType punchType, EmployeeEntity employeeEntity);
//
//      Optional<PunchTypeEntity> findTopByEmpIdOrderByPunchTimeDesc(PunchType punchType, EmployeeEntity employeeEntity);

//      PunchTypeEntity findTopByPunchTypeAndEmpIdOrderByTimeAsc(PunchType punchType,EmployeeEntity employeeEntity);

//      PunchTypeEntity findTopByPunchTypeAndEmpIdOrderByTimeDesc(PunchType punchType,EmployeeEntity employeeEntity);

      List<PunchTypeEntity> findByEmpIdAndPunchTimeBetween(EmployeeEntity employeeEntity, Timestamp startDateTime, Timestamp endDateTime);

      PunchTypeEntity findTopByEmpIdOrderByPunchTimeDesc(EmployeeEntity employeeEntity);

      PunchTypeEntity findTopByEmpIdAndPunchTimeBetweenOrderByPunchTimeDesc(EmployeeEntity employeeEntity, Timestamp startDateTime, Timestamp endDateTime);
}
