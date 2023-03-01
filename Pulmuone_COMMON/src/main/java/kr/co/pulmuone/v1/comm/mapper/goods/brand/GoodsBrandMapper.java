package kr.co.pulmuone.v1.comm.mapper.goods.brand;

import java.util.List;

import kr.co.pulmuone.v1.goods.brand.dto.vo.DpBrandListResultVo;
import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.goods.brand.dto.vo.UrBrandListResultVo;

@Mapper
public interface GoodsBrandMapper {

	List<UrBrandListResultVo> getUrBrandList() throws Exception;

	List<DpBrandListResultVo> getDpBrandList() throws Exception;

	String getDpBrandTitleById(Long dpBrandId) throws Exception;

}
