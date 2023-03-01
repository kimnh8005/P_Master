package kr.co.pulmuone.bos.comm.constant;

public enum StoreType {
	DIRECT("STORE_TYPE.DIRECT","매장"),
	BRANCH("STORE_TYPE.BRANCH","가맹점");

	private String code;
	private String name;

	StoreType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	public String getCode() {
		return this.code;
	}
	public String getName() {
		return this.name;
	}
}