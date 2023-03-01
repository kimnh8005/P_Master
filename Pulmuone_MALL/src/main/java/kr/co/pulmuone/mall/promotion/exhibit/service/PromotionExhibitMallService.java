package kr.co.pulmuone.mall.promotion.exhibit.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.exhibit.dto.ExhibitListByUserRequestDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.GreenJuiceOrderRequestDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.GreenJuicePageRequestDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.SelectOrderRequestDto;

public interface PromotionExhibitMallService {

    ApiResult<?> getExhibitListByUser(ExhibitListByUserRequestDto dto) throws Exception;

    ApiResult<?> getNormalByUser(Long evExhibitId) throws Exception;

    ApiResult<?> getSelectListByUser(ExhibitListByUserRequestDto dto) throws Exception;

    ApiResult<?> getSelectByUser(Long evExhibitId) throws Exception;

    ApiResult<?> getGiftByUser(Long evExhibitId) throws Exception;

    ApiResult<?> getGreenJuicePageInfo(GreenJuicePageRequestDto dto) throws Exception;

    ApiResult<?> getGreenJuiceGoods() throws Exception;

    ApiResult<?> addSelectOrder(SelectOrderRequestDto dto) throws Exception;

    ApiResult<?> addGreenJuiceOrder(GreenJuiceOrderRequestDto dto) throws Exception;

}
