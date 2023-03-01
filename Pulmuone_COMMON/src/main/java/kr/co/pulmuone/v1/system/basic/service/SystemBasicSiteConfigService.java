package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.comm.mapper.system.basic.SystemBasicSiteConfigMapper;
import kr.co.pulmuone.v1.system.basic.dto.*;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetPSKeyTypeListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetPsConfigListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetStShopListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetUrGroupResultResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemBasicSiteConfigService {
    private final SystemBasicSiteConfigMapper systemBasicSiteConfigMapper;

    /**
     * 회원그룹정보 목록
     * @param
     * @return GetUrGroupListResponseDto
     * @throws Exception
     */
    public GetUrGroupListResponseDto getUrGroup() {
        GetUrGroupListResponseDto result = new GetUrGroupListResponseDto();

        List<GetUrGroupResultResultVo> rows = systemBasicSiteConfigMapper.getUrGroup();
        int total = rows.size();

        result.setRows(rows);
        result.setTotal(total);

        return result;
    }


    /**
     * 상점정보 목록
     * @param
     * @return  GetStShopListResponseDto
     * @throws Exception
     */
    protected GetStShopListResponseDto getStShop() {
        GetStShopListResponseDto result = new GetStShopListResponseDto();

        List<GetStShopListResultVo> rows = systemBasicSiteConfigMapper.getStShop();
        int total = rows.size();

        result.setRows(rows);
        result.setTotal(total);

        return result;
    }


    /**
     * 상점별 정책정보 목록
     * @param
     * @return  GetPsConfigListResponseDto
     * @throws Exception
     */
    protected GetPsConfigListResponseDto getPsConfig() {
        GetPsConfigListResponseDto result = new GetPsConfigListResponseDto();

        List<GetPsConfigListResultVo> rows = systemBasicSiteConfigMapper.getPsConfig();
        int total = rows.size();

        result.setRows(rows);
        result.setTotal(total);

        return result;
    }


    /**
     * 키값
     * @param getPSKeyTypeRequestDto
     * @return  GetPSKeyTypeListResponseDto
     * @throws Exception
     */
    protected GetPSKeyTypeListResponseDto getPSKeyType(GetPSKeyTypeRequestDto getPSKeyTypeRequestDto) {
        GetPSKeyTypeListResponseDto result = new GetPSKeyTypeListResponseDto();

        List<GetPSKeyTypeListResultVo> rows = systemBasicSiteConfigMapper.getPSKeyType(getPSKeyTypeRequestDto);
        int total = rows.size();

        result.setRows(rows);
        result.setTotal(total);

        return result;
    }


    /**
     * 사이트 정보 목록
     * @param
     * @return Map<String, Object>
     * @throws Exception
     */
    protected Map<String, Object> siteConfigList() {
        Map<String, Object> map = new HashMap<>();

        List<GetUrGroupResultResultVo> listUserGroup = systemBasicSiteConfigMapper.getUrGroup() ;
        List<GetStShopListResultVo>    listStShop    = systemBasicSiteConfigMapper.getStShop()  ;
        List<GetPsConfigListResultVo>  listPsConfig  = systemBasicSiteConfigMapper.getPsConfig();

        map.put("UR_GROUP" , listUserGroup);
        map.put("ST_SHOP"  , listStShop   );
        map.put("PS_CONFIG", listPsConfig );

        return map;
    }

}
