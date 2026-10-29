package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir;

import com.proyekakhir.manajemendokumen.core.repository.search.BasePredicate;
import com.proyekakhir.manajemendokumen.core.repository.search.BaseSearchSpecification;
import com.proyekakhir.manajemendokumen.core.repository.search.SearchCriteria;
import com.proyekakhir.manajemendokumen.core.repository.search.SearchOperation;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class ManajemenDokumenSpecifications extends BaseSearchSpecification<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto> {
    protected ManajemenDokumenSpecifications(ManajemenDokumenProyekAkhirDto dto) {
        super(dto);
    }

    protected ManajemenDokumenSpecifications(List<String> processInstanceIds, ManajemenDokumenProyekAkhirDto dto) {
        super(processInstanceIds, dto);
    }

    public ManajemenDokumenSpecifications create(ManajemenDokumenProyekAkhirDto dto){
        ManajemenDokumenSpecifications specifications = new ManajemenDokumenSpecifications(dto);
        specifications.buildSpecification(dto);
        return specifications;
    }

    @Override
    public Predicate toPredicate(Root<ManajemenDokumenProyekAkhir> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        BasePredicate basePredicate = new BasePredicate();
        if(this.processInstanceIds != null){
            addCriteria(new SearchCriteria("processInstanceId", this.processInstanceIds, SearchOperation.IN));
        }
        List<Predicate> predicates = basePredicate.predicate(root, criteriaQuery, criteriaBuilder, searchCriteriaList);
        predicates.add(criteriaBuilder.isNull(root.get("deleteDate")));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    @Override
    public void buildSpecification(ManajemenDokumenProyekAkhirDto dto) {
        if (dto.getRequestorEmail() != null){
            addCriteria(new SearchCriteria("requestorEmail", dto.getRequestorEmail(), SearchOperation.EQUAL));
        }
        if (dto.getRequestorName() != null){
            addCriteria(new SearchCriteria("requestorName", dto.getRequestorName(), SearchOperation.MATCH));
        }
        if (dto.getJudulProyekAkhir() != null){
            addCriteria(new SearchCriteria("judulProyekAkhir", dto.getJudulProyekAkhir(), SearchOperation.MATCH));
        }
        if (dto.getRequestorCode() != null){
            addCriteria(new SearchCriteria("requestorCode", dto.getRequestorCode(), SearchOperation.EQUAL));
        }

        if (dto.getHasilReviewKaprodi() != null){
            addCriteria(new SearchCriteria("hasilReviewKaprodi", dto.getHasilReviewKaprodi(), SearchOperation.EQUAL));
        }

        if (dto.getHasilReviewKbk() != null){
            addCriteria(new SearchCriteria("hasilReviewKbk", dto.getHasilReviewKbk(), SearchOperation.EQUAL));
        }

    }
}
