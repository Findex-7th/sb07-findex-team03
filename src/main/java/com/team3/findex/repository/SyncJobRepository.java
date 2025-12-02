package com.team3.findex.repository;

import com.team3.findex.domain.synclog.SyncJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncJobRepository extends JpaRepository<SyncJob, Long> {
}
