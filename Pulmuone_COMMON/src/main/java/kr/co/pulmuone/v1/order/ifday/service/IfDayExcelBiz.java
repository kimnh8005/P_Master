package kr.co.pulmuone.v1.order.ifday.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelFailRequestDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelListRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelFailRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelListRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface IfDayExcelBiz {

    ApiResult<?> addIfDayExcelUpload(MultipartFile file) throws Exception;

    ApiResult<?> getIfDayExcelInfoList(IfDayExcelListRequestDto dto);

    ExcelDownloadDto getIfDayFailExcelDownload(IfDayExcelFailRequestDto dto);
}
