    package org.example.dajava.Repository;

    import org.example.dajava.Model.TaiKhoan;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    }
