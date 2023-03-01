package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "회원탈퇴 ResponseDto")
public class DropPageResponseDto {

    @ApiModelProperty(value = "탈퇴사유 구분")
    private List<CodeInfoVo> reason;

    @ApiModelProperty(value = "쿠폰 갯수")
    private int couponCount;

    @ApiModelProperty(value = "사용 가능한 적립금")
    private int pointUsable;

}
