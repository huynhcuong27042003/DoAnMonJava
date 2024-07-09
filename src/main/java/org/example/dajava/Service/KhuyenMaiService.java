package org.example.dajava.Service;

import jakarta.validation.constraints.NotNull;
import org.example.dajava.Model.KhuyenMai;
import org.example.dajava.Repository.KhuyenMaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KhuyenMaiService {

    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    public List<KhuyenMai> getAllKhuyenMai() {
        return khuyenMaiRepository.findAll();
    }

    public Optional<KhuyenMai> getKhuyenMaiById(Integer id) {
        return khuyenMaiRepository.findById(id);
    }

    public KhuyenMai createKhuyenMai(KhuyenMai khuyenMai) {
        return khuyenMaiRepository.save(khuyenMai);
    }

    public KhuyenMai updateKhuyenMai(@NotNull KhuyenMai khuyenMai) {
        KhuyenMai existingKhuyenMai = khuyenMaiRepository.findById(khuyenMai.getMaKhuyenMai()).orElseThrow(() -> new IllegalStateException("KhuyenMai with ID " + khuyenMai.getMaKhuyenMai() + " not found"));
        existingKhuyenMai.setMaKhuyenMai(khuyenMai.getMaKhuyenMai());
        existingKhuyenMai.setTenKhuyenMai(khuyenMai.getTenKhuyenMai());
        existingKhuyenMai.setCode(khuyenMai.getCode());
        existingKhuyenMai.setNoiDungKhuyenMai(khuyenMai.getNoiDungKhuyenMai());
        existingKhuyenMai.setPhanTramKhuyenMai(khuyenMai.getPhanTramKhuyenMai());
        existingKhuyenMai.setHinhAnhKhuyenMai(khuyenMai.getHinhAnhKhuyenMai());
        existingKhuyenMai.setNgayKhuyenMai(khuyenMai.getNgayKhuyenMai());
        existingKhuyenMai.setNgayKetThuc(khuyenMai.getNgayKetThuc());
        return khuyenMaiRepository.save(existingKhuyenMai);
    }

    public void deleteKhuyenMai(Integer id) {
        khuyenMaiRepository.deleteById(id);
    }
    public KhuyenMai findByCode(String code) {
        return khuyenMaiRepository.findByCode(code);
    }

}
