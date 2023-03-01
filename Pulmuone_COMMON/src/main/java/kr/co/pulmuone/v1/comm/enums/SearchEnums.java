package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class SearchEnums {

    @Getter
    @RequiredArgsConstructor
    public enum DictionaryMessage implements MessageCommEnum {
        DUPLICATE_DATA("DUPLICATE_DATA", "중복 데이터가 있습니다.")
        , SYNONYM_NEED_USER_DEFINE_WORD("SYNONYM_NEED_USER_DEFINE_WORD", "사용자 정의 사전에 미등록 되어 있는 단어가 발견되어, 자동으로 등록 처리 되었습니다.")
        ;

        private final String code;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum IndexMessage implements MessageCommEnum {
         GOODS_INDEX_FAIL("INDEX_FAIL", "상품 색인이 실패했습니다.")
        , GOODS_INDEX_SUCCESS("INDEX_SUCCESS", "상품 색인이 정상적으로 완료되었습니다.")
        , SUGGESTION_INDEX_FAIL("INDEX_FAIL", "자동완성 색인이 실패했습니다.")
        , SUGGESTION_INDEX_SUCCESS("INDEX_SUCCESS", "자동완성 색인이 정상적으로 완료되었습니다.")
        , PROMOTION_SUGGESTION_INDEX_SUCCESS("INDEX_SUCCESS", "이벤트/기획전 자동완성 색인이 정상적으로 완료되었습니다.")
        , PROMOTION_SUGGESTION_INDEX_FAIL("INDEX_FAIL", "이벤트/기획전 자동완성 색인이 실패했습니다. ")
        , CATEGORY_BOOST_INDEX_FAIL("INDEX_FAIL", "카테고리 부스팅 사전 색인이 실패했습니다.")
        , CATEGORY_BOOST_INDEX_SUCCESS("INDEX_SUCCESS", "카테고리 부스팅 사전 색인이 정상적으로 완료되었습니다.")
        , STORE_GOODS_INDEX_FAIL("INDEX_FAIL", "매장 상품 색인이 실패했습니다.")
        , STORE_GOODS_INDEX_SUCCESS("INDEX_SUCCESS", "매장 상품 색인이 정상적으로 완료되었습니다.")
        ;

        private final String code;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PopularSearchKeywordBatchMessage implements MessageCommEnum {
        SUCCESS("SUCCESS", "인기검색어 집계 성공")
        , FAIL("FAIL", "인기검색어 집계 실패")
        ;

        private final String code;
        private final String message;
    }

}
