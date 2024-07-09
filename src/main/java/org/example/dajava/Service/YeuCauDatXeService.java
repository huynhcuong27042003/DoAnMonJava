package org.example.dajava.Service;

import lombok.RequiredArgsConstructor;
import org.example.dajava.Model.YeuCauDatXe;
import org.example.dajava.Repository.YeuCauDatXeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YeuCauDatXeService {
    private final YeuCauDatXeRepository repository;

    public YeuCauDatXe addYeuCauDatXe(YeuCauDatXe yeuCauDatXe) {
        yeuCauDatXe.setHide(false);
        yeuCauDatXe.setTrangThaiChapNhan(false);
        return repository.save(yeuCauDatXe);
    }

    public List<YeuCauDatXe> findYCByEmail(String email) {
        return repository.findTaiKhoanByEmail(email);
    }

    public List<YeuCauDatXe> findByEmailAndBienSoXe(String email, String bienSoXe) {
        return repository.findByEmailAndBienSoXe(email, bienSoXe);
    }
    public YeuCauDatXe findById(int id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu đặt xe với id: " + id));
    }
    public void save(YeuCauDatXe yeuCauDatXe) {
        repository.save(yeuCauDatXe);
    }
}
