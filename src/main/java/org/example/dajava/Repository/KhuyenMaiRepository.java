package org.example.dajava.Repository;

import org.example.dajava.Model.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {
    @Query("select km from KhuyenMai km where km.Code=?1")
    KhuyenMai findByCode(String Code);
}