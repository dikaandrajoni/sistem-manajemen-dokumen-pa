package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.revisiproposal;

import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/BimbinganProposalProyekAkhir/revisiProposal")
public class RevisiproposalController extends UserTaskController<BimbinganProposalProyekAkhir, RevisiproposalDto, RevisiproposalVariable, RevisiproposalService, BimbinganProposalProyekAkhirRepository> {
    public RevisiproposalController(RevisiproposalService revisiProposalService) {
        super(revisiProposalService, "revisiProposal");
    }
}
