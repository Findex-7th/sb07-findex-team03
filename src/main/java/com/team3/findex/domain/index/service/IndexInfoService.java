package com.team3.findex.domain.index.service;

import com.team3.findex.domain.index.dto.request.IndexInfoCreateRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoUpdateRequest;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import java.util.List;

public interface IndexInfoService {
  IndexInfoDto create(IndexInfoCreateRequest request);
  IndexInfoDto update(IndexInfoUpdateRequest request);
  IndexInfoDto getById(Long id);
  List<IndexInfoDto> findAll();
  void delete(Long id);
}
