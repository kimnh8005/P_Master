package kr.co.pulmuone.v1.promotion.exhibit.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.exhibit.dto.*;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.ExhibitInfoFromMetaVo;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.GiftListVo;

import java.util.List;

public interface PromotionExhibitBiz {

    ExhibitListByUserResponseDto getExhibitListByUser(ExhibitListByUserRequestDto dto) throws Exception;

    ApiResult<?> getNormalByUser(ExhibitRequestDto dto) throws Exception;

    SelectListByUserResponseDto getSelectListByUser(ExhibitListByUserRequestDto dto) throws Exception;

    ApiResult<?> getSelectByUser(ExhibitRequestDto dto) throws Exception;

    ApiResult<?> addSelectOrder(SelectOrderRequestDto dto) throws Exception;

    ApiResult<?> addGreenJuiceOrder(GreenJuiceOrderRequestDto dto) throws Exception;

    List<GiftListVo> getGiftList(GiftListRequestDto dto) throws Exception;

    ApiResult<?> getGiftByUser(ExhibitRequestDto dto) throws Exception;

    SelectExhibitResponseDto getSelectExhibit(Long evExhibitId) throws Exception;

    List<Long> getGreenJuiceGoods(Long urSupplierId) throws Exception;

    ExhibitInfoFromMetaVo getExhibitFromMeta(Long evExhibitId) throws Exception;

}