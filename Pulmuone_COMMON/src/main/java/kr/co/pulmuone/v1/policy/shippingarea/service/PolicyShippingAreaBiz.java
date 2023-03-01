package kr.co.pulmuone.v1.policy.shippingarea.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadFailRequestDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadListRequestDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaListRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface PolicyShippingAreaBiz {

    ApiResult<?> addShippingAreaExcelUpload(MultipartFile file, ShippingAreaListRequestDto shippingAreaListRequestDto) throws Exception;

    ApiResult<?> getUploadShippingAreaInfo(ShippingAreaExcelUploadDto shippingAreaExcelUploadDto);

    ApiResult<?> getPolicyShippingAreaList(ShippingAreaExcelUploadListRequestDto shippingAreaExcelUploadListRequestDto);

    // 배송불가 우편번호 조회
    ApiResult<?> getPolicyShippingAreaInfoList(ShippingAreaExcelUploadListRequestDto shippingAreaExcelUploadListRequestDto);

    // 엑셀 적용 내역 다운로드
    ExcelDownloadDto getShippingAreaInfoExcelDownload(ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto);

    // 엑셀 업로드 실패내역 다운로드
    ExcelDownloadDto getShippingAreaFailExcelDownload(ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto);

    // 엑셀 적용 내역 일괄 삭제
    ApiResult<?> delShippingAreaInfo(ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto) throws Exception;

    // 등록된 우편번호 조회
    int getShippingAreaZipCd(String zipCd, String undeliverableTp);
}
