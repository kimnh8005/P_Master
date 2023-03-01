package kr.co.pulmuone.v1.calculate.pov.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalPovCostSummaryDto {

	String corporationCode;

	String corporationName;

	BigDecimal tempMeCost;

	BigDecimal tempOvCost;

	BigDecimal tempVdcCost;

	BigDecimal finalMeCost;

	BigDecimal finalOvCost;

	BigDecimal finalVdcCost;

	BigDecimal diffMeCost;

	BigDecimal diffOvCost;

	BigDecimal diffVdcCost;

	BigDecimal tempMogeCost;

	BigDecimal finalMogeCost;

	BigDecimal diffMogeCost;
}
