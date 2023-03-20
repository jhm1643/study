package com.kabang.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "popular_keyword_seq_generator",
        sequenceName = "popular_keyword_seq",
        allocationSize = 1
)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "popular_keyword",
        indexes = {
                @Index(name = "popular_keyword_in01", columnList = "keyword"),
                @Index(name = "popular_keyword_in02", columnList = "hits_count desc")
        })
public class PopularKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "popular_keyword_seq_generator")
    @Column(name = "id")
    private long id;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "hits_count")
    private Long hitsCount;

    public static PopularKeyword create(String keyword, Long hitCount){
        return PopularKeyword.builder()
                .keyword(keyword)
                .hitsCount(hitCount)
                .build();
    }

    public void updateHitCount(Long hitCount){
        this.hitsCount += hitCount;
    }
}
