package kr.co.pulmuone.v1.comm.mapper.policy.bbs;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsAuthDto;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsBannedWordDto;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsDivisionDto;
import kr.co.pulmuone.v1.policy.bbs.dto.vo.PolicyBbsAuthVo;
import kr.co.pulmuone.v1.policy.bbs.dto.vo.PolicyBbsDivisionVo;

@Mapper
public interface PolicyBbsMapper {

	PolicyBbsBannedWordDto getPolicyBbsBannedWord(PolicyBbsBannedWordDto dto);
	int addPolicyBbsBannedWord(PolicyBbsBannedWordDto dto);
	int putPolicyBbsBannedWord(PolicyBbsBannedWordDto dto);

	PolicyBbsDivisionVo getPolicyBbsDivisionInfo(Long csCategoryId);
    Page<PolicyBbsDivisionVo> getPolicyBbsDivisionList(PolicyBbsDivisionDto dto);
    List<PolicyBbsDivisionVo> getPolicyBbsDivisionParentCategoryList(String bbsTp);
	int addPolicyBbsDivision(PolicyBbsDivisionDto dto);
	int putPolicyBbsDivision(PolicyBbsDivisionDto dto);
	int delPolicyBbsDivision(Long csCategoryId);

	PolicyBbsAuthVo getPolicyBbsAuthInfo(Long csCategoryId);
	Page<PolicyBbsAuthVo> getPolicyBbsAuthList(PolicyBbsAuthDto dto);
	List<PolicyBbsDivisionVo> getPolicyBbsAuthCategoryList(String bbsTp);
	int addPolicyBbsAuth(PolicyBbsAuthDto dto);
	int putPolicyBbsAuth(PolicyBbsAuthDto dto);
	int delPolicyBbsAuth(Long csCategoryId);

	List<String> getSpam(String siteType);
}
