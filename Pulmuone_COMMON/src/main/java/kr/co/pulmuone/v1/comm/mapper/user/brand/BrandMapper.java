package kr.co.pulmuone.v1.comm.mapper.user.brand;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.user.brand.dto.GetBrandListRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.AddBrandParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandListResultVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandResultVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.PutBrandParamVo;

@Mapper
public interface BrandMapper {

	Page<GetBrandListResultVo> getBrandList(GetBrandListRequestDto getBrandListRequestDto) throws Exception;

	List<GetBrandListResultVo> searchBrandList(GetBrandRequestDto getBrandRequestDto) throws Exception;

	GetBrandResultVo getBrand(GetBrandParamVo getBrandParamVo) throws Exception;

	int addBrand(AddBrandParamVo addBrandParamVo) throws Exception;

	int putBrand(PutBrandParamVo putBrandParamVo) throws Exception;

	List<GetBrandResultVo> getUrIdList(GetBrandParamVo getBrandParamVo) throws Exception;
}
