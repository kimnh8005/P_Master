package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistApprRequestDto;
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
public class ItemRegistApprRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "상품승인 ID")
	private String ilItemApprId;

	@ApiModelProperty(value = "승인종류 유형 공통코드(APPR_KIND_TP.ITEM_REGIST : 품목등록, APPR_KIND_TP.ITEM_CLIENT : 거래처 품목 승인관리)", required = true)
	private String apprKindTp;

	@ApiModelProperty(value = "1차 승인자, 2차 승인자 구분", required = true)
	private String apprManagerTp;

	@ApiModelProperty(value = "1차/2차 승인 담당자", required = true)
	private String apprUserId;

	@ApiModelProperty(value = "승인자 LOGIN ID")
	private String apprLoginId;

}
