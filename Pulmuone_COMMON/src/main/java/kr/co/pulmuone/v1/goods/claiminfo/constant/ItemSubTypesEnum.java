package kr.co.pulmuone.v1.goods.claiminfo.constant;

public enum ItemSubTypesEnum {
	  BASIC("기본", "BASIC", new String[]{"BASIC"})
	, COMMON("공통", "COMMON", new String[]{"BASIC", "NORMAL", "DISPOSAL", "DAILY", "PACKAGE"})
	, SHOP_ONLY("매장전용", "SHOP_ONLY", new String[]{"BASIC"})
	, INCORPOREITY("무형", "INCORPOREITY", new String[]{"BASIC"})
	, RENTAL("랜탈", "RENTAL", new String[]{"BASIC"})
	;

	private String name;
	private String code;
	private String subCodes[];

	ItemSubTypesEnum(String name, String code, String subCodes[]) {
		this.name = name;
		this.code = code;
		this.subCodes = subCodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String[] getSubCodes() {
		return subCodes;
	}

	public void setSubCodes(String[] subCodes) {
		this.subCodes = subCodes;
	}

}
