package com.example.demo.Repository;

import com.example.demo.Entity.RoleEntity;
import com.example.demo.Entity.TkChuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
}
