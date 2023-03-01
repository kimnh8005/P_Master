package kr.co.pulmuone.v1.comm.mapper.system.basic;

import kr.co.pulmuone.v1.system.basic.dto.GetPSKeyTypeRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetPSKeyTypeListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetPsConfigListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetStShopListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetUrGroupResultResultVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemBasicSiteConfigMapper {
    List<GetUrGroupResultResultVo> getUrGroup();

    List<GetStShopListResultVo>    getStShop();

    List<GetPsConfigListResultVo>  getPsConfig();

    List<GetPSKeyTypeListResultVo> getPSKeyType(GetPSKeyTypeRequestDto getPSKeyTypeRequestDto);
}
