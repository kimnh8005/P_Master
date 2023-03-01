package kr.co.pulmuone.bos.user.shipping.service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserShippingBosServiceImpl implements UserShippingBosService {

	@Autowired
	private UserBuyerBiz userBuyerBiz;

	/**
	 * 배송지 단일조회
	 * @param	GetShippingAddressRequestDto
	 * @return	GetShippingAddressResponseDto
	 * @throws Exception
	 */
	@Override
	@UserMaskingRun(system = "BOS")
	public GetShippingAddressResponseDto getShippingAddress(CommonGetShippingAddressRequestDto dto) throws Exception {
		GetShippingAddressResponseDto result = new GetShippingAddressResponseDto();

		result.setRows(userBuyerBiz.getShippingAddress(dto));

		return result;
	}

	/**
	 * 배송지 수정
	 * @param	CommonSaveShippingAddressRequestDto
	 * @throws Exception
	 */
	@Override
	public int putShippingAddress(CommonSaveShippingAddressRequestDto dto)  throws Exception {
		return userBuyerBiz.putShippingAddress(dto);
	}

	/**
	 * 배송지 추가
	 * @param  CommonSaveShippingAddressRequestDto
	 * @throws Exception
	 */
	@Override
	public int addShippingAddress(CommonSaveShippingAddressRequestDto dto)  throws Exception {
		return userBuyerBiz.addShippingAddress(dto);
	}

	/**
	 * 배송지 List 조회
	 * @param	CommonGetShippingAddressListRequestDto
	 * @return	CommonGetShippingAddressListResponseDto
	 * @throws Exception
	 */
	@Override
	@UserMaskingRun(system = "BOS")
	public CommonGetShippingAddressListResponseDto getShippingAddressList(CommonGetShippingAddressListRequestDto dto) throws Exception {
		return userBuyerBiz.getShippingAddressList(dto);
	}

	/**
	 * @Desc 배송지 목록에서 주문배송지로 설정
	 * @param shippingAddressId
	 */
	@Override
	public ApiResult<?> putShippingAddressIntoOrderShippingZone(CommonSaveShippingAddressRequestDto dto) throws Exception{
		return userBuyerBiz.putShippingAddressIntoOrderShippingZone(dto);
	}

}
