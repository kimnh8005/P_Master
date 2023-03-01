package kr.co.pulmuone.v1.goods.brand.service;

import kr.co.pulmuone.v1.goods.brand.dto.vo.DpBrandListResultVo;
import kr.co.pulmuone.v1.goods.brand.dto.vo.UrBrandListResultVo;

import java.util.List;


public interface GoodsBrandBiz {

    List<UrBrandListResultVo> getUrBrandList() throws Exception;

    List<DpBrandListResultVo> getDpBrandList() throws Exception;

    String getDpBrandTitleById(Long dpBrandId) throws Exception;

}
