package org.example.dajava.Service;

import lombok.RequiredArgsConstructor;
import org.example.dajava.Model.LoaiXe;
import org.example.dajava.Repository.LoaiXeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoaiXeService {

    @Autowired
    private LoaiXeRepository loaiXeRepository;

    public List<LoaiXe> getAllLoaiXe() {
        return loaiXeRepository.findAll();
    }

    public Optional<LoaiXe> getLoaiXeById(Integer id) {
        return loaiXeRepository.findById(id);
    }

    public LoaiXe createLoaiXe(LoaiXe loaiXe) {
        return loaiXeRepository.save(loaiXe);
    }

    public Optional<LoaiXe> updateLoaiXe(Integer id, LoaiXe loaiXe) {
        Optional<LoaiXe> existingLoaiXe = loaiXeRepository.findById(id);
        if (existingLoaiXe.isPresent()) {
            loaiXe.setMaLoai(id);
            return Optional.of(loaiXeRepository.save(loaiXe));
        } else {
            return Optional.empty();
        }
    }

    public void deleteLoaiXe(Integer id) {
        loaiXeRepository.deleteById(id);
    }
}
