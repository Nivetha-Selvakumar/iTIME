package com.example.iTIME.repository;

import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.ShiftRoasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface ShiftRoasterRepo extends JpaRepository<ShiftRoasterEntity, Integer> {

//    ShiftRoasterEntity findByEmpIdAndOrderByDesc(EmployeeEntity employeeEntity);

    ShiftRoasterEntity findByEmpId(EmployeeEntity employeeEntity);
}
