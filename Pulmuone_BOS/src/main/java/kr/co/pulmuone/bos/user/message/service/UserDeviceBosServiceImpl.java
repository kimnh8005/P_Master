package kr.co.pulmuone.bos.user.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceRequestDto;
import kr.co.pulmuone.v1.send.device.service.SendDeviceBiz;

@Service
public class UserDeviceBosServiceImpl implements UserDeviceBosService {
    @Autowired
    private SendDeviceBiz sendDeviceBiz;

    @Override
    public ApiResult<?> getDeviceList(GetDeviceListRequestDto dto) throws Exception {
        return ApiResult.ofNullable(sendDeviceBiz.getDeviceList(dto));
    }

    @Override
    @UserMaskingRun(system = "BOS")
    public ApiResult<?> getBuyerDeviceList(GetBuyerDeviceListRequestDto dto) throws Exception {
        return ApiResult.ofNullable(sendDeviceBiz.getBuyerDeviceList(dto));
    }


    @Override
    public ApiResult<?> getDeviceEvnetImage(GetDeviceListRequestDto dto) throws Exception {
        return ApiResult.ofNullable(sendDeviceBiz.getDeviceEvnetImage(dto));
    }

    @Override
    public ApiResult<?> setDeviceEventImage(GetDeviceRequestDto dto) throws Exception {
        return ApiResult.ofNullable(sendDeviceBiz.setDeviceEventImage(dto));
    }
}
