package com.team3.findex.synclog.repository;

import com.team3.findex.synclog.entity.SyncJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncJobRepository extends JpaRepository<SyncJob, Long> {
}
