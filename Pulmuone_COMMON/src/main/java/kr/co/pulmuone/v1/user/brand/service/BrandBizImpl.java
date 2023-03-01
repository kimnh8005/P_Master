package kr.co.pulmuone.v1.user.brand.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.brand.dto.AddBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.AddBrandResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandListRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandListResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.PutBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.PutBrandResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.AddBrandParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandResultVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandListResultVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandResultVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.PutBrandParamVo;

@Service
public class BrandBizImpl implements BrandBiz{

	@Autowired
    BrandService brandService;

	@Override
	public ApiResult<?> getBrandList(GetBrandListRequestDto getBrandListRequestDto) throws Exception {
		GetBrandListResponseDto result = new GetBrandListResponseDto();

        Page<GetBrandListResultVo> resultVoList = brandService.getBrandList(getBrandListRequestDto);

        result.setRows(resultVoList.getResult());
        result.setTotal(resultVoList.getTotal());

        return ApiResult.success(result);
    }


	@Override
	public ApiResult<?> searchBrandList(GetBrandRequestDto getBrandRequestDto) throws Exception {
		GetBrandListResponseDto result = new GetBrandListResponseDto();

		List<GetBrandListResultVo> resultVoList = brandService.searchBrandList(getBrandRequestDto);

        result.setRows(resultVoList);


		return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> getBrand(GetBrandRequestDto getBrandRequestDto) throws Exception {


        GetBrandResponseDto brandInfo = new GetBrandResponseDto();

    	GetBrandParamVo getParamVo = GetBrandParamVo.builder()
                                        .urBrandId(getBrandRequestDto.getUrBrandId())
                                        .build() ;

        GetBrandResultVo vo = brandService.getBrand(getParamVo);

    	List<GetBrandResultVo> urVo = brandService.getUrIdList(getParamVo);

        if(urVo != null) {
        	vo.setUrIdList(urVo);
        }


        brandInfo.setRows(vo);


    	return ApiResult.success(brandInfo);
	}

	@Override
	public ApiResult<?> addBrand(AddBrandRequestDto addBrandRequestDto) throws Exception {

		//-----------------------------------------------------------
        //-- 브랜드 정보를 추가한다.
        //-----------------------------------------------------------
    	AddBrandParamVo addBrandParamVo = AddBrandParamVo.builder()
                .brandName   (addBrandRequestDto.getBrandName()   )
                .urSupplierId(addBrandRequestDto.getUrSupplierId())
                .useYn       (addBrandRequestDto.getUseYn()       )
                .build() ;

        //-- 브랜드 정보를 insert한 후 자동증가로 생성한 브랜드 id 를 가져온다.
    	brandService.addBrand(addBrandParamVo);

        AddBrandResponseDto addBrandResult = new AddBrandResponseDto();

    	return ApiResult.success(addBrandResult);
	}


    @Override
    public ApiResult<?> putBrand(PutBrandRequestDto putBrandRequestDto) throws Exception{

    	//-------------------------------------------------------------------------------
        //-- ur_brand 테이블에 브랜드 정보를 수정한다.
        //-------------------------------------------------------------------------------
        PutBrandResponseDto putBrandResult = new PutBrandResponseDto();

        PutBrandParamVo putParamVo = PutBrandParamVo.builder()
                                         .urBrandId   (putBrandRequestDto.getUrBrandId()   )
                                         .brandName   (putBrandRequestDto.getBrandName()   )
                                         .urSupplierId(putBrandRequestDto.getUrSupplierId())
                                         .useYn       (putBrandRequestDto.getUseYn()       )
                                         .build() ;

        brandService.putBrand(putParamVo);

    	return ApiResult.success(putBrandResult);
    }

}
