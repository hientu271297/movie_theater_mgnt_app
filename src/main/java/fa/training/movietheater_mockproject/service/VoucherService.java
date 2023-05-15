package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.Room;
import fa.training.movietheater_mockproject.model.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface VoucherService extends BaseService<Voucher,Long>{
    public List<Voucher> getAll();

    Page<Voucher> getAll(Specification<Voucher> specification, Pageable pageable);

    void deleteVoucher(Voucher voucher);

    Optional<Voucher> getVoucherById(Long id);

    void saveVoucher(Voucher voucher);

    Optional<Voucher> getVoucherByName(String name);
}
