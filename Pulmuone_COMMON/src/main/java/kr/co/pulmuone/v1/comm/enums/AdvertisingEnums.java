package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AdvertisingEnums {

    //분류값
    @Getter
    @RequiredArgsConstructor
    public enum SearchType implements CodeCommEnum {
        SOURCE("SOURCE","매체"),
        MEDIUM("MEDIUM","구좌"),
        CAMPAIGN("CAMPAIGN","캠페인"),
        CONTENT("CONTENT","콘텐츠"),
        TERM("TERM","키워드");

        private final String code;
        private final String codeName;
    }

    //광고 제휴사
    @Getter
    @RequiredArgsConstructor
    public enum AdvertCompany implements CodeCommEnum {
        SAMSUNG("10001","삼성쿠커"),
        LINK_PRICE("linkprice","링크프라이스");

        private final String code;
        private final String codeName;
    }

    //등록 validation
    @Getter
    @RequiredArgsConstructor
    public enum AddValidation implements MessageCommEnum {
        EXIST_SOURCE("EXIST_SOURCE","동일한 외부광고가 생성되어 있습니다. {매체(source)} 입력값을 확인 해 주세요."),
        EXIST_MEDIUM("EXIST_MEDIUM","동일한 외부광고가 생성되어 있습니다. {구좌(medium)} 입력값을 확인 해 주세요."),
        EXIST_CAMPAIGN("EXIST_CAMPAIGN","동일한 외부광고가 생성되어 있습니다. {캠페인(campaign)} 입력값을 확인 해 주세요.");

        private final String code;
        private final String message;
    }

}
