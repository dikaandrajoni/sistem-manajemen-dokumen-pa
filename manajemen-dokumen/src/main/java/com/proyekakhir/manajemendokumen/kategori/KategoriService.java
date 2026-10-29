package com.proyekakhir.manajemendokumen.kategori;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.services.CrudManajemenService;
import org.springframework.stereotype.Service;

@Service
public class KategoriService extends CrudManajemenService<Kategori, KategoriDto, KategoriRepository> {

    protected KategoriService(KategoriRepository simpleJpaRepository, CurrentUser currentUser) {
        super(simpleJpaRepository, currentUser);
    }
}
