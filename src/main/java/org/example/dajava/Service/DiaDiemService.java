package org.example.dajava.Service;

import lombok.RequiredArgsConstructor;
import org.example.dajava.Model.DiaDiem;
import org.example.dajava.Repository.DiaDiemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaDiemService {
    @Autowired
    private final DiaDiemRepository diaDiemRepository;

    public List<DiaDiem> getAllDiaDiem() {
        return diaDiemRepository.findAll();
    }

    public Optional<DiaDiem> getDiaDiemById(Integer id) {
        return diaDiemRepository.findById(id);
    }

    public DiaDiem addDiaDiem(DiaDiem diaDiem) {
        return diaDiemRepository.save(diaDiem);
    }

    public DiaDiem editDiaDiem(DiaDiem diaDiem) {
        if (diaDiem.getMaDiaDiem() == null || !diaDiemRepository.existsById(diaDiem.getMaDiaDiem())) {
            throw new IllegalArgumentException("Địa điểm không tồn tại");
        }
        return diaDiemRepository.save(diaDiem);
    }

    public void deleteDiaDiem(Integer id) {
        diaDiemRepository.deleteById(id);
    }
}
