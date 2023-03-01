package kr.co.pulmuone.bos.comm.constant;

public enum BosDomainPrefixEnum {
    UR("User", "회원") //
    , IL("Item Listing", "상품") //
    , SN("Send", "전송관리") //
    , CS("Customer Support", "고객지원") //
    , DP("Display", "전시") //
    , QR("Qrcode", "QR코드") //
    , ST("System", "시스템")
    , PS("Policy", "정책")
    ;

    private String fullDomainName;
    private String koreanDomainName;

    BosDomainPrefixEnum(String fullDomainName, String koreanDomainName) {
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
