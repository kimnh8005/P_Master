package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

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
 *  1.0    2020. 10. 14.               박주형         최초작성
 * =======================================================================
 * </PRE>
 */
public class StockEnums {

    // ERP 재고 조회 API 호출 후 IL_ITEM_ERP_STOCK 테이블에 저장시 STOCK_TP 컬럼의 재고 타입 코드
    @Getter
    @RequiredArgsConstructor
    public enum ErpStockType implements CodeCommEnum {

        ERP_STOCK_CLOSED("ERP_STOCK_TP.CLOSED", "전일마감재고") //
        , ERP_STOCK_CONFIRMED("ERP_STOCK_TP.CONFIRMED", "입고확정") //
        , ERP_STOCK_SCHEDULED("ERP_STOCK_TP.SCHEDULED", "입고예정") //
        , ERP_STOCK_DISCARD("ERP_STOCK_TP.DISCARD", "폐기예정") //
        , ERP_STOCK_ORDER("ERP_STOCK_TP.ORDER", "주문") //
        , ERP_STOCK_PO("ERP_STOCK_TP.PO", "발주") //
        , ERP_STOCK_OFFLINE("ERP_STOCK_TP.OFFLINE", "오프라인물류재고") //
        , ERP_STOCK_DISCARD_ORDER("ERP_STOCK_TP.DISCARD_ORDER", "폐기임박 주문") //
        , ERP_STOCK_CLOSED_ADJ("ERP_STOCK_TP.CLOSED_ADJ", "전일마감재고 조정") //
        , ERP_STOCK_SCHEDULED_ADJ("ERP_STOCK_TP.SCHEDULED_ADJ", "입고예정 조정") //
        , ERP_STOCK_CONFIRMED_ADJ("ERP_STOCK_TP.CONFIRMED_ADJ", "입고확정 조정") //
        ;

        private final String code;
        private final String codeName;

    }

    //재고 유통기간 상태
    @Getter
    @RequiredArgsConstructor
    public enum StockExprType implements CodeCommEnum {

        NORMAL("STOCK_EXPR_TP.NORMAL", "정상") //
        , IMMINENT("STOCK_EXPR_TP.IMMINENT", "임박") //
        , DISCARD("STOCK_EXPR_TP.DISCARD", "폐기");

        private final String code;
        private final String codeName;

        public static StockEnums.StockExprType findByCode(String code) {
            return Arrays.stream(StockEnums.StockExprType.values())
                    .filter(StockExprType -> StockExprType.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 재고 배치 실행시 예외 메시지
    @Getter
    @RequiredArgsConstructor
    public enum StockBatchExceptionMessage implements MessageCommEnum {

        NOT_REGISTERED_UR_WAREHOUSE_ID("NOT_REGISTERED_UR_WAREHOUSE_ID", "등록되지 않은 출고처 ID") //
        ;

        private final String code;
        private final String message;
    }


    //재고기한관리
  	@Getter
  	@RequiredArgsConstructor
  	public enum StockDeadlineAddDataType implements MessageCommEnum
  	{
  		DUPLICATE_DATA("DUPLICATE_DATA", "이미 등록 된 유통기간 정보 입니다")
  	  , UNABLE_UPDATE_DATA("UNABLE_UPDATE_DATA", "업데이트 할 수 없는 유통기간 정보 입니다.")
  	  , UNABLE_BASICYN_UPDATE_DATA("UNABLE_BASICYN_UPDATE_DATA", "기본설정 Y 한건은 존재해야 합니다.")
  	  ;

  		private final String code;
  		private final String message;
  	}

  	 //ERP 재고 엑셀 업로드 내역
  	@Getter
  	@RequiredArgsConstructor
  	public enum StockExcelUploadErrMsg implements MessageCommEnum
  	{
  	   NO_IL_ITEMCD("NO_IL_ITEMCD", "실패/ERP 품목코드 확인불가")
  	  ,NO_REG_IL_ITEMCD("NO_REG_IL_ITEMCD", "실패/ERP 품목코드 미등록")
  	  ,NO_EXPIRATION_DT("NO_EXPIRATION_DT","실패/유통기한 확인불가")
  	  ,NO_STOCK_QTY("NO_STOCK_QTY", "실패/수량 확인불가")
  	  ,IL_ITEMCD_DUPLICATE("IL_ITEMCD_DUPLICATE", "실패/ERP 품목코드 중복")
  	  ,UPLOAD_REJECT("UPLOAD_REJECT", "업로드 불가 시간대입니다.")
  	  ;
  		private final String code;
  		private final String message;
  	}

    //ERP 재고 수량
  	@Getter
  	@RequiredArgsConstructor
  	public enum StockQtyErrMsg implements MessageCommEnum
  	{
  	   OUT_OF_STOCK("OUT_OF_STOCK","일시품절 상태입니다.");
  		private final String code;
  		private final String message;
  	}


    // 공통코드 등록타입
    @Getter
    @RequiredArgsConstructor
    public enum HistTpCode implements CodeCommEnum {
        INSERT("HIST_TP.INSERT", "신규"),
        UPDATE("HIST_TP.UPDATE", "수정");

        private final String code;
        private final String codeName;
    }

    //출고처ID
    @Getter
    @RequiredArgsConstructor
    public enum UrWarehouseId implements CodeCommEnum {

    	  WAREHOUSE_YONGIN_ID("WAREHOUSE_YONGIN_ID", "용인")
        , WAREHOUSE_BAEKAM_ID("WAREHOUSE_BAEKAM_ID", "백암");

        private final String code;
        private final String codeName;

    }

    //공급처ID
    @Getter
    @RequiredArgsConstructor
    public enum UrSupplierId implements CodeCommEnum {

    	  SUPPLIER_PFF("1", "풀무원식품")
        , SUPPLIER_ORGA("2", "올가식품");

        private final String code;
        private final String codeName;

    }
}
