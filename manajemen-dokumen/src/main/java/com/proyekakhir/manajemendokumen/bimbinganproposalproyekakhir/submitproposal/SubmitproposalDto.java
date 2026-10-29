package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.submitproposal;

import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitproposalDto extends BaseDto<BimbinganProposalProyekAkhir, SubmitproposalDto> {
    private Integer bimbinganKe;
}
