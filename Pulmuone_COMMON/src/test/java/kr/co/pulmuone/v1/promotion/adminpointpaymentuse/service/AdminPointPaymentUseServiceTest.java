package kr.co.pulmuone.v1.promotion.adminpointpaymentuse.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.promotion.adminpointpaymentuse.AdminPointPaymentUseMapper;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.AdminPointPaymentUseListRequestDto;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.vo.AdminPointPaymentUseVo;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class AdminPointPaymentUseServiceTest extends CommonServiceTestBaseForJunit5 {
	@Autowired
	AdminPointPaymentUseService adminPointPaymentUseService;

    @InjectMocks
    private AdminPointPaymentUseService mockAdminPointPaymentUseService;

    @Mock
    AdminPointPaymentUseMapper mockAdminPointPaymentUseMapper;


    @Test
    void getAdminPointPaymentUseList_정상() throws Exception {
    	//given
    	AdminPointPaymentUseListRequestDto dto = new AdminPointPaymentUseListRequestDto();

    	//when
    	Page<AdminPointPaymentUseVo> result = adminPointPaymentUseService.getAdminPointPaymentUseList(dto);

        //then
        Assertions.assertTrue(result.getTotal()> 0);
    }

    @Test
    void getAdminPointPaymentUseList_조회내역정상() throws Exception {
    	//given
    	AdminPointPaymentUseListRequestDto dto = new AdminPointPaymentUseListRequestDto();
    	dto.setSearchPointType("POINT_ADMIN_ISSUE_TP");
    	//when
    	Page<AdminPointPaymentUseVo> result = adminPointPaymentUseService.getAdminPointPaymentUseList(dto);

        //then
        Assertions.assertFalse(result.getTotal()> 0);
    }


    @Test
    void getAdminPointPaymentUseList_조회내역실패() throws Exception {
    	//given
    	AdminPointPaymentUseListRequestDto dto = new AdminPointPaymentUseListRequestDto();
    	dto.setSearchPointType("POINT_ADMIN_ISSUE_TP.EVENT_AWARD");
    	dto.setGrantAuthEmployeeNumber("-1");
    	//when
    	Page<AdminPointPaymentUseVo> result = adminPointPaymentUseService.getAdminPointPaymentUseList(dto);

        //then
    	assertFalse( CollectionUtils.isNotEmpty(result.getResult()) );
    }





}
