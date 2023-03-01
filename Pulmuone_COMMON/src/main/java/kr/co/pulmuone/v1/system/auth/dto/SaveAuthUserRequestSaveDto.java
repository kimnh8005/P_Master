package kr.co.pulmuone.v1.system.auth.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.constants.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " SaveAuthUserRequestSaveDto")
public class SaveAuthUserRequestSaveDto extends BaseRequestDto {

	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "상점관리키")
	String stShopId;

	public String getStShopId()
	{
		return Constants.ST_SHOP_ID;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "사용자아이디")
	private Long urUserId;

	@ApiModelProperty(value = "사용자 명")
	private String userName;

	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "로그인아이디")
	String loginId;

	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "규칙타입아이디")
	String stRoleTypeId;

}
