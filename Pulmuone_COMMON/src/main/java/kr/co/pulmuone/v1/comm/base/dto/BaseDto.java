package kr.co.pulmuone.v1.comm.base.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 *	Dto의 최상위 객체 BaseDto
 *
 */
public class BaseDto extends BaseObj {

	@ApiModelProperty(value = "", hidden = true)
	private	String	gbLangId;

	@ApiModelProperty(value = "최고권한 여부", hidden = true)
	private	Boolean masterAuth;

	@ApiModelProperty(value = "", hidden = true)
	public UserVo getUserVo() {
		return SessionUtil.getBosUserVO();
	}

	@ApiModelProperty(value = "", hidden = true)
	public String getGbLangId() {
		if("".equals(StringUtil.nvl(this.gbLangId))) {
			this.gbLangId = this.getUserVo().getLangCode();
		}
		return gbLangId;
	}

	public Boolean getMasterAuth() {
		if (masterAuth == null) {
			masterAuth = false;
			for (String stRoleTpId : Constants.MASTER_AUTH_ST_ROLE_TP_ID) {
				if(this.getUserVo().getListRoleId().contains(stRoleTpId)) {
					masterAuth = true;
				}
			}
		}
		return masterAuth;
	}
}
