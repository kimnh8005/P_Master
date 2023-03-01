package kr.co.pulmuone.v1.goods.etc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.goods.etc.GoodsCertificationMapper;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.CertificationVo;
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
@Service
@RequiredArgsConstructor
public class GoodsCertificationService {

    @Autowired
    private final GoodsCertificationMapper goodsCertificationMapper;


    /**
     * @Desc 상품인증정보 조회
     * @param certificationListRequestDto
     * @return Page<CertificationVo>
     */
    protected Page<CertificationVo> getIlCertificationList(CertificationListRequestDto certificationListRequestDto) {
        PageMethod.startPage(certificationListRequestDto.getPage(), certificationListRequestDto.getPageSize());
        return goodsCertificationMapper.getIlCertificationList(certificationListRequestDto);
    }

    /**
     * @Desc 상품인증정보 상세 정보 조회
     * @param ilCertificationId
     * @return CertificationVo
     */
    protected CertificationVo getIlCertification(long ilCertificationId) {
        return goodsCertificationMapper.getIlCertification(ilCertificationId);
    }

    /**
     * @Desc 상품인증정보 추가
     * @param certificationRequestDto
     * @return int
     */
    protected int addIlCertification(CertificationRequestDto certificationRequestDto){
        return goodsCertificationMapper.addIlCertification(certificationRequestDto);
    }

    /**
     * @Desc 상품인증정보 수정
     * @param certificationRequestDto
     * @return int
     */
    protected int putIlCertification(CertificationRequestDto certificationRequestDto){
        return goodsCertificationMapper.putIlCertification(certificationRequestDto);
    }

    /**
     * @Desc 상품인증정보 삭제
     * @param certificationRequestDto
     * @return int
     */
    protected int delIlCertification(CertificationRequestDto certificationRequestDto){
        return goodsCertificationMapper.delIlCertification(certificationRequestDto);
    }
}
