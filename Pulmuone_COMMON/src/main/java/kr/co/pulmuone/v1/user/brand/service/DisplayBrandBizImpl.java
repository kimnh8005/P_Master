package kr.co.pulmuone.v1.user.brand.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.enums.BrandEnums;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandListRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandListResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.AddBrandAndLogoMappingParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandListResultVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandParamVo;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandResultVo;

@Service
public class DisplayBrandBizImpl implements DisplayBrandBiz{

	@Autowired
    DisplayBrandService displayBrandService;

	@Override
	public ApiResult<?> getDisplayBrandList(DisplayBrandListRequestDto displayBrandListRequestDto) throws Exception {
		DisplayBrandListResponseDto result = new DisplayBrandListResponseDto();

        Page<DisplayBrandListResultVo> resultVoList = displayBrandService.getDisplayBrandList(displayBrandListRequestDto);

        result.setRows(resultVoList.getResult());
        result.setTotal(resultVoList.getTotal());

        return ApiResult.success(result);
    }

	@Override
	public ApiResult<?> searchDisplayBrandList(DisplayBrandRequestDto displayBrandRequestDto) throws Exception {
		DisplayBrandListResponseDto result = new DisplayBrandListResponseDto();

		List<DisplayBrandListResultVo> resultVoList = displayBrandService.searchDisplayBrandList(displayBrandRequestDto);

        result.setRows(resultVoList);


		return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> getDisplayBrand(DisplayBrandRequestDto displayBrandRequestDto) throws Exception {

		DisplayBrandResponseDto brandInfo = new DisplayBrandResponseDto();

		DisplayBrandParamVo getParamVo = DisplayBrandParamVo.builder()
                                        .dpBrandId(displayBrandRequestDto.getDpBrandId())
                                        .rootPath(displayBrandRequestDto.getBaseRoot())
                                        .build() ;

    	DisplayBrandResultVo vo = displayBrandService.getDisplayBrand(getParamVo);

    	List<DisplayBrandResultVo> dpVo = displayBrandService.getDpIdList(getParamVo);

        if(dpVo != null) {
        	vo.setDpIdList(dpVo);
        }

        brandInfo.setRows(vo);


    	return ApiResult.success(brandInfo);
	}

	@Override
	public ApiResult<?> addDisplayBrand(DisplayBrandRequestDto displayBrandRequestDto) throws Exception {

		displayBrandRequestDto.setAddFileList(BindUtil.convertJsonArrayToDtoList(displayBrandRequestDto.getAddFile(), FileVo.class));

		//-----------------------------------------------------------
        //-- 브랜드 정보를 추가한다.
        //-----------------------------------------------------------
		DisplayBrandParamVo addBrandParamVo = DisplayBrandParamVo.builder()
                .dpBrandName   (displayBrandRequestDto.getDpBrandName()   )
                .useYn       (displayBrandRequestDto.getUseYn()       )
                .brandPavilionYn       (displayBrandRequestDto.getBrandPavilionYn()       )
                .build() ;

        //-- 브랜드 정보를 insert한 후 자동증가로 생성한 브랜드 id 를 가져온다.
		displayBrandService.addDisplayBrand(addBrandParamVo);
        String dpBrandId = addBrandParamVo.getDpBrandId();

        List<FileVo> fileList = displayBrandRequestDto.getAddFileList();

        for(int i=0 ; i< fileList.size();i++) {
        	FileVo fileVo = fileList.get(i);

        	String fileType = "";
        	if(fileVo.getFieldName().equals(BrandEnums.BrandLogoFileType.PC_MAIN_FILE.getCode())) {
				fileType = BrandEnums.BrandImageType.PC_MAIN.getCode();
			}else if(fileVo.getFieldName().equals(BrandEnums.BrandLogoFileType.PC_MAIN_OVER_FILE.getCode())) {
				fileType = BrandEnums.BrandImageType.PC_MAIN_OVER.getCode();
			}else if(fileVo.getFieldName().equals(BrandEnums.BrandLogoFileType.TITLE_BANNER_PC_FILE.getCode())) {
				fileType = BrandEnums.BrandImageType.TITLE_BANNER_PC.getCode();
			}else if(fileVo.getFieldName().equals(BrandEnums.BrandLogoFileType.MOBILE_MAIN_FILE.getCode())) {
				fileType = BrandEnums.BrandImageType.MOBILE_MAIN.getCode();
			}else if(fileVo.getFieldName().equals(BrandEnums.BrandLogoFileType.MOBILE_MAIN_OVER_FILE.getCode())) {
				fileType = BrandEnums.BrandImageType.MOBILE_MAIN_OVER.getCode();
			}else if(fileVo.getFieldName().equals(BrandEnums.BrandLogoFileType.TITLE_BANNER_MOBILE_FILE.getCode())) {
				fileType = BrandEnums.BrandImageType.TITLE_BANNER_MOBILE.getCode();
			}

			AddBrandAndLogoMappingParamVo addMappingParamVo = AddBrandAndLogoMappingParamVo.builder()
					.dpBrandId(dpBrandId)
					.imageType(fileType)
					.subPath(fileVo.getServerSubPath())
					.physicalName(fileVo.getPhysicalFileName())
					.originName(fileVo.getOriginalFileName())
					.build();
			displayBrandService.addDisplayBrandAttachfileMapping(addMappingParamVo);

        }

    	return ApiResult.success();
	}

	@Override
    public ApiResult<?> putDisplayBrand(DisplayBrandRequestDto displayBrandRequestDto) throws Exception{

		displayBrandRequestDto.setAddFileList(BindUtil.convertJsonArrayToDtoList(displayBrandRequestDto.getAddFile(), FileVo.class));

    	//-------------------------------------------------------------------------------
        //-- dp_brand 테이블에 브랜드 정보를 수정한다.
        //-------------------------------------------------------------------------------
        DisplayBrandParamVo putParamVo = DisplayBrandParamVo.builder()
                                         .dpBrandId   (displayBrandRequestDto.getDpBrandId()   )
                                         .dpBrandName   (displayBrandRequestDto.getDpBrandName()   )
                                         .useYn       (displayBrandRequestDto.getUseYn()       )
                                         .brandPavilionYn       (displayBrandRequestDto.getBrandPavilionYn()       )
                                         .build() ;

        displayBrandService.putDisplayBrand(putParamVo);

        List<FileVo> fileList = displayBrandRequestDto.getAddFileList();
        if(fileList.size()>0) {
	        for(int i=0 ; i< fileList.size();i++) {
	        	FileVo fileVo = fileList.get(i);

	        	String fileType = "";
				if(fileVo.getFieldName().equals(BrandEnums.BrandLogoFileType.PC_MAIN_FILE.getCode())) {
					fileType = BrandEnums.BrandImageType.PC_MAIN.getCode();
				}else if(fileVo.getFieldName().equals(BrandEnums.BrandLogoFileType.PC_MAIN_OVER_FILE.getCode())) {
					fileType = BrandEnums.BrandImageType.PC_MAIN_OVER.getCode();
				}else if(fileVo.getFieldName().equals(BrandEnums.BrandLogoFileType.TITLE_BANNER_PC_FILE.getCode())) {
					fileType = BrandEnums.BrandImageType.TITLE_BANNER_PC.getCode();
				}else if(fileVo.getFieldName().equals(BrandEnums.BrandLogoFileType.MOBILE_MAIN_FILE.getCode())) {
					fileType = BrandEnums.BrandImageType.MOBILE_MAIN.getCode();
				}else if(fileVo.getFieldName().equals(BrandEnums.BrandLogoFileType.MOBILE_MAIN_OVER_FILE.getCode())) {
					fileType = BrandEnums.BrandImageType.MOBILE_MAIN_OVER.getCode();
				}else if(fileVo.getFieldName().equals(BrandEnums.BrandLogoFileType.TITLE_BANNER_MOBILE_FILE.getCode())) {
					fileType = BrandEnums.BrandImageType.TITLE_BANNER_MOBILE.getCode();
				}

				DisplayBrandParamVo delLogoMappingVo = DisplayBrandParamVo.builder()
								.dpBrandId(displayBrandRequestDto.getDpBrandId())
								.imageType(fileType)
								.build();
				displayBrandService.delDisplayBrandLogoMappingInfo(delLogoMappingVo);

				AddBrandAndLogoMappingParamVo addMappingParamVo = AddBrandAndLogoMappingParamVo.builder()
						.dpBrandId(displayBrandRequestDto.getDpBrandId())
						.imageType(fileType)
						.subPath(fileVo.getServerSubPath())
						.physicalName(fileVo.getPhysicalFileName())
						.originName(fileVo.getOriginalFileName())
						.build();
				displayBrandService.addDisplayBrandAttachfileMapping(addMappingParamVo);
	        }
        }

    	return ApiResult.success();
    }
}
