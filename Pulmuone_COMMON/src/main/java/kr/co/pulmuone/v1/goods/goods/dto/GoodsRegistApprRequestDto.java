package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ApiModel(description = "상품등록승인 Request")
public class GoodsRegistApprRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "상품승인 ID")
	private String ilGoodsApprId;

	@ApiModelProperty(value = "승인종류 유형 공통코드(APPR_KIND_TP.GOODS_REGIST : 상품등록, APPR_KIND_TP.GOODS_CLIENT : 거래처 상품수정)", required = true)
	private String apprKindTp;

	@ApiModelProperty(value = "1차 승인자, 2차 승인자 구분", required = true)
	private String apprManagerTp;

	@ApiModelProperty(value = "1차/2차 승인 담당자", required = true)
	private String apprUserId;

	@ApiModelProperty(value = "승인자 LOGIN ID")
	private String apprLoginId;
}
