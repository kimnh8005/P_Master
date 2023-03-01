package kr.co.pulmuone.v1.goods.item.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemPoListResultVo {

	@ApiModelProperty(value = "순번")
	private int rownum;

	@ApiModelProperty(value = "발주SEQ")
	private long ilPoId;

	@ApiModelProperty(value = "박스입수량")
	private long pcsPerBox;

	@ApiModelProperty(value = "안전재고")
	private long targetStock;

	@ApiModelProperty(value = "권고수량")
	private long recommendPoQty;

	@ApiModelProperty(value = "낱개발주수량")
	private long piecePoQty;

	@ApiModelProperty(value = "BOX발주수량")
	private double boxPoQty;

	@ApiModelProperty(value = "행사발주수량")
	private long eventPoQty;

	@ApiModelProperty(value = "시스템 추가발주수량")
	private long poSystemQty;

	@ApiModelProperty(value = "낱개 발주수량 합계")
	private float sumPoUserQty;

	@ApiModelProperty(value = "BOX 발주수량 합계")
	private float sumBoxPoQty;

	@ApiModelProperty(value = "전일마감재고")
	private long stockClosed;

	@ApiModelProperty(value = "당일입고확정수량")
	private long stockConfirmed;

	@ApiModelProperty(value = "예상잔여수량")
	private long expectedResidualQty;

	@ApiModelProperty(value = "D0 폐기예정수량")
	private long stockDiscardD0;

	@ApiModelProperty(value = "D1 폐기예정수량")
	private long stockDiscardD1;

	@ApiModelProperty(value = "당일입고예정수량")
	private long stockScheduledD0;

	@ApiModelProperty(value = "D+1 입고예정수량")
	private long stockScheduledD1;

	@ApiModelProperty(value = "D+2 입고예정수량")
	private long stockScheduledD2;

	@ApiModelProperty(value = "D+3 입고예정수량")
	private long stockScheduledD3;

	@ApiModelProperty(value = "D+4 입고예정수량")
	private long stockScheduledD4;

	@ApiModelProperty(value = "D+5 입고예정수량")
	private long stockScheduledD5;

	@ApiModelProperty(value = "D+6 입고예정수량")
	private long stockScheduledD6;

	@ApiModelProperty(value = "D+7 입고예정수량")
	private long stockScheduledD7;

	@ApiModelProperty(value = "D+8 이후 입고예정수량")
	private long stockScheduledD8More;

	@ApiModelProperty(value = "D+1 이후입고예정수량")
	private long stockScheduledD1More;

	@ApiModelProperty(value = "직전 3주 출고수량")
	private long outbound3weekTotal;

	@ApiModelProperty(value = "직전 2주 출고수량")
	private long outbound2weekTotal;

	@ApiModelProperty(value = "직전 1주 출고수량")
	private long outbound1weekTotal;

	@ApiModelProperty(value = "직전 3주 출고수량 평균")
	private long outbound3weekAvg;

	@ApiModelProperty(value = "직전 2주 출고수량 평균")
	private long outbound2weekAvg;

	@ApiModelProperty(value = "직전 1주 출고수량 평균")
	private long outbound1weekAvg;

	@ApiModelProperty(value = "D0 출고예정수량")
	private long outbound0;

	@ApiModelProperty(value = "D1 출고예정수량")
	private long outbound1;

	@ApiModelProperty(value = "D2 출고예정수량")
	private long outbound2;

	@ApiModelProperty(value = "D3이상 출고예정수량")
	private long outbound3More;

	@ApiModelProperty(value = "일평균 출고량")
	private long outboundDayAvg;

	@ApiModelProperty(value = " OFF재고")
	private long offStock;

	@ApiModelProperty(value = "ERP 프로모션 기간 평균(올가전용)")
	private long erpEventOrderAvg;

	@ApiModelProperty(value = "30일 평균(올가전용)")
	private long nonErpEventOrderAvg;

	@ApiModelProperty(value = "결품예상일")
	private long missedOutbound;

	@ApiModelProperty(value = "품목별 출고처ID")
	private long ilItemWarehouseId;

	@ApiModelProperty(value = "상품코드")
	private String ilGoodsId;

	@ApiModelProperty(value = "저장시간/관리자")
	private String strManager;

	@ApiModelProperty(value = "올가 오프라인 발주상태")
	private String poProRea;

	@ApiModelProperty(value = "ERP 행사정보")
	private String erpEvent;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "품목명")
	private String itemNm;

	@ApiModelProperty(value = "아이템바코드")
	private String barcode;

	@ApiModelProperty(value = "보관방법")
	private String storageMethodNm;

	@ApiModelProperty(value = "유통기간")
	private int distributionPeriod;

	@ApiModelProperty(value = "판매상태명")
	private String saleStatusNm;

	@ApiModelProperty(value = "출고처ID")
	private String urWarehouseId;

	@ApiModelProperty(value = "출고처")
	private String warehouseNm;

	@ApiModelProperty(value = "공급업체코드")
	private String supplierCd;

	@ApiModelProperty(value = "공급업체명")
	private String supplierName;

	@ApiModelProperty(value = "발주유형 SEQ")
	private long ilPoTpId;

	@ApiModelProperty(value = "발주유형 템플릿명")
	private String poTpTemplateNm;

	@ApiModelProperty(value = "발주유형명")
	private String poTpNm;

	@ApiModelProperty(value = "발주유형 마감시간")
	private String poTpDeadline;

	@ApiModelProperty(value = "전시유무")
	private String dispYn;

	@ApiModelProperty(value = "표준카테고리(대분류)명")
	private String ctgryStdNm;

	@ApiModelProperty(value = "행사발주수량")
	private String poEventQty;

	@ApiModelProperty(value = "입고예정일자")
	private String scheduledDt;

	@ApiModelProperty(value = "등록일")
	private String createDt;

	@ApiModelProperty(value = "입고예정일자")
	private String stockScheduledDt;

	@ApiModelProperty(value = "메모")
	private String memo;

	@ApiModelProperty(value = "수정일")
	private String modifyDt;

	@ApiModelProperty(value = "사용자 입력 수량")
	private long poUserQty;

	@ApiModelProperty(value = "관리자에 의한 최종 저장 시각")
	private String userSavedDt;

	@ApiModelProperty(value = "최종 저장 관리자명")
	private String userNm;

	@ApiModelProperty(value = "최종 저장 관리자 ID")
	private String loginId;

	@ApiModelProperty(value = "엑셀 품목코드/바코드")
	private String excelIlItemCd;

	@ApiModelProperty(value = "관리자(엑셀용)")
    private String excelManager;

	@ApiModelProperty(value = "발주마감시각 이후 여부")
    private String isPoExpired;

	@ApiModelProperty(value = "발주가능여부")
    private String isPoPossible;

	@ApiModelProperty(value = "판매상태")
    private String saleStatus;

	@ApiModelProperty(value = "전시")
    private String dispStr;

	@ApiModelProperty(value = "기준일자")
    private String baseDt;

	@ApiModelProperty(value = "발주정보")
    private String strPoInfo;

	@ApiModelProperty(value = "ERP 카테고리 (대분류)")
    private String erpCtgryLv1Id;

	@ApiModelProperty(value = "상품 외부몰 판매 상태")
    private String goodsOutmallSaleStatNm;

	@ApiModelProperty(value = "발주여부")
	private String poIfYn;

	@JsonProperty("CODE")
	@ApiModelProperty(value = "")
	private String code;

	@JsonProperty("NAME")
	@ApiModelProperty(value = "")
	private String name;
}
