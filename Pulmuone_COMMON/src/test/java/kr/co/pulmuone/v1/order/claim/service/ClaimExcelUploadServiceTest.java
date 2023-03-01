package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadFailRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

@Slf4j
public class ClaimExcelUploadServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    ClaimInfoExcelUploadService claimInfoExcelUploadService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }


    @Test
    void 주문IF일자엑셀업로드() throws Exception {
        String fileName = "/Users/leemyungsu/Downloads/claimExcelUploadSample.xlsx";
        MockMultipartFile file = new MockMultipartFile("user-file", fileName, "text/plain", "test data".getBytes());
        claimInfoExcelUploadService.addClaimExcelUpload(file);

        Assertions.assertTrue(true);
    }

    @Test
    void 주문IF일자실패사유엑셀다운로드() throws Exception {

        ClaimInfoExcelUploadFailRequestDto claimInfoExcelUploadFailRequestDto = new ClaimInfoExcelUploadFailRequestDto();
        claimInfoExcelUploadFailRequestDto.setIfClaimExcelInfoId(4);

        claimInfoExcelUploadService.getClaimUpdateFailExcelDownload(claimInfoExcelUploadFailRequestDto);
        
        Assertions.assertTrue(true);
    }
}