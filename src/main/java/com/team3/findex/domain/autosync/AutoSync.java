package com.team3.findex.domain.autosync;

import com.team3.findex.domain.index.IndexInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = "indexInfo")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "auto_sync")
public class AutoSync {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_auto", nullable = false)
    private boolean isAuto = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "index_info_id", insertable = false, updatable = false)
    private IndexInfo indexInfo;
}
