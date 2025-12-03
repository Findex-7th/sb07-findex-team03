package com.team3.findex.domain.index;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity @Table(name ="IndexDataUser")
public class IndexDataUser extends IndexDataBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "index_info_id", unique = true)
    @NotNull(message = "🚨indexInfo 필수입니다.")
    private IndexInfo indexInfo; // 지수

    @Column(name = "user_id", nullable = false)
    @NotNull(message = "🚨userId 필수입니다.")
    private Long userId;

    @Column(name = "is_autosync",nullable = false)
    @NotNull(message = "🚨isAutosync 필수입니다.")
    private boolean isAutosync;

    @Column(name = "is_favorites",nullable = false)
    @NotNull(message = "🚨isFavorites 필수입니다.")
    private boolean isFavorites;
}
