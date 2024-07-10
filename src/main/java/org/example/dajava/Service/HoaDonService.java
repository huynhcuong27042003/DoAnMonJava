package org.example.dajava.Service;

import org.example.dajava.Model.HoaDon;
import org.example.dajava.Repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;
    public HoaDon  save(HoaDon hoaDon) {
        return hoaDonRepository.save(hoaDon);
    }
}
