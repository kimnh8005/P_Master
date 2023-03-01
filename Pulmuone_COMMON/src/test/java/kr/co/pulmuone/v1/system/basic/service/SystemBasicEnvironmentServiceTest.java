package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveEnvironmentRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveEnvironmentRequestSaveDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class SystemBasicEnvironmentServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private SystemBasicEnvironmentService service;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void 환경설정_리스트() {
        GetEnvironmentListResponseDto result = service.getEnvironmentList(new GetEnvironmentListRequestDto());

        boolean booleanFlag = result.getRows().size() > 0;

        assertTrue(booleanFlag);
    }

    @Test
    void 환경설정_저장() {
        SaveEnvironmentRequestSaveDto insDto = new SaveEnvironmentRequestSaveDto();
        insDto.setEnvironmentKey("TEST_TEST");
        insDto.setEnvironmentValue("test");
        insDto.setEnvironmentName("테스트");
        insDto.setComment("테스트 코멘트");

        GetEnvironmentListResultVo result = service.getEnvironmentList(new GetEnvironmentListRequestDto()).getRows().get(0);

        SaveEnvironmentRequestSaveDto putDto = new SaveEnvironmentRequestSaveDto();
        putDto.setEnvironmentKey(result.getEnvironmentKey());
        putDto.setEnvironmentValue(result.getEnvironmentValue());
        putDto.setEnvironmentName(result.getEnvironmentName());
        putDto.setComment(result.getComment());
        putDto.setStEnvId(result.getStEnvId());


        SaveEnvironmentRequestDto param = new SaveEnvironmentRequestDto();
        param.getInsertRequestDtoList().add(insDto);
        param.getUpdateRequestDtoList().add(putDto);
        param.getDeleteRequestDtoList().add(putDto);

        //유효성체크
        String validFlag = service.checkEnvironmentDuplicate(param.getInsertRequestDtoList()).getCode();
        assertEquals("0000", validFlag);


        //상세
        GetEnvironmentListRequestDto dtlDto = new GetEnvironmentListRequestDto();
        dtlDto.setSearchEnvironmentKey(result.getEnvironmentKey());

        GetEnvironmentListResultVo dtlResult = service.getEnvironment(dtlDto);

        assertEquals(result.getEnvironmentKey(), dtlResult.getEnvironmentKey());

        //저장
        ApiResult<?> apiResult = service.saveEnvironment(param);
        assertEquals("0000", apiResult.getCode());


        SaveEnvironmentRequestSaveDto saveDto = new SaveEnvironmentRequestSaveDto();
        saveDto.setStEnvId(dtlResult.getStEnvId());
        saveDto.setEnvironmentValue(dtlResult.getEnvironmentValue());
        GetEnvironmentListResponseDto putResult = service.putEnvironmentEnvVal(saveDto);
        assertNotNull(putResult);


        //GetEnvironmentListResponseDto putEnvironmentEnvVal(SaveEnvironmentRequestSaveDto saveEnvironmentRequestSaveDto)



        //		UPDATE ST_ENV
        //		SET
        //			 ENV_VAL		= #{environmentValue}
        //			,MODIFY_ID 		= #{userVo.userId}
        //		WHERE ST_ENV_ID     = #{stEnvId}
    }
}