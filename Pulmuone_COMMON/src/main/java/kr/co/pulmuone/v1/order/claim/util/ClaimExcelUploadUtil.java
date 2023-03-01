package kr.co.pulmuone.v1.order.claim.util;

import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadInfoVo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ClaimExcelUploadUtil {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ClaimInfoExcelUploadInfoVo setClaimExcelInfo() {
        //Session 정보
        long adminId = 0;
        UserVo userVo = SessionUtil.getBosUserVO();
        if (userVo != null) {
            adminId = Long.valueOf(userVo.getUserId());
        }
        ClaimInfoExcelUploadInfoVo infoVo = new ClaimInfoExcelUploadInfoVo();

        LocalDateTime startTime = LocalDateTime.now();
        infoVo.setUploadStartDateTime(startTime.format(dateTimeFormatter));
        infoVo.setUploadStatusCode(ExcelUploadValidateEnums.UploadStatusCode.UPLOAD_IN_PROGRESS.getCode());
        infoVo.setCreateId(adminId);
        return infoVo;
    }

    public ClaimInfoExcelUploadInfoVo setClaimExcelUpdateInfo(ClaimInfoExcelUploadInfoVo infoVo, int successCnt, int failCnt) {
        infoVo.setSuccessCount(successCnt);
        infoVo.setFailCount(failCnt);
        LocalDateTime endTime = LocalDateTime.now();
        infoVo.setUploadEndDateTime(endTime.format(dateTimeFormatter));
        infoVo.setUploadExecutionTime((int) ChronoUnit.SECONDS.between(LocalDateTime.parse(infoVo.getUploadStartDateTime(), dateTimeFormatter), endTime));
        infoVo.setUploadStatusCode(ExcelUploadValidateEnums.UploadStatusCode.UPLOAD_DONE.getCode());
        return infoVo;
    }



}
