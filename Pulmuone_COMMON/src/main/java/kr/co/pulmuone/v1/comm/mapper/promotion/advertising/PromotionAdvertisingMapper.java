package kr.co.pulmuone.v1.comm.mapper.promotion.advertising;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.promotion.advertising.dto.AddAdvertisingExternalRequestDto;
import kr.co.pulmuone.v1.promotion.advertising.dto.AdvertisingExternalListRequestDto;
import kr.co.pulmuone.v1.promotion.advertising.dto.AdvertisingExternalRequestDto;
import kr.co.pulmuone.v1.promotion.advertising.dto.AdvertisingTypeRequestDto;
import kr.co.pulmuone.v1.promotion.advertising.dto.vo.AdvertisingExternalVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromotionAdvertisingMapper {

    Page<AdvertisingExternalVo> getAdvertisingExternalList(AdvertisingExternalListRequestDto dto) throws Exception;

    List<AdvertisingExternalVo> getAdvertisingExternal(AdvertisingExternalRequestDto dto) throws Exception;

    void addAdvertisingExternal(@Param("dtoList") List<AddAdvertisingExternalRequestDto> dtoList) throws Exception;

    void putAdvertisingExternal(@Param("dtoList") List<AddAdvertisingExternalRequestDto> dtoList) throws Exception;

    List<GetCodeListResultVo> getAdvertisingType(AdvertisingTypeRequestDto dto) throws Exception;

    int getAdExternalCdCount(String pmAdExternalCd) throws Exception;

    int getAdExternalTypeCount(AddAdvertisingExternalRequestDto dto) throws Exception;

}