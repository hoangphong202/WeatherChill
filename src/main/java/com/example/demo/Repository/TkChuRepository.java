package com.example.demo.Repository;

import com.example.demo.Entity.TkChuEntity;
import com.example.demo.Entity.TkKhachEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TkChuRepository extends JpaRepository<TkChuEntity, Integer> {
    TkChuEntity findByTaikhoanAndMatkhau(String taikhoan, String matkhau);

    TkChuEntity findByTen(String ten);

}
