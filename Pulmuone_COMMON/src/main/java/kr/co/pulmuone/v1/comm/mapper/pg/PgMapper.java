package kr.co.pulmuone.v1.comm.mapper.pg;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.pg.dto.vo.PgActiveRateVo;

@Mapper
public interface PgMapper {

	String getPgBankCode(@Param("pgServiceTypeCode") String pgServiceTypeCode, @Param("paymentTypeCode") String paymentTypeCode, @Param("bankNameCode") String bankNameCode);

	PgActiveRateVo getPgActiveRate(String psPayCd);

	String getPgBankName(@Param("pgServiceTypeCode") String pgServiceTypeCode, @Param("paymentTypeCode") String paymentTypeCode, @Param("pgBankCode") String pgBankCode);
}
