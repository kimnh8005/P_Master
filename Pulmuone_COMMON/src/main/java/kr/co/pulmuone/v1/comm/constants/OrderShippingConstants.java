package kr.co.pulmuone.v1.comm.constants;

public final class OrderShippingConstants {
    private OrderShippingConstants(){}

    // 선불/착불 여부
    public static final int ORDER_SHIPPING_PAYMETHOD_PREPAYMENT = 1; // 선불
    public static final int ORDER_SHIPPING_PAYMETHOD_CASH_DELIVERY = 2; // 착불

    // 배송방법
    public static final int ORDER_SHIPPING_METHOD_DELIVERY = 1; // 택배

    public static final long FREE_SHIPPING_TEMPLATE_ID = 285; // 무료배송정책 : 주문생성_무료배송비
}
