package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.reviewproposal;

import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
public class ReviewproposalDto extends BaseDto<BimbinganProposalProyekAkhir, ReviewproposalDto> {
    @Type(type = "text")
    private String catatan;
}
