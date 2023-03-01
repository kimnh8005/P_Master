package kr.co.pulmuone.v1.policy.fee.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.policy.fee.dto.*;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmBasicFeeVo;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmLogisticsFeeHistVo;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmLogisticsFeeVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 물류 수수료 관리 BizImpl
 * </PRE>
 *
 */
@Slf4j
@Service
public class OmLogisticsFeeBizImpl implements OmLogisticsFeeBiz{

	@Autowired
	OmLogisticsFeeService omLogisticsFeeService;

	/**
	 * 물류 수수료 목록 조회
	 *
	 * @param omLogisticsFeeListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOmLogisticsFeeList(OmLogisticsFeeListRequestDto omLogisticsFeeListRequestDto) throws Exception {

		OmLogisticsFeeListResponseDto omLogisticsFeeListResponseDto = new OmLogisticsFeeListResponseDto();

		if(StringUtils.isNotEmpty(omLogisticsFeeListRequestDto.getSearchCalcType())){
			List<String> calcTypeList = StringUtil.getArrayList(omLogisticsFeeListRequestDto.getSearchCalcType());
			omLogisticsFeeListRequestDto.setCalcTypeList(calcTypeList);
		}
        Page<OmLogisticsFeeListDto> resultVoList = omLogisticsFeeService.getOmLogisticsFeeList(omLogisticsFeeListRequestDto);

		omLogisticsFeeListResponseDto.setRows(resultVoList.getResult());
		omLogisticsFeeListResponseDto.setTotal(resultVoList.getTotal());

        return ApiResult.success(omLogisticsFeeListResponseDto);
    }

	/**
	 * 물류 수수료 상세 조회
	 *
	 * @param omLogisticsFeeRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOmLogisticsFee(OmLogisticsFeeRequestDto omLogisticsFeeRequestDto) throws Exception {

		OmLogisticsFeeResponseDto omLogisticsFeeResponseDto = new OmLogisticsFeeResponseDto();

		OmLogisticsFeeDto omLogisticsFeeVoResult = omLogisticsFeeService.getOmLogisticsFee(omLogisticsFeeRequestDto);

		omLogisticsFeeResponseDto.setRow(omLogisticsFeeVoResult);

    	return ApiResult.success(omLogisticsFeeResponseDto);
	}

	/**
	 * 물류 수수료 이력 목록 조회
	 *
	 * @param omLogisticsFeeHistListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOmLogisticsFeeHistList(OmLogisticsFeeHistListRequestDto omLogisticsFeeHistListRequestDto) throws Exception {

		OmLogisticsFeeHistListResponseDto omLogisticsFeeHistListResponseDto = new OmLogisticsFeeHistListResponseDto();

		Page<OmLogisticsFeeHistListDto> resultVoList = omLogisticsFeeService.getOmLogisticsFeeHistList(omLogisticsFeeHistListRequestDto);

		omLogisticsFeeHistListResponseDto.setRows(resultVoList.getResult());
		omLogisticsFeeHistListResponseDto.setTotal(resultVoList.getTotal());

		return ApiResult.success(omLogisticsFeeHistListResponseDto);
	}

	/**
	 * 물류 수수료 등록
	 *
	 * @param omLogisticsFeeVo
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> addOmLogisticsFee(OmLogisticsFeeVo omLogisticsFeeVo) throws Exception {

		// 시작일자 중복 체크
		if(duplicateOmLogisticsFee(omLogisticsFeeVo)) {
			return ApiResult.result(PolicyEnums.OmBasicFeeMessage.DUPLICATE_START_DATE);
		}

		omLogisticsFeeService.addOmLogisticsFee(omLogisticsFeeVo);

		addOmLogisticsFeeHist(omLogisticsFeeVo);

		return ApiResult.success();
	}

	/**
	 * 물류 수수료 수정
	 *
	 * @param omLogisticsFeeVo
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOmLogisticsFee(OmLogisticsFeeVo omLogisticsFeeVo) throws Exception{

		// 시작일자 중복 체크
		if(duplicateOmLogisticsFee(omLogisticsFeeVo)) {
			return ApiResult.result(PolicyEnums.OmBasicFeeMessage.DUPLICATE_START_DATE);
		}

		omLogisticsFeeService.putOmLogisticsFee(omLogisticsFeeVo);

		addOmLogisticsFeeHist(omLogisticsFeeVo);

		return ApiResult.success();
	}

	/**
	 * 물류 수수료 이력 등록
	 *
	 * @param omLogisticsFeeVo
	 * @return void
	 * @throws Exception
	 */
	public void addOmLogisticsFeeHist(OmLogisticsFeeVo omLogisticsFeeVo) throws Exception {

		OmLogisticsFeeHistVo omLogisticsFeeHistVo = OmLogisticsFeeHistVo.builder()
														    			.urWarehouseId(omLogisticsFeeVo.getUrWarehouseId())
														    			.urSupplierId(omLogisticsFeeVo.getUrSupplierId())
																		.calcType(omLogisticsFeeVo.getCalcType())
																		.fee(omLogisticsFeeVo.getFee())
																		.startDt(omLogisticsFeeVo.getStartDt())
																		.createId(Long.parseLong(omLogisticsFeeVo.getUserVo().getUserId()))
																		.supplierNm(omLogisticsFeeVo.getSupplierNm())
																		.build();

		omLogisticsFeeService.addOmLogisticsFeeHist(omLogisticsFeeHistVo);

	}

    public boolean duplicateOmLogisticsFee(OmLogisticsFeeVo omLogisticsFeeVo) throws Exception{
    	OmLogisticsFeeListRequestDto omLogisticsFeeListRequestDto = new OmLogisticsFeeListRequestDto();
    	omLogisticsFeeListRequestDto.setSearchUrWarehouseId(omLogisticsFeeVo.getUrWarehouseId());
    	omLogisticsFeeListRequestDto.setSearchUrSupplierId(omLogisticsFeeVo.getUrSupplierId());
    	Page<OmLogisticsFeeListDto> resultVoList = omLogisticsFeeService.getOmLogisticsFeeList(omLogisticsFeeListRequestDto);

    	return resultVoList.stream()
				.anyMatch(a -> a.getStartDt().replace("-", "").equals(omLogisticsFeeVo.getStartDt()));
    }

}
