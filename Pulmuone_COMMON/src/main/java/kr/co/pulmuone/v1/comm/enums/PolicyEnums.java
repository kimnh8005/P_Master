package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
 *  1.0		20201012		박승현              최초작성
 * =======================================================================
 * </PRE>
 */
public class PolicyEnums
{

	// 결제 정책 관리
	@Getter
	@RequiredArgsConstructor
	public enum Payment implements MessageCommEnum
	{
		DUPLICATE_TERM("DUPLICATE_TERM", "해당 기간에 진행중인 신용카드 혜택이 있습니다.")/**/
		, UPDATE_FAIL("CARD_BENEFIT_UPDATE_FAIL", "저장에 실패하였습니다.")
		;

		private final String code;
		private final String message;
	}

	//정책 Key
	@Getter
	@RequiredArgsConstructor
	public enum PolicyKey implements CodeCommEnum {
		OD_CART_MAINTENANCE_PERIOD("OD_CART_MAINTENANCE_PERIOD", "장바구니 유지 기간"),
		ST_F_TITLE("ST_F_TITLE", "타이틀"),
		ST_SCH_EG_KEYWORD("ST_SCH_EG_KEYWORD","검색엔진키워드"),
		ST_DESCRIPTION("ST_DESCRIPTION","시스템 설명"),
		FAVICON_IMAGE_FILE_PATH("FAVICON_IMAGE_FILE_PATH","Favicon 이미지 파일 경로"),
		FAVICON_IMAGE_FILE_NAME("FAVICON_IMAGE_FILE_NAME","Favicon 이미지 파일명"),
		FAVICON_IMAGE_FILE_ORIG_NAME("FAVICON_IMAGE_FILE_ORIG_NAME","Favicon 이미지 원본 파일명")
		;

		private final String code;
		private final String codeName;
	}

	// 택배사 설정. 외부몰 코드
	@Getter
	@RequiredArgsConstructor
	public enum Outmallcode implements CodeCommEnum {
		E("E", "이지어드민"),
		S("S", "사방넷"),
		;

		private final String code;
		private final String codeName;
	}

	// BOS 클레임 사유 카테고리
	@Getter
	@RequiredArgsConstructor
	public enum ClaimCategoryCode implements CodeCommEnum {
		L_CLAIM("10", "클레임 사유(대)"),
		M_CLAIM("20", "클레임 사유(중)"),
		S_CLAIM("30", "귀책처")
		;

		private final String code;
		private final String codeName;
	}

	// 클레임 사유 입력 메세지 코드
	@Getter
	@RequiredArgsConstructor
	public enum ClaimMessage implements MessageCommEnum
	{
		DUPLICATE_REASON("DUPLICATE_REASON", "이미 등록된 사유 입니다."),
		FOREIGN_KEY_DATA("LOCAL_DEFINE_FOREIGN_KEY_DATA", "사용 중인 항목은 삭제할 수 없습니다.")
		;

		private final String code;
		private final String message;
	}

	// 기본 수수료관리 메세지 코드
	@Getter
	@RequiredArgsConstructor
	public enum OmBasicFeeMessage implements MessageCommEnum
	{
		DUPLICATE_START_DATE("DUPLICATE_START_DATE", "이미 등록 된 시작일자 입니다."),
		;

		private final String code;
		private final String message;
	}
}