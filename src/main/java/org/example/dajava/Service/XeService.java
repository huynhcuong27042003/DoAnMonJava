package org.example.dajava.Service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.dajava.Model.Xe;
import org.example.dajava.Repository.XeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class XeService {
    private final XeRepository xeRepository;

    @Autowired
    public XeService(XeRepository xeRepository) {
        this.xeRepository = xeRepository;
    }

    public List<Xe> getAllXe() {
        return xeRepository.findAll();
    }
    public List<Xe> getAllHiddenXe() {
            return xeRepository.findAll().stream()
            .filter(Xe::isHide)
            .collect(Collectors.toList());
    }
    public List<Xe> getAllNotHiddenXe() {
        return xeRepository.findByHide();
    }
    public Xe findXeByBienSoXe(String id) {
        return xeRepository.findByBienSoXe(id);
    }
    public Optional<Xe> getXeByBienSo(String bienSo) {
        return xeRepository.findById(bienSo);
    }

    public Xe createXe(Xe xe) {
        xe.setHide(true);
        xe.setTrangThai(false);
        return xeRepository.save(xe);
    }
    public Xe publicXe(Xe xe){
        xe.setHide(false);
        return xeRepository.save(xe);
    }
    public Optional<Xe> getDetailXe(String bienSo) {
        return xeRepository.findById(bienSo);
    }

    public Xe updateXe(@NotNull Xe xe) {
        Xe existingXe = xeRepository.findById(xe.getBienSoXe()).orElseThrow(() -> new IllegalStateException("Xe with ID " + xe.getBienSoXe() + " not found"));
        existingXe.setBienSoXe(xe.getBienSoXe());
        existingXe.setHangXe(xe.getHangXe());
        existingXe.setLoaiXe(xe.getLoaiXe());
        existingXe.setDiaDiem(xe.getDiaDiem());
        existingXe.setTenXe(xe.getTenXe());
        existingXe.setGiaThue(xe.getGiaThue());
        existingXe.setLucPhanKhoi(xe.getLucPhanKhoi());
        existingXe.setMoTa(xe.getMoTa());
        existingXe.setTrangThai(xe.isTrangThai());
        existingXe.setHide(xe.isHide());
        existingXe.setHinhAnh1(xe.getHinhAnh1());
        existingXe.setHinhAnh2(xe.getHinhAnh2());
        existingXe.setHinhAnh3(xe.getHinhAnh3());
        existingXe.setHinhAnh4(xe.getHinhAnh4());
        return xeRepository.save(existingXe);

    }

    public void hideXe(String bienSo) {
        Xe existingXe = xeRepository.findById(bienSo)
                .orElseThrow(() -> new IllegalStateException("Xe with ID " + bienSo + " not found"));
        existingXe.setHide(false);
        xeRepository.save(existingXe);
    }

    public List<Xe> findAll() {
        return xeRepository.findAll();
    }
}
