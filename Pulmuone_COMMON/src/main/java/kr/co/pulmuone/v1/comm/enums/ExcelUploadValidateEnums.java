package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ExcelUploadValidateEnums {

    // 판매처 그룹정보
    @Getter
    @RequiredArgsConstructor
    public enum SellersGroup implements CodeCommEnum {
        VENDOR("SELLERS_GROUP.VENDOR", "밴더"),
        DIRECT_BUY("SELLERS_GROUP.DIRECT_BUY", "직매입"),
        DIRECT_MNG("SELLERS_GROUP.DIRECT_MNG", "직관리");

        private final String code;
        private final String codeName;
    }

    // 아웃몰 처리상태
    @Getter
    @RequiredArgsConstructor
    public enum ProcessCode implements CodeCommEnum {
        W("W", "미처리"),
        I("I", "처리중"),
        E("E", "처리완료");

        private final String code;
        private final String codeName;
    }



    // 아웃몰 연동상태
    @Getter
    @RequiredArgsConstructor
    public enum UploadStatusCode implements CodeCommEnum {
        UPLOAD_IN_PROGRESS("10", "등록중"),
        UPLOAD_DONE("11", "등록완료"),
        UPLOAD_FAILED("12", "등록실패");

        private final String code;
        private final String codeName;
    }

    // 아웃몰 유형
    @Getter
    @RequiredArgsConstructor
    public enum BatchStatusCode implements CodeCommEnum {
        BATCH_READY("20", "배치대기중"),
        BATCH_IN_PROGRESS("21", "배치진행중"),
        BATCH_DONE("22", "배치완료");

        private final String code;
        private final String codeName;
    }

    // 아웃몰 Validation 유형
    @Getter
    @RequiredArgsConstructor
    public enum ValidateType implements MessageCommEnum {

        COLUMN_EMPTY("EMPTY", "{0} 컬럼 내용이 존재 하지 않습니다."),

        COLUMN_GOODS_ID_EMPTY("EMPTY", "상품코드 컬럼값이 존재하지 않습니다."),

        COLUMN_DATE_FORMAT_FAIL("DATE_FORMAT_FAIL", "주문I/F 일자를 확인 해주세요."),

        COLUMN_NOT_EXISTS_CLAIM_STATUS_TP("NOT_EXISTS_CLAIM_STATUS_TP", "{0} 컬럼 내용에 유효하지 않은 값입니다."),

        COLUMN_NOT_EXISTS_ORDER("NOT_EXISTS_ORDER", "주문번호를 확인해주세요."),

        COLUMN_NOT_EXISTS_ORDER_DETAIL("COLUMN_NOT_EXISTS_ORDER_DETAIL", "주문상세번호를 확인해주세요."),

        COLUMN_NOT_EXISTS_OUTMALL("COLUMN_NOT_EXISTS_OUTMALL", "외부몰 주문번호만 변경가능합니다."),

        IF_DAY_NOT_DELIVERY("IF_DAY_NOT_DELIVERY", "출고불가한 주문I/F 일자 입니다."),

        IS_SAME_ORDER_IF_DAY("IS_SAME_ORDER_IF_DAY", "동일한 주문 I/F 일자 입니다."),


        /*
        확인 후 제거 예정
        COLLECTION_MALL_ID_EMPTY("EMPTY", "수집몰 주문 번호가 존재 하지 않습니다."),
        COLLECTION_MALL_DETAIL_ID_EMPTY("EMPTY", "수집몰 상세 번호가 존재 하지 않습니다."),
        BUYER_NAME_EMPTY("EMPTY", "주문자 명이 존재 하지 않습니다."),
        BUYER_TEL_EMPTY("EMPTY", "주문자 연락처가 존재 하지 않습니다."),
        BUYER_MOBILE_EMPTY("EMPTY", "주문자 핸드폰이 존재 하지 않습니다."),
        RECEIVER_NAME_EMPTY("EMPTY", "수취인 명이 존재 하지 않습니다."),
        RECEIVER_TEL_EMPTY("EMPTY", "수취인 연락처가 존재 하지 않습니다."),
        RECEIVER_MOBILE_EMPTY("EMPTY", "수취인 핸드폰이 존재 하지 않습니다."),
        RECEIVER_ZIP_CODE_EMPTY("EMPTY", "수취인 우편번호가 존재 하지 않습니다."),
        RECEIVER_ADDRESS1_EMPTY("EMPTY", "수취인 주소1이 존재 하지 않습니다."),
        RECEIVER_ADDRESS2_EMPTY("EMPTY", "수취인 주소2이 존재 하지 않습니다."),
        IL_GOODS_ID_EMPTY("EMPTY", "상품코드가 존재 하지 않습니다."),
        ORDER_COUNT_EMPTY("EMPTY", "수량이 존재 하지 않습니다."),
        PAID_PRICE_EMPTY("EMPTY", "판매가가 존재 하지 않습니다."),
        */
        COLUMN_LENGTH_OVER("LENGTH_OVER", "{0} 컬럼 길이가 {1} bytes 를 초과 했습니다."),
        COLUMN_LENGTH_NOT_EQUALS("LENGTH_NOT_EQUALS", "{0} 컬럼 길이가 {1} bytes 와 일치 하지 않습니다."),
        /*
        확인 후 제거 예정
        COLLECTION_MALL_ID_LENGTH_OVER("LENGTH_OVER", "수집몰 주문 번호의 길이가 {0} bytes 를 초과 했습니다."),
        COLLECTION_MALL_DETAIL_ID_LENGTH_OVER("LENGTH_OVER", "수집몰 상세 번호의 길이가 {0} bytes 를 초과 했습니다."),
        BUYER_NAME_LENGTH_OVER("LENGTH_OVER", "주문자명의 길이가 {0} bytes 를 초과 했습니다."),
        BUYER_TEL_LENGTH_OVER("LENGTH_OVER", "주문자 연락처의 길이가 {0} bytes 를 초과 했습니다."),
        BUYER_MOBILE_LENGTH_OVER("LENGTH_OVER", "주문자 핸드폰의 길이가 {0} bytes 를 초과 했습니다."),
        RECEIVER_NAME_LENGTH_OVER("LENGTH_OVER", "수취인명의 길이가 {0} bytes 를 초과 했습니다."),
        RECEIVER_TEL_LENGTH_OVER("LENGTH_OVER", "수취인 연락처의 길이가 {0} bytes 를 초과 했습니다."),
        RECEIVER_MOBILE_LENGTH_OVER("LENGTH_OVER", "수취인 핸드폰의 길이가 {0} bytes 를 초과 했습니다."),
        RECEIVER_ZIP_CODE_LENGTH_OVER("LENGTH_OVER", "수취인 우편번호의 길이가 {0} bytes 를 초과 했습니다."),
        RECEIVER_ADDRESS1_LENGTH_OVER("LENGTH_OVER", "수취인 주소1의 길이가 {0} bytes 를 초과 했습니다."),
        RECEIVER_ADDRESS2_LENGTH_OVER("LENGTH_OVER", "수취인 주소2의 길이가 140 bytes 를 초과 했습니다."),
        SHIPPING_PRICE_LENGTH_OVER("LENGTH_OVER", "배송메세지의 길이가 {0} bytes 를 초과 했습니다."),

        OUT_MALL_ID_LENGTH_OVER("LENGTH_OVER", "외부몰 주문번호의 길이가 {0} bytes 를 초과 했습니다."),
        OUT_MALL_ID_SEQ1_LENGTH_OVER("LENGTH_OVER", "외부몰 주문상세번호1의 길이가 {0} bytes 를 초과 했습니다."),
        OUT_MALL_ID_SEQ2_LENGTH_OVER("LENGTH_OVER", "외부몰 주문상세번호2의 길이가 {0} bytes 를 초과 했습니다."),
        */

        COLUMN_STRING("STRING_ERROR", "{0} 컬럼에 숫자 이외의 문자가 포함되어있습니다."),
        COLUMN_DECIMAL_POINT("STRING_ERROR", "{0} 컬럼에 소수점이 포함되어있습니다."),
        /*
        GOODS_ID_STRING("STRING_ERROR", "상품코드에 숫자 이외의 문자가 포함되어있습니다."),
        ORDER_COUNT_STRING("STRING_ERROR", "수량에 숫자 이외의 문자가 포함되어있습니다."),
        PAID_PRICE_STRING("STRING_ERROR", "판매가에 숫자 이외의 문자가 포함되어있습니다."),
        ORDER_COUNT_ERROR("COUNT_ERROR", "상품 수량이 없거나 형식 오류 입니다."),
        PAID_PRICE_ERROR("PRICE_ERROR", "판매가는 0원 이상이어야 합니다."),
        */
        COLUMN_NUMBER_ZERO("NUMBER_ZERO_ERROR", "{0} 컬럼 값이 0보다 작은 값이 존재 합니다."),

        GOODS_STOP_SALE("GOODS_ERROR", "판매 중지된 상품 입니다."),
        GOODS_STOP_SALE_EXIST("GOODS_ERROR", "판매 중지 상품이 포함된 주문 입니다."),
        GOODS_STATUS_ERROR("GOODS_ERROR", "상품이 {0} 상태입니다."),
        GOODS_OUTMALL_STATUS_ERROR("GOODS_ERROR", "외부몰 판매 상태 확인이 필요한 상품 입니다."),
        GOODS_STATUS_ERROR_EXIST("GOODS_ERROR", "판매 상태 확인이 필요한 상품이 포함된 주문 입니다."),
        GOODS_ID_ERROR("GOODS_ERROR", "존재 하지 않는 상품 입니다."),
        GOODS_ID_ERROR_EXIST("GOODS_ERROR", "존재 하지 않는 상품이 포함된 주문 입니다."),
        ITEM_CD_ERROR("ITEM_CD_ERROR", "존재 하지 않는 품목코드 입니다."),
        BUYER_INFO_ERROR("BUYER_ERROR", "주문자 정보가 유효성 검증 실패하였습니다."),
        RECEIVER_INFO_ERROR("RECEIVER_ERROR", "수취인 정보가 유효성 검증에 실패 하였습니다."),

        COLLECTION_MALL_DETAIL_ID_UPLOAD_OVERLAP("RECEIVER_ERROR", "현재 업로드 내용 중 중복 된 {0}({1}) 입니다."),
        COLLECTION_MALL_DETAIL_ID_OVERLAP("RECEIVER_ERROR", "기존 업로드 내용 중 중복 된 {0}({1}) 입니다."),

        EASYADMIN_COLLECTION_MALL_ID_OVERLAP("EASYADMIN_COLLECTION_MALL_ID_OVERLAP", "기존 업로드 내용 중 중복 된 합포번호({0}) 입니다."),
        EASYADMIN_COLLECTION_MALL_DETAIL_ID_OVERLAP("EASYADMIN_COLLECTION_MALL_DETAIL_ID_OVERLAP", "기존 업로드 내용 중 중복 된 관리번호({0}) 입니다."),
        EASYADMIN_COLLECTION_MALL_DETAIL_ID_UPLOAD_OVERLAP("EASYADMIN_COLLECTION_MALL_DETAIL_ID_UPLOAD_OVERLAP", "업로드 내용 중 중복 된 관리번호({0}) 입니다."),

        SABANGNET_COLLECTION_MALL_ID_OVERLAP("SABANGNET_COLLECTION_MALL_ID_OVERLAP", "기존 업로드 내용 중 중복 된 수집몰주문번호({0}) 입니다."),
        SABANGNET_COLLECTION_MALL_DETAIL_ID_OVERLAP("SABANGNET_COLLECTION_MALL_DETAIL_ID_OVERLAP", "기존 업로드 내용 중 중복 된 수집몰상세번호({0}) 입니다."),
        SABANGNET_COLLECTION_MALL_DETAIL_ID_UPLOAD_OVERLAP("SABANGNET_COLLECTION_MALL_DETAIL_ID_UPLOAD_OVERLAP", "업로드 내용 중 중복 된 수집몰상세번호({0}) 입니다."),

        COLLECTION_MALL_OUTMALL_ID_OVERLAP("COLLECTION_MALL_OUTMALL_ID_OVERLAP", "기존 업로드 내용 중 중복 된 외부몰주문번호({0}) 입니다."),


        SELLERS_NOT_CODE("SELLERS_NOT_CODE", "판매처 코드가 존재하지 않습니다."),

        CLAIM_BOS_ID_NOT_CODE("CLAIM_BOS_ID_NOT_CODE", "BOS 클레임 사유 코드가 존재하지 않습니다."),
        IF_CHANGE_ORDER_IC("IF_CHANGE_ORDER_IC", "결제완료 상태만 변경가능합니다."),
        WAREHOUSE_UNDELIVERABLE_AREA("WAREHOUSE_UNDELIVERABLE_AREA", "출고처 배송불가 지역입니다."),
        NOT_REGISTRATION_GOODS_IN_SELLER("NOT_REGISTRATION_GOODS_IN_SELLER", "판매처에서 등록 할 수 없는 공급업체 상품입니다."),

        CLAIM_COUNT_OVER("CLAIM_COUNT_OVER", "취소 가능 수량({0})을 초과하였습니다."),

        SHIPPING_AREA_REGIST_NOT_CODE("SHIPPING_AREA_REGIST_NOT_CODE", "잘못된 구분값이 들어갔습니다. {0} 입니다."),
        DUPLICATE_ZIP_CODE("DUPLICATE_ZIP_CODE", "중복된 우편번호입니다. {0} 입니다."),
        NO_MATCH_ZIP_CODE("NO_MATCH_ZIP_CODE", "대상이 되는 우편번호가 없습니다."),
        NEW_ZIP_CODE("NEW_ZIP_CODE", "신규 우편번호 5자리만 등록 가능합니다."),
        DUPLICATE_KEYWORD("DUPLICATE_KEYWORD", "이미 사용중인 키워드 입니다.");
        ;

        private final String code;
        private final String message;
    }

    //  엑셀업로드 Response 값
    @Getter
    @RequiredArgsConstructor
    public enum UploadResponseCode implements MessageCommEnum {
        FILE_ERROR("FILE_ERROR", "이지어드민용 파일을 업로드해주세요."),
        EXCEL_UPLOAD_NONE("EXCEL_UPLOAD_NONE", "엑셀에 데이터가 없습니다."),
        EXCEL_TRANSFORM_FAIL("EXCEL_TRANSFORM_FAIL", "엑셀 변환이 실패 하였습니다."),
        UPLOAD_FAIL("UPLOAD_FAIL", "업로드 작업이 실패 하였습니다."),
        NOT_URUSERID("NOT_URUSERID", "유효하지 않은 정보가 있습니다.<br />엑셀 정보를 다시 확인 해 주세요.<br />(회원아이디)"),
        OVERLAP_URUSERID("OVERLAP_URUSERID", "유효하지 않은 정보가 있습니다.<br />엑셀 정보를 다시 확인 해 주세요.<br />(회원아이디 중복)"),
        IS_NVL("IS_NVL", "유효하지 않은 정보가 있습니다.<br />엑셀 정보를 다시 확인 해 주세요.<br />(빈값)"),
        IS_EVENTLIMIT("IS_EVENTLIMIT", "유효하지 않은 정보가 있습니다.<br />엑셀 정보를 다시 확인 해 주세요.<br />(이벤트제한여부)")
        ;

        private final String code;
        private final String message;
    }


    @Getter
    @RequiredArgsConstructor
    public enum ByteLength {
        BYTE_2(2),
        BYTE_5(5),
        BYTE_10(10),
        BYTE_12(12),
        BYTE_14(14),
        BYTE_16(16),
        BYTE_30(30),
        BYTE_40(40),
        BYTE_50(50),
        BYTE_60(60),
        BYTE_100(100),
        BYTE_140(140),
        BYTE_150(150),
        BYTE_200(200),
        BYTE_255(255),
        BYTE_400(400),
        BYTE_500(500),
        BYTE_1000(1000),
        BYTE_2000(2000),
        ;

        private final int byteLength;
    }

    // 식단컨텐츠 업로드 Validation 유형
    @Getter
    @RequiredArgsConstructor
    public enum mealContsValidateType implements MessageCommEnum {

        COLUMN_EMPTY("EMPTY", "{0} 컬럼 내용이 존재 하지 않습니다."),
        COLUMN_LENGTH_OVER("LENGTH_OVER", "{0} 컬럼 길이가 {1} bytes 를 초과 했습니다."),
        COLUMN_STRING("STRING_ERROR", "{0} 컬럼에 숫자 이외의 문자가 포함되어있습니다."),
        COLUMN_YN("STRING_ERROR", "{0} 컬럼에 Y이외의 문자가 포함되어있습니다."),
        OVERLAP_MEAL_CONTS_CD("OVERLAP_MEAL_CONTS_CD", "{0} : 등록된 식단품목코드입니다."),
        ;

        private final String code;
        private final String message;
    }
}