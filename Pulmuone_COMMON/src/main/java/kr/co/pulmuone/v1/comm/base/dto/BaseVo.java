package kr.co.pulmuone.v1.comm.base.dto;


import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
*
* Vo의 최상위 객체 BaseVo
*
* @author 오영민
*
*/
public class BaseVo extends BaseObj {


	@ApiModelProperty(value = "", hidden = true)
	public UserVo getUserVo() {
		return SessionUtil.getBosUserVO();
	}

}
