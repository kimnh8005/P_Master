package kr.co.pulmuone.v1.pg.service;

import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgServiceType;
import kr.co.pulmuone.v1.pg.dto.vo.PgActiveRateVo;

public interface PgBiz {

	PgAbstractService<?, ?> getService(PgServiceType pgServiceType) throws Exception;

	PgAbstractService<?, ?> getService(PaymentType paymentType, String cardCode) throws Exception;

	String orderSuccess(String odid, String cartType, String psPayCd, String presentYn) throws Exception;

	String orderFail(String errorCode, String errorMessage) throws Exception;

	String orderFail(String returnUrl) throws Exception;

	String claimSuccess(long claimId) throws Exception;

	String claimFail(String errorCode, String errorMessage) throws Exception;

	String getPgBankCode(String pgServiceTypeCode, String paymentTypeCode, String bankNameCode) throws Exception;

	String getVirtualAccountDateTime() throws Exception;

	PgActiveRateVo getPgActiveRate(String psPayCd) throws Exception;

	String getPgBankName(String pgServiceTypeCode, String paymentTypeCode, String pgBankCode) throws Exception;
}
