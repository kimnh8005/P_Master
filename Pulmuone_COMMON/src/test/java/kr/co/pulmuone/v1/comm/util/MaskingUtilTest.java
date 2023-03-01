package kr.co.pulmuone.v1.comm.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaskingUtilTest {

    @Test
    void 회원_이름_마스킹처리() {
        assertEquals("가", MaskingUtil.name("가"));
        assertEquals("가*", MaskingUtil.name("가나"));
        assertEquals("가*다", MaskingUtil.name("가나다"));
        assertEquals("가**라", MaskingUtil.name("가나다라"));
        assertEquals("가***마", MaskingUtil.name("가나다라마"));
        assertEquals("가*****사", MaskingUtil.name("가나다라마바사"));
        assertEquals("동***********고", MaskingUtil.name("동해물과 백두산이 마르고"));
        assertEquals("J********************r", MaskingUtil.name("Josepjus Adolphus Amer"));
    }

    @Test
    void 주소1_마스킹처리() {
        assertEquals("경기도 고양시 덕양구 마상로 75번길*****", MaskingUtil.address("경기도 고양시 덕양구 마상로 75번길 12-5"));
        assertEquals("서울특별시 서초구 바우뫼로 37길***", MaskingUtil.address("서울특별시 서초구 바우뫼로 37길 56"));
        assertEquals("서울특별시 강남구 광평로****", MaskingUtil.address("서울특별시 강남구 광평로 280"));
        assertEquals("경기도 고양시 덕양구 주교동", MaskingUtil.address("경기도 고양시 덕양구 주교동"));
    }

    @Test
    void 주소2_마스킹처리() {
        assertEquals("*********", MaskingUtil.addressDetail("111동 104호"));
        assertEquals("*************", MaskingUtil.addressDetail("건영빌딩2층 포비즈코리아"));
        assertEquals("*********", MaskingUtil.addressDetail("로즈데일빌딩 4층"));
        assertEquals("****", MaskingUtil.addressDetail("39-4"));
    }

    @Test
    void 스마트폰_마스킹처리() {
        assertEquals("0101231", MaskingUtil.cellPhone("0101231"));
        assertEquals("010****5678", MaskingUtil.cellPhone("01012345678"));
        assertEquals("010", MaskingUtil.cellPhone("010"));
        assertEquals("010***1234", MaskingUtil.cellPhone("0101231234"));
    }

    @Test
    void 전화번호_마스킹처리() {
        assertEquals("023243", MaskingUtil.telePhone("023243"));
        assertEquals("02****2838", MaskingUtil.telePhone("0228302838"));
        assertEquals("02***2313", MaskingUtil.telePhone("023702313"));
        assertEquals("031***2234", MaskingUtil.telePhone("0319862234"));
        assertEquals("077*****2932", MaskingUtil.telePhone("077077382932"));
        assertEquals("055****7443", MaskingUtil.telePhone("05504737443"));
    }

    @Test
    void 이메일주소_마스킹처리() {
        assertEquals("*@pulmuone.co.kr", MaskingUtil.email("a@pulmuone.co.kr"));
        assertEquals("a*@pulmuone.com", MaskingUtil.email("ab@pulmuone.com"));
        assertEquals("***@pulmuone.net", MaskingUtil.email("abc@pulmuone.net"));
        assertEquals("a***@pulmuone.info", MaskingUtil.email("a.bc@pulmuone.info"));
        assertEquals("kevin.m***@pulmuone.com", MaskingUtil.email("kevin.moon@pulmuone.com"));
        assertEquals("adcdefghij***@pulmuone.com", MaskingUtil.email("adcdefghijklm@pulmuone.com"));
    }

    @Test
    void 계좌번호_마스킹처리() {
        assertEquals("******", MaskingUtil.accountNumber("110334"));
        assertEquals("1******", MaskingUtil.accountNumber("1103345"));
        assertEquals("110334******", MaskingUtil.accountNumber("110334533421"));
        assertEquals("110334534******", MaskingUtil.accountNumber("110334534563443"));
    }

    @Test
    void 생년월일_마스킹처리() {
        assertEquals("******", MaskingUtil.birth("199909"));
        assertEquals("*******", MaskingUtil.birth("1999092"));
        assertEquals("********", MaskingUtil.birth("19990920"));
    }

    @Test
    void 로그인아이디_마스킹처리() {
        assertEquals("pmo", MaskingUtil.loginId("pmo"));
        assertEquals("kev*******", MaskingUtil.loginId("kevin.moon"));
        assertEquals("sky****", MaskingUtil.loginId("sky0034"));
        assertEquals("ski*****", MaskingUtil.loginId("skiekeee"));
    }
}