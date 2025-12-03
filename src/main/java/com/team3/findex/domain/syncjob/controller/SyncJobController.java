package com.team3.findex.domain.syncjob.controller;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.dto.SyncJobDto;
import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.service.SyncJobService;
import com.team3.findex.repository.IndexInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sync-job")
public class SyncJobController {

    private final SyncJobService syncJobService;


    @PostMapping("/index-infos")
    public ResponseEntity<?> getIndexInfo(HttpServletRequest request){
        String worker = ipIntercept(request);
        List<SyncJobDto> indexInfos = syncJobService.createSyncJobs(JobType.INDEX_INFO, worker);
        return ResponseEntity.ok(indexInfos);
    }

    @PostMapping("/index-data")
    public ResponseEntity<?> getIndexData(){
        return null;
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
