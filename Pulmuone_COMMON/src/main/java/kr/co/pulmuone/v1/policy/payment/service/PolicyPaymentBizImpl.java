package kr.co.pulmuone.v1.policy.payment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.PayEnums;
import kr.co.pulmuone.v1.comm.enums.PayEnums.InstallmentPeriod;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.policy.payment.dto.PayUseListDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentCardBenefitDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionRequestDto;

@Service
public class PolicyPaymentBizImpl implements PolicyPaymentBiz {

	@Autowired
	private PolicyPaymentService policyPaymentService;

    @Autowired
    private PolicyConfigBiz policyConfigBiz;

	/**
	 * 결제 정보 조회
	 */
	@Override
	public PayUseListDto getPayUseList() throws Exception {

		PayUseListDto payUseList = new PayUseListDto();

		//결제방법
		List<HashMap<String,String>> getPayUseList = policyPaymentService.getPayUseList();
		List<HashMap<String,String>> paymentType = new ArrayList<>();

		if(getPayUseList !=null && !getPayUseList.isEmpty()) {
			for(HashMap<String,String> map : getPayUseList) {
				String psConfigKey = PayEnums.PsPay.findPsConfigByDeviceType(map.get("psPayCd"), DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
				String psConfigValue = policyConfigBiz.getConfigValue(psConfigKey);
				if("Y".equals(psConfigValue)){
					paymentType.add(map);
				}
			}
		}
		payUseList.setPaymentType(paymentType);

		//카드정보
		List<HashMap<String,String>> cardList = policyPaymentService.getPayCardUseList();
		payUseList.setCardList(cardList);

		//할부기간
		List<HashMap<String,String>> installmentPeriod = new ArrayList<HashMap<String,String>>();
		for(InstallmentPeriod period : PayEnums.InstallmentPeriod.values()) {
			HashMap<String,String> code = new HashMap<String,String>();
			code.put("code", period.getCode());
			code.put("codeName", period.getCodeName());
			installmentPeriod.add(code);
		}
		payUseList.setInstallmentPeriod(installmentPeriod);

		//신용카드 혜택
		List<HashMap<String,String>> cartBenefit = policyPaymentService.getCartBenefitList();
		payUseList.setCartBenefit(cartBenefit);

		return payUseList;
	}

	/**
	 *제휴구분 PG 조회 DropDown
	 */
	@Override
	public ApiResult<?> getPaymentList(PolicyPaymentCardBenefitDto policyPaymentCardBenefitDto) throws Exception {
		return policyPaymentService.getPaymentList(policyPaymentCardBenefitDto);

	}

	/**
	 *제휴구분 결제수단 조회 DropDown
	 */
	@Override
	public ApiResult<?> getPaymentUseList(PolicyPaymentCardBenefitDto policyPaymentCardBenefitDto) throws Exception {
		return policyPaymentService.getPaymentUseList(policyPaymentCardBenefitDto);

	}

	/**
	 * 제휴구분 결제수단 상세 조회 DropDown
	 */
	@Override
	public ApiResult<?> getPayCardList(PolicyPaymentPromotionRequestDto policyPaymentPromotionRequestDto) throws Exception {
		return policyPaymentService.getPayCardList(policyPaymentPromotionRequestDto);
	}

}
