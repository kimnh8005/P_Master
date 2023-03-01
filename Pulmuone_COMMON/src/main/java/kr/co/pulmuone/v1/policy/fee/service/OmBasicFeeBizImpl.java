package kr.co.pulmuone.v1.policy.fee.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersListRequestDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersListResponseDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersRequestDto;
import kr.co.pulmuone.v1.policy.fee.dto.*;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmBasicFeeHistVo;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmBasicFeeVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 기본 수수료 관리 BizImpl
 * </PRE>
 *
 */
@Slf4j
@Service
public class OmBasicFeeBizImpl implements OmBasicFeeBiz{

	@Autowired
	OmBasicFeeService omBasicFeeService;

	/**
	 * 기본 수수료 목록 조회
	 *
	 * @param omBasicFeeListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOmBasicFeeList(OmBasicFeeListRequestDto omBasicFeeListRequestDto) throws Exception {

		OmBasicFeeListResponseDto omBasicFeeListResponseDto = new OmBasicFeeListResponseDto();

		if(StringUtils.isNotEmpty(omBasicFeeListRequestDto.getSearchCalcType())){
			List<String> calcTypeList = StringUtil.getArrayList(omBasicFeeListRequestDto.getSearchCalcType());
			omBasicFeeListRequestDto.setCalcTypeList(calcTypeList);
		}

        Page<OmBasicFeeListDto> resultVoList = omBasicFeeService.getOmBasicFeeList(omBasicFeeListRequestDto);

		omBasicFeeListResponseDto.setRows(resultVoList.getResult());
		omBasicFeeListResponseDto.setTotal(resultVoList.getTotal());

        return ApiResult.success(omBasicFeeListResponseDto);
    }


	/**
	 * 기본 수수료 상세 조회
	 *
	 * @param omBasicFeeRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOmBasicFee(OmBasicFeeRequestDto omBasicFeeRequestDto) throws Exception {

		OmBasicFeeResponseDto omBasicFeeResponseDto = new OmBasicFeeResponseDto();

		OmBasicFeeDto omBasicFeeVoResult = omBasicFeeService.getOmBasicFee(omBasicFeeRequestDto);

		omBasicFeeResponseDto.setRow(omBasicFeeVoResult);

    	return ApiResult.success(omBasicFeeResponseDto);
	}

	/**
	 * 기본 수수료 이력 목록 조회
	 *
	 * @param omBasicFeeHistListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOmBasicHistFeeList(OmBasicFeeHistListRequestDto omBasicFeeHistListRequestDto) throws Exception {

		OmBasicFeeHistListResponseDto omBasicFeeHistListResponseDto = new OmBasicFeeHistListResponseDto();

		Page<OmBasicFeeHistListDto> resultVoList = omBasicFeeService.getOmBasicHistFeeList(omBasicFeeHistListRequestDto);

		omBasicFeeHistListResponseDto.setRows(resultVoList.getResult());
		omBasicFeeHistListResponseDto.setTotal(resultVoList.getTotal());

		return ApiResult.success(omBasicFeeHistListResponseDto);
	}

	/**
	 * 기본 수수료 등록
	 *
	 * @param omBasicFeeVo
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> addOmBasicFee(OmBasicFeeVo omBasicFeeVo) throws Exception {

		// 시작일자 중복 체크
		if(duplicateOmBasicFee(omBasicFeeVo)) {
			return ApiResult.result(PolicyEnums.OmBasicFeeMessage.DUPLICATE_START_DATE);
		}

		omBasicFeeService.addOmBasicFee(omBasicFeeVo);

		addOmBasicFeeHist(omBasicFeeVo);

		return ApiResult.success();
	}

	/**
	 * 기본 수수료 수정
	 *
	 * @param omBasicFeeVo
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOmBasicFee(OmBasicFeeVo omBasicFeeVo) throws Exception{

		// 시작일자 중복 체크
		if(duplicateOmBasicFee(omBasicFeeVo)) {
			return ApiResult.result(PolicyEnums.OmBasicFeeMessage.DUPLICATE_START_DATE);
		}

		omBasicFeeService.putOmBasicFee(omBasicFeeVo);

		addOmBasicFeeHist(omBasicFeeVo);

		return ApiResult.success();
	}

	@Override
	public ApiResult<?> putOmBasicFeeSellersGroup(OmBasicFeeVo omBasicFeeVo) throws Exception {
		omBasicFeeService.putOmBasicFeeSellersGroup(omBasicFeeVo);
		return ApiResult.success();
	}

	/**
	 * 기본 수수료 이력 등록
	 *
	 * @param omBasicFeeVo
	 * @return void
	 * @throws Exception
	 */
	public void addOmBasicFeeHist(OmBasicFeeVo omBasicFeeVo) throws Exception {

		OmBasicFeeHistVo omBasicFeeHistVo = OmBasicFeeHistVo.builder()
														    .sellersGroupCd(omBasicFeeVo.getSellersGroupCd())
														    .omSellersId(omBasicFeeVo.getOmSellersId())
														    .urSupplierId(omBasicFeeVo.getUrSupplierId())
														    .calcType(omBasicFeeVo.getCalcType())
														    .fee(omBasicFeeVo.getFee())
														    .startDt(omBasicFeeVo.getStartDt())
														    .createId(Long.parseLong(omBasicFeeVo.getUserVo().getUserId()))
														    .supplierCd(omBasicFeeVo.getSupplierCd())
														    .supplierNm(omBasicFeeVo.getSupplierNm())
														    .build();

		omBasicFeeService.addOmBasicFeeHist(omBasicFeeHistVo);

	}

    public boolean duplicateOmBasicFee(OmBasicFeeVo omBasicFeeVo) throws Exception{
    	OmBasicFeeListRequestDto omBasicFeeListRequestDto = new OmBasicFeeListRequestDto();
    	omBasicFeeListRequestDto.setSearchSellersGroup(omBasicFeeVo.getSellersGroupCd());
    	omBasicFeeListRequestDto.setSearchOmSellersId(omBasicFeeVo.getOmSellersId());
    	omBasicFeeListRequestDto.setSearchSupplierId(omBasicFeeVo.getUrSupplierId());
    	List<OmBasicFeeListDto> resultVoList = omBasicFeeService.getOmBasicFeeList(omBasicFeeListRequestDto);

    	boolean result = false;
    	if(omBasicFeeVo.getOmBasicFeeId() > 0) {
    		result = resultVoList.stream()
    				.anyMatch(a -> a.getStartDt().replace("-", "").equals(omBasicFeeVo.getStartDt()) && a.getOmBasicFeeId() != omBasicFeeVo.getOmBasicFeeId());
    	}else {
    		result = resultVoList.stream()
    				.anyMatch(a -> a.getStartDt().replace("-", "").equals(omBasicFeeVo.getStartDt()));
    	}
    	return result;
    }

}
