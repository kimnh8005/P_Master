package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsAllModifyRequestDto;

public interface GoodsAllModifyBiz {

    // 상품 정보 일괄수정 대상 조회
	ApiResult<?> getGoodsAllModifyList(GoodsAllModifyRequestDto paramDto);

    // 상품 정보 일괄수정 수정
	ApiResult<?> putGoodsAllModify(GoodsAllModifyRequestDto paramDto);

    // 상품 정보 일괄수정 추가상품 조회
	ApiResult<?> getGoodsAdditionList(GoodsAllModifyRequestDto paramDto);

    // 상품 정보 일괄수 상품 공지 조회
	ApiResult<?> getGoodsNoticeInfoList(GoodsAllModifyRequestDto paramDto);

    // 상품 정보 일괄수정 엑셀 다운로드
	ExcelDownloadDto createGoodsAllModifyExcel(GoodsAllModifyRequestDto paramDto);




}
