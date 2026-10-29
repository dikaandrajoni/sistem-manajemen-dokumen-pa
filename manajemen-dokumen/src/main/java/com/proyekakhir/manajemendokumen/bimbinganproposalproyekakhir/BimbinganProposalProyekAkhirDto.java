package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
public class BimbinganProposalProyekAkhirDto extends BaseDto<BimbinganProposalProyekAkhir, BimbinganProposalProyekAkhirDto> {
    private ManajemenDokumenProyekAkhirDto manajemenDokumenProyekAkhir;
    private Boolean pembimbing1IsApproved;
    private Boolean pembimbing2IsApproved;
    private String processInstanceId;
    @Type(type = "text")
    private String catatanP1;
    @Type(type = "text")
    private String catatanP2;
    private Integer bimbinganKe;
}
