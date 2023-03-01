package kr.co.pulmuone.v1.customer.qna.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaOrderInfoByUserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "1:1 문의 주문 조회 팝업 조회 ResponseDto")
public class OnetooneQnaOrderInfoByUserResponseDto {

    @ApiModelProperty(value = "주문조회 팝업조회 리스트")
    List<OnetooneQnaOrderInfoByUserVo> order;


}
