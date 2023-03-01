package kr.co.pulmuone.v1.system.log.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.system.log.dto.IllegalDetectLogRequestDto;
import kr.co.pulmuone.v1.system.log.dto.IllegalDetectLogResponseDto;
import kr.co.pulmuone.v1.system.log.dto.vo.IllegalDetectLogResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class IllegalDetectLogServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private IllegalDetectLogService service;

    @Test
    void 부정거래탐지_로그_리스트() {
        IllegalDetectLogResponseDto result =  service.getIllegalDetectLogList(new IllegalDetectLogRequestDto());
        boolean total = result.getTotal() > 0;
        assertTrue(total);
    }



    @Test
    void 부정거래_탐지_상세조회() throws Exception {
        IllegalDetectLogRequestDto dto = new IllegalDetectLogRequestDto();
        dto.setStIllegalLogId("-1");

        IllegalDetectLogResultVo vo = service.getIllegalDetectLogDetail(dto);

        Assertions.assertNull(vo);
    }

    @Test
    void 부정거래_탐지_내용_수정() {
        int count = 0;

        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        SessionUtil.setUserVO(userVO);

        IllegalDetectLogRequestDto dto = new IllegalDetectLogRequestDto();
        dto.setStIllegalLogId("1");
        dto.setIllegalStatusType("ILLEGAL_STATUS_TYPE.CLEAR");

        try {
            count = service.putIllegalDetectDetailInfo(dto);
        } catch (Exception e) {

        }

        assertEquals(1, count);
    }


    @Test
    void 부정거래_탐지_리스트_엑셀_다운로드_목록_조회() {
        List<IllegalDetectLogResultVo> result =  service.illegalDetectListExportExcel(new IllegalDetectLogRequestDto());
        boolean total = result.size() > 0;
        assertTrue(total);
    }

    @Test
    void 부정거래_탐지_회원ID_엑셀_다운로드_목록_조회() {
        List<IllegalDetectLogResultVo> result =  service.illegalDetectUserIdxportExcel(new IllegalDetectLogRequestDto());
        boolean total = result.size() > 0;
        assertTrue(total);
    }

    @Test
    void 부정거래_탐지_주문번호_엑셀_다운로드_목록_조회() {
        List<IllegalDetectLogResultVo> result =  service.illegalDetectOrderExportExcel(new IllegalDetectLogRequestDto());
        boolean total = result.size() > 0;
        assertTrue(total);
    }

}
