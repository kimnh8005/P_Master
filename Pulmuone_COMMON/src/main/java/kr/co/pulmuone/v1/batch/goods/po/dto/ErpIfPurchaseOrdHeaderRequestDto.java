package kr.co.pulmuone.v1.batch.goods.po.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfPurchaseOrdHeaderRequestDto")
@Builder
public class ErpIfPurchaseOrdHeaderRequestDto {

	 /*
     * ERP API 구매발주 입력 Header dto
     */
    @ApiModelProperty(value = "입력시스템 코드값 ESHOP : 온라인몰, OERP : 온라인물류ERP, DMPHI : DM PHI, FDDPHI : FDD PHI, LDSPHI : LDS PHI, CJWMS : CJ 물류  ")
    private String srcSvr;

    @ApiModelProperty(value = "ERP 전용 key 값")
    private String oriSysSeq;

    @ApiModelProperty(value = "구분값 : 식품(FD), 올가(ORGA), 푸드머스(FM)")
    private String ordGbn;

    @ApiModelProperty(value = "식품 - FD 올가 - R1, R2 푸드머스 - FM")
    private String ordTyp;

    @ApiModelProperty(value = "발주일")
    private String ordDat;

    @ApiModelProperty(value = "운영조직")
    private long orgId;

    @ApiModelProperty(value = "유형 (SALES ORDER, TRANSFER ORDER)")
    private String ordCls;

    @ApiModelProperty(value = "주문출처(ERS/BABS/ECOS/ECMS/ERF)")
    private String ordSrc;

    @ApiModelProperty(value = "Header와 Line의 join key")
    private String hdrSeq;

    @ApiModelProperty(value = "사업부")
    private String divCd;

    @ApiModelProperty(value = "발주시간마감그룹")
    private String ordCloGrpCd;

    @ApiModelProperty(value = "(ERP 필요 정보)_주문출처(ERS/BABS/ECOS/ECMS/ERF) ID")
    private String ordSrcId;

    @ApiModelProperty(value = "line DTO")
    private List<ErpIfPurchaseOrdLineRequestDto> line;

}
