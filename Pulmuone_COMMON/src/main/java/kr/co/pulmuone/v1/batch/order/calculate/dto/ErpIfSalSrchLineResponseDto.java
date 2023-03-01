package kr.co.pulmuone.v1.batch.order.calculate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 매출 확정된 내역 조회 Line ResponseDto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfSalSrchLineResponseDto")
public class ErpIfSalSrchLineResponseDto {

    @ApiModelProperty(value = "중계시스템 고유번호")
    private String ifSeq;

    @ApiModelProperty(value = "전송 대상 시스템 구분 코드값")
    private String crpCd;

    @ApiModelProperty(value = "Header와 Line의 join Key")
    private String hrdSeq;

    @ApiModelProperty(value = "ERP 전용 key 값")
    private String oriSysSeq;

    @ApiModelProperty(value = "통합몰 주문번호")
    private String ordNum;

    @ApiModelProperty(value = "ERP 전용 Order Line Key 값 (주문상품 순번)")
    private String ordNoDtl;

    @ApiModelProperty(value = "정산처리 금액")
    private double setAmt;

    @ApiModelProperty(value = "정산처리 품목 수량")
    private int setItmCnt;

    @ApiModelProperty(value = "정산처리 품목 단가")
    private double setItmPrc;

    @ApiModelProperty(value = "정산처리 여부")
    private String setYn;

    @ApiModelProperty(value = "정산처리 일자")
    private String setDat;

    @ApiModelProperty(value = "정산정보 등록 일시")
    private String setUpdDat;

    @ApiModelProperty(value = "정산처리 수신여부")
    private String itfSetFlg;

    @ApiModelProperty(value = "과세여부")
    private String taxYn;

    @ApiModelProperty(value = "주문 클레임 상세 PK")
    private String refVal01;

    @ApiModelProperty(value = "스케줄라인번호")
    private String schLinNo;

}
