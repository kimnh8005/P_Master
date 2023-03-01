package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo.EZAdminOrderInfoOrderVo;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo.EZAdminQnaInfoVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "이지어드민 문의글조회 API Response DTO")
public class EZAdminQnaInfoResponseDto {

	@ApiModelProperty(value = "API 결과 주문리스트")
    private List<EZAdminQnaInfoVo> qnaInfoList;

    @ApiModelProperty(value = "API 호출 실패 count")
    private int failCount;

    
}
