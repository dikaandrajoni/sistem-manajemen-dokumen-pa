package com.proyekakhir.manajemendokumen.core.repository.search;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchCriteria {
    private String key;
    private Object value;
    private SearchOperation operation;

    /**
     * To search many to one relation, use dot as separator. For example, to search City that belongs to Province DKI Jakarta, add
     * new SearchCriteria("province.name", "DKI Jakarta", SearchOperation.EQUAL);
     *
     * @param key
     * @param value
     * @param operation
     */
    public SearchCriteria(String key, Object value, SearchOperation operation) {
        this.key = key;
        this.value = value;
        this.operation = operation;
    }
}
