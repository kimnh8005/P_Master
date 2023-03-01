package kr.co.pulmuone.v1.promotion.linkprice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class LinkPriceAPIResponse {

	@JsonProperty("is_success")
	private boolean isSuccess;

	@JsonProperty("error_message")
	private String errorMessage;

	@JsonProperty("error_code")
	private String errorCode;

	@JsonProperty("order_code")
	private String orderCode;

	@JsonProperty("product_code")
	private String productCode;

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap();
		map.put("successYn", this.isSuccess?"Y":"N");
		map.put("errorMessage", this.errorMessage);
		map.put("errorCode", this.errorCode);
		map.put("productCode", this.productCode);
		map.put("orderCode", this.orderCode);
		return map;
	}
}
