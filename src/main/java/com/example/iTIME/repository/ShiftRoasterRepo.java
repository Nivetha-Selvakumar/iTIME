package com.example.iTIME.repository;

import com.example.iTIME.entity.EmployeeEntity;
import com.example.iTIME.entity.ShiftRoasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface ShiftRoasterRepo extends JpaRepository<ShiftRoasterEntity, Integer> {

        Optional<ShiftRoasterEntity> findByEmpIdAndMonthAndYear(EmployeeEntity employeeEntity1, Integer month, Integer year);
}
