package kr.co.pulmuone.v1.policy.shippingarea.service;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.*;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class PolicyShippingAreaBizImpl implements PolicyShippingAreaBiz {

    @Autowired
    private PolicyShippingAreaService policyShippingAreaService;

    // 엑셀 업로드
    @Override
    public ApiResult<?> addShippingAreaExcelUpload(MultipartFile file, ShippingAreaListRequestDto shippingAreaListRequestDto) throws Exception {
        return policyShippingAreaService.addShippingAreaExcelUpload(file, shippingAreaListRequestDto);
    }

    @Override
    public ApiResult<?> getUploadShippingAreaInfo(ShippingAreaExcelUploadDto shippingAreaExcelUploadDto) {
        return ApiResult.success(policyShippingAreaService.getUploadShippingAreaInfo(shippingAreaExcelUploadDto));
    }

    // 엑셀 적용 내역 리스트 조회
    @Override
    public ApiResult<?> getPolicyShippingAreaList(ShippingAreaExcelUploadListRequestDto shippingAreaExcelUploadListRequestDto){
        return ApiResult.success(policyShippingAreaService.getPolicyShippingAreaList(shippingAreaExcelUploadListRequestDto));
    }

    // 팝업 > 배송불가 우편번호 조회
    @Override
    public ApiResult<?> getPolicyShippingAreaInfoList(ShippingAreaExcelUploadListRequestDto shippingAreaExcelUploadListRequestDto){
        return ApiResult.success(policyShippingAreaService.getPolicyShippingAreaInfoList(shippingAreaExcelUploadListRequestDto));
    }

    // 엑셀 적용 내역 엑셀 다운로드
    @Override
    public ExcelDownloadDto getShippingAreaInfoExcelDownload(ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto){
        return policyShippingAreaService.getShippingAreaInfoExcelDownload(shippingAreaExcelUploadFailRequestDto);
    }

    // 엑셀 업로드 실패내역 다운로드
    @Override
    public ExcelDownloadDto getShippingAreaFailExcelDownload(ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto){
        return policyShippingAreaService.getShippingAreaFailExcelDownload(shippingAreaExcelUploadFailRequestDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> delShippingAreaInfo(ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto) throws Exception {
        // PS_SHIPPING_AREA 삭제
        policyShippingAreaService.delShippingAreaInfo(shippingAreaExcelUploadFailRequestDto);

        // PS_SHIPPING_AREA_EXCEL_FAIL 삭제
        policyShippingAreaService.delShippingAreaExcelFail(shippingAreaExcelUploadFailRequestDto);

        // PS_SHIPPING_AREA_EXCEL_INFO 삭제
        policyShippingAreaService.delShippingAreaExcelInfo(shippingAreaExcelUploadFailRequestDto);

        return ApiResult.success();
    }

    @Override
    public int getShippingAreaZipCd(String zipCd, String undeliverableTp){
        return policyShippingAreaService.getShippingAreaZipCd(zipCd, undeliverableTp);
    }
}
