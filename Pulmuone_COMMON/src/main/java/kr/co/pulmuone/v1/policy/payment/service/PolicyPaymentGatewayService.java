package kr.co.pulmuone.v1.policy.payment.service;

import java.util.ArrayList;
import java.util.List;

import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.comm.mapper.policy.payment.PolicyPaymentGatewayMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.pg.service.inicis.service.InicisConfig;
import kr.co.pulmuone.v1.pg.service.kcp.service.KcpConfig;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentGatewayDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentCardBenefitVo;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentGatewayMethodVo;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentGatewayVo;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0		20201019		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */

@Service
@RequiredArgsConstructor
public class PolicyPaymentGatewayService {

	@Autowired
	KcpConfig kcpConfig;

	@Autowired
	InicisConfig inicisConfig;

	@Autowired
	private final PolicyPaymentGatewayMapper policyPaymentGatewayMapper;

	/**
     * PG사 기본설정 정보 목록 조회
     *
     * @param
     * @return PolicyPaymentGatewayDto
     */
    protected PolicyPaymentGatewayDto getPolicyPaymentGatewayList() {
    	PolicyPaymentGatewayDto result = new PolicyPaymentGatewayDto();
		List<PolicyPaymentGatewayVo> rows = policyPaymentGatewayMapper.getPolicyPaymentGatewayList();
		for(PolicyPaymentGatewayVo vo : rows) {
			if("PG_SERVICE.KCP".equals(vo.getPsPgCd())){
				/**
				 * to-do:Logo 의 저장 위치가 정해지면 그때 입력 필요.
		    	 */
				//vo.setPgLogo(""); //데이터 없음
				vo.setPgSiteCode(kcpConfig.getG_conf_site_cd());
				vo.setPgSiteKey(kcpConfig.getG_conf_site_key());
				vo.setPgBatchPayInfo(""); //데이터 없음
			}else if("PG_SERVICE.INICIS".equals(vo.getPsPgCd())){
				//vo.setPgLogo(""); //데이터 없음
				vo.setPgSiteCode(inicisConfig.getMid());
				vo.setPgSiteKey(inicisConfig.getKey());
				vo.setPgBatchPayInfo(""); //데이터 없음
			}
        }
		result.setRows(rows);
		return result;
    }

    /**
     * PG사 이중화 비율 정보 목록 조회
     *
     * @param
     * @return PolicyPaymentGatewayDto
     */
    protected PolicyPaymentGatewayDto getPolicyPaymentGatewayRatioList() {
    	PolicyPaymentGatewayDto result = new PolicyPaymentGatewayDto();
    	/**
    	 * to-do:현재 comm.kendo.min.js 에서 getData 시 rows 로 return 사용. 해당 js 수정이 필요해보임.
    	 * 아니면 직접 ajax call 필요
    	 */
    	List<PolicyPaymentGatewayVo> rows = policyPaymentGatewayMapper.getPolicyPaymentGatewayRatioList();
    	result.setRows(rows);
    	return result;
    }
    /**
     * PG 결제수단 관리 목록 조회
     *
     * @param
     * @return PolicyPaymentGatewayDto
     */
    protected PolicyPaymentGatewayDto getPolicyPaymentGatewayMethodList(String psPayCd) {
    	PolicyPaymentGatewayDto result = new PolicyPaymentGatewayDto();
    	List<PolicyPaymentGatewayMethodVo> rows = policyPaymentGatewayMapper.getPolicyPaymentGatewayMethodList(psPayCd);
    	result.setPolicyPaymentGatewayMethodList(rows);
    	return result;
    }

    /**
     * PG사 설정 정보 수정
     *
     * @param PolicyPaymentGatewayDto
     * @return int
     */
    protected ApiResult<?> putPolicyPaymentGateway(PolicyPaymentGatewayDto dto) {
    	int result = policyPaymentGatewayMapper.putPolicyPaymentGateway(dto);
    	if(result > 0) {
    		List<PolicyPaymentGatewayVo> list = dto.getRows();
    		List<PolicyPaymentGatewayVo> rows = new ArrayList<PolicyPaymentGatewayVo>();
            for(PolicyPaymentGatewayVo vo : list) {
            	PolicyPaymentGatewayVo row = new PolicyPaymentGatewayVo();
            	row.setPsPgCd("PG_SERVICE.KCP");
            	row.setPsPayCd(vo.getPsPayCd());
            	row.setUseRatio(vo.getUseRatioKcp());
            	rows.add(row);

            	row = new PolicyPaymentGatewayVo();
            	row.setPsPgCd("PG_SERVICE.INICIS");
            	row.setPsPayCd(vo.getPsPayCd());
            	row.setUseRatio(vo.getUseRatioInicis());
            	rows.add(row);
            }
            dto.setRows(rows);
            int cnt = policyPaymentGatewayMapper.putPolicyPaymentGatewayRatio(dto);
            if(cnt < 1) return ApiResult.result(PolicyEnums.Payment.UPDATE_FAIL);
    	}else return ApiResult.result(PolicyEnums.Payment.UPDATE_FAIL);
    	return ApiResult.success();
    }
    /**
     * PG 결제수단 정보 수정
     *
     * @param PolicyPaymentGatewayDto
     * @return int
     */
    protected int putPolicyPaymentGatewayMethod(PolicyPaymentGatewayDto dto) {
    	return policyPaymentGatewayMapper.putPolicyPaymentGatewayMethod(dto);
    }

	/**
	 * PG 가상계좌 결제은행 목록 조회
	 */
	protected ApiResult<?> getPgBankCodeList(PolicyPaymentPromotionRequestDto dto) {
		PolicyPaymentGatewayDto result = new PolicyPaymentGatewayDto();
		List<PolicyPaymentGatewayVo> rows = policyPaymentGatewayMapper.getPgBankCodeList(dto);
		result.setRows(rows);
		return ApiResult.success(result);
	}

}
