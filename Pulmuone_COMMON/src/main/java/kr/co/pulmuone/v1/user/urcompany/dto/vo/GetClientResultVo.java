package kr.co.pulmuone.v1.user.urcompany.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetClientResultVo")
public class GetClientResultVo {


	@ApiModelProperty(value = "거래처 ID")
	private String urClientId;

	@ApiModelProperty(value = "공급처 ID")
	private String urCompanyId;

	@ApiModelProperty(value = "회사타입")
	private String companyType;

	@ApiModelProperty(value = "등록 공급처 타입")
	private String inputTpCode;

	@ApiModelProperty(value = "공급업체")
	private String urSupplierId;

	@ApiModelProperty(value = "공급체")
	private String supplierCompany;

	@ApiModelProperty(value = "출고처 ID")
	private String warehouseId;

	@ApiModelProperty(value = "출고처 명")
	private String warehouseName;

	@ApiModelProperty(value = "회사명")
	private String companyName;

	@ApiModelProperty(value = "전화번호1")
	private String accountTelephone1;

	@ApiModelProperty(value = "전화번호2")
	private String accountTelephone2;

	@ApiModelProperty(value = "전화번호3")
	private String accountTelephone3;

	@ApiModelProperty(value = "매장 전화번호1")
	private String shopTelephone1;

	@ApiModelProperty(value = "매장 전화번호2")
	private String shopTelephone2;

	@ApiModelProperty(value = "매장 전화번호3")
	private String shopTelephone3;

	@ApiModelProperty(value = "메일")
	private String accountMail;

	@ApiModelProperty(value = "메모")
	private String accountMemo;

	@ApiModelProperty(value = "등록 사용여부")
	private String inputUseYn;

	@ApiModelProperty(value = "등록일자")
	private String createDate;

	@ApiModelProperty(value = "수정일자")
	private String modifyDate;

	@ApiModelProperty(value = "ERP 코드")
	private String erpCode;

	@ApiModelProperty(value = "채널 코드")
	private String channelCode;

	@ApiModelProperty(value = "매장 ID")
	private String storeId;

	@ApiModelProperty(value = "매장")
	private String store;

	@ApiModelProperty(value = "거래처 공급처 출고처 ID")
	private String urSupplierWarehouseId;

	@ApiModelProperty(value = "판매처 그룹 코드")
	private String sellersGroupCd;

	@ApiModelProperty(value = "판매처 명")
	private String sellersNm;

	@ApiModelProperty(value = "판매처 ID")
	private String omSellersId;

	@ApiModelProperty(value = "알림수신여부")
	private String orderAlarmRevYn;

	@ApiModelProperty(value = "배송상태변경여부")
	private String deliveryStatChgYn;

	@ApiModelProperty(value = "공급처 리스트")
	private	List<GetClientResultVo> clientSupplierWarehouseList;


	@ApiModelProperty(value = "판매처 리스트")
	private	List<GetClientResultVo> clientSellerList;


}
