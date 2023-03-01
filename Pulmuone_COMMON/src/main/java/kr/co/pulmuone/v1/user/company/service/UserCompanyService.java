package kr.co.pulmuone.v1.user.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.user.company.UserCompanyMapper;
import kr.co.pulmuone.v1.user.company.dto.vo.BusinessInformationVo;
import kr.co.pulmuone.v1.user.company.dto.vo.CommmonHeadQuartersCompanyVo;
import kr.co.pulmuone.v1.user.company.dto.vo.CompanyVo;
import kr.co.pulmuone.v1.user.company.dto.vo.HeadQuartersVo;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 회사 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 10. 26.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class UserCompanyService {

    @Autowired
    private final UserCompanyMapper userCompanyMapper;

    /**
     * @Desc 본사 회사정보 조회
     * @throws Exception
     */
    protected CommmonHeadQuartersCompanyVo getHeadquartersCompany() {
        return userCompanyMapper.getHeadquartersCompany();
    }

    /**
     * @Desc 사업자정보관리 조회
     * @return BusinessInformationVo
     */
    protected BusinessInformationVo getBizInfo() {
        return userCompanyMapper.getBizInfo();
    }

    /**
     * @Desc 회사정보 등록
     * @param companyVo
     * @throws Exception
     * @return int
     */
    protected int addCompany(CompanyVo companyVo) throws Exception{
        return userCompanyMapper.addCompany(companyVo);
    }

    /**
     * @Desc 회사정보 수정
     * @param companyVo
     * @throws Exception
     * @return int
     */
    protected int putCompany(CompanyVo companyVo) throws Exception{
        return userCompanyMapper.putCompany(companyVo);
    }

    /**
     * @Desc 본사정보 등록
     * @param headQuartersVo
     * @throws Exception
     * @return int
     */
    protected int addHeadQuarters(HeadQuartersVo headQuartersVo) throws Exception{
        return userCompanyMapper.addHeadQuarters(headQuartersVo);
    }

    /**
     * @Desc 본사정보 수정
     * @param headQuartersVo
     * @throws Exception
     * @return int
     */
    protected int putHeadQuarters(HeadQuartersVo headQuartersVo) throws Exception{
        return userCompanyMapper.putHeadQuarters(headQuartersVo);
    }
}
