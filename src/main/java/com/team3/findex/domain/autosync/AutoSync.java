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

    @Column(name = "is_enable", nullable = false)
    private boolean isEnable = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "index_info_id", insertable = false, updatable = false)
    private IndexInfo indexInfo;

    public AutoSync(IndexInfo indexInfo){
      this.indexInfo = indexInfo;
      this.isEnable = false;
    }

    public void updateEnable(boolean isEnable){
        this.isEnable = isEnable;
    }

}
