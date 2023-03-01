package kr.co.pulmuone.v1.promotion.serialnumber.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.GetSerialNumberListRequestDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.GetSerialNumberListResponseDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.PutSerialNumberCancelRequestDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.vo.GetSerialNumberListResultVo;

@Service
public class SerialNumberBizImpl implements SerialNumberBiz {

    @Autowired
    private SerialNumberService serialNumberService;

    /**
     * 카테고리별 상품 리스트 초기 정보
     *
     * @param    ilCategoryId
     * @throws Exception
     */
    @Override
    @UserMaskingRun(system = "BOS")
    public ApiResult<?> getSerialNumberList(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception {

    	GetSerialNumberListResponseDto result = new GetSerialNumberListResponseDto();

        if (StringUtils.isNotEmpty(getSerialNumberListRequestDto.getInputSearchValue())) {
            getSerialNumberListRequestDto.setSearchValueList(Stream.of(getSerialNumberListRequestDto.getInputSearchValue().split("\n|,"))
                    .map(String::trim)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList()));
        }

        Page<GetSerialNumberListResultVo> serialNumberListResultVoList = serialNumberService.getSerialNumberList(getSerialNumberListRequestDto);

        result.setRows(serialNumberListResultVoList.getResult());
        result.setTotal(serialNumberListResultVoList.getTotal());

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> putSerialNumberCancel(PutSerialNumberCancelRequestDto putSerialNumberCancelListRequestDto) throws Exception {
    	serialNumberService.putSerialNumberCancel(putSerialNumberCancelListRequestDto);
    	return ApiResult.success();
    }

    @Override
    public Boolean checkCaptcha(String useCaptcha) throws Exception {
        return serialNumberService.checkCaptcha(useCaptcha);
    }

    @Override
    public ApiResult<?> addPromotionByUser(String serialNumber, Long urUserId) throws Exception {
        return serialNumberService.addPromotionByUser(serialNumber, urUserId);
    }

    @Override
    public ExcelDownloadDto serialNumberListExportExcel(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception {
        return serialNumberService.serialNumberListExportExcel(getSerialNumberListRequestDto);
    }

    @Override
    public ExcelDownloadDto ticketNumberExportExcel(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception {
        return serialNumberService.ticketNumberExportExcel(getSerialNumberListRequestDto);
    }

    @Override
    @UserMaskingRun(system = "BOS")
    public List<GetSerialNumberListResultVo> serialNumberListExcel(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception {
        return serialNumberService.serialNumberListExcel(getSerialNumberListRequestDto);
    }

    @Override
    public Boolean redeemSerialNumber(Long urUserId, String serialNumber) {
        int count = serialNumberService.redeemSerialNumber(urUserId, serialNumber);
        if (count <= 0) return false;

        return true;
    }
}
