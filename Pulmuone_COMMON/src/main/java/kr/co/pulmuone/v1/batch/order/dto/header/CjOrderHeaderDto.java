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
 * 백암물류 주문 header Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "CjOrderHeaderDto")
public class CjOrderHeaderDto {

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

    @ApiModelProperty(value = "주문자명")
    private String ordNam;

    @ApiModelProperty(value = "수령자명")
    private String dlvNam;

    @ApiModelProperty(value = "수령자 주소 전체")
    private String dlvAdr;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String dlvZip;

    @ApiModelProperty(value = "수령자 전화번호 (0000-0000-0000)")
    private String dlvTel;

    @ApiModelProperty(value = "수령자 휴대폰번호 (0000-0000-0000)")
    private String dlvMobTel;

    @ApiModelProperty(value = "배송메시지")
    private String dlvMsg;

    @ApiModelProperty(value = "CJ통합물류 코드값 O999", required = true)
    private String shiToCj;

    @ApiModelProperty(value = "line DTO", required = true)
    private List<?> line;

    @Builder
    public CjOrderHeaderDto(String srcSvr, String hrdSeq, String oriSysSeq, String hdrTyp, String ordNum, String ordNam,
                            String dlvNam, String dlvAdr, String dlvZip, String dlvTel, String dlvMobTel, String dlvMsg,
                            String shiToCj, List<?> line) {
        this.srcSvr = srcSvr;
        this.hrdSeq = hrdSeq;
        this.oriSysSeq = oriSysSeq;
        this.hdrTyp = hdrTyp;
        this.ordNum = ordNum;
        this.ordNam = ordNam;
        this.dlvNam = dlvNam;
        this.dlvAdr = dlvAdr;
        this.dlvZip = dlvZip;
        this.dlvTel = dlvTel;
        this.dlvMobTel = dlvMobTel;
        this.dlvMsg = dlvMsg;
        this.shiToCj = shiToCj;
        this.line = line;
    }

}
