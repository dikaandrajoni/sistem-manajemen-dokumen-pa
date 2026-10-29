package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.reviewproposal;

import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/BimbinganProposalProyekAkhir/reviewProposal")
public class ReviewproposalController extends UserTaskController<BimbinganProposalProyekAkhir, ReviewproposalDto, ReviewproposalVariable, ReviewproposalService, BimbinganProposalProyekAkhirRepository> {
    public ReviewproposalController(ReviewproposalService reviewProposalService) {
        super(reviewProposalService, "reviewProposal");
    }
}
