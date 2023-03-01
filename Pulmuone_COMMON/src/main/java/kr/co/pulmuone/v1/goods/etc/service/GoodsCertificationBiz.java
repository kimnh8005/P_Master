package kr.co.pulmuone.v1.goods.etc.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationListResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationResponseDto;

public interface GoodsCertificationBiz {

    CertificationListResponseDto getIlCertificationList(CertificationListRequestDto certificationListRequestDto);
    CertificationResponseDto getIlCertification(long ilCertificationId);
    ApiResult<?> addIlCertification(CertificationRequestDto certificationRequestDto) throws Exception;
    ApiResult<?> putIlCertification(CertificationRequestDto certificationRequestDto) throws Exception;
    ApiResult<?> delIlCertification(CertificationRequestDto certificationRequestDto) throws Exception;
}
