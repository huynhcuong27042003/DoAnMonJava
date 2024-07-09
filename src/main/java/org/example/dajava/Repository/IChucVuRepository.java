package org.example.dajava.Repository;

import org.example.dajava.Model.ChucVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChucVuRepository extends JpaRepository<ChucVu, Long> {
    ChucVu findChucVuByIDChucVu(Long IDChucVu);
}
