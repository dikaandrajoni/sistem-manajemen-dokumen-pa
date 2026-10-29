package com.proyekakhir.manajemendokumen.bimbinganproyekakhir;

import com.proyekakhir.manajemendokumen.core.entity.BaseVariable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BimbinganProyekAkhirVariable extends BaseVariable {
    String requestor;
    Boolean isMbkm;
    String diploma;
    String kaprodi;
    String koordinatorPA;
    List<String> timKBK;
    List<String> reviewers;
    String penguji;
    String hasilSeminar;
    String hasilSidang;
    Boolean isApproved;
}
