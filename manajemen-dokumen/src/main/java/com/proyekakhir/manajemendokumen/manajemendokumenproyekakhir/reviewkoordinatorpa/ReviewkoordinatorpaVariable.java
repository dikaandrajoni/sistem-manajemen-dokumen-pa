package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.reviewkoordinatorpa;

import com.proyekakhir.manajemendokumen.core.entity.BaseVariable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewkoordinatorpaVariable extends BaseVariable {
    String requestor;
    String mahasiswa;
    Boolean isMbkm;
    String diploma;
    String kaprodi;
    String koordinatorPA;
    List<String> timKbk;
    String penguji;
    String hasilSeminar;
    String hasilSidang;
}
