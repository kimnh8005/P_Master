package kr.co.pulmuone.v1.batch.order.comm;

import kr.co.pulmuone.v1.batch.order.dto.vo.*;
import kr.co.pulmuone.v1.comm.api.constant.SupplierTypes;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErpApiComm {

	/**
	 * 하이톡 일일배송 주문 배달주기
	 * @param lineItem
	 * @return String
	 * @throws
	 */
    public static String getGoodsCycleTp(HitokDailyDeliveryOrderListVo lineItem) {

		// 디폴트 배달 고정
		String goodsCycleTp = ErpApiEnums.ErpGoodsCycleTp.FIXING.getCode();
		// 1일,4일 배달 비고정
		if(GoodsEnums.GoodsCycleTypeByGreenJuice.DAY1_PER_WEEK.getCode().equals(lineItem.getGoodsCycleTp())
				|| GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS4_PER_WEEK.getCode().equals(lineItem.getGoodsCycleTp()))
			goodsCycleTp = ErpApiEnums.ErpGoodsCycleTp.NON_FIXING.getCode();

		// 배달요일이면 1 아니면 0
		String mon = lineItem.getMonCnt() > 0
						? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode()
						: ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
		String tue = lineItem.getTueCnt() > 0
						? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode()
						: ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
		String wed = lineItem.getWedCnt() > 0
						? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode()
						: ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
		String thu = lineItem.getThuCnt() > 0
						? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode()
						: ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
		String fri = ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
		if(lineItem.getFriCnt() > 0)
			// 주6일이면 금요일 2 이외 1
			fri = GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS6_PER_WEEK.getCode().equals(lineItem.getGoodsCycleTp())
					? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.SATURDAY_DELIVERY.getCode()
					: ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode();
		// 토요일/일요일은 무조건 0
		String sun = ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
		String sat = ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();

		return lineItem.getOrderCnt()+"|"+sun+mon+tue+wed+thu+fri+sat+"|"+goodsCycleTp;
	}

	/**
	 * 주문경로
	 * @param agentTypeCd
	 * @param buyerTypeCd
	 * @return String
	 * @throws
	 */
	public static String getOrderHpnCd(String agentTypeCd, String buyerTypeCd) {

		String orderHpnCd = ErpApiEnums.ErpOrderHpnCd.WEB_EMPLOYEE.getCode();
		if(SystemEnums.AgentType.ADMIN.getCode().equals(agentTypeCd)) orderHpnCd = ErpApiEnums.ErpOrderHpnCd.MANAGER.getCode(); // 관리자
		else if(SystemEnums.AgentType.PC.getCode().equals(agentTypeCd)) { // 웹
			if(UserEnums.BuyerType.USER.getCode().equals(buyerTypeCd) || UserEnums.BuyerType.GUEST.getCode().equals(buyerTypeCd)) orderHpnCd = ErpApiEnums.ErpOrderHpnCd.WEB_CUSTOMER.getCode(); // 고객
			else if(UserEnums.BuyerType.EMPLOYEE.getCode().equals(buyerTypeCd) || UserEnums.BuyerType.EMPLOYEE_BASIC.getCode().equals(buyerTypeCd)) orderHpnCd = ErpApiEnums.ErpOrderHpnCd.WEB_EMPLOYEE.getCode(); // 임직원
		} else if(SystemEnums.AgentType.MOBILE.getCode().equals(agentTypeCd) || SystemEnums.AgentType.APP.getCode().equals(agentTypeCd)) { // 모바일, 앱
			if(UserEnums.BuyerType.USER.getCode().equals(buyerTypeCd) || UserEnums.BuyerType.GUEST.getCode().equals(buyerTypeCd)) orderHpnCd = ErpApiEnums.ErpOrderHpnCd.WEB_CUSTOMER.getCode(); // 고객
			else if(UserEnums.BuyerType.EMPLOYEE.getCode().equals(buyerTypeCd) || UserEnums.BuyerType.EMPLOYEE_BASIC.getCode().equals(buyerTypeCd)) orderHpnCd = ErpApiEnums.ErpOrderHpnCd.WEB_EMPLOYEE.getCode(); // 임직원
		}

		return orderHpnCd;
	}

	/**
	 * 베이비밀 일일배송 주문 배달방법
	 * @param lineItem
	 * @return String
	 * @throws
	 */
	public static String getDlvMt(BabymealOrderListVo lineItem) {

		String onDlvCd = "";
		if("Y".equals(lineItem.getDailyBulkYn())) onDlvCd = ErpApiEnums.ErpBabymealDlvMt.BATCH_DELIVERY.getCode(); // 일괄배송 "0"
		else{ // 일일배송
			if(GoodsEnums.GoodsCycleType.DAYS3_PER_WEEK.getCode().equals(lineItem.getGoodsCycleTp())) onDlvCd = ErpApiEnums.ErpBabymealDlvMt.DAYS3_PER_WEEK.getCode(); // 주3회(월수금) : 0002
			else if(GoodsEnums.GoodsCycleType.DAYS6_PER_WEEK.getCode().equals(lineItem.getGoodsCycleTp())) onDlvCd = ErpApiEnums.ErpBabymealDlvMt.DAYS6_PER_WEEK.getCode(); // 매일(월~금,금2개) : 00013
			else if(GoodsEnums.GoodsCycleType.DAYS7_PER_WEEK.getCode().equals(lineItem.getGoodsCycleTp())) onDlvCd = ErpApiEnums.ErpBabymealDlvMt.DAYS7_PER_WEEK.getCode(); // 매일(월~금,금3개) : 0003
		}

		return onDlvCd;
	}

	/**
	 * 주문출처
	 * @param supplierCd
	 * @param psWarehouseId
	 * @return String
	 * @throws
	 */
	public static String getOrdSrc(String supplierCd, String psWarehouseId) {
		String ordSrc;

		String calcType = ErpApiEnums.ErpCalcType.SLASE.getCode();
		if(!"".equals(StringUtil.nvl(supplierCd))) calcType = supplierCd.split(Constants.ARRAY_SEPARATORS)[1];

		// 용인물류 또는 백암물류여부
		if(ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode().equals(psWarehouseId) || ErpApiEnums.UrWarehouseId.BAEKAM_LOGISTICS.getCode().equals(psWarehouseId)){
			ordSrc = ErpApiEnums.ErpCalcType.SUPPLY.getCode().equals(calcType) // 공급가 기준이면
					 ? ErpApiEnums.ErpFoodOrderSource.OUTMALL.getCode() // 외부몰
					 : ErpApiEnums.ErpFoodOrderSource.PULMUONE_ESHOP.getCode(); // 풀무원이샵
		} else {
			ordSrc = ErpApiEnums.ErpCalcType.SUPPLY.getCode().equals(calcType) // 공급가 기준이면
					 ? ErpApiEnums.ErpFoodOrderSource.OUTMALL_DELIVERY.getCode() // 외부몰(직송)
					 : ErpApiEnums.ErpFoodOrderSource.PULMUONE_ESHOP_DELIVERY.getCode(); // 풀무원이샵(직송)
		}

		return ordSrc;
	}

	/**
	 * 반품매출 올가홀푸드 주문 주문/발주 연계 유형
	 * @param lineItem
	 * @return String
	 * @throws
	 */
	public static String getOrdPoTyp(ReturnSalesOrderListVo lineItem) {

		String ordPoTyp;
		if(!ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode().equals(lineItem.getPsWarehouseId())) ordPoTyp = ErpApiEnums.ErpOrderPoType.DIRECT_DELIVERY.getCode(); // 신지직송
		else {
			ordPoTyp = GoodsEnums.GoodsDeliveryType.DAWN.getCode().equals(lineItem.getGoodsDeliveryType())
					   ? ErpApiEnums.ErpOrderPoType.DAWN_DELIVERY.getCode() // 새벽
					   : ErpApiEnums.ErpOrderPoType.NORMAL_DELIVERY.getCode(); // 일반
		}

		return ordPoTyp;
	}

	/**
	 * 납품처 ID
	 * @param supplierCds
	 * @return String
	 * @throws
	 */
	public static int getShiToOrgId(String supplierCds) {

		int shiToOrgId = 0;

		if(!"".equals(StringUtil.nvl(supplierCds))) shiToOrgId = StringUtil.nvlInt(supplierCds.split(Constants.ARRAY_SEPARATORS)[0]);

		return shiToOrgId;
	}

	/**
	 * 풀무원식품 반품주문 사유
	 * @param claimNm
	 * @return String
	 * @throws
	 */
	public static String getFoodRtnRsnDes(String claimNm) {

		String rtnRsnDes = ErpApiEnums.ErpRtnRsnDes.BROKEN_PACKAGING.getCode(); // 포장파손
		if(ErpApiEnums.ErpFoodOrdTyp.TAKEOVER_REJECT.getCodeName().equals(claimNm)) rtnRsnDes = ErpApiEnums.ErpRtnRsnDes.CIRCULATION_PURCHASE.getCode(); // 인수거절 -> 유통 매입차이
		else if(ErpApiEnums.ErpFoodOrdTyp.STORE_RETURNS.getCodeName().equals(claimNm)) rtnRsnDes = ErpApiEnums.ErpRtnRsnDes.RETURN_ORDER.getCode(); // 매장반품 -> 반품

		return rtnRsnDes;
	}

	/**
	 * 전체주소 자르기
	 * @param name
	 * @return
	 */
	public static String getCutAddrAll(String name) {
		return StringUtil.getByteStr(name, 1, Constants.ERP_API_CUT_ADDR_ALL_MAX_BYTE_SIZE, 3, false);
	}

	/**
	 * 주소 자르기
	 * @param name
	 * @return
	 */
	public static String getCutAddr(String name) {
		return StringUtil.getByteStr(name, 1, Constants.ERP_API_CUT_ADDR_DETAIL_MAX_BYTE_SIZE, 3, false);
	}

	/**
	 * 주문자명, 수령자명 자르기
	 * @param name
	 * @return
	 */
	public static String getCutName(String name) {
		return StringUtil.getByteStr(name, 1, Constants.ERP_API_CUT_NAME_MAX_BYTE_SIZE, 3, false);
	}

	/**
	 * 기타주문 수령자명 자르기
	 * @param name
	 * @return
	 */
	public static String getCutDlvName(String name) {
		return StringUtil.getByteStr(name, 1, Constants.ERP_API_CUT_DLV_NAME_MAX_BYTE_SIZE, 3, false);
	}

	/**
	 * 이메일 자르기
	 * @param name
	 * @return
	 */
	public static String getCutEmail(String name) {
		return StringUtil.getByteStr(name, 1, Constants.ERP_API_CUT_EMAIL_MAX_BYTE_SIZE, 3, false);
	}

	/**
	 * CJ 주문 바이트 자르기
	 * @param name
	 * @return
	 */
	public static String getCjCutName(String name, int inByte) {
		return StringUtil.getByteStr(name, 1, inByte, 3, false);
	}

}