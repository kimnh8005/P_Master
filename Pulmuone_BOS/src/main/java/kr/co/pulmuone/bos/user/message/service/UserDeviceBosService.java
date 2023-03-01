package kr.co.pulmuone.bos.user.message.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandRequestDto;

public interface UserDeviceBosService {

    ApiResult<?> getDeviceList(GetDeviceListRequestDto dto) throws Exception;

    ApiResult<?> getBuyerDeviceList(GetBuyerDeviceListRequestDto dto) throws Exception;

    ApiResult<?> getDeviceEvnetImage(GetDeviceListRequestDto dto) throws Exception;

    ApiResult<?> setDeviceEventImage(GetDeviceRequestDto dto) throws Exception;

}
