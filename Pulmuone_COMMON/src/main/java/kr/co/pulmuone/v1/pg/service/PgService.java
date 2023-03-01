package kr.co.pulmuone.v1.pg.service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgServiceType;
import kr.co.pulmuone.v1.comm.mapper.pg.PgMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.pg.dto.vo.PgActiveRateVo;
import kr.co.pulmuone.v1.pg.service.inicis.service.InicisPgService;
import kr.co.pulmuone.v1.pg.service.kcp.service.KcpPgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200924   	 홍진영           최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PgService {

	@Value("${app.domain}")
	private String appDomain;

	private final PgMapper pgMapper;

	private Map<PgServiceType, PgAbstractService> pgServices = new HashMap<PgServiceType, PgAbstractService>();

	@Autowired
	private KcpPgService kcpPgService;

	@Autowired
	private InicisPgService inicisPgService;

	/**
	 * PG service 인스턴스 가지고 오기
	 *
	 * @param pgServiceType
	 * @return
	 * @throws Exception
	 */
	protected PgAbstractService<?, ?> getService(PgServiceType pgServiceType) throws Exception {
		if (pgServices.isEmpty()) {
			for (PgServiceType type : PgServiceType.values()) {
				if (type.getServiceName().equals(PgServiceType.KCP.getServiceName())) {
					pgServices.put(type, kcpPgService);
				} else if (type.getServiceName().equals(PgServiceType.INICIS.getServiceName())) {
					pgServices.put(type, inicisPgService);
				}
			}
		}
		return pgServices.get(pgServiceType);
	}

	/**
	 * PG service 인스턴스 가지고 오기 (확률 - 결제타입, 카드코드)
	 *
	 * @param paymentType
	 * @param cardCode
	 * @return
	 * @throws Exception
	 */
	protected PgAbstractService<?, ?> getService(PaymentType paymentType, String cardCode) throws Exception {
		PgActiveRateVo pgActiveRateVo = getPgActiveRate(paymentType.getCode());
		PgServiceType pgServiceType = PgServiceType.getProbabilityChoiceService(pgActiveRateVo.getKcpRate(), pgActiveRateVo.getInicisRate());
		String pgBankCode = "";

		if (paymentType.equals(PaymentType.CARD) && StringUtil.isNotEmpty(cardCode)) {
			// 카드 코드
			pgBankCode = getPgBankCode(pgServiceType.getCode(), paymentType.getCode(), cardCode);

			// 은행을 사용안한다면 다른 PG로 재시도
			if (pgBankCode == null) {
				if (PgServiceType.KCP.getCode().equals(pgServiceType.getCode())) {
					pgServiceType = PgServiceType.INICIS;
				} else {
					pgServiceType = PgServiceType.KCP;
				}
				pgBankCode = getPgBankCode(pgServiceType.getCode(), paymentType.getCode(), cardCode);
				if (pgBankCode == null) {
					pgBankCode = "";
				}
			}
		}

		return getService(pgServiceType);
	}

	/**
	 * 결제 성공시 이동 경로
	 *
	 * @return
	 */
	protected String redirectOrderCompleted() {
		return "redirect:" + appDomain + "/shop/orderCompleted";
	}

	/**
	 * 결제 성공시 이동
	 *
	 * @param odid
	 * @param cartType
	 * @param psPayCd
	 * @return
	 */
	protected String orderSuccess(String odid, String cartType, String psPayCd, String presentYn) {
		return redirectOrderCompleted() + "?odid=" + odid + "&cartType=" + cartType + "&psPayCd=" + psPayCd + "&presentYn=" + presentYn;
	}

	/**
	 * 결제 실패시 이동
	 *
	 * @param errorCode
	 * @param errorMessage
	 * @return
	 * @throws Exception
	 */
	protected String orderFail(String errorCode, String errorMessage) throws Exception {
		return redirectOrderCompleted() + "?errorCode=" + errorCode + "&errorMessage="
				+ URLEncoder.encode(errorMessage);
	}

	/**
	 * 결제 실패시 이동 특정 페이지 이동
	 *
	 * @param returnUrl
	 * @return
	 * @throws Exception
	 */
	protected String orderFail(String returnUrl) throws Exception {
		return "redirect:" + returnUrl;
	}

	/**
	 * 클래임 성공시 이동
	 *
	 * @param claimId
	 * @return
	 * @throws Exception
	 */
	protected String claimSuccess(long claimId) throws Exception {
		return "redirect:" + appDomain + "/mypage/cancelDetail?claim=" + claimId;
	}

	/**
	 * 클래임 실패시 이동
	 *
	 * @param errorCode
	 * @param errorMessage
	 * @return
	 * @throws Exception
	 */
	protected String claimFail(String errorCode, String errorMessage) throws Exception {
		return "redirect:" + appDomain + "/order/claim/claimCompleted" + "?errorCode=" + errorCode + "&errorMessage=" + URLEncoder.encode(errorMessage);
	}

	/**
	 * PG사 은행,카드사 코드 조회
	 *
	 * @param pgServiceTypeCode
	 * @param paymentTypeCode
	 * @param bankNameCode
	 * @return
	 * @throws Exception
	 */
	protected String getPgBankCode(String pgServiceTypeCode, String paymentTypeCode, String bankNameCode)
			throws Exception {
		return StringUtil.nvl(pgMapper.getPgBankCode(pgServiceTypeCode, paymentTypeCode, bankNameCode));
	}

	/**
	 * 가상계좌 입금 시간
	 *
	 * @return
	 * @throws Exception
	 */
	protected String getVirtualAccountDateTime() throws Exception {
		return DateUtil.getCurrentDate() + "235959";
	}

	/**
	 * PG 결제 종류에 따른 확률 조회
	 *
	 * @param psPayCd
	 * @return
	 * @throws Exception
	 */
	protected PgActiveRateVo getPgActiveRate(String psPayCd) throws Exception {
		return pgMapper.getPgActiveRate(psPayCd);
	}

	/**
	 * PG사 은행, 카드사명 조회
	 *
	 * @param pgServiceTypeCode
	 * @param paymentTypeCode
	 * @param pgBankCode
	 * @return
	 * @throws Exception
	 */
	protected String getPgBankName(String pgServiceTypeCode, String paymentTypeCode, String pgBankCode) throws Exception {
		return pgMapper.getPgBankName(pgServiceTypeCode, paymentTypeCode, pgBankCode);
	}
}
