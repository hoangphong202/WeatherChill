package com.example.demo.Repository;


import com.example.demo.Entity.TkKhachEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TkKhachRepository extends JpaRepository<TkKhachEntity, Integer> {
    TkKhachEntity findByTaikhoanAndMatkhau(String taikhoan, String matkhau);
    TkKhachEntity findByTaikhoan(String taikhoan);

    TkKhachEntity findByTen(String ten);
}
