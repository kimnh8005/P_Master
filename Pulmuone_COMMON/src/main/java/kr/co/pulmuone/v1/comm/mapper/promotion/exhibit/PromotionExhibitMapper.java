package kr.co.pulmuone.v1.comm.mapper.promotion.exhibit;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.promotion.exhibit.dto.ExhibitListByUserRequestDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.GiftListRequestDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromotionExhibitMapper {

    Page<ExhibitListByUserVo> getExhibitListByUser(ExhibitListByUserRequestDto dto) throws Exception;

    NormalByUserVo getNormalByUser(@Param("evExhibitId") Long evExhibitId, @Param("deviceType") String deviceType) throws Exception;

    List<ExhibitGroupByUserVo> getGroupByUser(Long evExhibitId) throws Exception;

    List<Long> getGroupDetailByUser(Long evExhibitGroupId) throws Exception;

    Page<SelectListByUserVo> getSelectListByUser(ExhibitListByUserRequestDto dto) throws Exception;

    List<Long> getSelectGoodsByUser(Long evExhibitId) throws Exception;

    List<SelectAddGoodsVo> getSelectAddGoods(Long evExhibitId) throws Exception;

    SelectByUserVo getSelectByUser(@Param("evExhibitId") Long evExhibitId, @Param("deviceType") String deviceType) throws Exception;

    List<GiftListVo> getGiftList(GiftListRequestDto dto) throws Exception;

    GiftByUserVo getGiftByUser(@Param("evExhibitId") Long evExhibitId, @Param("deviceType") String deviceType) throws Exception;

    List<GiftGoodsVo> getGiftGoods(Long evExhibitId) throws Exception;

    List<Long> getGiftTargetGoods(Long evExhibitId) throws Exception;

    List<Long> getGiftTargetBrand(Long evExhibitId) throws Exception;

    SelectExhibitVo getSelectExhibit(@Param("evExhibitId") Long evExhibitId) throws Exception;

    List<Long> getGreenJuiceGoods(Long urSupplierId) throws Exception;

    List<ExhibitUserGroupByUserVo> getUserGroup(@Param("evExhibitId") Long evExhibitId) throws Exception;

    ExhibitInfoFromMetaVo getExhibitInfoFromMeta(Long evExhibitId) throws Exception;

}