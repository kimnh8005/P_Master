package kr.co.pulmuone.v1.system.shopconfig.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.mapper.system.shopconfig.SystemShopConfigMapper;

import kr.co.pulmuone.v1.system.shopconfig.dto.basic.GetSystemShopConfigListRequestDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.SaveSystemShopConfigRequestSaveDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.vo.GetSystemShopConfigListResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemShopConfigService {

    @Autowired
    private SystemShopConfigMapper systemShopConfigMapper;

    /**
     * @Desc 상점별 세부정책 사항 조회
     * @param getSystemShopConfigListRequestDto
     * @return Page<GetSystemShopConfigListResultVo>
     */
    protected Page<GetSystemShopConfigListResultVo> getSystemShopConfigList(GetSystemShopConfigListRequestDto getSystemShopConfigListRequestDto) {
        PageMethod.startPage(getSystemShopConfigListRequestDto.getPage(), getSystemShopConfigListRequestDto.getPageSize());
        return systemShopConfigMapper.getSystemShopConfigList(getSystemShopConfigListRequestDto);
    }

    /**
     * @Desc 상점별 세부정책 사항 저장
     * @param insertRequestDtoList
     * @return int
     */
    protected int addShopConfig(List<SaveSystemShopConfigRequestSaveDto> insertRequestDtoList) {
        return systemShopConfigMapper.addSystemShopConfig(insertRequestDtoList);
    }

    /**
     * @Desc 상점별 세부정책 사항 수정
     * @param updateRequestDtoList
     * @return int
     */
    protected int putShopConfig(List<SaveSystemShopConfigRequestSaveDto> updateRequestDtoList) {
        return systemShopConfigMapper.putSystemShopConfig(updateRequestDtoList);
    }

    /**
     * @Desc 상점별 세부정책 사항 삭제
     * @param deleteRequestDtoList
     * @return int
     */
    protected int delShopConfig(List<SaveSystemShopConfigRequestSaveDto> deleteRequestDtoList) {
        return systemShopConfigMapper.delSystemShopConfig(deleteRequestDtoList);
    }
}
