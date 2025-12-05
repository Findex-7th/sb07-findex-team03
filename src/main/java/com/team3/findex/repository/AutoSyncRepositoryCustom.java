package com.team3.findex.repository;

import com.team3.findex.domain.autosync.AutoSync;
import java.util.List;

public interface AutoSyncRepositoryCustom {

  List<AutoSync> findWithCursor(
      Long indexInfoId,
      boolean enabled,
      Long idAfter,
      String cursor,
      String sortField,
      String sortDirection,
      int size
  );

  long countWithFilter(Long indexInfoId, boolean enabled);
}
