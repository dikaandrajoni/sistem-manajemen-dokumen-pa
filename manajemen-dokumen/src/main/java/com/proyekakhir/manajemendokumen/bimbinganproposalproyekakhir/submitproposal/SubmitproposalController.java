package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.submitproposal;

import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirVariable;
import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/BimbinganProposalProyekAkhir/submitProposal")
public class SubmitproposalController extends UserTaskController<BimbinganProposalProyekAkhir, SubmitproposalDto, BimbinganProposalProyekAkhirVariable, SubmitproposalService, BimbinganProposalProyekAkhirRepository> {
    public SubmitproposalController(SubmitproposalService submitProposalService) {
        super(submitProposalService, "submitProposal");
    }
}
