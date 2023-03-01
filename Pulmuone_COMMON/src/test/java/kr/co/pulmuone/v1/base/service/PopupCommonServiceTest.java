package kr.co.pulmuone.v1.base.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.dto.ExcelDownLogRequestDto;
import kr.co.pulmuone.v1.base.dto.GetClientPopupRequestDto;
import kr.co.pulmuone.v1.base.dto.GetGrantAuthEmployeePopupRequestDto;
import kr.co.pulmuone.v1.base.dto.GoodsSearchRequestDto;
import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponListResultVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PopupCommonServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    PopupCommonService popupCommonService;

    @BeforeEach
    void setUp() {
        preLogin();
    }

    @Test
    void 거래처_리스트_조회() {
        GetClientPopupRequestDto dto = new GetClientPopupRequestDto();
        dto.setClientType("CLIENT_TYPE.SHOP∀CLIENT_TYPE.VENDOR");

        ApiResult<?> apiResult = popupCommonService.getClientList(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 담당자_리스트_조회() {
        GetGrantAuthEmployeePopupRequestDto dto = new GetGrantAuthEmployeePopupRequestDto();

        ApiResult<?> apiResult = popupCommonService.getGrantAuthEmployeeList(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 상품_리스트_조회() {
        GoodsSearchRequestDto dto = new GoodsSearchRequestDto();
        dto.setCategoryType("CATEGORY_STANDARD");
        Page<GoodsSearchVo> apiResult = popupCommonService.getGoodsList(dto);

        assertNotNull(apiResult.getResult());
//        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 엑셀다운로드_사유등록()  {
        ExcelDownLogRequestDto excelDownLogRequestDto  = new ExcelDownLogRequestDto();
        excelDownLogRequestDto.setExcelDownloadType("EXCEL_DOWN_TP.EVENT_PARTICIPANT");
        excelDownLogRequestDto.setDownloadReason("test");

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = servletContainer.getRequest();


        assertTrue(popupCommonService.addExcelDownReason(excelDownLogRequestDto, req) > 0);
    }

    @Test
    void 쿠폰_리스트_조회() throws Exception{
    	CouponRequestDto dto = new CouponRequestDto();

    	Page<CouponListResultVo> result = popupCommonService.getCouponList(dto);

        assertTrue(result.getResult().size() > 0);
    }
}