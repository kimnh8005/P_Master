package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * Java 에서 코드성 값을 사용해야 할때 여기에 추가해서 사용
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 7. 15.                jg          최초작성
 * =======================================================================
 * </PRE>
 */
public class CompanyEnums {

    // 회사구분 (CLIENT: 거래처, SUPPLIER: 공급처, HEADQUARTERS: 본사,  공통코드 : COMPANY_TYPE)
    @Getter
    @RequiredArgsConstructor
    public enum CompanyType implements CodeCommEnum {
        CLIENT("COMPANY_TYPE.CLIENT", "거래처"),
        SUPPLIER("COMPANY_TYPE.SUPPLIER", "공급처"),
        HEADQUARTERS("COMPANY_TYPE.HEADQUARTERS", "본사");

        private final String code;
        private final String codeName;
    }

    // 벤더 오류 메시지
    @Getter
	@RequiredArgsConstructor
    public enum vendorCheckMessage implements MessageCommEnum {
    	DUPCHECK_CMT("VENDOR_MESSAGE.DUPCHECK_CMT","이미 등록 된 외부몰 입니다. 확인 후 등록 바랍니다.");

        private final String code;
        private final String message;
    }

    // 공지 구분
    @Getter
    @RequiredArgsConstructor
    public enum BosBBSType implements CodeCommEnum {
        CLIENT("BOS_BBS_TYPE.COMMON", "공통"),
        SUPPLIER("BOS_BBS_TYPE.ADMIN", "내부관리자"),
        HEADQUARTERS("BOS_BBS_TYPE.COMPANY", "거래처");

        private final String code;
        private final String codeName;
    }

    // 거래처 타입 (CLIENT: 출고처, SHOP: 매장, VENDOR: 벤더,  공통코드 : CLIENT_TYPE)
    @Getter
    @RequiredArgsConstructor
    public enum ClientType implements CodeCommEnum {
        CLIENT("CLIENT_TYPE.CLIENT", "출고처"),
        SUPPLIER("CLIENT_TYPE.SHOP", "매장"),
        HEADQUARTERS("CLIENT_TYPE.VENDOR", "벤더");

        private final String code;
        private final String codeName;
	}

// 매장 오류 메시지
    @Getter
    @RequiredArgsConstructor
    public enum shopCheckMessage implements MessageCommEnum {
        DUPCHECK_CMT("SHOP_MESSAGE.DUPCHECK_CMT","이미 등록 된 매장입니다.");

        private final String code;
        private final String message;
    }
}
