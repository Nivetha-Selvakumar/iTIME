package com.example.iTIME.repository;

import com.example.iTIME.entity.ShiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface ShiftRepo extends JpaRepository<ShiftEntity, Integer> {


}
