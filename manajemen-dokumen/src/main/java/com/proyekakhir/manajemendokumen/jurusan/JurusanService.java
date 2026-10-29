package com.proyekakhir.manajemendokumen.jurusan;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.services.CrudManajemenService;
import org.springframework.stereotype.Service;

@Service
public class JurusanService extends CrudManajemenService<Jurusan, JurusanDto, JurusanRepository> {

    protected JurusanService(JurusanRepository simpleJpaRepository, CurrentUser currentUser) {
        super(simpleJpaRepository, currentUser);
    }
}
