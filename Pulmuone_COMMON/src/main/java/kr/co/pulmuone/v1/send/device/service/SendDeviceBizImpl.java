package kr.co.pulmuone.v1.send.device.service;

import java.util.List;

import kr.co.pulmuone.v1.send.device.dto.vo.GetBuyerDeviceListParamVo;
import kr.co.pulmuone.v1.send.push.dto.PushSendListRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.enums.BrandEnums;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListResponseDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceListResponseDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.AddBrandAndLogoMappingParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandParamVo;

@Service
public class SendDeviceBizImpl implements SendDeviceBiz {

    @Autowired
    SendDeviceService sendDeviceService;

    @Override
    public GetDeviceListResponseDto getDeviceList(GetDeviceListRequestDto getDeviceListRequestDto) throws Exception {
        return sendDeviceService.getDeviceList(getDeviceListRequestDto);
    }

    @Override
    public GetBuyerDeviceListResponseDto getBuyerDeviceList(GetBuyerDeviceListRequestDto getBuyerDeviceListRequestDto) throws Exception {
        return sendDeviceService.getBuyerDeviceList(getBuyerDeviceListRequestDto);
    }

    @Override

    public GetDeviceResponseDto getDeviceEvnetImage(GetDeviceListRequestDto getDeviceRequestDto) throws Exception {
        return sendDeviceService.getDeviceEvnetImage(getDeviceRequestDto);
    }

    @Override
    public ApiResult<?> setDeviceEventImage(GetDeviceRequestDto getDeviceRequestDto) throws Exception{

    	getDeviceRequestDto.setAddFileList(BindUtil.convertJsonArrayToDtoList(getDeviceRequestDto.getAddFile(), FileVo.class));


    	getDeviceRequestDto.setEnvVal(getDeviceRequestDto.getUseYn());
    	if(getDeviceRequestDto.getDeviceType().equals(SystemEnums.DeviceType.IOS.getCode())) {
    		getDeviceRequestDto.setEnvKey(SystemEnums.DeviceEventImageType.IOS_YN.getCode());
    		sendDeviceService.setDeviceEventImage(getDeviceRequestDto);
    	}else {
    		getDeviceRequestDto.setEnvKey(SystemEnums.DeviceEventImageType.ANDROID_YN.getCode());
    		sendDeviceService.setDeviceEventImage(getDeviceRequestDto);
    	}

    	if(getDeviceRequestDto.getUseYn().equals("Y")) {
	    	List<FileVo> fileList = getDeviceRequestDto.getAddFileList();
	    	if(fileList.size()>0) {
	    		for(int i=0 ; i< fileList.size();i++) {
	    			FileVo fileVo = fileList.get(i);
	    			String imageUrl = "";

	    			imageUrl= getDeviceRequestDto.getRootPath() + fileVo.getServerSubPath() + fileVo.getPhysicalFileName();

	    			getDeviceRequestDto.setEnvVal(imageUrl);
	    			if(getDeviceRequestDto.getDeviceType().equals(SystemEnums.DeviceType.IOS.getCode())) {
	    	    		getDeviceRequestDto.setEnvKey(SystemEnums.DeviceEventImageType.IOS_URL.getCode());
	    	    	}else {
	    	    		getDeviceRequestDto.setEnvKey(SystemEnums.DeviceEventImageType.ANDROID_URL.getCode());
	    	    	}
	    			sendDeviceService.setDeviceEventImage(getDeviceRequestDto);
	    		}
	    	}
    	}

    	return ApiResult.success();
    }

    @Override
    public List<PushSendListRequestDto> getBuyerDeviceSearchAllList(GetBuyerDeviceListRequestDto dto) throws Exception {
    	 boolean isPage = false;
    	 GetBuyerDeviceListParamVo vo = sendDeviceService.setBuyerDeviceListParamVo(dto, isPage);

        return sendDeviceService.getBuyerDeviceSearchAllList(vo);
    }


}
