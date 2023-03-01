package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadFailRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadListRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface ClaimInfoExcelUploadBiz {

    ApiResult<?> addClaimExcelUpload(MultipartFile file) throws Exception;

    ApiResult<?> getClaimExcelInfoList(ClaimInfoExcelUploadListRequestDto dto);

    ExcelDownloadDto getClaimFailExcelDownload(ClaimInfoExcelUploadFailRequestDto dto);
}
