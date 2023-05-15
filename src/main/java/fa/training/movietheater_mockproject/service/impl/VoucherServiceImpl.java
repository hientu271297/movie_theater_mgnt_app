package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.Voucher;
import fa.training.movietheater_mockproject.repository.VoucherRepository;
import fa.training.movietheater_mockproject.service.VoucherService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    VoucherRepository voucherRepository;
    @Override
    public List<Voucher> getAll() {
        return (List<Voucher>) voucherRepository.findAll();
    }

    @Override
    public Page<Voucher> getAll(Specification<Voucher> specification, Pageable pageable) {
        return voucherRepository.findAll(specification,pageable);
    }

    @Override
    public void deleteVoucher(Voucher voucher) {
        voucher.setDeleted(true);
        voucherRepository.save(voucher);
    }

    @Override
    public Optional<Voucher> getVoucherById(Long id) {
        return voucherRepository.findByVoucherIdAndDeletedFalse(id);
    }

    @Override
    public void saveVoucher(Voucher voucher) {
        voucher.setDeleted(false);
        voucherRepository.save(voucher);
    }

    @Override
    public Optional<Voucher> getVoucherByName(String name) {
        return voucherRepository.findByVoucherName(name);
    }



}
