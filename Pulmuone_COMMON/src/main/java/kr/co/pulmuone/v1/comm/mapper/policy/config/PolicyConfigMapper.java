package kr.co.pulmuone.v1.comm.mapper.policy.config;

import kr.co.pulmuone.v1.goods.goods.dto.RegularShippingConfigDto;
import kr.co.pulmuone.v1.policy.config.dto.vo.MetaConfigVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PolicyConfigMapper {

    RegularShippingConfigDto getRegularShippingConfig() throws Exception;

    String getConfigValue(String psKey) throws Exception;

    List<MetaConfigVo> getMetaConfig() throws Exception;

}
