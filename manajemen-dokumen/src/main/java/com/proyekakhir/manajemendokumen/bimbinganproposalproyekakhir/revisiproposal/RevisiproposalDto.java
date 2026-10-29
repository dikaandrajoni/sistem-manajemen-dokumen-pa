package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.revisiproposal;

import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevisiproposalDto extends BaseDto<BimbinganProposalProyekAkhir, RevisiproposalDto> {
    private Integer bimbinganKe;
}
