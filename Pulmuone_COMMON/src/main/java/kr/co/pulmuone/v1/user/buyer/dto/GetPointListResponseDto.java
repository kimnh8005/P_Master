package kr.co.pulmuone.v1.user.buyer.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetPointInfoVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetPointListResponseDto")
public class GetPointListResponseDto extends BaseResponseDto{


	@ApiModelProperty(value = "")
	private	List<GetPointInfoVo> rows;

	@ApiModelProperty(value = "")
	private long total;


//	@ApiModelProperty(value = "")
//	private String loginInfo;

//	@Builder
//	public GetPointListResponseDto(int total, List<GetPointInfoVo> rows,String loginInfo) {
//		this.total = total;
//		this.rows = rows;
////		this.loginInfo = loginInfo;
//	}
}
