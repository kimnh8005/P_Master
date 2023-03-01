package kr.co.pulmuone.v1.api.Integratederp.order.service;

import kr.co.pulmuone.v1.api.Integratederp.order.dto.*;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class OrderErpServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private OrderErpService orderErpService;

    @Test
    void 송장조회_ERP연동_IF_DLV_SRCH_성공() throws Exception {
    	/* 테스트 가능 20200928 현재
    	 * 200928171121124001
    	 * 200920171121124001
    	 */
    	String ordNum = "200928171121124001";//"201008105021124001";
        List<ErpIfDeliveryResponseDto> erpIfDeliveryResponseDto = orderErpService.getIfDlvSrchByErp(ordNum);

        erpIfDeliveryResponseDto.forEach(
                i -> log.info(" erpIfDeliveryResponseDto : {}",  i.getLine())
        );

        assertTrue(CollectionUtils.isNotEmpty(erpIfDeliveryResponseDto));

    }


    @Test
    void 송장조회_ERP연동_IF_DLV_SRCH_실패() throws Exception {
    	String ordNum = "201008105021124001";//"201008105021124001";
        List<ErpIfDeliveryResponseDto> erpIfDeliveryResponseDto = orderErpService.getIfDlvSrchByErp(ordNum);

        erpIfDeliveryResponseDto.forEach(
                i -> log.info(" erpIfDeliveryResponseDto : {}",  i.getLine())
        );

        assertFalse(CollectionUtils.isEmpty(erpIfDeliveryResponseDto));

    }



    @Test
    void 송장조회완료_ERP연동_IF_DLV_FLAG_성공() throws Exception {
    	List<ErpIfDeliveryRequestDto> erpIfDeliveryList = new ArrayList<>();

		for(int i = 0; i < 1; i++) {

			//test data
			String hrdSeq = "SHOP2009250099";
			String oriSysSeq = "SHOP2009250099";

		    List<ErpIfDlvConditionRequestDto> orderList = new ArrayList<ErpIfDlvConditionRequestDto>();
		    ErpIfDlvConditionRequestDto erpIfDlvConditionRequestDto = new ErpIfDlvConditionRequestDto();
		    erpIfDlvConditionRequestDto.setHrdSeq(hrdSeq);
		    erpIfDlvConditionRequestDto.setOriSysSeq(oriSysSeq);
		    erpIfDlvConditionRequestDto.setDlvNo("1111");
		    orderList.add(erpIfDlvConditionRequestDto);

		    ErpIfDlvConditionRequestDto erpIfDlvConditionRequestDto2 = new ErpIfDlvConditionRequestDto();
		    erpIfDlvConditionRequestDto2.setHrdSeq(hrdSeq);
		    erpIfDlvConditionRequestDto2.setOriSysSeq(oriSysSeq);
		    erpIfDlvConditionRequestDto2.setDlvNo("2222");
			orderList.add(erpIfDlvConditionRequestDto2);


			ErpIfDeliveryRequestDto erpIfDeliveryRequestDto = new ErpIfDeliveryRequestDto();
			erpIfDeliveryRequestDto.setLine(orderList);
			erpIfDeliveryRequestDto.setHrdSeq(hrdSeq);
			erpIfDeliveryRequestDto.setOriSysSeq(oriSysSeq);

/*
			log.info("lineList2:"+ erpIfDeliveryRequestDto);
			log.info("lineList erpIfDeliveryRequestDto:" + JsonUtil.serializeWithPrettyPrinting(erpIfDeliveryRequestDto));
*/

			erpIfDeliveryList.add(erpIfDeliveryRequestDto);
		}

		log.info("lineList paramList:" + JsonUtil.serializeWithPrettyPrinting(erpIfDeliveryList));

		BaseApiResponseVo rtn =  orderErpService.putIfDlvFlagByErp(erpIfDeliveryList);

		log.info("TEMP:"+ JsonUtil.serializeWithPrettyPrinting(rtn.getResponseBody()));

		JSONObject json = new JSONObject().fromObject(rtn.getResponseBody());
		JSONObject unAffectedJson = (JSONObject) json.get("unAffected");

		log.info("unAffectedJson:" +unAffectedJson.get("line"));

		//assertEquals("000", rtn.getResponseCode());

		assertTrue(unAffectedJson.get("line") == null);
    }


    @Test
    void 송장조회완료_ERP연동_IF_DLV_FLAG_실패() throws Exception {

    	List<ErpIfDeliveryRequestDto> erpIfDeliveryList = new ArrayList<>();

		for(int i = 0; i < 1; i++) {

			//test data
			String hrdSeq = "SHOP2009250099TEST";
			String oriSysSeq = "SHOP2009250099TEST";

		    List<ErpIfDlvConditionRequestDto> orderList = new ArrayList<ErpIfDlvConditionRequestDto>();
		    ErpIfDlvConditionRequestDto erpIfDlvConditionRequestDto = new ErpIfDlvConditionRequestDto();
		    erpIfDlvConditionRequestDto.setHrdSeq(hrdSeq);
		    erpIfDlvConditionRequestDto.setOriSysSeq(oriSysSeq);
		    erpIfDlvConditionRequestDto.setDlvNo("1111");
		    orderList.add(erpIfDlvConditionRequestDto);

		    ErpIfDlvConditionRequestDto erpIfDlvConditionRequestDto2 = new ErpIfDlvConditionRequestDto();
		    erpIfDlvConditionRequestDto2.setHrdSeq(hrdSeq);
		    erpIfDlvConditionRequestDto2.setOriSysSeq(oriSysSeq);
		    erpIfDlvConditionRequestDto2.setDlvNo("2222");
			orderList.add(erpIfDlvConditionRequestDto2);


			ErpIfDeliveryRequestDto erpIfDeliveryRequestDto = new ErpIfDeliveryRequestDto();
			erpIfDeliveryRequestDto.setLine(orderList);
			erpIfDeliveryRequestDto.setHrdSeq(hrdSeq);
			erpIfDeliveryRequestDto.setOriSysSeq(oriSysSeq);

/*
			log.info("lineList2:"+ erpIfDeliveryRequestDto);
			log.info("lineList erpIfDeliveryRequestDto:" + JsonUtil.serializeWithPrettyPrinting(erpIfDeliveryRequestDto));
*/

			erpIfDeliveryList.add(erpIfDeliveryRequestDto);
		}

		log.info("lineList paramList:" + JsonUtil.serializeWithPrettyPrinting(erpIfDeliveryList));

		BaseApiResponseVo rtn =  orderErpService.putIfDlvFlagByErp(erpIfDeliveryList);

		log.info("TEMP:"+ JsonUtil.serializeWithPrettyPrinting(rtn.getResponseBody()));

		JSONObject json = new JSONObject().fromObject(rtn.getResponseBody());
		JSONObject unAffectedJson = (JSONObject) json.get("unAffected");

		log.info("unAffectedJson:" +unAffectedJson.get("line"));

		assertTrue(unAffectedJson.get("line") != null);

		//assertFalse("000".equals(rtn.getResponseCode()));

    }



    @Test
    void 미출조회_ERP연동_IF_MIS_SRCH_성공() throws Exception {
    	/* 테스트 가능 20200928 현재
    	 * 200928171121124001
    	 * 200920171121124001
    	 */
    	String ordNum = "200928171121124001";
        List<ErpIfMissResponseDto> erpIfMissResponseDto = orderErpService.getIfMisSrchByErp(ordNum);

        erpIfMissResponseDto.stream().forEach(
                i -> log.info(" erpIfMissRequestDto : {}",  i.getLine())
        );


        assertTrue(CollectionUtils.isNotEmpty(erpIfMissResponseDto));

    }


    @Test
    void 미출조회_ERP연동_IF_MIS_SRCH_실패() throws Exception {
    	/* 테스트 가능 20200928 현재
    	 * 200928171121124001
    	 * 200920171121124001
    	 */

    	String ordNum = "200928171121124001TEST";
        List<ErpIfMissResponseDto> erpIfMissResponseDto = orderErpService.getIfMisSrchByErp(ordNum);

        erpIfMissResponseDto.stream().forEach(
                i -> log.info(" erpIfDeliveryResponseDto : {}",  i.getLine())
        );


        assertFalse(CollectionUtils.isEmpty(erpIfMissResponseDto));

    }




    @Test
    void 미출조회완료_ERP연동_IF_MIS_FLAG_성공() throws Exception {
    	List<ErpIfMissRequestDto> erpIfMissList = new ArrayList<>();

		for(int i = 0; i < 1; i++) {

			//test data
			String hrdSeq = "SHOP2009250099";
			String oriSysSeq = "SHOP2009250099";
			String ordNum = "200928171121124001";

		    List<ErpIfOrderConditionRequestDto> orderList = new ArrayList<ErpIfOrderConditionRequestDto>();

		    ErpIfOrderConditionRequestDto condition = new ErpIfOrderConditionRequestDto();
		    condition.setHrdSeq(hrdSeq);
		    condition.setOriSysSeq(oriSysSeq);
		    condition.setOrdNum(ordNum);

		    orderList.add(condition);

		    ErpIfMissRequestDto erpIfMissRequestDto = new ErpIfMissRequestDto();
		    erpIfMissRequestDto.setLine(orderList);
		    erpIfMissRequestDto.setHrdSeq(hrdSeq);
		    erpIfMissRequestDto.setOriSysSeq(oriSysSeq);

/*
			log.info("lineList2:"+ erpIfDeliveryRequestDto);
			log.info("lineList erpIfDeliveryRequestDto:" + JsonUtil.serializeWithPrettyPrinting(erpIfDeliveryRequestDto));
*/

			erpIfMissList.add(erpIfMissRequestDto);
		}

		log.info("lineList paramList:" + JsonUtil.serializeWithPrettyPrinting(erpIfMissList));

		BaseApiResponseVo rtn =  orderErpService.putIfMisFlagByErp(erpIfMissList);

		log.info("TEMP:"+ JsonUtil.serializeWithPrettyPrinting(rtn.getResponseBody()));

		JSONObject json = new JSONObject().fromObject(rtn.getResponseBody());
		JSONObject unAffectedJson = (JSONObject) json.get("unAffected");

		log.info("unAffectedJson:" +unAffectedJson.get("line"));

		//assertEquals("000", rtn.getResponseCode());

		assertTrue(unAffectedJson.get("line") == null);
    }


    @Test
    void 미출조회완료_ERP연동_IF_MIS_FLAG_실패() throws Exception {

      	List<ErpIfMissRequestDto> erpIfMissList = new ArrayList<>();

		for(int i = 0; i < 1; i++) {

			//test data
			String hrdSeq = "SHOP2009250099TEST";
			String oriSysSeq = "SHOP2009250099TEST";
			String ordNum = "200928171121124001TEST";

		    List<ErpIfOrderConditionRequestDto> orderList = new ArrayList<ErpIfOrderConditionRequestDto>();

		    ErpIfOrderConditionRequestDto condition = new ErpIfOrderConditionRequestDto();
		    condition.setHrdSeq(hrdSeq);
		    condition.setOriSysSeq(oriSysSeq);
		    condition.setOrdNum(ordNum);

		    orderList.add(condition);

		    ErpIfMissRequestDto erpIfMissRequestDto = new ErpIfMissRequestDto();
		    erpIfMissRequestDto.setLine(orderList);
		    erpIfMissRequestDto.setHrdSeq(hrdSeq);
		    erpIfMissRequestDto.setOriSysSeq(oriSysSeq);

/*
			log.info("lineList2:"+ erpIfDeliveryRequestDto);
			log.info("lineList erpIfDeliveryRequestDto:" + JsonUtil.serializeWithPrettyPrinting(erpIfDeliveryRequestDto));
*/

			erpIfMissList.add(erpIfMissRequestDto);
		}

		log.info("lineList paramList:" + JsonUtil.serializeWithPrettyPrinting(erpIfMissList));

		BaseApiResponseVo rtn =  orderErpService.putIfMisFlagByErp(erpIfMissList);

		log.info("TEMP:"+ JsonUtil.serializeWithPrettyPrinting(rtn.getResponseBody()));

		JSONObject json = new JSONObject().fromObject(rtn.getResponseBody());
		JSONObject unAffectedJson = (JSONObject) json.get("unAffected");

		log.info("unAffectedJson:" +unAffectedJson.get("line"));

		//assertEquals("000", rtn.getResponseCode());

		assertTrue(unAffectedJson.get("line") != null);

    }




    @Test
    void 매출확정조회_ERP연동_IF_SAL_SRCH_성공() throws Exception {
    	String ordNum = "200928171121124001";

        List<ErpIfSalesResponseDto> erpIfSalesResponseDto = orderErpService.getIfSalSrchByErp(ordNum);

        erpIfSalesResponseDto.forEach(
                i -> log.info(" 매출확정조회_ERP연동_IF_SAL_SRCH_성공 erpIfSalesResponseDto : {}",  i.getLine())
        );

        assertTrue(CollectionUtils.isNotEmpty(erpIfSalesResponseDto));

    }


    @Test
    void 매출확정조회_ERP연동_IF_SAL_SRCH_실패() throws Exception {
       	String ordNum = "200928171121124001";

        List<ErpIfSalesResponseDto> erpIfSalesResponseDto = orderErpService.getIfSalSrchByErp(ordNum);

        erpIfSalesResponseDto.stream().forEach(
                i -> log.info(" 매출확정조회_ERP연동_IF_SAL_SRCH_실패 erpIfSalesResponseDto : {}",  i.getLine())
        );

        assertFalse(CollectionUtils.isEmpty(erpIfSalesResponseDto));

    }




    @Test
    void 매출확정조회완료_ERP연동_IF_SAL_FLAG_성공() throws Exception {
    	List<ErpIfSalesRequestDto> erpIfSalList = new ArrayList<>();

		for(int i = 0; i < 1; i++) {

			//test data
			String hrdSeq = "SHOP2009250099";
			String oriSysSeq = "SHOP2009250099";
			String ordNum = "200928171121124001";

		    List<ErpIfOrderConditionRequestDto> orderList = new ArrayList<ErpIfOrderConditionRequestDto>();

		    ErpIfOrderConditionRequestDto condition = new ErpIfOrderConditionRequestDto();
		    condition.setHrdSeq(hrdSeq);
		    condition.setOriSysSeq(oriSysSeq);
		    condition.setOrdNum(ordNum);

		    orderList.add(condition);

		    ErpIfSalesRequestDto erpIfSalRequestDto = new ErpIfSalesRequestDto();
		    erpIfSalRequestDto.setLine(orderList);
		    erpIfSalRequestDto.setHrdSeq(hrdSeq);
		    erpIfSalRequestDto.setOriSysSeq(oriSysSeq);

/*
			log.info("lineList2:"+ erpIfDeliveryRequestDto);
			log.info("lineList erpIfDeliveryRequestDto:" + JsonUtil.serializeWithPrettyPrinting(erpIfDeliveryRequestDto));
*/

		    erpIfSalList.add(erpIfSalRequestDto);
		}

		log.info("lineList paramList:" + JsonUtil.serializeWithPrettyPrinting(erpIfSalList));

		BaseApiResponseVo rtn =  orderErpService.putIfSalFlagByErp(erpIfSalList);

		log.info("TEMP:"+ JsonUtil.serializeWithPrettyPrinting(rtn.getResponseBody()));

		JSONObject json = new JSONObject().fromObject(rtn.getResponseBody());
		JSONObject unAffectedJson = (JSONObject) json.get("unAffected");

		log.info("unAffectedJson:" +unAffectedJson.get("line"));

		//assertEquals("000", rtn.getResponseCode());

		assertTrue(unAffectedJson.get("line") == null);
    }


    @Test
    void 매출확정조회완료_ERP연동_IF_SAL_FLAG_실패() throws Exception {

      	List<ErpIfSalesRequestDto> erpIfSalList = new ArrayList<>();

		for(int i = 0; i < 1; i++) {

			//test data
			String hrdSeq = "SHOP2009250099TT";
			String oriSysSeq = "SHOP2009250099TT";
			String ordNum = "200928171121124001TT";

		    List<ErpIfOrderConditionRequestDto> orderList = new ArrayList<ErpIfOrderConditionRequestDto>();

		    ErpIfOrderConditionRequestDto condition = new ErpIfOrderConditionRequestDto();
		    condition.setHrdSeq(hrdSeq);
		    condition.setOriSysSeq(oriSysSeq);
		    condition.setOrdNum(ordNum);

		    orderList.add(condition);

		    ErpIfSalesRequestDto erpIfSalRequestDto = new ErpIfSalesRequestDto();
		    erpIfSalRequestDto.setLine(orderList);
		    erpIfSalRequestDto.setHrdSeq(hrdSeq);
		    erpIfSalRequestDto.setOriSysSeq(oriSysSeq);

/*
			log.info("lineList2:"+ erpIfDeliveryRequestDto);
			log.info("lineList erpIfDeliveryRequestDto:" + JsonUtil.serializeWithPrettyPrinting(erpIfDeliveryRequestDto));
*/

		    erpIfSalList.add(erpIfSalRequestDto);
		}

		log.info("lineList paramList:" + JsonUtil.serializeWithPrettyPrinting(erpIfSalList));

		BaseApiResponseVo rtn =  orderErpService.putIfSalFlagByErp(erpIfSalList);

		log.info("TEMP:"+ JsonUtil.serializeWithPrettyPrinting(rtn.getResponseBody()));

		JSONObject json = new JSONObject().fromObject(rtn.getResponseBody());
		JSONObject unAffectedJson = (JSONObject) json.get("unAffected");

		log.info("unAffectedJson:" +unAffectedJson.get("line"));

		//assertEquals("000", rtn.getResponseCode());

		assertTrue(unAffectedJson.get("line") != null);

    }

    @Test
    void addIfCustordInpByErp() throws Exception {
    	// [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
		assertTrue(orderErpService.addIfCustordInpByErp() == null);
    }
}