package kr.co.pulmuone.v1.comm.constants;

public final class CustomerConstants {
    private CustomerConstants(){}

	public static final int FEEDBACK_DAY = 30;    //후기 작성 가능기간

    public static final int BEST_COUNT = 10;      //후기 추천 베스트 기준

    // 후기 - 등록 - 후기유형 분류 기준 start
    public static final int PHOTO_IMAGE_N = 1;
    public static final int PHOTO_TEXT_N = 1;
    public static final int PREMIUM_IMAGE_N = 1;
    public static final int PREMIUM_TEXT_N = 100;
    // 후기 - 등록 - 후기유형 분류 기준 end

    public static final String REWARD_ALWAYS = "2999-12-31";    //고객보상제 - 상시여부

}
