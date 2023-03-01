package kr.co.pulmuone.mall.comm.constant;

public enum MallDomainPrefixEnum {
    UR("User", "회원") //
    , IL("Item Listing", "상품") //
    , SN("Send", "전송관리") //
    , CS("Customer Support", "고객지원") //
    , FB("FeedBack", "후기") //
    ;

    private String fullDomainName;
    private String koreanDomainName;

    MallDomainPrefixEnum(String fullDomainName, String koreanDomainName) {
        this.fullDomainName = fullDomainName;
        this.koreanDomainName = koreanDomainName;
    }

    private String getFullDomainName() {
        return this.fullDomainName;
    }

    private String getKoreanDomainName() {
        return this.koreanDomainName;
    }

}
