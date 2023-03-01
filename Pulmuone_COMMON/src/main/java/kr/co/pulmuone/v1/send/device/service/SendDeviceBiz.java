package kr.co.pulmuone.v1.send.device.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListResponseDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceListResponseDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceResponseDto;
import kr.co.pulmuone.v1.send.device.dto.vo.GetBuyerDeviceListParamVo;
import kr.co.pulmuone.v1.send.push.dto.PushSendListRequestDto;

import java.util.List;

public interface SendDeviceBiz {
    GetDeviceListResponseDto getDeviceList(GetDeviceListRequestDto dto) throws Exception;

    GetBuyerDeviceListResponseDto getBuyerDeviceList(GetBuyerDeviceListRequestDto dto) throws Exception;

    GetDeviceResponseDto getDeviceEvnetImage(GetDeviceListRequestDto dto) throws Exception;

    ApiResult<?> setDeviceEventImage(GetDeviceRequestDto dto) throws Exception;

    List<PushSendListRequestDto> getBuyerDeviceSearchAllList(GetBuyerDeviceListRequestDto dto) throws Exception;

}
