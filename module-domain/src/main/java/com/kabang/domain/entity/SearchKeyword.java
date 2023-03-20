package com.kabang.domain.entity;

import com.kabang.common.dto.type.FlagType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SequenceGenerator(
        name = "search_keyword_hits_seq_generator",
        sequenceName = "search_keyword_hits_seq",
        allocationSize = 1
)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "search_keyword",
        uniqueConstraints = {
                @UniqueConstraint(name = "search_keyword_hits_uk01", columnNames = {"access_id", "keyword"})
        },
        indexes = {
                @Index(name = "search_keyword_hits_in01", columnList = "keyword"),
                @Index(name = "search_keyword_hits_in02", columnList = "check_yn")
        })
public class SearchKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "search_keyword_hits_seq_generator")
    @Column(name = "id")
    private long id;

    @Column(name = "access_id")
    private String accessId;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "check_yn")
    private FlagType checkYn;

    @Column(name = "createDt")
    private LocalDateTime createDt;

    public static SearchKeyword create(String accessId, String keyword){
        return SearchKeyword.builder()
                .accessId(accessId)
                .keyword(keyword)
                .checkYn(FlagType.N)
                .createDt(LocalDateTime.now())
                .build();
    }

    public void updateCheck(FlagType checkYn){
        this.checkYn = checkYn;
    }
}
