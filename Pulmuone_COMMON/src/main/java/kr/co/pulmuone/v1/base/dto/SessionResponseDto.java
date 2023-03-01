package kr.co.pulmuone.v1.base.dto;


import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " SessionResponseDto")
public class SessionResponseDto extends BaseResponseDto {

    private UserVo session;

}
