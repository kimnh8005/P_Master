package kr.co.pulmuone.v1.comm.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
/**
 * 최상위 ResponseDto 객체
 *
 * @author 오영민
 *
 */
@NoArgsConstructor
public class BaseResponseDto extends BaseObj {

	@JsonProperty("RETURN_CODE")
	@JsonSerialize(using =  ToStringSerializer.class)
	@ApiModelProperty(value = "")
	private String RETURN_CODE;

	@JsonProperty("RETURN_MSG")
	@JsonSerialize(using =  ToStringSerializer.class)
	@ApiModelProperty(value = "")
	private String RETURN_MSG;

	public String getRETURN_CODE() {
		if(StringUtil.isNvl(this.RETURN_CODE)){
			return BaseEnums.Default.SUCCESS.getCode();
		}
		return RETURN_CODE;
	}

	public BaseResponseDto(MessageCommEnum returnCode) {
		this.RETURN_CODE = returnCode.getCode();
		this.RETURN_MSG = returnCode.getMessage();
	}

}
