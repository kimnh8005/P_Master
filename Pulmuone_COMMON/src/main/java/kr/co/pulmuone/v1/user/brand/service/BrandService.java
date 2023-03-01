package kr.co.pulmuone.v1.user.brand.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.user.brand.BrandMapper;
import kr.co.pulmuone.v1.user.brand.dto.AddBrandResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandListRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.PutBrandResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.AddBrandParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandListResultVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandResultVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.PutBrandParamVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BrandService {

	private final BrandMapper brandMapper;

    /**
     * 브랜드 목록 조회
     * @param getBrandListRequestDto
     * @return
     * @throws Exception
     */
    protected Page<GetBrandListResultVo> getBrandList(GetBrandListRequestDto getBrandListRequestDto) throws Exception {
    	PageMethod.startPage(getBrandListRequestDto.getPage(), getBrandListRequestDto.getPageSize());
        return brandMapper.getBrandList(getBrandListRequestDto);
    }

    /**
     * 브랜드 목록 조회 (리스트박스 조회용)
     * @param getBrandRequestDto
     * @return
     * @throws Exception
     */
    protected List<GetBrandListResultVo> searchBrandList(GetBrandRequestDto getBrandRequestDto) throws Exception {
    	List<GetBrandListResultVo> resultVoList = brandMapper.searchBrandList(getBrandRequestDto);
        return resultVoList;
    }

    /**
     * 브랜드 상세 조회
     * @param getBrandParamVo
     * @return
     * @throws Exception
     */
    protected GetBrandResultVo getBrand(GetBrandParamVo getBrandParamVo) throws Exception {
    	GetBrandResultVo resultVo = new GetBrandResultVo();
    	resultVo = brandMapper.getBrand(getBrandParamVo);
    	return resultVo;
    }


    /**
     * 브랜드 등록
     * @param addBrandRequestDto
     * @return
     * @throws Exception
     */
    protected AddBrandResponseDto addBrand(AddBrandParamVo addBrandParamVo) throws Exception {
    	AddBrandResponseDto result = new AddBrandResponseDto();
    	brandMapper.addBrand(addBrandParamVo);
    	return result;
    }



    /**
     * 브랜드 수정
     * @param putBrandParamVo
     * @return
     * @throws Exception
     */
    protected PutBrandResponseDto putBrand(PutBrandParamVo putBrandParamVo) throws Exception {
    	PutBrandResponseDto result = new PutBrandResponseDto();
    	brandMapper.putBrand(putBrandParamVo);
    	return result;
    }


    /**
     * 사용상품 ID 조회
     * @param getBrandParamVo
     * @return
     * @throws Exception
     */
    protected List<GetBrandResultVo> getUrIdList(GetBrandParamVo getBrandParamVo) throws Exception {
    	return brandMapper.getUrIdList(getBrandParamVo);
    }

}
