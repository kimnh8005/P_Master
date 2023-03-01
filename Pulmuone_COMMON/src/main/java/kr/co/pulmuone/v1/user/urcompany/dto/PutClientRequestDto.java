package kr.co.pulmuone.v1.user.urcompany.dto;

import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.comm.constants.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PutClientRequestDto")
public class PutClientRequestDto extends BaseRequestDto{



	@JsonSerialize(using =  ToStringSerializer.class)
	@ApiModelProperty(value = "공급처ID" ,required = false)
	private String urSupplierId;

	@ApiModelProperty(value = "타입코드")
	private String inputTpCode;

	@ApiModelProperty(value = "공급처")
	private String supplierCompany;

	@ApiModelProperty(value = "출고처ID")
	private String warehouseId;

	@ApiModelProperty(value = "매장")
	private String store;

	@ApiModelProperty(value = "채널코드")
	private String channelCode;

	@ApiModelProperty(value = "ERP ID")
	private String erpId;

	@ApiModelProperty(value = "회사명")
	private String companyName;

	@ApiModelProperty(value = "메일")
	private String accountMail;

	@ApiModelProperty(value = "전화번호 1")
	private String accountTelephone1;

	@ApiModelProperty(value = "전화번호 2")
	private String accountTelephone2;

	@ApiModelProperty(value = "전화번호 3")
	private String accountTelephone3;

	@ApiModelProperty(value = "매장 전화번호 1")
	private String shopTelephone1;

	@ApiModelProperty(value = "매장 전화번호 2")
	private String shopTelephone2;

	@ApiModelProperty(value = "매장 전화번호 3")
	private String shopTelephone3;


	@ApiModelProperty(value = "메모")
	private String accountMemo;

	@ApiModelProperty(value = "사용여부")
	private String inputUseYn;

	@ApiModelProperty(value = "회사 ID")
	private String urCompanyId;

	@ApiModelProperty(value = "거래처 ID")
	private String urClientId;

	@ApiModelProperty(value = "ERP 코드")
	private String erpCode;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "알림수신여부")
	private String orderAlarmRevYn;

	@ApiModelProperty(value = "배송상태변경여부")
	private String deliveryStatChgYn;


	public ArrayList<String> getWarehouseIdList() {
		if(!StringUtil.isEmpty(this.warehouseId)) {
			return new ArrayList<String>(Arrays.asList(warehouseId.split(Constants.ARRAY_SEPARATORS)));
		}
		return new ArrayList<String>();
	}

	@ApiModelProperty(value = "거래처공급처출고처 ID")
	private String urSupplierWarehouse;

	public ArrayList<String> getUrSupplierWarehouseList() {
		if(!StringUtil.isEmpty(this.urSupplierWarehouse)) {
			return new ArrayList<String>(Arrays.asList(urSupplierWarehouse.split(Constants.ARRAY_SEPARATORS)));
		}
		return new ArrayList<String>();

	}

	@ApiModelProperty(value = "판매처 ID")
	private String omSellersId;


	public ArrayList<String> getOmSellsersIdList() {

		if(!StringUtil.isEmpty(this.omSellersId)) {
			return new ArrayList<String>(Arrays.asList(omSellersId.split(Constants.ARRAY_SEPARATORS)));
		}

		return new ArrayList<String>();

	}

}
