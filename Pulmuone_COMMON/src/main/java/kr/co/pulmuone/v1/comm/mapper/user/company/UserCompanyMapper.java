package kr.co.pulmuone.v1.comm.mapper.user.company;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.user.company.dto.vo.BusinessInformationVo;
import kr.co.pulmuone.v1.user.company.dto.vo.CommmonHeadQuartersCompanyVo;
import kr.co.pulmuone.v1.user.company.dto.vo.CompanyVo;
import kr.co.pulmuone.v1.user.company.dto.vo.HeadQuartersVo;

@Mapper
public interface UserCompanyMapper {

	/**
	 * @Desc 본사 회사정보 조회
	 * @return CommmonHeadQuartersCompanyVo
	 */
	CommmonHeadQuartersCompanyVo getHeadquartersCompany();

	/**
	 * @Desc 사업자정보관리 조회
	 * @return BusinessInformationVo
	 */
	BusinessInformationVo getBizInfo();

	/**
	 * @Desc 회사정보 등록
	 * @param companyVo
	 * @return int
	 */
	int addCompany(CompanyVo companyVo);

	/**
	 * @Desc 회사정보 수정
	 * @param companyVo
	 * @return int
	 */
	int putCompany(CompanyVo companyVo);

	/**
	 * @Desc 본사정보 등록
	 * @param headQuartersVo
	 * @return int
	 */
	int addHeadQuarters(HeadQuartersVo headQuartersVo);

	/**
	 * @Desc 본사정보 수정
	 * @param headQuartersVo
	 * @return int
	 */
	int putHeadQuarters(HeadQuartersVo headQuartersVo);
}
