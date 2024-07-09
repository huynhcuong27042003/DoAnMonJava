package org.example.dajava.Service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.dajava.Model.PhuongThucThanhToan;
import org.example.dajava.Repository.PhuongThucThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PhuongThucThanhToanService {

    @Autowired
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    public List<PhuongThucThanhToan> findAll() {
        return phuongThucThanhToanRepository.findAll();
    }

    public Optional<PhuongThucThanhToan> findById(Integer id) {
        return phuongThucThanhToanRepository.findById(id);
    }

    public PhuongThucThanhToan createThanhToan(PhuongThucThanhToan phuongThucThanhToan) {
        return phuongThucThanhToanRepository.save(phuongThucThanhToan);
    }

    public PhuongThucThanhToan updateThanhToan(PhuongThucThanhToan phuongThucThanhToan) {
        PhuongThucThanhToan existingThanhToan = phuongThucThanhToanRepository.findById(phuongThucThanhToan.getMaPhuongThuc())
                .orElseThrow(() -> new IllegalStateException("Phuong Thuc Thanh Toan Not Found: " + phuongThucThanhToan.getMaPhuongThuc()));

        // Update other fields
        existingThanhToan.setTenPhuongThuc(phuongThucThanhToan.getTenPhuongThuc());

        return phuongThucThanhToanRepository.save(existingThanhToan);
    }


    public void deleteById(Integer id) {
        phuongThucThanhToanRepository.deleteById(id);
    }
}