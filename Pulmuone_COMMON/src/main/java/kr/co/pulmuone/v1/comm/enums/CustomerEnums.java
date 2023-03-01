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
 *  1.0    2020. 12. 14.     안치열          최초작성
 * =======================================================================
 * </PRE>
 */

public class CustomerEnums {

	//공지채널타입
    @Getter
    @RequiredArgsConstructor
    public enum NoticeChannelType implements CodeCommEnum {
        PC("NOTICE_CHANNEL.PC","PC"),
        MOBILE("NOTICE_CHANNEL.MOBILE","MOBILE");

        private final String code;
        private final String codeName;
    }

    // FAQ 노출순서 초기값
    @Getter
    @RequiredArgsConstructor
    public enum ViewSOrt implements CodeCommEnum{
    	VIEW_SORT("999", "초기설정값")
    	;

        private final String code;
        private final String codeName;

    }


    // 고객센터>통합몰 관리 >답변 템플릿
 	@Getter
 	@RequiredArgsConstructor
 	public enum AnswerTempType implements MessageCommEnum
 	{
 		 ANSWER("CUSTOMER_QNA.ANSWER","안녕하세요 고객님.<br/> #풀무원 고객기쁨센터입니다. <br/><br/> 늘 건강하시길 바라며, 감사합니다.<br/> #풀무원 담당자 드림.");
 		//ANSWER_TEAM("CUSTOMER_QNA.ANSWER_TEAM","팀 "),
     	//ANSWER_CMT("CUSTOMER_QNA.ANSWER_CMT"," 입니다.<br/><br/> 늘 건강하시길 바라며, 감사합니다.<br/> #풀무원 담당자 드림.");

 		private final String code;
 		private final String message;
 	}

    // 고객센터 > 고객보상 > 보상제 처리 신청상태
    @Getter
    @RequiredArgsConstructor
    public enum RewardApplyStatus implements CodeCommEnum {
        ACCEPT("REWARD_APPLY_STATUS.ACCEPT","접수"),
        CONFIRM("REWARD_APPLY_STATUS.CONFIRM","확인중"),
        COMPLETE("REWARD_APPLY_STATUS.COMPLETE","보상완료"),
        IMPOSSIBLE("REWARD_APPLY_STATUS.IMPOSSIBLE","보상불가");

        private final String code;
        private final String codeName;
    }

    // 고객센터 > 고객보상 > 보상제 처리 메시지
    @Getter
    @RequiredArgsConstructor
    public enum RewardApply implements MessageCommEnum {
        NO_CS_REWARD("NO_CS_REWARD","고객보상제 없음"),
        ALREADY_APPLY("ALREADY_APPLY","기 등록건"),
        APPLY_CONFIRM("APPLY_CONFIRM","관리자 확인중"),
        APPLY_DONE("APPLY_DONE","관리자 처리 완료"),
        NOT_USER("NOT_USER","해당 유저 아님");

        private final String code;
        private final String message;
    }

    // 고객센터 > 고객보상 > 보상제 상품정보
    @Getter
    @RequiredArgsConstructor
    public enum RewardApplyStandard implements CodeCommEnum {
        ORDER_NUMBER("REWARD_APPLY_STANDARD.ORDER_NUMBER","주문번호"),
        ORDER_GOODS("REWARD_APPLY_STANDARD.ORDER_GOODS","주문상품"),
        PACK_DELIVERY("REWARD_APPLY_STANDARD.PACK_DELIVERY","합배송"),
        NONE("REWARD_APPLY_STANDARD.NONE","해당 없음");

        private final String code;
        private final String codeName;
    }

    // 고객센터 > 고객보상 > 보상제 Validation 구분
    @Getter
    @RequiredArgsConstructor
    public enum RewardValidationType implements CodeCommEnum {
        NORMAL("NORMAL","일반"),
        PUT_DEL_YN("PUT_DEL_YN","삭제처리");

        private final String code;
        private final String codeName;
    }

    // 고객센터 > 고객보상 > 보상제 상품유형 구분
    @Getter
    @RequiredArgsConstructor
    public enum RewardGoodsType implements CodeCommEnum {
        ALL("ALL","전체"),
        TARGET_GOODS("TARGET_GOODS","선택상품");

        private final String code;
        private final String codeName;
    }

}
