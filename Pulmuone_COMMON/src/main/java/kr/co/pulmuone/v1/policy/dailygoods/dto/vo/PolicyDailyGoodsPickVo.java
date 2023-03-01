package kr.co.pulmuone.v1.policy.dailygoods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "일일배송골라담기 VO")
public class PolicyDailyGoodsPickVo extends BaseRequestDto{

	@ApiModelProperty(value = "일일배송골라담기.상품 SEQ")
	private String goodsId;

	@ApiModelProperty(value = "일일배송골라담기.상품 품목 코드")
	private String itemCode;

	@ApiModelProperty(value = "일일배송골라담기.상품명")
	private String goodsName;

	@ApiModelProperty(value = "일일배송골라담기.상품유형 공통코드(GOODS_TYPE)")
	private String goodsTp;

	@ApiModelProperty(value = "일일배송골라담기.상품유형 공통코드명")
	private String goodsTpName;

	@ApiModelProperty(value = "일일배송골라담기.공급업체 PK")
	private String supplierId;

	@ApiModelProperty(value = "일일배송골라담기.회사정보(공급처) PK")
	private String supplierCompanyId;

	@ApiModelProperty(value = "일일배송골라담기.회사명")
	private String supplierName;

	@ApiModelProperty(value = "일일배송골라담기.브랜드 PK")
	private String brandId;

	@ApiModelProperty(value = "일일배송골라담기.브랜드 명")
	private String brandName;

	@ApiModelProperty(value = "일일배송골라담기.일일상품 골라담기 허용여부(Y:허용)")
	private String pickableYn;

	@ApiModelProperty(value = "녹즙 클렌즈 옵션 사용여부(Y:사용)")
	private String greenJuiceCleanseOptYn;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;

	// rownum
	private String rnum;
}
