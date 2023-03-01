package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "추가상품 Vo")
public class GoodsRegistAdditionalGoodsVo {

	@ApiModelProperty(value="추가상품맵핑 ID")
	String ilGoodsAdditionalGoodsMappingId;

	@ApiModelProperty(value="추가상품 ID")
	String targetGoodsId;

	@ApiModelProperty(value="판매가")
	int salePrice;

	@ApiModelProperty(value="원가")
	int standardPrice;

	@ApiModelProperty(value="정상가")
	int recommendedPrice;

	@ApiModelProperty(value="상품명")
	String goodsName;

	@ApiModelProperty(value="등록자 ID")
	String createId;

	@ApiModelProperty(value="등록일")
	String createDate;

	@ApiModelProperty(value="수정자 ID")
	String modifyId;

	@ApiModelProperty(value="수정일")
	String modifyDate;

	@ApiModelProperty(value="품목코드")
	String ilItemCode;

	@ApiModelProperty(value="품목출고처 ID")
	String ilItemWarehouseId;

	@ApiModelProperty(value="재고연동/미연동 여부")
	String stockOrderYn;
}
