package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * <PRE>
 * Forbiz Korea
 * Java 에서 코드성 값을 사용해야 할때 여기에 추가해서 사용
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0		20210114		박승현              최초작성
 * =======================================================================
 * </PRE>
 */
public class ApprovalEnums
{

	//승인권한 관리
	//승인권한 유형
    @Getter
    @RequiredArgsConstructor
    public enum ApprovalAuthType implements CodeCommEnum {
    	APPR_MANAGER_TP_1ST("APPR_MANAGER_TP.FIRST", "1차 승인관리자"),
    	APPR_MANAGER_TP_2ND("APPR_MANAGER_TP.SECOND", "2차 승인관리자"),
    	APPR_KIND_TP_ITEM_REG("APPR_KIND_TP.ITEM_REGIST", "품목등록 승인관리"),
    	APPR_KIND_TP_GOODS_REG("APPR_KIND_TP.GOODS_REGIST", "상품등록 승인관리"),
    	APPR_KIND_TP_ITEM_PRICE("APPR_KIND_TP.ITEM_PRICE", "품목가격 승인관리"),
    	APPR_KIND_TP_GOODS_DISCOUNT("APPR_KIND_TP.GOODS_DISCOUNT", "상품할인 승인관리"),
    	APPR_KIND_TP_EXHIBIT_SELECT("APPR_KIND_TP.EXHIBIT_SELECT", "골라담기(균일가) 승인관리"),
    	APPR_KIND_TP_EXHIBIT_GIFT("APPR_KIND_TP.EXHIBIT_GIFT", "증정행사 승인관리"),
    	APPR_KIND_TP_COUPON("APPR_KIND_TP.COUPON", "쿠폰 승인관리"),
    	APPR_KIND_TP_POINT("APPR_KIND_TP.POINT", "적립금 승인관리"),
    	APPR_KIND_TP_CS_REFUND("APPR_KIND_TP.CS_REFUND", "CS환불 승인관리"),
    	APPR_KIND_TP_ITEM_CLIENT("APPR_KIND_TP.ITEM_CLIENT", "거래처 품목 승인관리"),
    	APPR_KIND_TP_GOODS_CLIENT("APPR_KIND_TP.GOODS_CLIENT", "거래처 상품 승인관리");

        private final String code;
        private final String codeName;
    }

    //승인 상태
    @Getter
    @RequiredArgsConstructor
    public enum ApprovalStatus implements CodeCommEnum {
    	SAVE("APPR_STAT.SAVE", "저장"),
    	NONE("APPR_STAT.NONE", "승인대기"),
        REQUEST("APPR_STAT.REQUEST", "승인요청"),
        CANCEL("APPR_STAT.CANCEL", "요청철회"),
        DENIED("APPR_STAT.DENIED", "승인반려"),
        SUB_APPROVED("APPR_STAT.SUB_APPROVED", "승인완료(부)"),
        APPROVED("APPR_STAT.APPROVED", "승인완료(정)"),
        APPROVED_BY_SYSTEM("APPR_STAT.APPROVED_BY_SYSTEM", "승인완료(시스템)"),
        DISPOSAL("APPR_STAT.DISPOSAL", "폐기");

        private final String code;
        private final String codeName;

		public static ApprovalEnums.ApprovalStatus findByCode(String code) {
			return Arrays.stream(ApprovalEnums.ApprovalStatus.values())
					.filter(approvalStatus -> approvalStatus.getCode().equals(code))
					.findAny()
					.orElse(null);

		}
    }

  	//승인 검증
    @Getter
    @RequiredArgsConstructor
    public enum ApprovalValidation implements MessageCommEnum {
    	SUB_APPROVABLE("SUB_APPROVABLE", "1차 승인가능한 상태입니다"),
    	APPROVABLE("APPROVABLE", "최종 승인가능한 상태입니다."),
    	NOTAPPROVABLE("APPROVABLE", "승인 불가능한 상태입니다."),
    	CANCELABLE("CANCELABLE", " 승인철회 가능한 상태입니다."),
    	UNCANCELABLE("UNCANCELABLE", " 승인철회 불가능한 상태입니다."),
    	DISPOSABLE("DISPOSABLE", " 폐기 가능한 상태입니다."),
    	UNDISPOSABLE("UNDISPOSABLE", " 폐기 불가능한 상태입니다."),
    	AUTH_DENIED("AUTH_DENIED", "허가되지 않은 권한입니다."),
    	NONE_REQUEST("NONE_REQUEST", "승인요청 상태가 없습니다."),
    	ALREADY_APPROVAL_REQUEST("ALREADY_APPROVAL_REQUEST", "이미 승인요청 중인 상태입니다. 승인요청중인 정보는 수정이 불가합니다."),
    	ALREADY_APPROVED("ALREADY_APPROVED", "이미 승인이 완료되어 철회가 불가능합니다."),
    	ALREADY_DENIED("ALREADY_DENIED", "이미 승인이 반려되었습니다. 반려 정보를 확인해 주세요."),
    	ALREADY_CANCEL_REQUEST("ALREADY_CANCEL_REQUEST", "승인요청자가 승인요청을 철회하였습니다."),
		DUPLICATE_APPROVAL_REQUEST("DUPLICATE_APPROVAL_REQUEST", "상품 품목 중복 승인 요청중입니다. 중복 승인요청중인 정보는 수정이 불가합니다."),
    	REQUIRED_APPROVAL_USER("REQUIRED_APPROVAL_USER", "최종승인관리자는 1명이상 등록되거나 정상상태이어야 합니다."),
    	REQUEST_ALREADY_APPROVED("REQUEST_ALREADY_APPROVED", "이미 승인 처리 하였습니다."),
		HAS_ITEM_PRICE_APPR("HAS_ITEM_PRICE_APPR", "품목 가격정보가 승인요청 중인 상태입니다. 가격 승인 요청중 할인 설정이 불가합니다."),
		HAS_GOODS_DISCOUNT_APPR("HAS_GOODS_DISCOUNT_APPR", "상품 할인정보 승인요청 중인 상태입니다. 할인 승인 요청중 할인 설정이 불가합니다.");

    	private final String code;
    	private final String message;
    }

}