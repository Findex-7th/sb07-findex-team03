package com.team3.findex.domain.syncjob.controller;

import com.team3.findex.domain.syncjob.dto.request.CursorPageRequestSyncJobDto;
import com.team3.findex.domain.syncjob.dto.response.CursorPageResponseSyncJobDto;
import com.team3.findex.domain.syncjob.dto.request.IndexDataSyncRequest;
import com.team3.findex.domain.syncjob.dto.response.SyncJobDto;
import com.team3.findex.domain.syncjob.service.SyncJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SyncJob (동기화 작업)", description = "외부 API 연동 및 동기화 작업 이력 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sync-jobs")
public class SyncJobController {

    private final SyncJobService syncJobService;

    @Operation(summary = "지수 목록(Index Info) 동기화", description = "외부 API에서 전체 지수 목록을 가져와 DB와 동기화합니다.")
    @PostMapping("/index-infos")
    public ResponseEntity<List<SyncJobDto>> syncIndexInfos(HttpServletRequest request){
        String worker = ipIntercept(request);
        List<SyncJobDto> indexInfos = syncJobService.syncIndexInfos(worker);
        return ResponseEntity.ok(indexInfos);
    }

    @Operation(summary = "지수 데이터(Index Data) 동기화", description = "특정 지수의 기간별 데이터를 외부 API에서 가져와 동기화합니다.")
    @PostMapping("/index-data")
    public ResponseEntity<List<SyncJobDto>> syncIndexData(
            @RequestBody IndexDataSyncRequest indexDataSyncRequest,
            HttpServletRequest request
    ){
        String worker = ipIntercept(request);
        List<SyncJobDto> syncJobDtos = syncJobService.syncIndexData(indexDataSyncRequest, worker);
        return ResponseEntity.ok(syncJobDtos);
    }

    @Operation(summary = "동기화 이력 조회 (커서 페이징)", description = "동기화 작업 이력을 커서 기반으로 조회합니다.")
    @GetMapping
    public ResponseEntity<CursorPageResponseSyncJobDto> cursorPageResponse(
            @ParameterObject @ModelAttribute CursorPageRequestSyncJobDto cursorPageRequestSyncJobDto
            ){
        return ResponseEntity.ok(syncJobService.getSyncJobsByCursor(cursorPageRequestSyncJobDto));
    }

    private String ipIntercept(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
