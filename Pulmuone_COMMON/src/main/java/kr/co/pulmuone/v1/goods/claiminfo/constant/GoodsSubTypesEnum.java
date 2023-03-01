package kr.co.pulmuone.v1.goods.claiminfo.constant;

public enum GoodsSubTypesEnum {
	  BASIC("기본", "", new String[]{"BASIC"})
	, NORMAL("일반", "NORMAL", new String[]{"BASIC", "NORMAL", "NORMAL_DAWN", "SHOP_DELIVERY"})
	, DISPOSAL("폐기임박", "DISPOSAL", new String[]{"BASIC", "NORMAL", "NORMAL_DAWN"})
	, DAILY("일일", "DAILY", new String[]{"BASIC", "NORMAL", "DAILY"})
	, PACKAGE("묶음", "PACKAGE", new String[]{"BASIC", "NORMAL", "NORMAL_DAWN"})
	;

	private String name;
	private String code;
	private String subCodes[];

	GoodsSubTypesEnum(String name, String code, String subCodes[]) {
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
