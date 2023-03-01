package kr.co.pulmuone.v1.pg.service.kcp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.v1.comm.enums.PgEnums;
import lombok.Getter;

@Getter
@Component
public class KcpConfig implements Cloneable {

	@Value("${app.domain}")
	private String appDomain;

	/**
	 * log 디렉토리 설정
	 */
	@Value("${kcp.g_conf_log_dir}")
	private String g_conf_log_dir;

	/**
	 * gw rul
	 */
	@Value("${kcp.g_conf_gw_url}")
	private String g_conf_gw_url;

	/**
	 * 표준웹 결제창 g_conf_js_url 설정
	 */
	@Value("${kcp.g_conf_js_url}")
	private String g_conf_js_url;

	/**
	 * 스마트폰 SOAP 통신 설정 (테스트 시 : false 실결제 시 : true)
	 */
	@Value("${kcp.g_conf_server}")
	private boolean g_conf_server;

	/**
	 * 사이트명 설정(한글 불가)
	 */
	@Value("${kcp.g_conf_site_name}")
	private String g_conf_site_name;

	/**
	 * 기본 설정
	 */
	private String g_conf_log_level = "3";
	private String g_conf_gw_port = "8090"; // 포트번호(변경불가)
	private String module_type = "01"; // 변경불가
	private int g_conf_tx_mode = 0; // 변경불가

	/**
	 * 사이트코드
	 */
	private String g_conf_site_cd;

	/**
	 * 사이트키
	 */
	private String g_conf_site_key;

	/**
	 * 정기결제 group id
	 */
	private String kcpgroup_id;

	public KcpConfig getKcpConfig(String pgAccountType) throws Exception {
		KcpConfig result = (KcpConfig) this.clone();
		if (PgEnums.PgAccountType.KCP_SIMPLE.getCode().equals(pgAccountType)) {
			result.g_conf_site_cd = result.simple_site_cd;
			result.g_conf_site_key = result.simple_site_key;
		} else if (PgEnums.PgAccountType.KCP_REGULAR.getCode().equals(pgAccountType)) {
			result.g_conf_site_cd = result.regular_site_cd;
			result.g_conf_site_key = result.regular_site_key;
			result.kcpgroup_id = result.regular_group_id;
		} else {
			result.g_conf_site_cd = result.basic_site_cd;
			result.g_conf_site_key = result.basic_site_key;
		}
		return result;
	}

	public KcpConfig getKcpConfigRemitTest() throws Exception {
		KcpConfig result = (KcpConfig) this.clone();
		result.g_conf_site_cd = "AO01Q";
		result.g_conf_site_key = "";
		result.g_conf_gw_url = "testpaygw.kcp.co.kr";
		result.g_conf_server = false;
		return result;
	}

	/**
	 * 일반결제 사이트코드
	 */
	@Value("${kcp.account.basic.site_cd}")
	private String basic_site_cd;

	/**
	 * 일반결제 사이트키
	 */
	@Value("${kcp.account.basic.site_key}")
	private String basic_site_key;

	/**
	 * 간편결제 사이트코드
	 */
	@Value("${kcp.account.simple.site_cd}")
	private String simple_site_cd;

	/**
	 * 간편결제 사이트키
	 */
	@Value("${kcp.account.simple.site_key}")
	private String simple_site_key;

	/**
	 * 간편결제 사이트코드
	 */
	@Value("${kcp.account.regular.site_cd}")
	private String regular_site_cd;

	/**
	 * 간편결제 사이트키
	 */
	@Value("${kcp.account.regular.site_key}")
	private String regular_site_key;

	/**
	 * 정기결제 group id
	 */
	@Value("${kcp.account.regular.group_id}")
	private String regular_group_id;
}
