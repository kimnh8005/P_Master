package kr.co.pulmuone.v1.policy.shippingcomp.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.policy.shippingcomp.PolicyShippingCompMapper;
import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompDto;
import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompRequestDto;
import kr.co.pulmuone.v1.policy.shippingcomp.vo.PolicyShippingCompVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
public class PolicyShippingCompServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private PolicyShippingCompService policyShippingCompService;

	@InjectMocks
	private PolicyShippingCompService mockPolicyShippingCompService;

	@Mock
	PolicyShippingCompMapper mockPolicyShippingCompMapper;

	@BeforeEach
	void setUp() {
		preLogin();
		mockPolicyShippingCompService = new PolicyShippingCompService(mockPolicyShippingCompMapper);
	}

    @Test
    public void test_키워드_결과없음()  {
    	String keyword = "453bh67dfbyh";

    	// given
    	PolicyShippingCompRequestDto dto = new PolicyShippingCompRequestDto();
    	dto.setShippingCompNm(keyword);

        // when
    	PolicyShippingCompDto result = policyShippingCompService.getPolicyShippingCompList(dto);
    	List<PolicyShippingCompVo> vo = result.getRows();

    	assertTrue(CollectionUtils.isEmpty(vo));
    }


    @Test
    public void test_키워드_결과있음()  {
    	String keyword = "";
    	String useYn = "";

    	// given
    	PolicyShippingCompRequestDto dto = new PolicyShippingCompRequestDto();
    	dto.setShippingCompNm(keyword);
    	dto.setUseYn(useYn);

        // when
    	PolicyShippingCompDto result = policyShippingCompService.getPolicyShippingCompList(dto);

        assertNotNull(result.getRows());
//        Assert.assertTrue(list.size() >= 0);
    }

    @Test
    public void 택배사설정_신규등록() {
    	// given
    	PolicyShippingCompRequestDto dto = new PolicyShippingCompRequestDto();
    	dto.setShippingCompNm("테스트택배사");
    	dto.setTrackingUrl("https://shippingCompUrl");
    	dto.setHttpRequestTp("G");
    	dto.setInvoiceParam("testParam");
    	dto.setUseYn("Y");

        // when
        int count = policyShippingCompService.addPolicyShippingComp(dto);

        // then
        assertEquals(1, count);
    }

    @Test
    public void 택배사설정_수정() {
    	// given
    	PolicyShippingCompRequestDto dto = new PolicyShippingCompRequestDto();
    	dto.setShippingCompNm("테스트택배사");
    	dto.setTrackingUrl("https://shippingCompUrl");
    	dto.setHttpRequestTp("G");
    	dto.setInvoiceParam("testParam");
    	dto.setUseYn("Y");
    	dto.setPsShippingCompId(214);

        // when
        int count = policyShippingCompService.putPolicyShippingComp(dto);

        // then
        assertEquals(1, count);
    }

    @Test
    public void 택배사설정_삭제() {
    	// given
    	PolicyShippingCompRequestDto dto = new PolicyShippingCompRequestDto();
    	dto.setPsShippingCompId(214);

    	// when
    	int count = policyShippingCompService.delPolicyShippingComp(dto);

    	// then
    	assertEquals(1, count);
    }

    @Test
    void getPolicyShippingCompInfo() {
		PolicyShippingCompDto result = policyShippingCompService.getPolicyShippingCompInfo(88L);
		assertTrue(result != null);
    }

    @Test
    void addPolicyShippingComp() {
		given(mockPolicyShippingCompMapper.addPolicyShippingComp(any())).willReturn(1);
		int n = mockPolicyShippingCompService.addPolicyShippingComp(null);
		assertTrue(n > 0);
    }
}
