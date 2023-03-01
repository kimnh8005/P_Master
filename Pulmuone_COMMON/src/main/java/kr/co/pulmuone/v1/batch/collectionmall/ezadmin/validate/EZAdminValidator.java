package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.validate;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.StringUtil;

public class EZAdminValidator {

	/**
	 * 관리번호
	 * @param seq
	 * @throws BaseException
	 */
	public void validationSeq(int seq) throws BaseException{
		if(StringUtil.nvlInt(seq) == 0) {
			throw new BaseException("관리번호 없음",null,null);
		}
	}

	/**
	 * 상품 관리번호
	 * @param prd_seq
	 */
	public String validationProductSeq(int prd_seq) throws BaseException{
		String returnStr = "";
		if(StringUtil.nvlInt(prd_seq) == 0) {
			//throw new BaseException("상품 관리번호 없음",null,null);
			returnStr = "상품 관리번호 없음";
		}
		return returnStr;
	}

	/**
	 * 주문번호
	 * @param order_id
	 */
	public void validationOrderId(String order_id) throws BaseException{
		if(StringUtil.isEmpty(order_id)) {
			throw new BaseException("주문번호 없음",null,null);
		}
	}

	/**
	 * 판매처 상품코드
	 * @param shop_product_id
	 */
	public void validationShopProductId(String shop_product_id) throws BaseException{
		if(StringUtil.isEmpty(shop_product_id)) {
			throw new BaseException("판매처 상품코드 없음",null,null);
		}
	}

	/**
	 * 상품코드
	 * @param product_id
	 */
	public String validationProductId(String product_id) throws BaseException{
		String returnStr = "";
		if(StringUtil.isEmpty(product_id)) {
			//throw new BaseException("상품코드 없음",null,null);
			returnStr = "상품코드 없음";
		}
		return returnStr;
	}

	/**
	 * 주문자명
	 * @param order_name
	 */
	public String validationOrderName(String order_name) throws BaseException{
		String returnStr = "";
		if(StringUtil.isEmpty(order_name)) {
			// throw new BaseException("주문자명 없음",null,null);
			returnStr = "주문자명 오류";
		}
		return returnStr;
	}

	/**
	 * 주문자연락처
	 * @param order_mobile
	 */
	public void validationOrderMobile(String order_mobile) throws BaseException{
		if(StringUtil.isEmpty(order_mobile)) {
			throw new BaseException("주문자연락처 없음",null,null);
		}
	}

	/**
	 * 수취인명
	 * @param recv_name
	 */
	public void validationRecvName(String recv_name) throws BaseException{
		if(StringUtil.isEmpty(recv_name)) {
			throw new BaseException("수취인명 없음",null,null);
		}
	}

	/**
	 * 수취인연락처
	 * @param recv_mobile
	 */
	public void validationRecvMobile(String recv_mobile) throws BaseException{
		if(StringUtil.isEmpty(recv_mobile)) {
			throw new BaseException("수취인연락처 없음",null,null);
		}
	}

	/**
	 * 우편번호
	 * @param recv_zip
	 */
	public void validationRecvZip(String recv_zip) throws BaseException{
		if(StringUtil.isEmpty(recv_zip)) {
			throw new BaseException("우편번호 없음",null,null);
		}
	}

	/**
	 * 주소
	 * @param recv_address
	 */
	public void validationRecvAddress(String recv_address) throws BaseException{
		if(StringUtil.isEmpty(recv_address)) {
			throw new BaseException("주소 없음",null,null);
		}
	}

	/**
	 * 주문 수량
	 * @param qty
	 */
	public void validationQty(int qty) throws BaseException{
		if(StringUtil.nvlInt(qty) == 0) {
			throw new BaseException("주문수량 없음",null,null);
		}
	}

	/**
	 * 주문 판매금액
	 * @param amount
	 */
	public void validationAmount(int amount) throws BaseException{
		if(StringUtil.nvlInt(amount) == 0) {
			throw new BaseException("주문 판매금액 없음",null,null);
		}
	}
}
