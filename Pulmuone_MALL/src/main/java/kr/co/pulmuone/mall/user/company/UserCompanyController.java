package kr.co.pulmuone.mall.user.company;

import kr.co.pulmuone.v1.user.company.dto.vo.CommmonHeadQuartersCompanyVo;
import kr.co.pulmuone.v1.user.company.service.UserCompanyBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 7. 20.                손진구         최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class UserCompanyController {

    @Autowired
    private UserCompanyBiz userCompanyBiz;

    /**
     * @Desc 본사 회사정보 조회
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/common/ur/company/getHeadquartersCompany")
    public CommmonHeadQuartersCompanyVo getHeadquartersCompany() throws Exception{
        return userCompanyBiz.getHeadquartersCompany();
    }
}
