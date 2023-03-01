package kr.co.pulmuone.v1.policy.shippingtemplate.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateManagementRequestDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateRequestDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingConditionVo;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingTemplateVo;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingWarehouseVo;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PolicyShippingTemplateServiceTest extends CommonServiceTestBaseForJunit5{

    @Autowired
    private PolicyShippingTemplateService policyShippingTemplateService;

    @Test
    void 출고처기준_배송정책설정_조회_성공() {
        UserVo userVO = new UserVo();
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        ShippingTemplateRequestDto shippingTemplateRequestDto = new ShippingTemplateRequestDto();
        shippingTemplateRequestDto.setPage(1);
        shippingTemplateRequestDto.setPageSize(20);

        Page<ShippingTemplateVo> warehousePeriodShippingTemplateList = policyShippingTemplateService.getWarehousePeriodShippingTemplateList(shippingTemplateRequestDto);

        assertTrue( CollectionUtils.isNotEmpty(warehousePeriodShippingTemplateList.getResult()) );

        SessionUtil.setUserVO(null);
    }

    @Test
    void 출고처기준_배송정책설정_조회_실패() {
        ShippingTemplateRequestDto shippingTemplateRequestDto = new ShippingTemplateRequestDto();
        shippingTemplateRequestDto.setPage(1);
        shippingTemplateRequestDto.setPageSize(20);
        shippingTemplateRequestDto.setWarehouseId("0");

        Page<ShippingTemplateVo> warehousePeriodShippingTemplateList = policyShippingTemplateService.getWarehousePeriodShippingTemplateList(shippingTemplateRequestDto);

        assertFalse( CollectionUtils.isNotEmpty(warehousePeriodShippingTemplateList.getResult()) );
    }

    @Test
    void 배송정책설정_조회_성공() {
        UserVo userVO = new UserVo();
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        ShippingTemplateRequestDto shippingTemplateRequestDto = new ShippingTemplateRequestDto();
        shippingTemplateRequestDto.setPage(1);
        shippingTemplateRequestDto.setPageSize(20);

        Page<ShippingTemplateVo> shippingTemplateList = policyShippingTemplateService.getShippingTemplateList(shippingTemplateRequestDto);

        assertTrue( CollectionUtils.isNotEmpty(shippingTemplateList.getResult()) );

        SessionUtil.setUserVO(null);
    }

    @Test
    void 배송정책설정_조회_실패() {
        ShippingTemplateRequestDto shippingTemplateRequestDto = new ShippingTemplateRequestDto();
        shippingTemplateRequestDto.setPage(1);
        shippingTemplateRequestDto.setPageSize(20);
        shippingTemplateRequestDto.setWarehouseId("0");

        Page<ShippingTemplateVo> shippingTemplateList = policyShippingTemplateService.getShippingTemplateList(shippingTemplateRequestDto);

        assertFalse( CollectionUtils.isNotEmpty(shippingTemplateList.getResult()) );
    }

    @Test
    void 배송비탬플릿_상세_조회_성공() {
        ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto = new ShippingTemplateManagementRequestDto();
        shippingTemplateManagementRequestDto.setShippingTemplateId(12L);

        ShippingTemplateVo shippingTemplateVo = policyShippingTemplateService.getShippingTemplateInfo(shippingTemplateManagementRequestDto);

        assertEquals(12L, shippingTemplateVo.getShippingTemplateId());
    }

    @Test
    void 배송비탬플릿_상세_조회_실패() {
        ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto = new ShippingTemplateManagementRequestDto();

        ShippingTemplateVo shippingTemplateVo = policyShippingTemplateService.getShippingTemplateInfo(shippingTemplateManagementRequestDto);

        assertTrue(ObjectUtils.isEmpty(shippingTemplateVo));
    }

    @Test
    void 배송비_출고처정보_조회_성공() {
        ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto = new ShippingTemplateManagementRequestDto();
        shippingTemplateManagementRequestDto.setShippingTemplateId(12L);

        List<ShippingWarehouseVo> shippingWarehouseList = policyShippingTemplateService.getShippingWarehouseList(shippingTemplateManagementRequestDto);

        assertTrue(CollectionUtils.isNotEmpty(shippingWarehouseList));
    }

    @Test
    void 배송비_출고처정보_조회_실패() {
        ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto = new ShippingTemplateManagementRequestDto();

        List<ShippingWarehouseVo> shippingWarehouseList = policyShippingTemplateService.getShippingWarehouseList(shippingTemplateManagementRequestDto);

        assertTrue(CollectionUtils.isEmpty(shippingWarehouseList));
    }

    @Test
    void 배송비_조건정보_조회_성공() {
        ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto = new ShippingTemplateManagementRequestDto();
        shippingTemplateManagementRequestDto.setShippingTemplateId(12L);

        List<ShippingConditionVo> shippingConditionList = policyShippingTemplateService.getShippingConditionList(shippingTemplateManagementRequestDto);

        assertTrue(CollectionUtils.isNotEmpty(shippingConditionList));
    }

    @Test
    void 배송비_조건정보_조회_실패() {
        ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto = new ShippingTemplateManagementRequestDto();

        List<ShippingConditionVo> shippingConditionList = policyShippingTemplateService.getShippingConditionList(shippingTemplateManagementRequestDto);

        assertTrue(CollectionUtils.isEmpty(shippingConditionList));
    }

    @Test
    void 배송정책_탬플릿_등록_성공() {
        ShippingTemplateVo shippingTemplateVo = new ShippingTemplateVo();
        shippingTemplateVo.setOriginalShippingTemplateId(0L);
        shippingTemplateVo.setShippingTemplateName("테스트");
        shippingTemplateVo.setPaymentMethodType("PAYMENT_METHOD_TYPE.1");
        shippingTemplateVo.setBundleYn("N");
        shippingTemplateVo.setConditionTypeCode("CONDITION_TYPE.1");
        shippingTemplateVo.setShippingPrice(3000);
        shippingTemplateVo.setClaimShippingPrice(4000);
        shippingTemplateVo.setAreaShippingDeliveryYn("N");
        shippingTemplateVo.setAreaShippingYn("N");
        shippingTemplateVo.setJejuShippingPrice(0);
        shippingTemplateVo.setIslandShippingPrice(0);
        shippingTemplateVo.setCreateId("1");
        shippingTemplateVo.setUndeliverableAreaTp("test");

        int addShippingCount = policyShippingTemplateService.addShippingTemplate(shippingTemplateVo);

        assertEquals(1, addShippingCount);
    }

    @Test
    void 배송정책_탬플릿_등록_실패() {
        ShippingTemplateVo shippingTemplateVo = new ShippingTemplateVo();

        assertThrows(DataIntegrityViolationException.class, () -> {
            policyShippingTemplateService.addShippingTemplate(shippingTemplateVo);
        });
    }

    @Test
    void 배송비_탬플릿_원본_정책번호_업데이트_성공() {
        ShippingTemplateVo shippingTemplateVo = new ShippingTemplateVo();
        shippingTemplateVo.setShippingTemplateId(12L);
        shippingTemplateVo.setOriginalShippingTemplateId(12L);
        shippingTemplateVo.setCreateId("1");

        int updateCount = policyShippingTemplateService.putOriginalShippingTemplateId(shippingTemplateVo);

        assertEquals(1, updateCount);
    }

    @Test
    void 배송비_탬플릿_원본_정책번호_업데이트_실패() {
        ShippingTemplateVo shippingTemplateVo = new ShippingTemplateVo();

        int updateCount = policyShippingTemplateService.putOriginalShippingTemplateId(shippingTemplateVo);

        assertEquals(0, updateCount);
    }

    @Test
    void 배송비_출고처정보_동일한_출고처_기본유무_N으로_변경_성공() {
        ShippingWarehouseVo shippingWarehouseVo = new ShippingWarehouseVo();
        shippingWarehouseVo.setWarehouseId(4L);
        shippingWarehouseVo.setCreateId("1");

        int updateCount = policyShippingTemplateService.putShippingWarehouseBasicInitialization(shippingWarehouseVo);

        assertEquals(1, updateCount);
    }

    @Test
    void 배송비_출고처정보_동일한_출고처_기본유무_N으로_변경_실패() {
        ShippingWarehouseVo shippingWarehouseVo = new ShippingWarehouseVo();

        int updateCount = policyShippingTemplateService.putShippingWarehouseBasicInitialization(shippingWarehouseVo);

        assertEquals(0, updateCount);
    }

    @Test
    void 배송비_출고처정보_등록_성공() {
        ShippingWarehouseVo shippingWarehouseVo = new ShippingWarehouseVo();
        shippingWarehouseVo.setShippingTemplateId(12L);
        shippingWarehouseVo.setShippingWarehouseId(3L);
        shippingWarehouseVo.setWarehouseId(4L);
        shippingWarehouseVo.setBasicYn( "N" );
        shippingWarehouseVo.setCreateId("1");

        int addCount = policyShippingTemplateService.addShippingWarehouse(shippingWarehouseVo);

        assertEquals(1, addCount);
    }

    @Test
    void 배송비_출고처정보_등록_실패() {
        ShippingWarehouseVo shippingWarehouseVo = new ShippingWarehouseVo();

        assertThrows(DataIntegrityViolationException.class, () -> {
            policyShippingTemplateService.addShippingWarehouse(shippingWarehouseVo);
        });
    }

    @Test
    void 배송비_조건정보_등록_성공() {
        ShippingConditionVo shippingConditionVo = new ShippingConditionVo();
        shippingConditionVo.setShippingTemplateId(12L);
        shippingConditionVo.setShippingConditionId(1L);
        shippingConditionVo.setShippingPrice(0);
        shippingConditionVo.setConditionValue(40000);
        shippingConditionVo.setCreateId("1");

        int addCount = policyShippingTemplateService.addShippingCondition(shippingConditionVo);

        assertEquals(1, addCount);
    }

    @Test
    void 배송비_조건정보_등록_실패() {
        ShippingConditionVo shippingConditionVo = new ShippingConditionVo();

        assertThrows(DataIntegrityViolationException.class, () -> {
            policyShippingTemplateService.addShippingCondition(shippingConditionVo);
        });
    }

    @Test
    void 배송비탬플릿_삭제여부_업데이트_성공() {
        ShippingTemplateVo shippingTemplateVo = new ShippingTemplateVo();
        shippingTemplateVo.setShippingTemplateId(12L);
        shippingTemplateVo.setCreateId("1");

        int updateCount = policyShippingTemplateService.putShippingTemplateDeleteYn(shippingTemplateVo);

        assertEquals(1, updateCount);
    }

    @Test
    void 배송비탬플릿_삭제여부_업데이트_실패() {
        ShippingTemplateVo shippingTemplateVo = new ShippingTemplateVo();

        int updateCount = policyShippingTemplateService.putShippingTemplateDeleteYn(shippingTemplateVo);

        assertEquals(0, updateCount);
    }

    @Test
    void 상품_배송비_관계_출고처_유무_체크_성공() {
        ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto = new ShippingTemplateManagementRequestDto();
        shippingTemplateManagementRequestDto.setOriginalShippingTemplateId(6L);
        shippingTemplateManagementRequestDto.setWarehouseId(4L);

        Boolean goodsShippingTemplateYn = policyShippingTemplateService.getGoodsShippingTemplateYn(shippingTemplateManagementRequestDto);

        assertTrue(goodsShippingTemplateYn);
    }

    @Test
    void 상품_배송비_관계_출고처_유무_체크_실패() {
        ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto = new ShippingTemplateManagementRequestDto();

        Boolean goodsShippingTemplateYn = policyShippingTemplateService.getGoodsShippingTemplateYn(shippingTemplateManagementRequestDto);

        assertFalse(goodsShippingTemplateYn);
    }

    @Test
    void 배송정책_출고처_기본여부_체크_성공() {
        ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto = new ShippingTemplateManagementRequestDto();
        List<ShippingWarehouseVo>  shippingWarehouseList = new ArrayList<ShippingWarehouseVo>();
        ShippingWarehouseVo shippingWarehouseVo = new ShippingWarehouseVo();
        shippingWarehouseVo.setWarehouseId(4L);
        shippingWarehouseList.add(shippingWarehouseVo);
        shippingTemplateManagementRequestDto.setShippingWarehouseList(shippingWarehouseList);

        Boolean shippingWarehouseBasicYn = policyShippingTemplateService.getShippingWarehouseBasicYnCheck(shippingTemplateManagementRequestDto);

        assertTrue(shippingWarehouseBasicYn);
    }

    @Test
    void 배송정책_출고처_기본여부_체크_실패() {
        ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto = new ShippingTemplateManagementRequestDto();

        assertThrows(MyBatisSystemException.class, () -> {
            policyShippingTemplateService.getShippingWarehouseBasicYnCheck(shippingTemplateManagementRequestDto);
        });
    }

    @Test
    void 배송비_조건정보_목록_셋팅_성공() {
        ShippingTemplateVo shippingTemplateVo = new ShippingTemplateVo();
        List<ShippingConditionVo> shippingConditionList = new ArrayList<ShippingConditionVo>();
        ShippingConditionVo shippingConditionVo = new ShippingConditionVo();
        shippingConditionVo.setConditionValue(10000);
        shippingConditionVo.setShippingPrice(3000);
        shippingConditionList.add(shippingConditionVo);

        shippingConditionVo = new ShippingConditionVo();
        shippingConditionVo.setConditionValue(20000);
        shippingConditionVo.setShippingPrice(2000);
        shippingConditionList.add(shippingConditionVo);

        List<ShippingConditionVo> settingShippingConditionList = policyShippingTemplateService.shippingConditionListSetting(shippingTemplateVo, shippingConditionList);

        assertEquals(2000, settingShippingConditionList.get(0).getShippingPrice());
    }

    @Test
    void 배송비_조건정보_목록_셋팅_실패() {
        ShippingTemplateVo shippingTemplateVo = new ShippingTemplateVo();
        List<ShippingConditionVo> shippingConditionList = new ArrayList<ShippingConditionVo>();
        ShippingConditionVo shippingConditionVo = new ShippingConditionVo();
        shippingConditionVo.setConditionValue(10000);
        shippingConditionVo.setShippingPrice(3000);
        shippingConditionList.add(shippingConditionVo);

        shippingConditionVo = new ShippingConditionVo();
        shippingConditionVo.setConditionValue(20000);
        shippingConditionVo.setShippingPrice(2000);
        shippingConditionList.add(shippingConditionVo);

        List<ShippingConditionVo> settingShippingConditionList = policyShippingTemplateService.shippingConditionListSetting(shippingTemplateVo, shippingConditionList);

        assertFalse( settingShippingConditionList.get(0).getShippingPrice() == 3000 );
    }
}
