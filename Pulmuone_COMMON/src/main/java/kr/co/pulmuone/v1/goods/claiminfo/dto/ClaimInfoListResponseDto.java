package kr.co.pulmuone.v1.goods.claiminfo.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.claiminfo.dto.vo.ClaimInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ClaimInfoListResponseDto")
public class ClaimInfoListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "목록 레코드")
	private	List<ClaimInfoVo> rows;

}