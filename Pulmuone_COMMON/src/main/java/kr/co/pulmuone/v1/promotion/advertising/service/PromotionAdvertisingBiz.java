package kr.co.pulmuone.v1.promotion.advertising.service;

import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.promotion.advertising.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PromotionAdvertisingBiz {

    /**
     * 외부광고 코드 관리 목록 조회
     */
    AdvertisingExternalListResponseDto getAdvertisingExternalList(AdvertisingExternalListRequestDto dto) throws Exception;

    /**
     * 외부광고 코드 관리 조회
     */
    AdvertisingExternalResponseDto getAdvertisingExternal(AdvertisingExternalRequestDto dto) throws Exception;

    /**
     * 외부광고 코드 관리 목록 엑셀 다운로드
     */
    ExcelDownloadDto getAdvertisingExternalListExcelDownload(AdvertisingExternalListRequestDto dto) throws Exception;

    /**
     * 외부광고 코드 관리 단건 등록
     */
    ApiResult<?> addAdvertisingExternal(AddAdvertisingExternalRequestDto dto) throws Exception;

    /**
     * 외부광고 코드 관리 단건 수정
     */
    void putAdvertisingExternal(AddAdvertisingExternalRequestDto dto) throws Exception;

    /**
     * 외부광고 코드 관리 엑셀 업로드
     */
    ApiResult<?> addAdvertisingExternalExcelUpload(MultipartFile file) throws Exception;

    /**
     * 외부광고 코드 관리 유형 조회
     */
    GetCodeListResponseDto getAdvertisingType(AdvertisingTypeRequestDto dto) throws Exception;

    /**
     * 외부광고 코드 중복여부 검색
     */
    boolean isExistPmAdExternalCd(String pmAdExternalCd) throws Exception;

}