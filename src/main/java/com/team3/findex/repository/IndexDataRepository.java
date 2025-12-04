package com.team3.findex.repository;

import com.team3.findex.domain.index.IndexData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IndexDataRepository extends JpaRepository<IndexData, Long> {
}
