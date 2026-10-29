package com.proyekakhir.manajemendokumen.bimbinganproyekakhir.reviewpembimbing;

import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/BimbinganProyekAkhir/reviewPembimbing")
public class ReviewpembimbingController extends UserTaskController<BimbinganProyekAkhir, ReviewpembimbingDto, ReviewpembimbingVariable, ReviewpembimbingService, BimbinganProyekAkhirRepository> {
    public ReviewpembimbingController(ReviewpembimbingService reviewPembimbingService) {
        super(reviewPembimbingService, "reviewPembimbing");
    }
}
