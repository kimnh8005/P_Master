package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class NaverCategoryEnums {

    // 기획전관리 응답코드
    @Getter
    @RequiredArgsConstructor
    public enum NaverCategoryMessage implements MessageCommEnum
    {
        NAVER_CATEGORY_SUCCESS                          ("0000", "정상처리 되었습니다.")
      // 입력파람채크
      , NAVER_CATEGORY_PARAM_NO_INPUT                   ("1001", "입력정보가 존재하지 않습니다.")
      // 조회
      , NAVER_CATEGORY_NO_DATA                          ("2001", "카테고리 조회내역이 없습니다.")
      , NAVER_CATEGORY_SUCCESS_GET_LIST                 ("2002", "카테고리 조회 성공")
      // 등록
      , NAVER_CATEGORY_ADD_FAIL_INPUT_TARGET            ("3011", "카테고리 매핑등록 오류입니다.")
      , NAVER_CATEGORY_ADD_SUCCESS_INPUT_TARGET         ("3012", "카테고리 매핑등록 성공.")
      // 수정
      , NAVER_CATEGORY_PUT_FAIL_INPUT_TARGET            ("4011", "카테고리 매핑수정 오류입니다.")
      , NAVER_CATEGORY_PUT_SUCCESS_INPUT_TARGET         ("4012", "카테고리 매핑수정 성공.")
      // 삭제
      , NAVER_CATEGORY_DEL_FAIL_INPUT_TARGET            ("5011", "카테고리 매핑삭제 오류입니다.")
      , NAVER_CATEGORY_DEL_SUCCESS_INPUT_TARGET         ("5012", "카테고리 매핑삭제 성공.")
      ;

      private final String code;
      private final String message;

    }

}
