package com.kabang.domain.entity;

import com.kabang.common.dto.type.FlagType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SequenceGenerator(
        name = "search_keyword_history_seq",
        sequenceName = "search_keyword_history_seq",
        allocationSize = 1
)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "search_keyword_history",
        indexes = {
                @Index(name = "search_keyword_history_in01", columnList = "keyword"),
                @Index(name = "search_keyword_history_in02", columnList = "check_yn")
        })
public class SearchKeywordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "search_keyword_history_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "check_yn")
    @Enumerated(EnumType.STRING)
    private FlagType checkYn;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    public static SearchKeywordHistory create(String keyword){
        return SearchKeywordHistory.builder()
                .keyword(keyword)
                .checkYn(FlagType.N)
                .createDt(LocalDateTime.now())
                .build();
    }

    public void updateCheck(FlagType checkYn){
        this.checkYn = checkYn;
    }
    public void updateCreateDt(LocalDateTime createDt) {
        this.createDt = createDt;
    }
}
