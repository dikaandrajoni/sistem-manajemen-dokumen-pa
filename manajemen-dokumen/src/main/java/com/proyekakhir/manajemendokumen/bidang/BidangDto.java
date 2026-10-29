package com.proyekakhir.manajemendokumen.bidang;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import com.proyekakhir.manajemendokumen.position.Position;
import com.proyekakhir.manajemendokumen.position.PositionDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

@Getter
@Setter
public class BidangDto extends BaseDto<Bidang, BidangDto> {
    private String name;
    private Position timKBK;

    @Override
    public Bidang fromDto() {
        Bidang bidang = new Bidang();
        BeanUtils.copyProperties(this, bidang, getNullPropertyNames(this));
        bidang.setId(this.getId());
        if (!ObjectUtils.isEmpty(timKBK) && !ObjectUtils.isEmpty(timKBK.getId())){
            Position position = new Position();
            position.setId(this.timKBK.getId());
            bidang.setTimKBK(position);
        }
        return bidang;
    }

    @Override
    public BidangDto toDto(Bidang entity) {
        BidangDto dto = super.toDto(entity);
        if(entity.getTimKBK()!=null){
            PositionDto positionDto = new PositionDto();
            positionDto.toDto(entity.getTimKBK());
            dto.setTimKBK(positionDto.fromDto());
        }
        return dto;
    }

}
