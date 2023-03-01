package kr.co.pulmuone.v1.user.store.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "매장/가맹점 배송권역 VO")
public class StoreListVo {

	@ApiModelProperty(value = "매장/가맹점 코드")
	private String urStoreId;

	@ApiModelProperty(value = "매장/가맹점 명")
	private String storeName;

	@ApiModelProperty(value = "매장주소")
	private String address;

	@ApiModelProperty(value = "대표번호")
	private String tel;

	@ApiModelProperty(value = "노출여부")
	private String useYn;

	@ApiModelProperty(value = "매장유형")
	private String storeType;

	@ApiModelProperty(value = "매장유형명")
	private String storeTypeName;

	@ApiModelProperty(value = "배송방법")
	private String deliveryType;

	@ApiModelProperty(value = "배송방법명")
	private String deliveryTypeName;

	@ApiModelProperty(value = "운영상태")
	private String status;

	@ApiModelProperty(value = "운영상태명")
	private String statusName;

	@ApiModelProperty(value = "O2O사용여부")
	private String onlineDivYn;

	@ApiModelProperty(value = "매장유형")
	private String storeCategoryName;

	@ApiModelProperty(value = "최근수정일")
	private String modifyDate;

}
