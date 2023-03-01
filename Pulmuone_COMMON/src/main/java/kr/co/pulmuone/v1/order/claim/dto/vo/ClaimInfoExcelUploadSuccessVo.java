package kr.co.pulmuone.v1.order.claim.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClaimInfoExcelUploadSuccessVo {

    @ApiModelProperty(value = "클레임업로드성공PK")
    private long ifClaimExcelSuccId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문상세순번")
    private int odOrderDetlSeq;

    @ApiModelProperty(value = "클레임수량")
    private int claimCnt;

    @ApiModelProperty(value = "클레임상태구분")
    private String claimStatusTp;

    @ApiModelProperty(value = "BOS 클레임 사유 PK")
    private long psClaimBosId;

    @ApiModelProperty(value = "반품회수여부")
    private String returnsYn;

    @ApiModelProperty(value = "고객상담내용")
    private String consultMsg;

    @ApiModelProperty(value = "실패사유")
    private String failMessage;

    @ApiModelProperty(value = "클레임 엑셀 업로드 GROUP용 컬럼")
    private String excelGroup;


    public ClaimInfoExcelUploadSuccessVo(){}

    public ClaimInfoExcelUploadSuccessVo(ClaimInfoExcelUploadDto dto){
        this.odid               = dto.getOdid();
        this.odOrderDetlSeq     = dto.getOdOrderDetlSeq();
        this.claimCnt           = dto.getClaimCnt();
        this.claimStatusTp      = dto.getClaimStatusTp();
        this.psClaimBosId       = dto.getPsClaimBosId();
        this.returnsYn          = dto.getReturnsYn();
        this.consultMsg         = dto.getConsultMsg();
    }
}
