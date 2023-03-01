package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClaimInfoExcelUploadDto {

    @ApiModelProperty(value = "클레임 엑셀 정보 PK")
    private long ifClaimExcelInfoId;

    @ApiModelProperty(value = "엑셀 업로드 성공 정보 PK")
    private String ifClaimExcelSuccId;

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

    @ApiModelProperty(value = "성공여부")
    private boolean success;

    @ApiModelProperty(value = "실패사유")
    private String failMessage;
}
