package kr.co.pulmuone.bos.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfCustOrdRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDeliveryRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDlvConditionRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfMissRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfOrderConditionRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfSalesRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.service.OrderErpBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * <PRE>
 * Forbiz Korea
 * ERP 연동 테스트 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200923   	강윤경     최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@RestController
public class IntegratederpOrderController {

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
	private OrderErpBiz orderErpBiz;




	/**
	 * 주문|취소 입력 ERP 연동 IF_CUSTORD_INP
	 * @param
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/api/addIfCustordInpByErp")
	public ApiResult<?> addIfCustordInpByErp(ErpIfCustOrdRequestDto erpIfCustOrdRequestDto) throws Exception{
		return orderErpBiz.addIfCustordInpByErp();
	}



	/**
	 * 송장 조회 ERP 연동 IF_DLV_SRCH
	 * @param
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/api/getIfDlvSrchByErp")
	public ApiResult<?> getIfDlvSrchByErp() throws Exception{
		String ordNum = "";// "201008105021124001";
		return orderErpBiz.getIfDlvSrchByErp(ordNum);
	}



	/**
	 * 송장 조회 완료 ERP 연동 IF_DLV_FLAG
	 * @param
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@PutMapping(value = "/admin/api/putIfDlvFlagByErp")
	public ApiResult<?> putIfDlvFlagByErp() throws Exception{

		List<ErpIfDeliveryRequestDto> erpIfDeliveryList = new ArrayList<>();
		/* 호출 테스트 위해
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


			//log.info("lineList2:"+ erpIfDeliveryRequestDto);
			//log.info("lineList erpIfDeliveryRequestDto:" + JsonUtil.serializeWithPrettyPrinting(erpIfDeliveryRequestDto));

			erpIfDeliveryList.add(erpIfDeliveryRequestDto);
		}
		*/

		log.info("lineList paramList:" + JsonUtil.serializeWithPrettyPrinting(erpIfDeliveryList));

		return orderErpBiz.putIfDlvFlagByErp(erpIfDeliveryList);
	}



	/**
	 * 미출 조회 ERP 연동 IF_MIS_SRCH
	 * @param
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/api/getIfMisSrchByErp")
	public ApiResult<?> getIfMisSrchByErp() throws Exception{
		String ordNum = "";
		return orderErpBiz.getIfMisSrchByErp(ordNum);
	}



	/**
	 * 미출 조회 완료 ERP 연동 IF_MIS_FLAG
	 * @param
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@PutMapping(value = "/admin/api/putIfMisFlagByErp")
	public ApiResult<?> putIfMisFlagByErp() throws Exception{

		List<ErpIfMissRequestDto> erpIfMissList = new ArrayList<>();
		/* 호출 테스트 위해
		for(int i = 0; i < 1; i++) {

			//test data
			String hrdSeq = "SHOP2009240008";
			String oriSysSeq = "SHOP2009240008";
			String ordNum = "200924144721124001";

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

			//log.info("lineList2:"+ erpIfDeliveryRequestDto);
			//log.info("lineList erpIfDeliveryRequestDto:" + JsonUtil.serializeWithPrettyPrinting(erpIfDeliveryRequestDto));

			erpIfMissList.add(erpIfMissRequestDto);
		}
		*/

		log.info("lineList paramList:" + JsonUtil.serializeWithPrettyPrinting(erpIfMissList));

		return orderErpBiz.putIfMisFlagByErp(erpIfMissList);
	}



	/**
	 * 매출 확정 내역 조회 ERP 연동 IF_SAL_SRCH
	 * @param
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/api/getIfSalSrchByErp")
	public ApiResult<?> getIfSalSrchByErp() throws Exception{
		String ordNum = "";
		return orderErpBiz.getIfSalSrchByErp(ordNum);
	}



	/**
	 * 매출 확정 조회 완료 ERP 연동 IF_SAL_FLAG
	 * @param
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@PutMapping(value = "/admin/api/putIfSalFlagByErp")
	public ApiResult<?> putIfSalFlagByErp() throws Exception{

		List<ErpIfSalesRequestDto> erpIfSalesList = new ArrayList<>();

		/* 호출 테스트 위해
		for(int i = 0; i < 1; i++) {

			//test data
			String hrdSeq = "SHOP2010130007";
			String oriSysSeq = "SHOP2010130007";
			String ordNum = "201013142621129001";

		    List<ErpIfOrderConditionRequestDto> orderList = new ArrayList<ErpIfOrderConditionRequestDto>();

		    ErpIfOrderConditionRequestDto condition = new ErpIfOrderConditionRequestDto();
		    condition.setHrdSeq(hrdSeq);
		    condition.setOriSysSeq(oriSysSeq);
		    condition.setOrdNum(ordNum);

		    orderList.add(condition);

		    ErpIfSalesRequestDto erpIfSalesRequestDto = new ErpIfSalesRequestDto();
		    erpIfSalesRequestDto.setLine(orderList);
		    erpIfSalesRequestDto.setHrdSeq(hrdSeq);
		    erpIfSalesRequestDto.setOriSysSeq(oriSysSeq);

			//log.info("lineList2:"+ erpIfDeliveryRequestDto);
			//log.info("lineList erpIfDeliveryRequestDto:" + JsonUtil.serializeWithPrettyPrinting(erpIfDeliveryRequestDto));


		    erpIfSalesList.add(erpIfSalesRequestDto);
		}
		*/

		log.info("lineList paramList:" + JsonUtil.serializeWithPrettyPrinting(erpIfSalesList));

		return orderErpBiz.putIfSalFlagByErp(erpIfSalesList);
	}



}

