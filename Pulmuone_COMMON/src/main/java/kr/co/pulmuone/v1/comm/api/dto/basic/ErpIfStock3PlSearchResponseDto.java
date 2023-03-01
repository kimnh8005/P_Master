package kr.co.pulmuone.v1.comm.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpIfStock3PlSearchResponseDto {

    /*
     * ERP API 3PL재고 수량 조회 dto
     */

    @JsonAlias({ "srcSvr" })
    private String sourceServer; // 입력 시스템 (예:ERP, ORGA …)

    @JsonAlias({ "strKey" })
    private String strKey; // 고객사코드

    @JsonAlias({ "inpDat" })
    private String inputDate; // 입력일 ( 8자리 년월일 )

    @JsonAlias({ "erpItmNo" })
    private String ilItemCd; // ERP 품목번호

    @JsonAlias({ "locCnt" })
    private String stockQty; // 재고수량

    @JsonAlias({ "lifDat" })
    private String expirationDt; // 유통기한

    @JsonAlias({ "ifSeq" })
    private Integer interfaceSequence; // I/F 시퀀스 번호

    @JsonAlias({ "itfFlg" })
    private Boolean interfaceReceivingFlag; // I/F 수신 여부 (Y / N)

    @JsonAlias({ "itfDat" })
    private String interfaceReceivingDate; // I/F 수신 일시 (14자리 년월일시분초)

    @ApiModelProperty(value = "남은 일수")
    private long restDay;

    @ApiModelProperty(value = "출고처ID")
    private long urWarehouseId;

    @ApiModelProperty(value = "기준일")
    private String baseDt;

}
