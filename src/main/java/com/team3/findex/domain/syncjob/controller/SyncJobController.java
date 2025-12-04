package com.team3.findex.domain.syncjob.controller;

import com.team3.findex.domain.syncjob.dto.CursorPageRequestSyncJobDto;
import com.team3.findex.domain.syncjob.dto.CursorPageResponseSyncJobDto;
import com.team3.findex.domain.syncjob.dto.IndexDataSyncRequest;
import com.team3.findex.domain.syncjob.dto.SyncJobDto;
import com.team3.findex.domain.syncjob.service.SyncJobService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sync-jobs")
public class SyncJobController {

    private final SyncJobService syncJobService;


    @PostMapping("/index-infos")
    public ResponseEntity<List<SyncJobDto>> getIndexInfo(HttpServletRequest request){
        String worker = ipIntercept(request);
        List<SyncJobDto> indexInfos = syncJobService.syncIndexInfos(worker);
        return ResponseEntity.ok(indexInfos);
    }

    @PostMapping("/index-data")
    public ResponseEntity<List<SyncJobDto>> getIndexData(
            @RequestBody IndexDataSyncRequest indexDataSyncRequest,
            HttpServletRequest request
    ){
        String worker = ipIntercept(request);
        return null;
    }

    @GetMapping
    public ResponseEntity<CursorPageResponseSyncJobDto> cursorPageResponse(
            @RequestBody CursorPageRequestSyncJobDto cursorPageRequestSyncJobDto
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
