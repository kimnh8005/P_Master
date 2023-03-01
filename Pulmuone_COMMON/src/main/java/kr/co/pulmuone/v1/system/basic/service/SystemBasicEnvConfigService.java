package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.comm.mapper.system.basic.SystemBasicEnvConfigMapper;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.GetLangListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetLangListResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemBasicEnvConfigService {

    private final SystemBasicEnvConfigMapper systemBasicEnvConfigMapper;

    /**
     * 환경설정 목록
     * @param
     * @return  GetEnvListResponseDto
     * @throws Exception
     */
    protected GetEnvListResponseDto getEnvList() {
        GetEnvListResponseDto result = new GetEnvListResponseDto();

        List<GetEnvListResultVo> rows = systemBasicEnvConfigMapper.getEnvList();
        int total = rows.size();

        result.setRows(rows);
        result.setTotal(total);

        return result;
    }

    /**
     * 다국어정보 목록
     * @param
     * @return  GetLangListResponseDto
     * @throws Exception
     */
    protected GetLangListResponseDto getLangList() {
        GetLangListResponseDto result = new GetLangListResponseDto();

        List<GetLangListResultVo> rows = systemBasicEnvConfigMapper.getLangList();

        int total = rows.size();

        result.setRows(rows);
        result.setTotal(total);

        return result;
    }
}
