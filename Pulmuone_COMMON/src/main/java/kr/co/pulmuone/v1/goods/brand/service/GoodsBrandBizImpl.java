package kr.co.pulmuone.v1.goods.brand.service;

import kr.co.pulmuone.v1.goods.brand.dto.vo.DpBrandListResultVo;
import kr.co.pulmuone.v1.goods.brand.dto.vo.UrBrandListResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsBrandBizImpl implements GoodsBrandBiz {

    @Autowired
    private GoodsBrandService goodsBrandService;

    @Override
    public List<UrBrandListResultVo> getUrBrandList() throws Exception {
        return goodsBrandService.getUrBrandList();
    }

    @Override
    public List<DpBrandListResultVo> getDpBrandList() throws Exception {
        return goodsBrandService.getDpBrandList();
    }

    @Override
    public String getDpBrandTitleById(Long dpBrandId) throws Exception {
        return goodsBrandService.getDpBrandTitleById(dpBrandId);
    }

}
