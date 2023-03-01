package kr.co.pulmuone.v1.goods.etc.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.goods.etc.GoodsCertificationMapper;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.CertificationRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.CertificationVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class GoodsCertificationServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private GoodsCertificationService goodsCertificationService;

    @InjectMocks
    private GoodsCertificationService mockGoodsCertificationService;

    @Mock
    GoodsCertificationMapper mockGoodsCertificationMapper;

    @BeforeEach
    void setUp() {
        preLogin();
        mockGoodsCertificationService = new GoodsCertificationService(mockGoodsCertificationMapper);
    }

    @Test
    void getIlCertificationList() {
        // given
        CertificationListRequestDto certificationListRequestDto = new CertificationListRequestDto();
        certificationListRequestDto.setSearchUseYn("Y");
        certificationListRequestDto.setPage(0);
        certificationListRequestDto.setPageSize(2);

        // when
        Page<CertificationVo> pageList = goodsCertificationService.getIlCertificationList(certificationListRequestDto);

        // then
        assertTrue(pageList.size() > 0);
    }

    @Test
    void getIlCertification() {
        // given
        long ilCertificationId = 3;

        // when
        CertificationVo certificationVo = goodsCertificationService.getIlCertification(ilCertificationId);

        // then
        assertNotNull(certificationVo);
    }

    @Test
    void addIlCertification() {

        given(mockGoodsCertificationMapper.addIlCertification(any())).willReturn(1);

        // when
        int count = mockGoodsCertificationService.addIlCertification(null);

        // then
        assertTrue(count > 0);
    }

    @Test
    void putIlCertification() {
        // given
        CertificationRequestDto certificationRequestDto = new CertificationRequestDto();
        certificationRequestDto.setUseYn("N");
        certificationRequestDto.setCertificationName("title");
        certificationRequestDto.setDefaultCertificationDescribe("Desc");
        certificationRequestDto.setIlCertificationId("3");

        // when
        int count = goodsCertificationService.putIlCertification(certificationRequestDto);

        // then
        assertTrue(count > 0);
    }

    @Test
    void delIlCertification() {
        // given
        CertificationRequestDto certificationRequestDto = new CertificationRequestDto();
        certificationRequestDto.setIlCertificationId("3");

        // when
        int count = goodsCertificationService.delIlCertification(certificationRequestDto);

        // then
        assertTrue(count > 0);
    }
}