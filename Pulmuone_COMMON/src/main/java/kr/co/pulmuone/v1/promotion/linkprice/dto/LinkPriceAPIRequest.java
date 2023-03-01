package kr.co.pulmuone.v1.promotion.linkprice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import kr.co.pulmuone.v1.promotion.linkprice.service.ISO8601DateSerializer;
import lombok.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class LinkPriceAPIRequest{

	@JsonProperty("order")
	private Order order;
	@JsonProperty("products")
	private List<Product> products = new ArrayList<>();
	@JsonProperty("linkprice")
	private LinkPrice linkPrice;

	public LinkPriceAPIRequest(LinkPriceDto dto) {
		this.order = new Order(dto);
		this.linkPrice = new LinkPrice(dto);
	}

	public void setProducts(List<LinkPriceOrderDetailVo> list) {
		if (list != null) {
			List<Product> productList = new ArrayList<>();
			for (LinkPriceOrderDetailVo vo : list) {
				Product prd = new Product(vo);
				productList.add(prd);
			}
			this.products = productList;
		}
		setOrderFinalPaidPrice();
	}

	private void setOrderFinalPaidPrice() {
		int sum = 0;
		for (Product product : products) {
			sum += product.getProductFinalPrice();
		}
		this.order.finalPaidPrice = sum;
	}

	@Getter
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Order {
		@JsonProperty("order_id")
		private String orderId;

		@JsonProperty("final_paid_price")
		private long finalPaidPrice;

		@JsonProperty("currency")
		private final String currency = PromotionConstants.LP_CURRENCY_CODE;

		@JsonProperty("user_name")
		private String userName;

		private Order(LinkPriceDto dto) {
			this.orderId = dto.getOrderId();
			this.userName = dto.getUrUserName();
		}
	}


	@Getter
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Product {
		@JsonProperty("product_id")
		private String productId;

		@JsonProperty("product_name")
		private String productName;

		@JsonProperty("category_code")
		private String categoryCode;

		@JsonProperty("category_name")
		private List<String> categoryNames;

		@JsonProperty("quantity")
		private int quantity;

		@JsonProperty("product_final_price")
		private long productFinalPrice;

		@JsonProperty("paid_at")
		@JsonSerialize(using = ISO8601DateSerializer.class)
		private Date paidAt;

		@JsonProperty("confirmed_at")
		@JsonSerialize(using = ISO8601DateSerializer.class)
		private Date confirmedAt;

		@JsonProperty("canceled_at")
		@JsonSerialize(using = ISO8601DateSerializer.class)
		private Date canceledAt;

		public Product(LinkPriceOrderDetailVo vo) {
			this.productId = vo.getIlGoodsId();
			this.productName = vo.getGoodsNm();
			this.categoryCode = vo.getCategoryCode();
			this.categoryNames = vo.getCategoryNames();
			this.quantity = vo.getOrderCnt();
			this.productFinalPrice = vo.getPaidPrice();
			this.paidAt = vo.getPaidDt();
			this.confirmedAt = null;
			this.canceledAt = null;
		}
	}


	@Getter
	@ToString
	@EqualsAndHashCode
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public class LinkPrice {
		@JsonProperty("merchant_id")
		private String merchantId = PromotionConstants.LP_MERCHANT_ID;

		@JsonProperty("lpinfo")
		private String lpinfo;

		@JsonProperty("user_agent")
		private String userAgent;

		@JsonProperty("device_type")
		private String deviceType;

		@JsonProperty("remote_addr")
		private String remoteAddr;

		private LinkPrice(LinkPriceDto dto) {
			this.lpinfo = urlDecode(dto.getLpinfo());
			this.userAgent = dto.getUserAgent();
			this.deviceType = dto.getDeviceType();
			this.remoteAddr = dto.getIp();
		}

		private String urlDecode(String s) {
			try {
				return URLDecoder.decode(s, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return s;
			}
		}
	}
}
