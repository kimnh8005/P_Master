package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadFailRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadListRequestDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelFailRequestDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelListRequestDto;
import kr.co.pulmuone.v1.order.ifday.service.IfDayExcelBiz;
import kr.co.pulmuone.v1.order.ifday.service.IfDayExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ClaimInfoExcelUploadBizImpl implements ClaimInfoExcelUploadBiz {

    @Autowired
    private ClaimInfoExcelUploadService claimInfoExcelUploadService;

    @Override
    public ApiResult<?> addClaimExcelUpload(MultipartFile file) throws Exception {
        return claimInfoExcelUploadService.addClaimExcelUpload(file);
    }

    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public ApiResult<?> getClaimExcelInfoList(ClaimInfoExcelUploadListRequestDto dto) {
        return ApiResult.success(claimInfoExcelUploadService.getClaimExcelInfoList(dto));
    }

    @Override
    public ExcelDownloadDto getClaimFailExcelDownload(ClaimInfoExcelUploadFailRequestDto dto) {
        // 클레임 엑셀 업로드 원본 파일 데이터 다운로드
        if("O".equals(dto.getFailType())) {
            return claimInfoExcelUploadService.getClaimUploadExcelDownload(dto);
        }
        // 클레임 엑셀 업로드 업로드 실패내역 다운로드
        else if("U".equals(dto.getFailType())) {
            return claimInfoExcelUploadService.getClaimUploadFailExcelDownload(dto);
        }
        // 클레임 엑셀 업로드 업데이트 실패내역 다운로드
        else {
            return claimInfoExcelUploadService.getClaimUpdateFailExcelDownload(dto);
        }
    }
}
