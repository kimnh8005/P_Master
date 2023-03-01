package kr.co.pulmuone.v1.goods.goods.dto.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "일일상품 일일 판매 옵션설정 Vo")
public class GoodsDailyCycleBulkVo
{
	@ApiModelProperty(value="식단주기 ID")
	private String ilGoodsDailyCycleId;

	@ApiModelProperty(value="상품ID")
	private String ilGoodsId;

	/* 식단주기선택 관련 시작 */
	@ApiModelProperty(value = "식단주기 유형 공통코드")
	private String goodsCycleType;

	@ApiModelProperty(value = "식단주기 기간 유형 공통코드")
	private String goodsCycleTermType;
	/* 식단주기선택 관련 시작 */

	/* 일괄배달 정보 관련 시작 */
	@ApiModelProperty(value = "일괄배달정보 ID")
	private String ilGoodsDailyBulkId;

	@ApiModelProperty(value = "식단주기 기간 유형 공통코드")
	private String goodsBulkType;
	/* 일괄배달 정보 관련 시작 */

	@ApiModelProperty(value = "등록자 ID")
	private String createId;

	@ApiModelProperty(value = "수정자 ID")
	private String modifyId;

}