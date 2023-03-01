package kr.co.pulmuone.v1.order.ifday.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelFailRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Objects;

@Slf4j
public class IfDayExcelServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    IfDayExcelService ifDayExcelService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }


    @Test
    void 주문IF일자엑셀업로드() throws Exception {
        String fileName = "/Users/leemyungsu/Downloads/interfaceDayChangeSample.xlsx";
        MockMultipartFile file = new MockMultipartFile("user-file", fileName, "text/plain", "test data".getBytes());
        ifDayExcelService.addIfDayExcelUpload(file);

        Assertions.assertTrue(true);
    }

    @Test
    void 주문IF일자실패사유엑셀다운로드() throws Exception {

        IfDayExcelFailRequestDto ifDayExcelFailRequestDto = new IfDayExcelFailRequestDto();
        ifDayExcelFailRequestDto.setIfIfDayExcelInfoId(4);

        ifDayExcelService.getIfDayFailExcelDownload(ifDayExcelFailRequestDto);
        
        Assertions.assertTrue(true);
    }
}