package org.wallride.model;

import lombok.Getter;
import lombok.Setter;
import org.wallride.domain.CodeStoreItem;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class PropertySearchRequest implements Serializable {
    private Set<CodeStoreItem> rentPeriods = new HashSet<>();
    private String title;
    private List<Integer> termOfLease;
    private Date from;
    private Date to;
    private Integer currency;
    private Integer minPrice;
    private Integer maxPrice;
    private Long authUserId;
}
