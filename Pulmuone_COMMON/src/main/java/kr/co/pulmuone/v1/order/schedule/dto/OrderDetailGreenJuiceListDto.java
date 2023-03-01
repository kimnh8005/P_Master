package kr.co.pulmuone.v1.order.schedule.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * <PRE>
 * Forbiz Korea
 * 주문 상세 녹즙 ERP 전송 리스트 관련 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 2. 23.       석세동         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 녹즙 ERP 전송 리스트 관련 Dto")
public class OrderDetailGreenJuiceListDto {

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문상세 PK")
    private String odOrderDetlId;

    @ApiModelProperty(value = "주문상품 순번")
    private String odOrderDetlSeq;

    @ApiModelProperty(value = "주문경로 ERP I/F 주문경로 필요")
    private String orderHpnCd;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

    @ApiModelProperty(value = "주문자 전화번호")
    private String buyerTel;

    @ApiModelProperty(value = "주문자 휴대폰번호")
    private String buyerHp;

    @ApiModelProperty(value = "주문자 이메일")
    private String buyerMail;

    @ApiModelProperty(value = "수령자명")
    private String recvNm;

    @ApiModelProperty(value = "수령자 전화번호")
    private String recvTel;

    @ApiModelProperty(value = "수령자 휴대폰번호")
    private String recvHp;

    @ApiModelProperty(value = "수령자 주소 전체")
    private String recvAddr;

    @ApiModelProperty(value = "수령자 주소 앞")
    private String recvAddr1;

    @ApiModelProperty(value = "수령자 주소 뒤")
    private String recvAddr2;

    @ApiModelProperty(value = "수령자 주소 건물번호")
    private String recvBldNo;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "고객 요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "출입정보")
    private String doorMsgNm;

    @ApiModelProperty(value = "배송출입 현관 비밀번호")
    private String doorMsg;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "주문수량-주문취소수량")
    private Integer orderCancelCnt;

    @ApiModelProperty(value = "도착예정일자")
    private String deliveryDt;

    @ApiModelProperty(value = "상품판매단가")
    private double salePrice;

    @ApiModelProperty(value = "상품유형")
    private String goodsTpCd;

    @ApiModelProperty(value = "시퀀스번호")
    private String seqNo;

    @ApiModelProperty(value = "주문 상세 일일배송 패턴 PK")
    private long odOrderDetlDailyId;

    @ApiModelProperty(value = "주문 상세 일일배송 패턴 스케줄 PK")
    private long odOrderDetlDailySchId;

    @ApiModelProperty(value = "주문 상세 일일배송 패턴 스케줄 라인번호")
    private Integer odOrderDetlDailySchSeq;

    @ApiModelProperty(value = "주문 상세 일일배송 패턴 스케줄 주문 상태")
    private String orderSchStatus;

    @ApiModelProperty(value = "스케줄 주문 횟수")
    private Integer orderCnt;

    @ApiModelProperty(value = "매장/가맹점 코드")
    private String urStoreId;

    @ApiModelProperty(value = "월요일 수량")
    private int monCnt;

    @ApiModelProperty(value = "화요일 수량")
    private int tueCnt;

    @ApiModelProperty(value = "수요일 수량")
    private int wedCnt;

    @ApiModelProperty(value = "목요일 수량")
    private int thuCnt;

    @ApiModelProperty(value = "금요일 수량")
    private int friCnt;

    @ApiModelProperty(value = "배송기간코드명")
    private String goodsCycleTermTpNm;

	@ApiModelProperty(value = "주문배송지 PK")
	private long odShippingZoneId;

    @ApiModelProperty(value = "배송주기 공통코드")
    private String goodsCycleTp;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "과세여부")
    private String taxYn;

    @ApiModelProperty(value = "납품처ID")
    private int supplierCd;
}
