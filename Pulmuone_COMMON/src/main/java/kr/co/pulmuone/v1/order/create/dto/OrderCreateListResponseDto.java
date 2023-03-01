package kr.co.pulmuone.v1.order.create.dto;


import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.order.create.dto.vo.CreateInfoVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문생성 목록 결과 Response")
public class OrderCreateListResponseDto {
    private List<CreateInfoDto> rows;
    private long total;
}
