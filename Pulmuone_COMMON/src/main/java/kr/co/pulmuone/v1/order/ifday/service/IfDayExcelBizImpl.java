package kr.co.pulmuone.v1.order.ifday.service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.excel.factory.OrderExcelUploadFactory;
import kr.co.pulmuone.v1.comm.excel.util.OrderExcelSetData;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeResponseDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelFailRequestDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelListRequestDto;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelFailVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelInfoVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelSuccessVo;
import kr.co.pulmuone.v1.order.ifday.util.IfDayChangeUtil;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelFailRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderResponseDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderSellersDto;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelFailVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelSuccessVo;
import kr.co.pulmuone.v1.outmall.order.service.OutmallOrderService;
import kr.co.pulmuone.v1.outmall.order.util.OutmallOrderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IfDayExcelBizImpl implements IfDayExcelBiz {

    @Autowired
    private IfDayExcelService ifDayExcelService;


    @Override
    public ApiResult<?> addIfDayExcelUpload(MultipartFile file) throws Exception {
        return ifDayExcelService.addIfDayExcelUpload(file);
    }

    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public ApiResult<?> getIfDayExcelInfoList(IfDayExcelListRequestDto dto) {
        return ApiResult.success(ifDayExcelService.getIfDayExcelInfoList(dto));
    }


    @Override
    public ExcelDownloadDto getIfDayFailExcelDownload(IfDayExcelFailRequestDto dto) {
        return ifDayExcelService.getIfDayFailExcelDownload(dto);
    }
}
