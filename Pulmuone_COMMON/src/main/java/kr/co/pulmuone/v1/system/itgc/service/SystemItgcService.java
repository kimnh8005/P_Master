package kr.co.pulmuone.v1.system.itgc.service;

import kr.co.pulmuone.v1.comm.mapper.system.itgc.SystemItgcMapper;
import kr.co.pulmuone.v1.system.itgc.dto.ItgcRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    2021. 5. 31.               이원호         최초작성
 * =======================================================================
 */
@Service
@RequiredArgsConstructor
public class SystemItgcService {

    private final SystemItgcMapper systemItgcMapper;

    /**
     * itgc List 등록
     *
     * @param insertList List<itgcRequestDto>
     * @return int
     */
    protected int addItgcList(List<ItgcRequestDto> insertList) {
        if (insertList.isEmpty()) {
            return -1;
        }

        return systemItgcMapper.addItgcList(insertList);
    }

}
