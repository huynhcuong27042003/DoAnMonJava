package org.example.dajava.Service;

import lombok.RequiredArgsConstructor;
import org.example.dajava.Model.HangXe;
import org.example.dajava.Repository.HangXeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HangXeService {
    @Autowired
    private HangXeRepository hangXeRepository;

    public List<HangXe> getAllHangXe() {
        return hangXeRepository.findAll();
    }

    public Optional<HangXe> getHangXeById(Integer id) {
        return hangXeRepository.findById(id);
    }

    public HangXe createHangXe(HangXe hangXe) {
        return hangXeRepository.save(hangXe);
    }

    public Optional<HangXe> updateHangXe(Integer id, HangXe hangXe) {
        Optional<HangXe> optionalHangXe = hangXeRepository.findById(id);
        if (optionalHangXe.isPresent()) {
            HangXe existingHangXe = optionalHangXe.get();
            existingHangXe.setTenHang(hangXe.getTenHang()); // Update only the TenHang field
            return Optional.of(hangXeRepository.save(existingHangXe));
        } else {
            return Optional.empty();
        }
    }

    public void deleteHangXe(Integer id) {
        hangXeRepository.deleteById(id);
    }
}
