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
 *  1.0    2021. 3. 3.       김명진          최초작성
 * =======================================================================
 * </PRE>
 */
public class EZAdminEnums {

    // EZAdmin 배치 연동 상태
    @Getter
    @RequiredArgsConstructor
    public enum EZAdminSyncCd implements MessageCommEnum {
    	SYNC_CD_WAIT("W", "대기중"),
    	SYNC_CD_ING("I", "진행중"),
    	SYNC_CD_END("E", " 완료"),
        ;
        private final String code;
        private final String message;

        public static EZAdminSyncCd findByCode(String code) {
            return Arrays.stream(EZAdminSyncCd.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // EZAdmin 처리상태
    @Getter
    @RequiredArgsConstructor
    public enum EZAdminProcessCd implements MessageCommEnum {
    	PROCESS_CD_WAIT("W", "대기중"),
    	PROCESS_CD_ING("I", "진행중"),
    	PROCESS_CD_END("E", " 완료"),
    	;
    	private final String code;
    	private final String message;

    	public static EZAdminProcessCd findByCode(String code) {
    		return Arrays.stream(EZAdminProcessCd.values())
    				.filter(item -> item.getCode().equals(code))
    				.findAny()
    				.orElse(null);
    	}
    }

    // EZAdmin 배치 타입 정보
    @Getter
    @RequiredArgsConstructor
    public enum EZAdminBatchTypeCd implements CodeCommEnum{
    	ORDER_SEARCH("EZADMIN_BATCH_TYPE.ORDER_SEARCH", "EZAdmin 주문조회 배치"),
    	CLAIM_SEARCH("EZADMIN_BATCH_TYPE.CLAIM_SEARCH", "EZAdmin 클레임조회 배치"),
    	CREATE_ORDER("EZADMIN_BATCH_TYPE.CREATE_ORDER", "EZAdmin 주문생성 배치"),
		QNA_SEARCH("EZADMIN_BATCH_TYPE.QNA_SEARCH", "EZAdmin 문의글조회 배치"),
		QNA_ANSWER("EZADMIN_BATCH_TYPE.QNA_ANSWER", "EZAdmin 문의글답변 배치")
    	;

    	private final String code;
    	private final String codeName;
    }

    // EZAdmin 상품 유효성 체크 정보
    @Getter
    @RequiredArgsConstructor
    public enum EZAdminGoodsValidMessage implements MessageCommEnum{
    	SUCCESS("0000", "성공"),
    	REGIST_GOODS_NONE("0001", "등록된 상품 정보 없음"),
    	SALE_ON_NONE("0002", "판매중인 상품정보가 아님"),
    	ORDER_IF_DATE_NONE("0003", "주문I/F일자정보 없음"),
    	WAREHOUSE_ID_NONE("0004", "출고처 정보 없음"),
		GOODS_STATUS_ERROR("GOODS_ERROR", "판매 상태 확인이 필요한 상품 입니다."),
		WAREHOUSE_UNDELIVERABLE_AREA("WAREHOUSE_UNDELIVERABLE_AREA", "출고처 배송불가 지역입니다."),
		NOT_REGISTRATION_GOODS_IN_SELLER("NOT_REGISTRATION_GOODS_IN_SELLER", "판매처에서 등록 할 수 없는 공급업체 상품입니다."),
    	;

    	private final String code;
    	private final String message;
    }
}