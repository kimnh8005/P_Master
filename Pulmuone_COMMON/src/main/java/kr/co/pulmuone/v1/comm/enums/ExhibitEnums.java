package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ExhibitEnums {
    //증정 조건 유형
    @Getter
    @RequiredArgsConstructor
    public enum GiftType implements CodeCommEnum {
        GOODS("GIFT_TP.GOODS", "상품별"),
        CART("GIFT_TP.CART", "장바구니별");

        private final String code;
        private final String codeName;
    }

    //증정 범위 유형
    @Getter
    @RequiredArgsConstructor
    public enum GiftRangeType implements CodeCommEnum {
        INCLUDE("GIFT_RANGE_TP.INCLUDE", "포함"),
        EQUAL("GIFT_RANGE_TP.EQUAL", "동일");

        private final String code;
        private final String codeName;
    }

    //증정 적용 대상 유형
    @Getter
    @RequiredArgsConstructor
    public enum GiftTargetType implements CodeCommEnum {
        GOODS("GIFT_TARGET_TP.GOODS", "상품"),
        BRAND("GIFT_TARGET_TP.BRAND", "브랜드");

        private final String code;
        private final String codeName;
    }

    //증정 적용 대상 브랜드 유형
    @Getter
    @RequiredArgsConstructor
    public enum GiftTargetBrandType implements CodeCommEnum {
        STANDARD("GIFT_TARGET_BRAND_TP.STANDARD", "표준브랜드"),
        DISPLAY("GIFT_TARGET_BRAND_TP.DISPLAY", "전시브랜드");

        private final String code;
        private final String codeName;
    }

    //기획전 Validation
    @Getter
    @RequiredArgsConstructor
    public enum GetValidation implements MessageCommEnum {
        NO_EXHIBIT("NO_EXHIBIT", "기획전 없음"),
        NOT_DATE_BEFORE("NOT_DATE_BEFORE", "진행기간 이전"),
        NOT_DATE("NOT_DATE", "기간 아님"),
        ONLY_EMPLOYEE("ONLY_EMPLOYEE", "임직원 전용 기획전"),
        NOT_EMPLOYEE("NOT_EMPLOYEE", "임직원 제외 기획전"),
        NOT_DEVICE("NOT_DEVICE", "디바이스 다름"),
        NOT_GROUP_NONE("NOT_GROUP_NONE", "비회원"),
        NOT_GROUP("NOT_GROUP", "등급 오류"),
        NOT_QTY("NOT_QTY", "수량 오류"),
        NOT_GOODS("NOT_GOODS", "골라담기 대상상품 아님"),
        NO_SALE("NO_SALE", "판매중 아님"),
        NO_STOCK("NO_STOCK", "재고 부족"),
        NO_SHIPPING_ADDRESS("NO_SHIPPING_ADDRESS", "배송권역 아님"),
        PASS_VALIDATION("PASS_VALIDATION", "통과");

        private final String code;
        private final String message;
    }


    // 기획전관리 응답코드
    @Getter
    @RequiredArgsConstructor
    public enum ExhibitMessage implements MessageCommEnum
    {
        EXHIBIT_MANAGE_SUCCESS                                                  ("0000", "정상처리 되었습니다.")
      // 입력파람채크
      , EXHIBIT_MANAGE_PARAM_NO_INPUT                                           ("1001", "입력정보가 존재하지 않습니다.")
      , EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID                                      ("1002", "기획전ID를 입력하세요.")
      , EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_GROUPT_ID                               ("1003", "그룹ID를 입력하세요.")
      , EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_TP                                      ("1004", "기획전유형을 확인하세요.")
      , EXHIBIT_MANAGE_NO_JOB                                                   ("1005", "업무종류를 확인하세요.")
      , EXHIBIT_MANAGE_PARAM_NO_GROUP_GOODS_ID_LIST                             ("1006", "상품코드정보를 확인하세요.")
      // 조회
      , EXHIBIT_MANAGE_DETAIL_NO_DATA                                           ("2001", "기획전 기본정보가 없습니다.")
      , EXHIBIT_MANAGE_GROUP_NO_DATA                                            ("2101", "그룹 정보가 없습니다.")
      , EXHIBIT_MANAGE_SELECT_NO_DATA                                           ("2201", "골라담기 정보가 없습니다.")
      , EXHIBIT_MANAGE_GIFT_NO_DATA                                             ("2301", "증정행사 정보가 없습니다.")
      // 등록
      , EXHIBIT_MANAGE_EXHIBIT_ADD_FAIL_INPUT_TARGET                            ("3011", "기획전 기본 정보등록 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_ADD_FAIL                                         ("3012", "기획전 기본 정보등록 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_ADD_FAIL_PROC                                    ("3013", "기획전 기본 정보등록 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_ADD_FAIL_INPUT_TARGET                      ("3111", "기획전 그룹 정보등록 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_ADD_FAIL                                   ("3112", "기획전 그룹 정보등록 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_ADD_FAIL_PROC                              ("3113", "기획전 그룹 정보등록 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_ADD_FAIL_INPUT_TARGET                 ("3121", "기획전 그룹상세 정보등록 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_ADD_FAIL                              ("3122", "기획전 그룹상세 정보등록 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_ADD_FAIL_PROC                         ("3123", "기획전 그룹상세 정보등록 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_FAIL_INPUT_TARGET                     ("3211", "기획전 골라담기 정보등록 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_FAIL                                  ("3212", "기획전 골라담기 정보등록 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_FAIL_PROC                             ("3213", "기획전 골라담기 정보등록 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_ADD_FAIL_INPUT_TARGET               ("3221", "기획전 골라담기 상품 정보등록 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_ADD_FAIL                            ("3222", "기획전 골라담기 상품 정보등록 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_ADD_FAIL_PROC                       ("3223", "기획전 골라담기 상품 정보등록 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_ADD_FAIL_INPUT_TARGET           ("3231", "기획전 골라담기 추가상품 정보등록 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_ADD_FAIL                        ("3232", "기획전 골라담기 추가상품 정보등록 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_ADD_FAIL_PROC                   ("3233", "기획전 골라담기 추가상품 정보등록 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_ADD_FAIL_INPUT_TARGET                       ("3311", "기획전 증정행사 정보등록 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_ADD_FAIL                                    ("3312", "기획전 증정행사 정보등록 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_ADD_FAIL_PROC                               ("3313", "기획전 증정행사 정보등록 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_ADD_FAIL_INPUT_TARGET                 ("3321", "기획전 증정행사 상품 정보등록 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_ADD_FAIL                              ("3322", "기획전 증정행사 상품 정보등록 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_ADD_FAIL_PROC                         ("3323", "기획전 증정행사 상품 정보등록 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_ADD_FAIL_INPUT_TARGET_TP             ("3330", "기획전 증정행사 적용대상유형 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_ADD_FAIL_INPUT_TARGET          ("3331", "기획전 증정행사 대상상품 정보등록 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_ADD_FAIL                       ("3332", "기획전 증정행사 대상상품 정보등록 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_ADD_FAIL_PROC                  ("3333", "기획전 증정행사 대상상품 정보등록 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_ADD_FAIL_INPUT_TARGET          ("3341", "기획전 증정행사 대상브랜드 정보등록 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_ADD_FAIL                       ("3342", "기획전 증정행사 대상브랜드 정보등록 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_ADD_FAIL_PROC                  ("3343", "기획전 증정행사 대상브랜드 정보등록 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GROUP_ADD_FAIL_INPUT_TARGET                 ("3351", "기획전 증정행사 그룹 정보등록 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GROUP_ADD_FAIL                              ("3352", "기획전 증정행사 그룹 정보등록 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GROUP_ADD_FAIL_PROC                         ("3353", "기획전 증정행사 그룹 정보등록 처리 오류입니다.")
      // 수정
      , EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_INPUT_TARGET                            ("4011", "기획전 기본 정보수정 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL                                         ("4012", "기획전 기본 정보수정 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_PROC                                    ("4013", "기획전 기본 정보수정 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_PUT_FAIL_INPUT_TARGET                      ("4111", "기획전 그룹 정보수정 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_PUT_FAIL                                   ("4112", "기획전 그룹 정보수정 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_PUT_FAIL_PROC                              ("4113", "기획전 그룹 정보수정 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_PUT_FAIL_INPUT_TARGET                 ("4121", "기획전 그룹상세 정보수정 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_PUT_FAIL                              ("4122", "기획전 그룹상세 정보수정 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_PUT_FAIL_PROC                         ("4123", "기획전 그룹상세 정보수정 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_PUT_FAIL_INPUT_TARGET                     ("4211", "기획전 골라담기 정보수정 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_PUT_FAIL                                  ("4212", "기획전 골라담기 정보수정 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_PUT_FAIL_PROC                             ("4213", "기획전 골라담기 정보수정 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_PUT_FAIL_INPUT_TARGET               ("4221", "기획전 골라담기 상품 정보수정 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_PUT_FAIL                            ("4222", "기획전 골라담기 상품 정보수정 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_PUT_FAIL_PROC                       ("4223", "기획전 골라담기 상품 정보수정 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_PUT_FAIL_INPUT_TARGET           ("4231", "기획전 골라담기 추가상품 정보수정 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_PUT_FAIL                        ("4232", "기획전 골라담기 추가상품 정보수정 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_PUT_FAIL_PROC                   ("4233", "기획전 골라담기 추가상품 정보수정 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_PUT_FAIL_INPUT_TARGET                       ("4311", "기획전 증정행사 정보수정 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_PUT_FAIL                                    ("4312", "기획전 증정행사 정보수정 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_PUT_FAIL_PROC                               ("4313", "기획전 증정행사 정보수정 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_PUT_FAIL_INPUT_TARGET                 ("4321", "기획전 증정행사 상품 정보수정 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_PUT_FAIL                              ("4322", "기획전 증정행사 상품 정보수정 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_PUT_FAIL_PROC                         ("4323", "기획전 증정행사 상품 정보수정 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_PUT_FAIL_INPUT_TARGET_TP             ("4330", "기획전 증정행사 적용대상유형 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_PUT_FAIL_INPUT_TARGET          ("4331", "기획전 증정행사 대상상품 정보수정 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_PUT_FAIL                       ("4332", "기획전 증정행사 대상상품 정보수정 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_PUT_FAIL_PROC                  ("4333", "기획전 증정행사 대상상품 정보수정 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_PUT_FAIL_INPUT_TARGET          ("4341", "기획전 증정행사 대상브랜드 정보수정 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_PUT_FAIL                       ("4342", "기획전 증정행사 대상브랜드 정보수정 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_PUT_FAIL_PROC                  ("4343", "기획전 증정행사 대상브랜드 정보수정 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GROUP_PUT_FAIL_INPUT_TARGET                 ("4351", "기획전 증정행사 그룹 정보수정 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GROUP_PUT_FAIL                              ("4352", "기획전 증정행사 그룹 정보수정 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GROUP_PUT_FAIL_PROC                         ("4353", "기획전 증정행사 그룹 정보수정 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_REP_PUT_FAIL                                ("4360", "대표상품변경 정보수정 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_REP_CANCEL_PUT_FAIL_PROC                    ("4361", "대표상품변경 해제 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_REP_REGIST_PUT_FAIL_PROC                    ("4361", "대표상품변경 설정 오류입니다.")
      // 삭제
      , EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL_INPUT_TARGET                            ("5011", "기획전 기본 정보삭제 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL                                         ("5012", "기획전 기본 정보삭제 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL_PROC                                    ("5013", "기획전 기본 정보삭제 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL_INPUT_TARGET_CONVERT                    ("5014", "기획전 기본 정보삭제 입력정보 변환 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DEL_FAIL_INPUT_TARGET                      ("5111", "기획전 그룹 정보삭제 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DEL_FAIL                                   ("5112", "기획전 그룹 정보삭제 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DEL_FAIL_PROC                              ("5113", "기획전 그룹 정보삭제 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL_INPUT_TARGET                 ("5121", "기획전 그룹상세 정보삭제 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL                              ("5122", "기획전 그룹상세 정보삭제 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL_PROC                         ("5123", "기획전 그룹상세 정보삭제 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL_INPUT_TARGET_CONVERT         ("5124", "기획전 그룹상세 정보삭제 입력정보 변환 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_DEL_FAIL_INPUT_TARGET                     ("5211", "기획전 골라담기 정보삭제 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_DEL_FAIL                                  ("5212", "기획전 골라담기 정보삭제 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_DEL_FAIL_PROC                             ("5213", "기획전 골라담기 정보삭제 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL_INPUT_TARGET               ("5221", "기획전 골라담기 상품 정보삭제 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL                            ("5222", "기획전 골라담기 상품 정보삭제 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL_PROC                       ("5223", "기획전 골라담기 상품 정보삭제 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL_INPUT_TARGET_CONVERT       ("5224", "기획전 골라담기 상품 정보삭제 입력정보 변환 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL_INPUT_TARGET           ("5231", "기획전 골라담기 추가상품 정보삭제 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL                        ("5232", "기획전 골라담기 추가상품 정보삭제 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL_PROC                   ("5233", "기획전 골라담기 추가상품 정보삭제 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL_INPUT_TARGET_CONVERT   ("5234", "기획전 골라담기 추가상품 정보삭제 입력정보 변환 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_DEL_FAIL_INPUT_TARGET                       ("5311", "기획전 증정행사 정보삭제 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_DEL_FAIL                                    ("5312", "기획전 증정행사 정보삭제 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_DEL_FAIL_PROC                               ("5313", "기획전 증정행사 정보삭제 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL_INPUT_TARGET                 ("5321", "기획전 증정행사 상품 정보삭제 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL                              ("5322", "기획전 증정행사 상품 정보삭제 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL_PROC                         ("5323", "기획전 증정행사 상품 정보삭제 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL_INPUT_TARGET_CONVERT         ("5324", "기획전 증정행사 상품 정보삭제 입력정보 변환 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_INPUT_TARGET          ("5331", "기획전 증정행사 대상상품 정보삭제 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL                       ("5332", "기획전 증정행사 대상상품 정보삭제 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_PROC                  ("5333", "기획전 증정행사 대상상품 정보삭제 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_INPUT_TARGET_CONVERT  ("5334", "기획전 증정행사 대상상품 정보삭제 입력정보 변환 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_DEL_FAIL_INPUT_TARGET          ("5341", "기획전 증정행사 대상브랜드 정보삭제 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_DEL_FAIL                       ("5342", "기획전 증정행사 대상브랜드 정보삭제 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_DEL_FAIL_PROC                  ("5343", "기획전 증정행사 대상브랜드 정보삭제 처리 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_DEL_FAIL_INPUT_TARGET_CONVERT  ("5344", "기획전 증정행사 대상브랜드 정보삭제 입력정보 변환 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GROUP_DEL_FAIL_INPUT_TARGET                 ("3351", "기획전 증정행사 그룹 정보삭제 입력정보 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GROUP_DEL_FAIL                              ("3352", "기획전 증정행사 그룹 정보삭제 오류입니다.")
      , EXHIBIT_MANAGE_EXHIBIT_GIFT_GROUP_DEL_FAIL_PROC                         ("3353", "기획전 증정행사 그룹 정보삭제 처리 오류입니다.")
      ;

      private final String code;
      private final String message;
    }

    // 기획전유형
    @Getter
    @RequiredArgsConstructor
    public enum ExhibitTp implements CodeCommEnum {
        NORMAL("EXHIBIT_TP.NORMAL", "일반기획전")
      , SELECT("EXHIBIT_TP.SELECT", "골라담기(균일가)기획전")
      , GIFT("EXHIBIT_TP.GIFT"    , "증정행사")
      ;
        private final String code;
        private final String codeName;
    }

    // 기획전이미지유형
    @Getter
    @RequiredArgsConstructor
    public enum ExhibitImgTp implements CodeCommEnum {
        NOT_USE("EXHIBIT_IMG_TP.NOT_USE", "사용안함")
      , BG("EXHIBIT_IMG_TP.BG"          , "배경컬러")
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

    // 증정배송유형
    @Getter
    @RequiredArgsConstructor
    public enum GiftShippingTp implements CodeCommEnum {
        COMBINED("GIFT_SHIPPING_TP.COMBINED", "합배송")
      , INDIVIDUAL("GIFT_SHIPPING_TP.INDIVIDUAL", "개별배송")
      ;
      private final String code;
      private final String codeName;
    }

    // 배송불가지역 유형
    @Getter
    @RequiredArgsConstructor
    public enum UndeliverableAreaTp implements CodeCommEnum {
        NONE("UNDELIVERABLE_AREA_TP.NONE", "없음")
      , A1("UNDELIVERABLE_AREA_TP.A1", "도서산간(1권역)")
      , A2("UNDELIVERABLE_AREA_TP.A2", "제주(2권역)")
      , A1_A2("UNDELIVERABLE_AREA_TP.A1_A2", "1권역/2권역")
      ;
      private final String code;
      private final String codeName;
    }

    //기획전 승인/발급상태
    @Getter
    @RequiredArgsConstructor
    public enum ExhibitStatus implements CodeCommEnum {
    	SAVE("EXHIBIT_STATUS.SAVE", "저장"),
    	APPROVED("EXHIBIT_STATUS.APPROVED","사용가능");

        private final String code;
        private final String codeName;
    }
}
