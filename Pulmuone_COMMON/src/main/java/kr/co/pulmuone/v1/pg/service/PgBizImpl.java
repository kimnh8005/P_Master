package kr.co.pulmuone.v1.pg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgServiceType;
import kr.co.pulmuone.v1.pg.dto.vo.PgActiveRateVo;

@Service
public class PgBizImpl implements PgBiz {

	@Autowired
	private PgService pgService;

	@Override
	public PgAbstractService<?, ?> getService(PgServiceType pgServiceType) throws Exception {
		return pgService.getService(pgServiceType);
	}

	@Override
	public PgAbstractService<?, ?> getService(PaymentType paymentType, String cardCode) throws Exception {
		return pgService.getService(paymentType, cardCode);
	}

	@Override
	public String orderSuccess(String odid, String cartType, String psPayCd, String presentYn) throws Exception {
		return pgService.orderSuccess(odid, cartType, psPayCd, presentYn);
	}

	@Override
	public String orderFail(String errorCode, String errorMessage) throws Exception {
		return pgService.orderFail(errorCode, errorMessage);
	}

	@Override
	public String orderFail(String returnUrl) throws Exception {
		return pgService.orderFail(returnUrl);
	}

	@Override
	public String claimSuccess(long claimId) throws Exception {
		return pgService.claimSuccess(claimId);
	}

	@Override
	public String claimFail(String errorCode, String errorMessage) throws Exception {
		return pgService.claimFail(errorCode, errorMessage);
	}

	@Override
	public String getPgBankCode(String pgServiceTypeCode, String paymentTypeCode, String bankNameCode)
			throws Exception {
		return pgService.getPgBankCode(pgServiceTypeCode, paymentTypeCode, bankNameCode);
	}

	@Override
	public String getVirtualAccountDateTime() throws Exception {
		return pgService.getVirtualAccountDateTime();
	}

	@Override
	public PgActiveRateVo getPgActiveRate(String psPayCd) throws Exception {
		return pgService.getPgActiveRate(psPayCd);
	}

	@Override
	public String getPgBankName(String pgServiceTypeCode, String paymentTypeCode, String pgBankCode) throws Exception {
		return pgService.getPgBankName(pgServiceTypeCode, paymentTypeCode, pgBankCode);
	}
}
