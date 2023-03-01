package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.ExcelDownloadUtil;
import kr.co.pulmuone.v1.order.order.dto.OrderListRequestDto;
import kr.co.pulmuone.v1.policy.excel.service.PolicyExcelTmpltBiz;
import kr.co.pulmuone.v1.system.log.service.SystemLogBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
public class OrderListAsyncBizImpl implements OrderListAsyncBiz {

    @Autowired
    private OrderListService orderListService;

    @Autowired
    private PolicyExcelTmpltBiz policyExcelTmpltBiz;

    @Autowired
    private ExcelDownloadUtil excelDownloadUtil;

    @Autowired
    private SystemLogBiz systemLogBiz;

    @Override
    @Async
    public void runOrderDetailExcelMake(OrderListRequestDto orderListRequestDto, Long stExcelDownloadAsyncId) {
        try {
            // DB 조회
            List<LinkedHashMap<String, Object>> orderDetailList = orderListService.getOrderDetailExcelListMap(orderListRequestDto);

            //엑셀다운로드 양식을 위한 공통
            ExcelWorkSheetDto firstWorkSheetDto = policyExcelTmpltBiz.getCommonDownloadExcelTmplt(orderListRequestDto.getPsExcelTemplateId());

            // excelDownloadDto 생성 후 workSheetDto 추가
            ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder()
                    .excelFileName(orderListRequestDto.getPsExcelNm())
                    .build();
            excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

            // 엑셀파일 생성
            excelDownloadUtil.buildExcelDocument(excelDownloadDto, orderDetailList);

            // 비동기 정보 DB 수정 - 완료
            systemLogBiz.putExcelDownloadAsyncSetUse(stExcelDownloadAsyncId);

        } catch (Exception e) {
            // 비동기 정보 DB 수정 - Error
            systemLogBiz.putExcelDownloadAsyncSetError(stExcelDownloadAsyncId);
            log.error(e.getMessage(), e);
        }
    }

}
