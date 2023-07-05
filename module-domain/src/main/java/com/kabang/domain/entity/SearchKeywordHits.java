package com.kabang.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "search_keyword_hits_seq",
        sequenceName = "search_keyword_hits_seq",
        allocationSize = 1
)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "search_keyword_hits",
        indexes = {
                @Index(name = "search_keyword_hits_in01", columnList = "keyword"),
                @Index(name = "search_keyword_hits_in02", columnList = "hits_count desc")
        })
public class SearchKeywordHits {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "search_keyword_hits_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "keyword", unique = true)
    private String keyword;

    @Column(name = "hits_count")
    private Long hitsCount;

    public static SearchKeywordHits create(String keyword, Long hitCount){
        return SearchKeywordHits.builder()
                .keyword(keyword)
                .hitsCount(hitCount)
                .build();
    }

    public SearchKeywordHits updateHitCount(Long hitCount){
        this.hitsCount += hitCount;
        return this;
    }
}
