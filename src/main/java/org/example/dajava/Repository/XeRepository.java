package org.example.dajava.Repository;
import org.example.dajava.Model.Xe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface XeRepository  extends JpaRepository<Xe, String>{
    List<Xe> findAll();
    @Query("SELECT m FROM Xe m WHERE m.bienSoXe=?1")
    Xe findByBienSoXe(String bienSoXe);
    @Query("SELECT m FROM Xe m WHERE m.hide=false ")
    List<Xe> findByHide();
}
