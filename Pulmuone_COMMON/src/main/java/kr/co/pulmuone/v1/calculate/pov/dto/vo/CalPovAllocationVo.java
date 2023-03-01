package kr.co.pulmuone.v1.calculate.pov.dto.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.util.BigDecimalUtils;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * POV I/F 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@ToString
@ApiModel(description = "POV I/F CA_POV_ALLOCATION VO")
public class CalPovAllocationVo implements Serializable {

	public static final String DEFAULT_CODE = "0000000";
	public static final int SCALE = 33;

	private String scenario;
	private String allocationType;
	private String year;
	private String month;
	private String corporationCode;
	private String channelCode;
	private String skuCode;
	private String accountCode;
	private BigDecimal cost;
	private String creator;
	private Date loadTime;
	private String factoryCode;

	public CalPovAllocationVo(String scenario, String allocationType, String year, String month, String corporationCode,
			String channelCode, String skuCode, String accountCode, BigDecimal cost, String creator,
			String factoryCode) {
		this.scenario = scenario;
		this.allocationType = allocationType;
		this.year = year;
		this.month = month;
		this.corporationCode = corporationCode;
		this.channelCode = emptyToDefaultCode(channelCode);
		this.skuCode = emptyToDefaultCode(skuCode);
		this.accountCode = emptyToDefaultCode(accountCode);
		this.cost = BigDecimalUtils.nvl(cost).setScale(SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
		this.creator = creator;
		this.loadTime = new Date();
		this.factoryCode = emptyToDefaultCode(factoryCode);
	}

	private String emptyToDefaultCode(String s) {
		return StringUtils.isNotEmpty(s) ? s : DEFAULT_CODE;
	}

}
