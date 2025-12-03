package com.team3.findex.domain.autosync.service;

import com.team3.findex.domain.autosync.AutoSync;
import com.team3.findex.domain.autosync.dto.AutoSyncConfigUpdateRequest;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.repository.AutoSyncRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutoSyncService {

  private final AutoSyncRepository autoSyncRepository;

  @Transactional
  public AutoSync create(IndexInfo indexInfo){

    if(indexInfo == null){
      throw new IllegalArgumentException("지수 정보가 없습니다.");
    }
    if(autoSyncRepository.existsByIndexInfo(indexInfo)){
      throw new IllegalArgumentException("지수의 자동 연동 설정이 이미 되어있습니다.");
    }

    AutoSync autoSync = new AutoSync(indexInfo);
    return autoSyncRepository.save(autoSync);
  }

  @Transactional
  public AutoSync updateEnable(Long id, AutoSyncConfigUpdateRequest enable){
    AutoSync autoSync = autoSyncRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("자동 설정할 ID가 없습니다."));
    autoSync.updateEnable(enable.enabled());
    return autoSync;
  }

}
