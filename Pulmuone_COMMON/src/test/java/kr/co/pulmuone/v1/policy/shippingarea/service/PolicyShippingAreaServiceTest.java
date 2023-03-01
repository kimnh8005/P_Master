package kr.co.pulmuone.v1.policy.shippingarea.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadFailRequestDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadListRequestDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaListRequestDto;
import kr.co.pulmuone.v1.policy.shippingarea.service.PolicyShippingAreaService;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

@Slf4j
public class PolicyShippingAreaServiceTest  extends CommonServiceTestBaseForJunit5 {

    @Autowired
    PolicyShippingAreaService policyShippingAreaService;

    @BeforeEach
    void beforeEach() { preLogin(); }

    @Test
    void 도서산간_배송불가_엑셀업로드() throws Exception {

        String fileName = "/Users/kjlee/Downloads/shippingAreaExcelUploadSample.xlsx";
        MockMultipartFile file = new MockMultipartFile("user-file", fileName, "text/plain", "test data".getBytes());

        ShippingAreaListRequestDto shippingAreaListRequestDto = new ShippingAreaListRequestDto();
        shippingAreaListRequestDto.setKeyword("샘플업로드");
        shippingAreaListRequestDto.setUndeliverableTp("UNDELIVERABLE_TP.ISLAND");

        policyShippingAreaService.addShippingAreaExcelUpload(file, shippingAreaListRequestDto);

        Assertions.assertTrue(true);
    }

    @Test
    void 도서산간_배송불가_일괄삭제() throws Exception {
        ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto = new ShippingAreaExcelUploadFailRequestDto();
        shippingAreaExcelUploadFailRequestDto.setPsShippingAreaExcelInfoId(9999L);

        policyShippingAreaService.delShippingAreaInfo(shippingAreaExcelUploadFailRequestDto);

        Assertions.assertTrue(true);
    }
}
