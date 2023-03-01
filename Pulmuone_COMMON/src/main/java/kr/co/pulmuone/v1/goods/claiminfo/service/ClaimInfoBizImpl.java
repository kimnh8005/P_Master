package kr.co.pulmuone.v1.goods.claiminfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.claiminfo.dto.ClaimInfoListResponseDto;
import kr.co.pulmuone.v1.goods.claiminfo.dto.ClaimInfoRequestDto;
import kr.co.pulmuone.v1.goods.claiminfo.dto.ClaimInfoResponseDto;
import kr.co.pulmuone.v1.goods.claiminfo.dto.vo.ClaimInfoVo;

@Service
public class ClaimInfoBizImpl implements ClaimInfoBiz {

	@Autowired
	ClaimInfoService claimInfoService;

	/**
	* @Desc  배송/반품/취소안내 목록 조회
	* @param ClaimInfoRequestDto
	* @return ApiResult
	* @throws Exception
	*/
	@Override
	public ApiResult<?> getClaimInfoList(ClaimInfoRequestDto claimInfoRequestDto) {

		ClaimInfoListResponseDto resultList = new ClaimInfoListResponseDto();

		List<ClaimInfoVo> rows = claimInfoService.getClaimInfoList(claimInfoRequestDto);

		resultList.setRows(rows);

		return ApiResult.success(resultList);
	}

	/**
	* @Desc  배송/반품/취소안내 상세 조회
	* @param ilClaimInfoId
	* @return ApiResult
	* @throws Exception
	*/
	@Override
	public ApiResult<?> getClaimInfo(String ilClaimInfoId){

		ClaimInfoResponseDto result = new ClaimInfoResponseDto();

		ClaimInfoVo claiminfoVo = claimInfoService.getClaimInfo(ilClaimInfoId);

		result.setRows(claiminfoVo);

		return ApiResult.success(result);
	}

	/**
	* @Desc  배송/반품/취소안내 수정
	* @param ClaimInfoRequestDto
	* @return ApiResult
	* @throws Exception
	*/
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putClaimInfo(ClaimInfoRequestDto claimInfoRequestDto) throws Exception {

//      if(ilClaimDescriptionInfomationMapper.duplicateIlClaimDescriptionInfomationCount(ilClaimDescriptionInfomationRequestDto) > 0) {
//    	result.setRETURN_CODE(BaseEnums.CommBase.DUPLICATE_DATA.getCode());
//    	result.setRETURN_MSG("품목유형/상품유형/배송유형 조합은 중복될 수 없습니다.");
//
//    	return result;
//    }
//
//    if (!validateCodeType(ilClaimDescriptionInfomationRequestDto)) {
//    	result.setRETURN_CODE(BaseEnums.CommBase.VALID_ERROR.getCode());
//    	result.setRETURN_MSG(BaseEnums.CommBase.VALID_ERROR.getMessage());
//
//		return result;
//    }

        claimInfoService.putClaimInfo(claimInfoRequestDto);

		return ApiResult.success();
	}

	/**
	 * 배송/반품/취소안내 추가
	 * @param ilClaimDescriptionInfomationRequestDto
	 * @return IlClaimDescriptionInfomationResponseDto
	 * @throws Exception
	 */
//	@Override
//	public IlClaimDescriptionInfomationResponseDto addIlClaimDescriptionInfomation(
//			IlClaimDescriptionInfomationRequestDto ilClaimDescriptionInfomationRequestDto) throws Exception {
//		IlClaimDescriptionInfomationResponseDto result = new IlClaimDescriptionInfomationResponseDto();
//
//        if(ilClaimDescriptionInfomationMapper.duplicateIlClaimDescriptionInfomationCount(ilClaimDescriptionInfomationRequestDto) > 0) {
//        	result.setRETURN_CODE(BaseEnums.CommBase.DUPLICATE_DATA.getCode());
//			result.setRETURN_MSG("품목유형/상품유형/배송유형 조합은 중복될 수 없습니다.");
//
//			return result;
//        }
//
//        if (!validateCodeType(ilClaimDescriptionInfomationRequestDto)) {
//        	result.setRETURN_CODE(BaseEnums.CommBase.VALID_ERROR.getCode());
//        	result.setRETURN_MSG(BaseEnums.CommBase.VALID_ERROR.getMessage());
//
//			return result;
//        }
//
//       	ilClaimDescriptionInfomationMapper.addIlClaimDescriptionInfomation(ilClaimDescriptionInfomationRequestDto);
//
//		return result;
//	}

	/**
	 * 배송/반품/취소안내 삭제
	 * @param ilClaimInfoId 배송/반품/취소안내  ID(PK)
	 * @return IlClaimDescriptionInfomationResponseDto
	 * @throws Exception
	 */
//	@Override
//	public IlClaimDescriptionInfomationResponseDto delIlClaimDescriptionInfomation(String ilClaimInfoId)
//			throws Exception {
//		IlClaimDescriptionInfomationResponseDto result = new IlClaimDescriptionInfomationResponseDto();
//
//		ilClaimDescriptionInfomationMapper.delIlClaimDescriptionInfomation(ilClaimInfoId);
//
//		return result;
//	}


	/**
	 * 품목유형/상품유형/배송유형 계층 데이타 무결성 검사
	 * @param ilClaimDescriptionInfomationRequestDto
	 * @return 무결성 통과여부
	 * @throws Exception
	 */
//	private boolean validateCodeType(IlClaimDescriptionInfomationRequestDto ilClaimDescriptionInfomationRequestDto) {
//        boolean resultFlag = false;
//
//        String itemType = StringUtils.isBlank(ilClaimDescriptionInfomationRequestDto.getItemType()) ? "BASIC" : ilClaimDescriptionInfomationRequestDto.getItemType();
//		String goodsType = StringUtils.isBlank(ilClaimDescriptionInfomationRequestDto.getGoodsType()) ? "BASIC" : ilClaimDescriptionInfomationRequestDto.getGoodsType();
//        String deliveryType = StringUtils.isBlank(ilClaimDescriptionInfomationRequestDto.getDeliveryType()) ? "BASIC" : ilClaimDescriptionInfomationRequestDto.getDeliveryType();
//
//        ItemSubTypesEnum itemSubTypesEnum = ItemSubTypesEnum.valueOf(itemType);
//        GoodsSubTypesEnum goodsSubTypesEnum = GoodsSubTypesEnum.valueOf(goodsType);
//
//
//        for (String itemSubTypeCode : itemSubTypesEnum.getSubCodes()) {
//			if (itemSubTypeCode.equals(goodsType)) {
//				resultFlag = true;
//				break;
//			}
//		}
//
//        if (resultFlag) {
//        	resultFlag = false;
//
//        	for (String goodsSubTypeCode : goodsSubTypesEnum.getSubCodes()) {
//				if (goodsSubTypeCode.equals(deliveryType)) {
//					resultFlag = true;
//					break;
//				}
//			}
//        }
//
//        return resultFlag;
//	}

}
