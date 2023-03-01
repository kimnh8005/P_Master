package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.system.basic.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200529    오영민              최초작성
 * =======================================================================
 * </PRE>
 */

@RequiredArgsConstructor
@Service
public class SystemBasicClassificationBizImpl implements SystemBasicClassificationBiz {
	private final SystemBasicClassificationService service;

	/**
	 * 분류관리
	 */
	@Override
	public GetClassificationListResponseDto getClassificationList(GetClassificationListParamDto dto){
		return service.getClassificationList(dto);
	}


	/**
	 * 분류관리 상세조회
	 */
	@Override
	public GetClassificationResponseDto getClassification(Long stClassificationId) {
		return service.getClassification(stClassificationId);
	}

	/**
	 * 분류관리 삽입
	 */
	@Override
	public ApiResult<?> addClassification(SaveClassificationRequestDto dto)  {
		if(service.checkClassificationDuplicate(dto)) {
			service.addClassification(dto);
		} else {
			return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
		}
		
		return ApiResult.success();
	}

	/**
	 * 분류관리 수정
	 */
	@Override
	public ApiResult<?> putClassification(SaveClassificationRequestDto dto)  {
		if(service.checkClassificationDuplicate(dto)) {
			service.putClassification(dto);
		} else {
			return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
		}
		
		return ApiResult.success();
	}

	/**
	 * 분류관리 삭제
	 */
	@Override
	public ApiResult<?> delClassification(Long id)  {
		if(service.delClassification(id) <= 0) {
			return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
		}
		
		return ApiResult.success();
	}

	/**
	 * 분류코드 리스트 조회
	 */
    @Override
	public GetTypeListResponseDto getTypeList() {
		return service.getTypeList();
	}


}

