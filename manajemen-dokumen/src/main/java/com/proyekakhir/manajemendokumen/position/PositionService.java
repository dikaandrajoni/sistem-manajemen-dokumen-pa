package com.proyekakhir.manajemendokumen.position;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.services.CrudManajemenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionService extends CrudManajemenService<Position, PositionDto, PositionRepository> {

    protected PositionService(PositionRepository simpleJpaRepository, CurrentUser currentUser) {
        super(simpleJpaRepository, currentUser);
    }

    public List<Position> getTimKBK() {
        List<Position> positions = this.jpaRepository.findAll();
        positions = positions.stream().filter(position -> position.getName().toLowerCase().contains("tim kbk")).collect(Collectors.toList());
        return positions;
    }
}
