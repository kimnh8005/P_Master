package kr.co.pulmuone.v1.comm.mapper.statics.pm;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.statics.pm.dto.PromotionStaticsRequestDto;
import kr.co.pulmuone.v1.statics.pm.dto.vo.PromotionStaticsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PromotionStaticsMapper {

    Page<PromotionStaticsVo> getStaticsInternalAdvertisingList(PromotionStaticsRequestDto dto) throws BaseException;

    Page<PromotionStaticsVo> getStaticsAdvertisingList(PromotionStaticsRequestDto dto) throws BaseException;

    Page<PromotionStaticsVo> getStaticsCouponSaleStatusList(PromotionStaticsRequestDto dto) throws BaseException;

    Page<PromotionStaticsVo> getStaticsUserGroupCouponStatusList(PromotionStaticsRequestDto dto) throws BaseException;

    Page<PromotionStaticsVo> getStaticsPointStatusList(PromotionStaticsRequestDto dto) throws BaseException;

    List<PromotionStaticsVo> getAdvertisingType(PromotionStaticsRequestDto dto) throws Exception;

    Page<PromotionStaticsVo> getStaticsAdvertisingGoodsList(PromotionStaticsRequestDto dto) throws BaseException;
}
