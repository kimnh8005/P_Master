package kr.co.pulmuone.v1.comm.mapper.goods.etc;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.etc.dto.CertificationListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.CertificationVo;

@Mapper
public interface GoodsCertificationMapper {

    /**
     * @Desc 상품인증정보 조회
     * @param certificationListRequestDto
     * @return Page<CertificationVo>
     */
    Page<CertificationVo> getIlCertificationList(CertificationListRequestDto certificationListRequestDto);

    /**
     * @Desc 상품인증정보 상세 정보 조회
     * @param ilCertificationId
     * @return CertificationVo
     */
    CertificationVo getIlCertification(@Param("ilCertificationId") long ilCertificationId);

    /**
     * @Desc 상품인증정보 추가
     * @param certificationRequestDto
     * @return int
     */
    int addIlCertification(CertificationRequestDto certificationRequestDto);

    /**
     * @Desc 상품인증정보 수정
     * @param certificationRequestDto
     * @return int
     */
    int putIlCertification(CertificationRequestDto certificationRequestDto);

    /**
     * @Desc 상품인증정보 삭제
     * @param certificationRequestDto
     * @return int
     */
    int delIlCertification(CertificationRequestDto certificationRequestDto);
}
