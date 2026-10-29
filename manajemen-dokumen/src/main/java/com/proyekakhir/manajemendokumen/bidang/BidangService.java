package com.proyekakhir.manajemendokumen.bidang;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.services.CrudManajemenService;
import org.springframework.stereotype.Service;

@Service
public class BidangService extends CrudManajemenService<Bidang, BidangDto, BidangRepository> {

    protected BidangService(BidangRepository simpleJpaRepository, CurrentUser currentUser) {
        super(simpleJpaRepository, currentUser);
    }
}
