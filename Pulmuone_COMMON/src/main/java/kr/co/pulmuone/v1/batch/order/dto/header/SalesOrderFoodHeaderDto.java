package kr.co.pulmuone.v1.batch.order.dto.header;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 매출 주문 (풀무원식품) header Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "SalesOrderFoodHeaderDto")
public class SalesOrderFoodHeaderDto {

    @ApiModelProperty(value = "입력시스템 코드값 ESHOP : 온라인몰", required = true)
    private String srcSvr;

    @ApiModelProperty(value = "Header와 Line의 join Key", required = true)
    private String hrdSeq;

    @ApiModelProperty(value = "ERP 전용 key 값", required = true)
    private String oriSysSeq;

    @ApiModelProperty(value = "Header 유형 주문:1, (현재 미사용)취소:2, (현재 미사용)재배송:3, 반품:4 , 5:매출", required = true)
    private String hdrTyp;

    @ApiModelProperty(value = "통합몰 주문번호", required = true)
    private String ordNum;

    @ApiModelProperty(value = "주문일시 (YYYYMMDDHHMISS)")
    private String ordDat;

    @ApiModelProperty(value = "수령자명")
    private String dlvNam;

    @ApiModelProperty(value = "수령자 주소 전체")
    private String dlvAdr;

    @ApiModelProperty(value = "유형 (SALES ORDER, TRANSFER ORDER)")
    private String ordCls;

    @ApiModelProperty(value = "line DTO", required = true)
    private List<?> line;

    @Builder
    public SalesOrderFoodHeaderDto(String srcSvr, String hrdSeq, String oriSysSeq, String hdrTyp, String ordNum, String ordDat,
                                   String dlvNam, String dlvAdr, String ordCls, List<?> line) {
        this.srcSvr = srcSvr;
        this.hrdSeq = hrdSeq;
        this.oriSysSeq = oriSysSeq;
        this.hdrTyp = hdrTyp;
        this.ordNum = ordNum;
        this.ordDat = ordDat;
        this.dlvNam = dlvNam;
        this.dlvAdr = dlvAdr;
        this.ordCls = ordCls;
        this.line = line;
    }

}
