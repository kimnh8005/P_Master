package kr.co.pulmuone.bos.user.shipping.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.*;

public interface UserShippingBosService {

	GetShippingAddressResponseDto getShippingAddress(CommonGetShippingAddressRequestDto dto) throws Exception;

	int putShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception;

	int addShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception;

	CommonGetShippingAddressListResponseDto getShippingAddressList(CommonGetShippingAddressListRequestDto dto) throws Exception;

	ApiResult<?> putShippingAddressIntoOrderShippingZone(CommonSaveShippingAddressRequestDto dto) throws Exception;
}
