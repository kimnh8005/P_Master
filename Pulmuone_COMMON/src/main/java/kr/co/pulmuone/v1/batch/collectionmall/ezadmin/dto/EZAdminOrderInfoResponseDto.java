package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo.EZAdminOrderInfoOrderVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "이지어드민 주문조회 API Response DTO")
public class EZAdminOrderInfoResponseDto {

	@ApiModelProperty(value = "API 결과 주문리스트")
    private List<EZAdminOrderInfoOrderVo> orderInfoList;

}
