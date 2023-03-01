package kr.co.pulmuone.v1.comm.mapper.system.shopconfig;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.GetSystemShopConfigListRequestDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.SaveSystemShopConfigRequestSaveDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.vo.GetSystemShopConfigListResultVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface  SystemShopConfigMapper {

    Page<GetSystemShopConfigListResultVo> getSystemShopConfigList(GetSystemShopConfigListRequestDto getSystemShopConfigListRequestDto);

    int addSystemShopConfig(List<SaveSystemShopConfigRequestSaveDto> insertRequestDtoList);

    int putSystemShopConfig(List<SaveSystemShopConfigRequestSaveDto> updateRequestDtoList);

    int delSystemShopConfig(List<SaveSystemShopConfigRequestSaveDto> deleteRequestDtoList);
}
