package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class EventEnums {
    //이벤트 등록 검증 유형
    @Getter
    @RequiredArgsConstructor
    public enum EventValidationType implements CodeCommEnum {
        GET("GET", "조회"),
        ADD("ADD", "등록");

        private final String code;
        private final String codeName;
    }

    // 이벤트이미지유형
    @Getter
    @RequiredArgsConstructor
    public enum EventImgTp implements CodeCommEnum {
        NOT_USE("EVENT_IMG_TP.NOT_USE", "사용안함")
        , BG("EVENT_IMG_TP.BG"          , "배경컬러")
        ;
        private final String code;
        private final String codeName;
    }

    // 적용대상유형
    @Getter
    @RequiredArgsConstructor
    public enum GiftTargetTp implements CodeCommEnum {
        GOODS("GIFT_TARGET_TP.GOODS", "상품")
        , BRAND("GIFT_TARGET_TP.BRAND", "브랜드")
        ;
        private final String code;
        private final String codeName;
    }

    //이벤트 등록 검증
    @Getter
    @RequiredArgsConstructor
    public enum EventValidation implements MessageCommEnum {
        NO_EVENT("NO_EVENT", "이벤트 정보 없음"),
        NOT_DATE("NOT_DATE", "이벤트 기간 아님"),
        NOT_EMPLOYEE("NOT_EMPLOYEE", "임직원 참여 불가"),
        ONLY_EMPLOYEE("ONLY_EMPLOYEE", "임직원 전용 이벤트"),
        NOT_DEVICE("NOT_DEVICE", "디바이스 다름"),
        NOT_DEVICE_APP_ONLY_EVENT("NOT_DEVICE_APP_ONLY_EVENT", "앱전용 이벤트 디바이스 아님"),
        NOT_GROUP_NONE("NOT_GROUP_NONE", "비회원"),
        NOT_AVAILABLE("NOT_AVAILABLE", "이벤트 참여 제한"),
        ALREADY_JOIN_DATE("ALREADY_JOIN_DATE", "1일1회 중복 참여"),
        ALREADY_JOIN_EVENT("ALREADY_JOIN_EVENT", "1이벤트1회 중복 참여"),
        EMPTY_COMMENT_VALUE("EMPTY_COMMENT_VALUE", "댓글 구분값 없음"),
        NOT_GROUP("NOT_GROUP", "등급 오류"),
        FIRST_COME_CLOSE("FIRST_COME_CLOSE", "선착순 종료"),
        FULL_BENEFIT("FULL_BENEFIT", "잔여 당첨 인원 없음"),
        NOT_REMAIN_BENEFIT("NOT_REMAIN_BENEFIT", "잔여 혜택 없음"),
        NEED_ORDER("NEED_ORDER", "주문 정보 부족"),
        SERVICEABLE("SERVICEABLE","진행 중인 이벤트"),
        NOT_NEW_USER("NOT_NEW_USER", "신규 회원 아님"),
        FULL_COUPON("FULL_COUPON", "선택한 쿠폰 잔여 없음"),
        FULL_ALL_COUPON("FULL_ALL_COUPON", "모든 쿠폰 잔여 없음"),
        ;

        private final String code;
        private final String message;
    }

    //참여횟수 설정 유형
    @Getter
    @RequiredArgsConstructor
    public enum EventJoinType implements CodeCommEnum {
        DAY_1("EVENT_JOIN_TP.DAY_1", "일1회"),
        RANGE_1("EVENT_JOIN_TP.RANGE_1", "기간내1회"),
        NO_LIMIT("EVENT_JOIN_TP.NO_LIMIT", "제한없음");

        private final String code;
        private final String codeName;
    }

    //당첨자 설정 유형
    @Getter
    @RequiredArgsConstructor
    public enum EventDrawType implements CodeCommEnum {
        ADMIN("EVENT_DRAW_TP.ADMIN", "관리자추첨"),
        AUTO("EVENT_DRAW_TP.AUTO", "즉시당첨"),
        FIRST_COME("EVENT_DRAW_TP.FIRST_COME", "선착순당첨");

        private final String code;
        private final String codeName;
    }

    //당첨자 혜택 유형
    @Getter
    @RequiredArgsConstructor
    public enum EventBenefitType implements CodeCommEnum {
        COUPON("EVENT_BENEFIT_TP.COUPON", "쿠폰"),
        POINT("EVENT_BENEFIT_TP.POINT", "적립금"),
        GIFT("EVENT_BENEFIT_TP.GIFT", "경품"),
        AUTO("EVENT_BENEFIT_TP.ENTER", "자동응모"),
        NONE("EVENT_BENEFIT_TP.NONE", "제공안함");

        private final String code;
        private final String codeName;
    }

    //이벤트 유형
    @Getter
    @RequiredArgsConstructor
    public enum EventType implements CodeCommEnum {
        NORMAL("EVENT_TP.NORMAL", "일반이벤트"),
        SURVEY("EVENT_TP.SURVEY", "설문이벤트"),
        ATTEND("EVENT_TP.ATTEND", "스탬프(출석)"),
        MISSION("EVENT_TP.MISSION", "스탬프(미션)"),
        PURCHASE("EVENT_TP.PURCHASE", "스탬프(구매)"),
        ROULETTE("EVENT_TP.ROULETTE", "룰렛이벤트"),
        EXPERIENCE("EVENT_TP.EXPERIENCE", "체험단이벤트")
        ;

        private final String code;
        private final String codeName;
    }

    //임직원 전용 유형
    @Getter
    @RequiredArgsConstructor
    public enum EvEmployeeType implements CodeCommEnum {
        NO_LIMIT("EV_EMPLOYEE_TP.NO_LIMIT", "제한없음"),
        EMPLOYEE_ONLY("EV_EMPLOYEE_TP.EMPLOYEE_ONLY", "임직원전용"),
        EMPLOYEE_EXCEPT("EV_EMPLOYEE_TP.EMPLOYEE_EXCEPT", "임직원제외");

        private final String code;
        private final String codeName;
    }

    //접근권한 설정 유형
    @Getter
    @RequiredArgsConstructor
    public enum EvGroupType implements CodeCommEnum {
        NO_LIMIT("EV_GROUP_TP.NO_LIMIT", "제한없음"),
        NORMAL("EV_GROUP_TP.NORMAL", "일반"),
        PREMIUM("EV_GROUP_TP.PREMIUM", "프리미엄");
        private final String code;
        private final String codeName;
    }

    //체험단 상품 주문상태 유형
    @Getter
    @RequiredArgsConstructor
    public enum ExperienceOrderCode implements CodeCommEnum {
        ORDER_BEFORE("ORDER_BEFORE", "결제이전"),
        INCOM_COMPLETE("INCOM_COMPLETE", "결제완료"),
        DELIVERY_DOING("DELIVERY_DOING", "배송중"),
        DELIVERY_COMPLETE("DELIVERY_COMPLETE", "배송완료")
        ;
        private final String code;
        private final String codeName;
    }

    //이벤트유형
    @Getter
    @RequiredArgsConstructor
    public enum EventTp implements CodeCommEnum {
        NORMAL  ("EVENT_TP.NORMAL"    , "일반이벤트"   )
        , SURVEY    ("EVENT_TP.SURVEY"    , "설문이벤트"   )
        , ATTEND    ("EVENT_TP.ATTEND"    , "스탬프(출석)" )
        , MISSION   ("EVENT_TP.MISSION"   , "스탬프(미션)" )
        , PURCHASE  ("EVENT_TP.PURCHASE"  , "스탬프(구매)" )
        , ROULETTE  ("EVENT_TP.ROULETTE"  , "룰렛이벤트"   )
        , EXPERIENCE("EVENT_TP.EXPERIENCE", "체험단이벤트"  )
        ;
        private final String code;
        private final String codeName;
    }

    //당첨자선택유형 (공통코드 아님)
    @Getter
    @RequiredArgsConstructor
    public enum WinnerSelectTp implements CodeCommEnum {
        DIRECT  ("DIRECT"    , "당첨자 직접선택"   )
        , RANDOM  ("RANDOM"    , "당첨자 랜덤선정"   )
        ;
        private final String code;
        private final String codeName;
    }

    // 이벤트관리 응답코드
    @Getter
    @RequiredArgsConstructor
    public enum EventMessage implements MessageCommEnum
    {
        EVENT_MANAGE_SUCCESS                                                  ("0000", "정상처리 되었습니다.")
        // 입력파람채크
        , EVENT_MANAGE_PARAM_NO_INPUT                                           ("1001", "입력정보가 존재하지 않습니다.")
        , EVENT_MANAGE_PARAM_NO_EVENT_ID                                        ("1002", "이벤트ID를 입력하세요.")
        , EVENT_MANAGE_PARAM_NO_EVENT_TP                                        ("1003", "이벤트유형을 확인하세요.")
        , EVENT_MANAGE_PARAM_NO_WINNER_SELECT_TP                                ("1004", "당첨자선택유형을 확인하세요.")
        , EVENT_MANAGE_PARAM_NO_WINNER_CNT                                      ("1005", "당첨자수를 확인하세요.")
        , EVENT_MANAGE_PARAM_NO_GROUP_GOODS_ID_LIST                             ("1006", "상품코드정보를 확인하세요.")
        , EVENT_MANAGE_NO_JOB                                                   ("1007", "업무종류를 확인하세요.")
        , EVENT_MANAGE_PARAM_NO_EVENT_JOIN_ID                                   ("1008", "이벤트참여ID를 확인하세요.")
        , EVENT_MANAGE_PARAM_NO_ADMIN_SECRET_YN                                 ("1009", "댓글차단여부를 확인하세요.")
        , EVENT_MANAGE_PARAM_NO_EVENT_GROUPT_ID                                 ("1010", "그룹ID를 입력하세요.")
        // 조회
        , EVENT_MANAGE_DETAIL_NO_DATA                                           ("2001", "이벤트 기본정보가 없습니다.")
        , EVENT_MANAGE_SELECT_NO_DATA                                           ("2101", "이벤트 상세정보가 없습니다.")
        , EVENT_MANAGE_DETAIL_NO_EVENT_TP                                       ("2102", "이벤트 유형정보가 없습니다.")

        // 등록 - 공통
        , EVENT_MANAGE_EVENT_ADD_PROC_FAIL                                      ("3000", "이벤트 등록처리 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_BASIC_FAIL                                     ("3001", "이벤트 기본정보등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_USER_GROUP_FAIL                                ("3002", "이벤트 접근권한설정 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_FAIL_INPUT_TARGET                              ("3003", "이벤트 기본정보등록 입력정보 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_CONV_BASIC_FAIL                                ("3004", "이벤트 기본정보등록 변환 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_CONV_USER_GROUP_FAIL                           ("3005", "이벤트 접근권한 변환 오류입니다.")
        // 등록 - 일반
        , EVENT_MANAGE_EVENT_ADD_NORMAL_PROC_FAIL                               ("3100", "일반이벤트 등록처리 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_NORMAL_FAIL                                    ("3101", "일반이벤트 상세정보등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_NORMAL_COMMENT_FAIL                            ("3102", "일반이벤트 댓글구분등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_NORMAL_COUPON_FAIL                             ("3103", "일반이벤트 쿠폰정보등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_NORMAL_CONV_FAIL                               ("3104", "일반이벤트 상세정보 변환 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_INPUT_TARGET                      ("3105", "이벤트 그룹 정보등록 입력정보 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_ADD_FAIL                                   ("3106", "이벤트 그룹 정보등록 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC                              ("3107", "이벤트 그룹 정보등록 처리 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_DETL_ADD_FAIL_INPUT_TARGET                 ("3108", "이벤트 그룹상세 정보등록 입력정보 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_DETL_ADD_FAIL                              ("3109", "이벤트 그룹상세 정보등록 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_DETL_ADD_FAIL_PROC                         ("3110", "이벤트 그룹상세 정보등록 처리 오류입니다.")

        // 등록 - 설문
        , EVENT_MANAGE_EVENT_ADD_SURVEY_PROC_FAIL                               ("3200", "설문이벤트 등록처리 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_SURVEY_FAIL                                    ("3201", "설문이벤트 상세정보등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_SURVEY_COUPON_FAIL                             ("3202", "설문이벤트 쿠폰정보등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_SURVEY_CONV_FAIL                               ("3204", "설문이벤트 상세정보 변환 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_SURVEY_QUESTION_FAIL                           ("3211", "설문이벤트 설문항목등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_SURVEY_ITEM_FAIL                               ("3212", "설문이벤트 설문항목아이템등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_SURVEY_ITEM_ATTC_FAIL                          ("3213", "설문이벤트 설문항목아이템첨부파일등록 오류입니다.")
        // 등록 - 스탬프
        , EVENT_MANAGE_EVENT_ADD_STAMP_PROC_FAIL                                ("3300", "스탬프이벤트 등록처리 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_STAMP_FAIL                                     ("3301", "스탬프이벤트 상세정보등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_STAMP_STARMP_FAIL                              ("3302", "스탬프이벤트 스탬프정보등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_STAMP_COUPON_FAIL                              ("3303", "스탬프이벤트 쿠폰정보등록 오류입니다.")
        // 등록 - 룰렛
        , EVENT_MANAGE_EVENT_ADD_ROULETTE_PROC_FAIL                             ("3600", "룰렛이벤트 등록처리 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_ROULETTE_FAIL                                  ("3601", "룰렛이벤트 상세정보등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_ROULETTE_ITEM_FAIL                             ("3602", "룰렛이벤트 이벤트아이템정보등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_ROULETTE_COUPON_FAIL                           ("3603", "룰렛이벤트 쿠폰정보등록 오류입니다.")
        // 등록 - 체험단
        , EVENT_MANAGE_EVENT_ADD_EXPERIENCE_PROC_FAIL                           ("3700", "체험단이벤트 등록처리 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_EXPERIENCE_FAIL                                ("3701", "체험단이벤트 상세정보등록 오류입니다.")
        , EVENT_MANAGE_EVENT_ADD_EXPERIENCE_COMMENT_FAIL                        ("3702", "체험단이벤트 댓글구분등록 오류입니다.")

        // 수정 - 공통
        , EVENT_MANAGE_EVENT_PUT_PROC_FAIL                                      ("4000", "이벤트 수정처리 오류입니다.")
        , EVENT_MANAGE_EVENT_PUT_BASIC_FAIL                                     ("4001", "이벤트 기본정보수정 오류입니다.")
        , EVENT_MANAGE_EVENT_PUT_USER_GROUP_FAIL                                ("4002", "이벤트 접근권한설정 오류입니다.")
        , EVENT_MANAGE_EVENT_PUT_FAIL_INPUT_TARGET                              ("4003", "이벤트 기본정보수정 입력정보 오류입니다.")
        , EVENT_MANAGE_EVENT_PUT_CONV_BASIC_FAIL                                ("4004", "이벤트 기본정보수정 변환 오류입니다.")
        , EVENT_MANAGE_EVENT_PUT_CONV_USER_GROUP_FAIL                           ("4005", "이벤트 접근권한 변환 오류입니다.")
        , EVENT_MANAGE_EVENT_PUT_WINNER_NORIXW_FAIL                             ("4006", "이벤트 당첨자공지사항 수정 오류입니다.")
        // 수정 - 일반
        , EVENT_MANAGE_EVENT_PUT_NORMAL_PROC_FAIL                               ("4100", "일반이벤트 수정처리 오류입니다.")
        , EVENT_MANAGE_EVENT_PUT_NORMAL_FAIL                                    ("4101", "일반이벤트 상세정보수정 오류입니다.")
        , EVENT_MANAGE_EVENT_PUT_NORMAL_COMMENT_FAIL                            ("4102", "일반이벤트 댓글구분수정 오류입니다.")
        , EVENT_MANAGE_EVENT_PUT_NORMAL_COUPON_FAIL                             ("4103", "일반이벤트 쿠폰정보수정 오류입니다.")
        , EVENT_MANAGE_EVENT_PUT_NORMAL_CONV_FAIL                               ("4104", "일반이벤트 상세정보 변환 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_PUT_FAIL_INPUT_TARGET                      ("4105", "이벤트 그룹 정보수정 입력정보 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_PUT_FAIL                                   ("4106", "이벤트 그룹 정보수정 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_PUT_FAIL_PROC                              ("4107", "이벤트 그룹 정보수정 처리 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_DETL_PUT_FAIL_INPUT_TARGET                 ("4108", "이벤트 그룹상세 정보수정 입력정보 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_DETL_PUT_FAIL                              ("4109", "이벤트 그룹상세 정보수정 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_DETL_PUT_FAIL_PROC                         ("4110", "이벤트 그룹상세 정보수정 처리 오류입니다.")

        // 수정 - 이벤트참여
        , EVENT_MANAGE_WINNER_PUT_FAIL                                          ("5101", "당첨자 처리가 실패했습니다.")
        , EVENT_MANAGE_WINNER_PUT_NO_PROC                                       ("5102", "당첨 처리건이 없습니다.")
        , EVENT_MANAGE_WINNER_PUT_INPUT_TARGET                                  ("5103", "당첨처리 대상 입력정보 오류입니다.")
        , EVENT_MANAGE_WINNER_PUT_DIRECT_FAIL                                   ("5104", "직접선택 당첨자 처리가 실패했습니다.")
        , EVENT_MANAGE_WINNER_PUT_DIRECT_NO_DATA                                ("5105", "직접선택한 당첨자를 확인하세요.")
        , EVENT_MANAGE_WINNER_PUT_RANDOM_FAIL                                   ("5106", "직접선택 당첨자 처리가 실패했습니다.")
        , EVENT_MANAGE_WINNER_PUT_RANDOM_NO_DATA                                ("5107", "당첨대상자가 없습니다.")
        , EVENT_MANAGE_WINNER_PUT_BENEFIT_COUPON                                ("5108", "혜택 등록 쿠폰 오류입니다.")
        , EVENT_MANAGE_WINNER_PUT_BENEFIT_COUPON_EXPERIENCE                     ("5109", "체험단 이벤트 등록 쿠폰 오류입니다.")
        , EVENT_MANAGE_WINNER_PUT_BENEFIT_POINT                                 ("5110", "혜택 등록 적립금 오류입니다.")
        , EVENT_MANAGE_WINNER_PUT_BENEFIT_POINT_TOOMANY                         ("5111", "혜택 등록 적립금 여러개 존재합니다.")

        , EVENT_MANAGE_SECRET_PUT_HIDDEN_FAIL                                   ("5211", "댓글 차단 처리가 실패했습니다.")
        , EVENT_MANAGE_SECRET_PUT_HIDDEN_FAIL_PROC                              ("5212", "댓글 차단 처리 오류입니다.")
        , EVENT_MANAGE_SECRET_PUT_DISPLAY_FAIL                                  ("5221", "댓글 차단해제 처리가 실패했습니다.")
        , EVENT_MANAGE_SECRET_PUT_DISPLAY_FAIL_PROC                             ("5222", "댓글 차단해제 처리 오류입니다.")


        // 삭제
        , EVENT_MANAGE_EVENT_DEL_FAIL_INPUT_TARGET                              ("6011", "이벤트 기본 정보삭제 입력정보 오류입니다.")
        , EVENT_MANAGE_EVENT_DEL_FAIL                                           ("6012", "이벤트 기본 정보삭제 오류입니다.")
        , EVENT_MANAGE_EVENT_DEL_FAIL_PROC                                      ("6013", "이벤트 기본 정보삭제 처리 오류입니다.")
        , EVENT_MANAGE_EVENT_DEL_FAIL_INPUT_TARGET_CONVERT                      ("6014", "이벤트 기본 정보삭제 입력정보 변환 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_DETL_DEL_FAIL_INPUT_TARGET                   ("6015", "이벤트 그룹상세 정보삭제 입력정보 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_DETL_DEL_FAIL                                ("6016", "이벤트 그룹상세 정보삭제 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_DETL_DEL_FAIL_PROC                           ("6017", "이벤트 그룹상세 정보삭제 처리 오류입니다.")
        , EVENT_MANAGE_EVENT_GROUP_DETL_DEL_FAIL_INPUT_TARGET_CONVERT           ("6018", "이벤트 그룹상세 정보삭제 입력정보 변환 오류입니다.")


        ;

        private final String code;
        private final String message;
    }

    // 적용범위
    @Getter
    @RequiredArgsConstructor
    public enum CoverageType implements CodeCommEnum {
        COVERAGE_ALL("APPLYCOVERAGE.ALL", "전체"),
        COVERAGE_GOODS("APPLYCOVERAGE.GOODS", "상품"),
        COVERAGE_BRAND("APPLYCOVERAGE.BRAND", "전시브랜드"),
        COVERAGE_DISPLAY_CATEGORY("APPLYCOVERAGE.DISPLAY_CATEGORY", "전시카테고리");

        private final String code;
        private final String codeName;
    }

    //일반이벤트 참여방법
    @Getter
    @RequiredArgsConstructor
    public enum NormalEventType implements CodeCommEnum {
        BUTTON("NORMAL_EVENT_TP.BUTTON", "응모버튼"),
        COMMENT("NORMAL_EVENT_TP.COMMENT", "댓글응모"),
        NONE("NORMAL_EVENT_TP.NONE", "응모없음");

        private final String code;
        private final String codeName;
    }

    //일반이벤트 참여조건
    @Getter
    @RequiredArgsConstructor
    public enum JoinConditionType implements CodeCommEnum {
        NO_LIMIT("JOIN_CONDITION.NO_LIMIT", "제한없음"),
        ORDER("JOIN_CONDITION.ORDER", "주문고객");

        private final String code;
        private final String codeName;
    }

    //랜덤 당첨 혜택 유형
    @Getter
    @RequiredArgsConstructor
    public enum RandomBenefitType implements CodeCommEnum {
        RANDOM_BENEFIT_SINGLE("RANDOM_BENEFIT_TP.SINGLE", "단일 혜택"),
        RANDOM_BENEFIT_DIFFERENTIAL("RANDOM_BENEFIT_TP.DIFFERENTIAL", "차등 혜택");

        private final String code;
        private final String codeName;
    }

    //수기 추첨 유형
    @Getter
    @RequiredArgsConstructor
    public enum HandwrittenLotteryType implements MessageCommEnum {
        DIRECT_LOTTERY("HANDWRITTEN_LOTTERY_TP.DIRECT_LOTTERY","직접"),
        RANDOM_BENEFIT_SINGLE("HANDWRITTEN_LOTTERY_TP.RANDOM_SINGLE", "랜덤(단일)"),
        RANDOM_BENEFIT_DIFFERENTIAL("HANDWRITTEN_LOTTERY_TP.RANDOM_DIFFERENTIAL", "랜덤(차등)");

        private final String code;
        private final String message;
    }
}
