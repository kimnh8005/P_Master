package kr.co.pulmuone.v1.comm.enums;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 3. 7.       김명진          최초작성
 * =======================================================================
 * </PRE>
 */
public class OutmallEnums {

    // 아웃몰 배치 연동 상태
    @Getter
    @RequiredArgsConstructor
    public enum OutmallBatchStatusCd implements CodeCommEnum {
    	READY("20", "배치대기중"),
    	ING("21", "배치진행중"),
    	END("22", "배치완료"),
        ;
        private final String code;
        private final String codeName;

        public static OutmallBatchStatusCd findByCode(String code) {
            return Arrays.stream(OutmallBatchStatusCd.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // EZAdmin 배치 타입 정보
    @Getter
    @RequiredArgsConstructor
    public enum OutmallBatchTypeCd implements CodeCommEnum{
    	CREATE_ORDER("OUTMALL_BATCH_TYPE.CREATE_ORDER", "OUTMALL 주문생성 배치"),
    	;

    	private final String code;
    	private final String codeName;
    }

    // 외부몰 타입
    @Getter
    @RequiredArgsConstructor
    public enum OutmallType implements CodeCommEnum{
        EASYADMIN("E", "이지어드민"),
        SABANGNET("S", "사방넷")
    	;

    	private final String code;
    	private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum OutmallDownloadType implements CodeCommEnum{
        UPLOAD("U", "업로드"),
        BATCH("B", "배치"),
        ORG_UPLOAD("O", "업로드원본"),
        ;

        private final String code;
        private final String codeName;
    }

    // 주문생성 오류 메세지
    @Getter
    @RequiredArgsConstructor
    public enum orderCreateMsg implements MessageCommEnum {

        STOCK_FAIL("STOCK_FAIL", "재고 차감 실패"),
        ORDER_CREATE_FAIL("ORDER_CREATE_FAIL", "주문서 생성 실패"),
        ORDER_BIND_FAIL("ORDER_BIND_FAIL", "주문데이터 바인딩 실패"),
        ORDER_CREATE_FAIL_IN_VERIFICATION("ORDER_CREATE_FAIL_IN_VERIFICATION", "주문생성 정상여부 검증 실패")
        ;
        private final String code;
        private final String message;
    }

    // 이지어드민 주문조회 API 수집상태
    @Getter
    @RequiredArgsConstructor
    public enum CollectionCode implements CodeCommEnum{
        ING("I", "수집중"),
        SUCCESS("S", "수집성공"),
        FAIL("F", "수집실패")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum OutmallFailType implements CodeCommEnum{
        UPLOAD("U", "업로드"),
        BATCH("B", "배치"),
        TRANS("T", "송장연동")
        ;

        private final String code;
        private final String codeName;
    }
}