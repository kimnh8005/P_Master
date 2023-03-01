package kr.co.pulmuone.v1.goods.etc.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationListResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.CertificationVo;


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
@Service
public class GoodsCertificationBizImpl  implements GoodsCertificationBiz {

    @Autowired
    GoodsCertificationService goodsCertificationService;

    /**
     * @Desc 상품인증정보 조회
     * @param certificationListRequestDto
     * @return ApiResult
     */
    @Override
    @UserMaskingRun(system = "BOS")
    public CertificationListResponseDto getIlCertificationList(CertificationListRequestDto certificationListRequestDto){
        CertificationListResponseDto certificationListResponseDto = new CertificationListResponseDto();

        Page<CertificationVo> certificationList = goodsCertificationService.getIlCertificationList(certificationListRequestDto);

        certificationListResponseDto.setTotal(certificationList.getTotal());
        certificationListResponseDto.setRows(certificationList.getResult());

        return certificationListResponseDto;
    }

    /**
     * @Desc 상품인증정보 상세 정보 조회
     * @param ilCertificationId
     * @return ApiResult
     */
    @Override
    @UserMaskingRun(system = "BOS")
    public CertificationResponseDto getIlCertification(long ilCertificationId){
        CertificationResponseDto certificationResponseDto = new CertificationResponseDto();

        CertificationVo certificationVo = goodsCertificationService.getIlCertification(ilCertificationId);
        certificationResponseDto.setCertificationInfo(certificationVo);

        return certificationResponseDto;
    }

    /**
     * @Desc 상품인증정보 추가
     * @param certificationRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addIlCertification(CertificationRequestDto certificationRequestDto) throws Exception{

        if( CollectionUtils.isNotEmpty(certificationRequestDto.getAddFileList()) ) {
            for(FileVo fileVo : certificationRequestDto.getAddFileList()) {
                certificationRequestDto.setImagePath(fileVo.getServerSubPath());
                certificationRequestDto.setImageName(fileVo.getPhysicalFileName());
                certificationRequestDto.setImageOriginName(fileVo.getOriginalFileName());
            }
        }

        goodsCertificationService.addIlCertification(certificationRequestDto);

        return ApiResult.success();
    }

    /**
     * @Desc 상품인증정보 수정
     * @param certificationRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putIlCertification(CertificationRequestDto certificationRequestDto) throws Exception{

        if( CollectionUtils.isNotEmpty(certificationRequestDto.getAddFileList()) ) {
            for(FileVo fileVo : certificationRequestDto.getAddFileList()) {
                certificationRequestDto.setImagePath(fileVo.getServerSubPath());
                certificationRequestDto.setImageName(fileVo.getPhysicalFileName());
                certificationRequestDto.setImageOriginName(fileVo.getOriginalFileName());
            }
        }

        goodsCertificationService.putIlCertification(certificationRequestDto);

        return ApiResult.success();
    }

    /**
     * @Desc 상품인증정보 삭제
     * @param certificationRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> delIlCertification(CertificationRequestDto certificationRequestDto) throws Exception{

        goodsCertificationService.delIlCertification(certificationRequestDto);

        return ApiResult.success();
    }
}
