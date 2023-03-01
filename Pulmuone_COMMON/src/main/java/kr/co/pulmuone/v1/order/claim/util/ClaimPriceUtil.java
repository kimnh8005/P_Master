package kr.co.pulmuone.v1.order.claim.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClaimPriceUtil {

	/**
	 * 전체 금액대비 비율 계산 처리
	 * @param totalPrice
	 * @param targetCnt
	 * @param totalCnt
	 * @return
	 */
	public int getFloorRatePrice(int totalPrice, int targetCnt, int totalCnt) {
		int resultPrice = 0;
		if(targetCnt < 1) {
			return resultPrice;
		}
		// totalPrice : 계산 대상 총금액
		// totalCnt : 계산 대상 총 수량
		// targetCnt : 계산하려고 하는 수량
		// (총금액 / 총수량 = 수량 1개당 수량)을 구하여 계산하고자 하는 수량을 곱한다.
		// ex) 주문금액 = 10000, 주문수량 = 10, 클레임수량 = 2
		//     (int) Math.floor(2 * (10000 / (double) 10));
		resultPrice = (int) Math.floor(targetCnt * (totalPrice / (double) totalCnt));
		return resultPrice;
	}
}
