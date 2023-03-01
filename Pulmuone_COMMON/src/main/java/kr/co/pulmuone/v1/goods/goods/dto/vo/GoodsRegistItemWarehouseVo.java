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
@ApiModel(description = "배송유형, 배송정책 Vo")
public class GoodsRegistItemWarehouseVo {

	//품목별 출고처별 배송유형 리스트
	@ApiModelProperty(value = "품목별 출고처 관리 ID")
	String ilItemWarehouseId;

	@ApiModelProperty(value = "공급처_출고처 ID")
	String urSupplierWarehouseId;

	@ApiModelProperty(value = "공급사 ID")
	String urSupplierId;

	@ApiModelProperty(value = "출고처 ID")
	String urWarehouseId;

	@ApiModelProperty(value = "배송유형")
	String deliveryTypeName;

	@ApiModelProperty(value = "배송CODE")
	String deliveryTypeCode;

	@ApiModelProperty(value = "출고처")
	String warehouseName;

	@ApiModelProperty(value = "출고처 우편번호")
	String zipCd;

	@ApiModelProperty(value = "출고처 주소 앞자리")
	String address1;

	@ApiModelProperty(value = "출고처 주소 뒷자리")
	String address2;

	//배송 정책
	@ApiModelProperty(value = "배송정책 템플릿 ID")
	String ilShippingTemplateId;

	@ApiModelProperty(value = "배송정책 템플릿 원본 ID")
	String orgIlShippingTemplateId;

	@ApiModelProperty(value = "ERP 발주유형")
	String erpPoType;

	@ApiModelProperty(value = "발주유형명")
	String poTypeName;

	@ApiModelProperty(value = "배송정책 상세보기 팝업 버튼 보여주기 유무")
	boolean poTypeDetailPopupYn;

	@ApiModelProperty(value = "ERP품목명")
	String erpItemName;

	@ApiModelProperty(value = "품목 ID")
	String ilItemCode;

	@ApiModelProperty(value = "발주유형 ID")
	String ilPoTypeId;

	@ApiModelProperty(value = "배송정책 템플릿 명")
	String shppingTemplateName;
}
