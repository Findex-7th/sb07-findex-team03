package com.team3.findex.synclog.entity;

import com.team3.findex.synclog.syncjobenums.JobType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@ToString(exclude = "indexInfoId")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class SyncJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private JobType jobType;

    @CreatedDate
    @Column( name = "created_at", updatable = false, nullable = false)
    @NotNull
    private Instant createdAt;

    @Column(nullable = false)
    @NotNull
    private String worker;

    private Long indexInfoId;
    
}
