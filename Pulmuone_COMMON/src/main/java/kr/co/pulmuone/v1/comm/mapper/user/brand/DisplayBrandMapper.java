package kr.co.pulmuone.v1.comm.mapper.user.brand;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandListRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.AddBrandAndLogoMappingParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandListResultVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandResultVo;

@Mapper
public interface DisplayBrandMapper {

	Page<DisplayBrandListResultVo> getDisplayBrandList(DisplayBrandListRequestDto displayBrandListRequestDto) throws Exception;

	List<DisplayBrandListResultVo> searchDisplayBrandList(DisplayBrandRequestDto displayBrandRequestDto) throws Exception;

	DisplayBrandResultVo getDisplayBrand(DisplayBrandParamVo displayBrandParamVo) throws Exception;

	int addDisplayBrand(DisplayBrandParamVo displayBrandParamVo) throws Exception;

	int addDisplayBrandAttachfileMapping(AddBrandAndLogoMappingParamVo addBrandAndLogoMappingParamVo) throws Exception;

	int putDisplayBrand(DisplayBrandParamVo displayBrandParamVo) throws Exception;

	int delDisplayBrandLogoMappingInfo(DisplayBrandParamVo displayBrandParamVo) throws Exception;

	List<DisplayBrandResultVo> getDpIdList(DisplayBrandParamVo displayBrandParamVo) throws Exception;
}
