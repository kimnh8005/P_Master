package kr.co.pulmuone.v1.base.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetUserGroupListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원그룹 조회 Response")
public class GetUserGroupListResponseDto extends BaseResponseDto{

	private	List<GetUserGroupListResultVo>	rows;
	private	int	total;

}
