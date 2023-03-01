package kr.co.pulmuone.v1.promotion.serialnumber.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.GetSerialNumberListRequestDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.PutSerialNumberCancelRequestDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.vo.GetSerialNumberListResultVo;

public interface SerialNumberBiz {

	ApiResult<?> getSerialNumberList(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception;

	ApiResult<?> putSerialNumberCancel(PutSerialNumberCancelRequestDto putSerialNumberCancelRequestDto) throws Exception;

    Boolean checkCaptcha(String useCaptcha) throws Exception;

    ApiResult<?> addPromotionByUser(String serialNumber, Long urUserId) throws Exception;

    ExcelDownloadDto serialNumberListExportExcel(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception;

    ExcelDownloadDto ticketNumberExportExcel(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception;

    List<GetSerialNumberListResultVo> serialNumberListExcel(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception;

    Boolean redeemSerialNumber(Long urUserId, String serialNumber);
}
