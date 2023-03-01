package kr.co.pulmuone.v1.pg.service.inicis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.v1.comm.enums.PgEnums;
import lombok.Getter;

@Getter
@Component
public class InicisConfig implements Cloneable {

	@Value("${app.domain}")
	private String appDomain;

	@Value("${inicis.payScriptUrl}")
	private String payScriptUrl;

	@Value("${inicis.closeScriptUrl}")
	private String closeScriptUrl;

	private String mid;

	private String signKey;

	private String cancelPassword;

	private String key;

	private String iv;

	public InicisConfig getInicisConfig(String pgAccountType) throws Exception {
		InicisConfig result = (InicisConfig) this.clone();
		if (PgEnums.PgAccountType.INICIS_ADMIN.getCode().equals(pgAccountType)) {
			result.mid = result.admin_mid;
			result.signKey = result.admin_signKey;
			result.cancelPassword = result.admin_cancelPassword;
			result.key = result.admin_key;
			result.iv = result.admin_iv;
		} else {
			result.mid = result.basic_mid;
			result.signKey = result.basic_signKey;
			result.cancelPassword = result.basic_cancelPassword;
			result.key = result.basic_key;
			result.iv = result.basic_iv;
		}
		return result;
	}

	@Value("${inicis.account.basic.mid}")
	private String basic_mid;

	@Value("${inicis.account.basic.signKey}")
	private String basic_signKey;

	@Value("${inicis.account.basic.cancelPassword}")
	private String basic_cancelPassword;

	@Value("${inicis.account.basic.key}")
	private String basic_key;

	@Value("${inicis.account.basic.iv}")
	private String basic_iv;

	@Value("${inicis.account.admin.mid}")
	private String admin_mid;

	@Value("${inicis.account.admin.signKey}")
	private String admin_signKey;

	@Value("${inicis.account.admin.cancelPassword}")
	private String admin_cancelPassword;

	@Value("${inicis.account.admin.key}")
	private String admin_key;

	@Value("${inicis.account.admin.iv}")
	private String admin_iv;
}
