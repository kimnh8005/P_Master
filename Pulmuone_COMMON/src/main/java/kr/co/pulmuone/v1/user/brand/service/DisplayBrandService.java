package kr.co.pulmuone.v1.user.brand.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.user.brand.DisplayBrandMapper;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandListRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.AddBrandAndLogoMappingParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandListResultVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class DisplayBrandService {

	private final DisplayBrandMapper displayBrandMapper;

    /**
     * 전시 브랜드 목록 조회
     * @param DisplayBrandListRequestDto
     * @return
     * @throws Exception
     */
    protected Page<DisplayBrandListResultVo> getDisplayBrandList(DisplayBrandListRequestDto displayBrandListRequestDto) throws Exception {
    	PageMethod.startPage(displayBrandListRequestDto.getPage(), displayBrandListRequestDto.getPageSize());
        return displayBrandMapper.getDisplayBrandList(displayBrandListRequestDto);
    }

    /**
     * 전시 브랜드 목록 조회 (리스트박스 조회용)
     * @param DisplayBrandRequestDto
     * @return
     * @throws Exception
     */
    protected List<DisplayBrandListResultVo> searchDisplayBrandList(DisplayBrandRequestDto displayBrandRequestDto) throws Exception {
    	List<DisplayBrandListResultVo> resultVoList = displayBrandMapper.searchDisplayBrandList(displayBrandRequestDto);
        return resultVoList;
    }


    /**
     * 전시 브랜드 상세 조회
     * @param DisplayBrandParamVo
     * @return
     * @throws Exception
     */
    protected DisplayBrandResultVo getDisplayBrand(DisplayBrandParamVo displayBrandParamVo) throws Exception {
    	DisplayBrandResultVo resultVo = new DisplayBrandResultVo();
    	resultVo = displayBrandMapper.getDisplayBrand(displayBrandParamVo);
    	return resultVo;
    }

    /**
     * 전시 브랜드 등록
     * @param addBrandRequestDto
     * @return
     * @throws Exception
     */
    protected DisplayBrandResponseDto addDisplayBrand(DisplayBrandParamVo displayBrandParamVo) throws Exception {
    	DisplayBrandResponseDto result = new DisplayBrandResponseDto();
    	displayBrandMapper.addDisplayBrand(displayBrandParamVo);
    	return result;
    }

    /**
     * 브랜드 첨부파일 등록
     * @param addBrandAndLogoMappingParamVo
     * @return
     * @throws Exception
     */
    protected DisplayBrandResponseDto addDisplayBrandAttachfileMapping(AddBrandAndLogoMappingParamVo addBrandAndLogoMappingParamVo) throws Exception {
    	DisplayBrandResponseDto result = new DisplayBrandResponseDto();
    	displayBrandMapper.addDisplayBrandAttachfileMapping(addBrandAndLogoMappingParamVo);
    	return result;
    }

    /**
     * 브랜드 수정
     * @param putBrandParamVo
     * @return
     * @throws Exception
     */
    protected DisplayBrandResponseDto putDisplayBrand(DisplayBrandParamVo displayBrandParamVo) throws Exception {
    	DisplayBrandResponseDto result = new DisplayBrandResponseDto();
    	displayBrandMapper.putDisplayBrand(displayBrandParamVo);
    	return result;
    }

    /**
     * 브랜드 수정
     * @param delBrandLogoMappingParamVo
     * @return
     * @throws Exception
     */
    protected DisplayBrandResponseDto delDisplayBrandLogoMappingInfo(DisplayBrandParamVo displayBrandParamVo) throws Exception {
    	DisplayBrandResponseDto result = new DisplayBrandResponseDto();
    	displayBrandMapper.delDisplayBrandLogoMappingInfo(displayBrandParamVo);
    	return result;
    }

    /**
     * 사용상품 ID 조회
     * @param getBrandParamVo
     * @return
     * @throws Exception
     */
    protected List<DisplayBrandResultVo> getDpIdList(DisplayBrandParamVo getBrandParamVo) throws Exception {
    	return displayBrandMapper.getDpIdList(getBrandParamVo);
    }


}
