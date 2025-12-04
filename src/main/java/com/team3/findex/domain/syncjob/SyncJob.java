package com.team3.findex.domain.syncjob;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.enums.Result;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@ToString(exclude = "indexInfo")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table
public class SyncJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private JobType jobType;

    @Column
    private String worker;

    @Column
    private LocalDate targetDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Result result = Result.SUCCESS;

    @CreatedDate
    @Column( name = "created_at", updatable = false, nullable = false)
    @NotNull
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "index_info_id", updatable = false)
    private IndexInfo indexInfo;


    public SyncJob(JobType jobType, String worker, LocalDate targetDate, IndexInfo indexInfo, Result result){
        this.jobType = jobType;
        this.worker = worker;
        this.targetDate = targetDate;
        this.indexInfo = indexInfo;
        this.result = result;
    }

    public static SyncJob ofSuccess(JobType jobType, String worker, LocalDate targetDate, IndexInfo indexInfo){
        return new SyncJob(jobType, worker, targetDate, indexInfo, Result.SUCCESS);
    }

    public static SyncJob ofFailure(JobType jobType, String worker, LocalDate targetDate, IndexInfo indexInfo){
        return new SyncJob(jobType, worker, targetDate, indexInfo, Result.FAILED);
    }
}
