package kr.co.pulmuone.bos.goods.certification;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationRequestDto;
import kr.co.pulmuone.v1.goods.etc.service.GoodsCertificationBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 상품인증정보관리
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 07. 14.               박영후          최초작성
*  1.0    2020. 10. 8.                손진구          NEW 변경
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class GoodsCertificationController {
    private final GoodsCertificationBiz goodsCertificationBiz;

    @ApiOperation(value = "상품인증정보 목록 조회")
	@PostMapping(value = "/admin/goods/certification/getIlCertificationList")
	public ApiResult<?> getIlCertificationList(HttpServletRequest request, CertificationListRequestDto certificationListRequestDto) throws Exception{

        return ApiResult.success(goodsCertificationBiz.getIlCertificationList(BindUtil.bindDto(request, CertificationListRequestDto.class)));
	}

    @ApiOperation(value = "상품인증정보 상세 조회")
    @GetMapping(value = "/admin/goods/certification/getIlCertification")
    @ApiImplicitParams({ @ApiImplicitParam(name = "ilCertificationId", value = "상품인증정보 PK", required = true, dataType = "long") })
    public ApiResult<?> getIlCertification(@RequestParam(value = "ilCertificationId", required = true) long ilCertificationId){

        return ApiResult.success(goodsCertificationBiz.getIlCertification(ilCertificationId));
    }

    @ApiOperation(value = "상품인증정보 추가")
	@PostMapping(value = "/admin/goods/certification/addIlCertification")
	public ApiResult<?> addIlCertification(CertificationRequestDto certificationRequestDto) throws Exception{

        certificationRequestDto.setAddFileList(BindUtil.convertJsonArrayToDtoList(certificationRequestDto.getAddFile(), FileVo.class));

        return goodsCertificationBiz.addIlCertification(certificationRequestDto);
	}

    @ApiOperation(value = "상품인증정보 수정")
	@PostMapping(value = "/admin/goods/certification/putIlCertification")
	public ApiResult<?> putIlCertification(CertificationRequestDto certificationRequestDto) throws Exception{

        if (certificationRequestDto.getAddFile() != null) {
            certificationRequestDto.setAddFileList(BindUtil.convertJsonArrayToDtoList(certificationRequestDto.getAddFile(), FileVo.class));
        }

        return goodsCertificationBiz.putIlCertification(certificationRequestDto);
	}

    @ApiOperation(value = "상품인증정보 삭제")
	@PostMapping(value = "/admin/goods/certification/delIlCertification")
	public ApiResult<?> delIlCertification(@RequestBody CertificationRequestDto certificationRequestDto) throws Exception{

        return goodsCertificationBiz.delIlCertification(certificationRequestDto);
	}
}