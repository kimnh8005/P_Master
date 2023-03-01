package kr.co.pulmuone.v1.policy.claim.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "쇼핑몰 클레임 사유 목록 요청 DTO")
public class PolicyClaimMallRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "BOS 클레임 사유 카테고리 PK")
	private Long psClaimCtgryId;

    @ApiModelProperty(value = "사유 구분")
    private String searchResaonType;
    
    @ApiModelProperty(value = "사유 구분 리스트")
    private List<String> searchReasonTypeList;

    @ApiModelProperty(value = "클래임 사유(대)")
    private Long searchLClaimCtgryId;

    @ApiModelProperty(value = "클레임 사유(중)")
    private Long searchMClaimCtgryId;

    @ApiModelProperty(value = "귀책처")
    private Long searchSClaimCtgryId;

    @ApiModelProperty(value = "사용여부")
    private String searchUseYn;

    @ApiModelProperty(value = "첨부파일 필수여부")
    private String searchAttcRequiredYn;

    @ApiModelProperty(value = "쇼핑몰 클레임 사유")
    private String searchKeyword;
}
