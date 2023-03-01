package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistReservationOptionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ApiModel(description = "상품 변경내역 Vo")
public class GoodsChangeLogVo {
	@ApiModelProperty(value = "상품 ID")
	private String ilGoodsId;

	@ApiModelProperty(value = "테이블명")
	private String tableNm;

	@ApiModelProperty(value = "수정전테이블 ID")
	private String tableIdOrig;

	@ApiModelProperty(value = "수정후테이블 ID")
	private String tableIdNew;

	@ApiModelProperty(value = "수정전데이타")
	private String beforeData;

	@ApiModelProperty(value = "수정후데이타")
	private String afterData;

	@ApiModelProperty(value = "수정컬럼네임")
	private String columnNm;

	@ApiModelProperty(value = "수정컬럼명")
	private String columnLabel;

	@ApiModelProperty(value = "등록자")
	private String createId;

	@ApiModelProperty(value = "등록일")
	private String createDate;
}