package kr.co.pulmuone.v1.comm.mapper.policy.clause;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.policy.clause.dto.GetClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyAddClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyDelClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseGroupListRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseGroupNameListRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseListRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseModifyViewRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseViewRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyPutClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicySaveClauseGroupRequestSaveDto;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetClauseHistoryResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetClauseResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetLatestJoinClauseListResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseGroupListResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseGroupNameListResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseListResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseModifyViewResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseViewResultVo;


@Mapper
public interface PolicyClauseMapper {

	List<GetLatestJoinClauseListResultVo> getLatestJoinClauseList() throws Exception;

	GetClauseResultVo getLatestClause(String psClauseGrpCd) throws Exception;

	List<GetClauseHistoryResultVo> getClauseHistoryList(String psClauseGrpCd) throws Exception;

	GetClauseResultVo getClause(GetClauseRequestDto dto) throws Exception;

	List<GetLatestJoinClauseListResultVo> getPurchaseTermsClauseList(GetClauseRequestDto dto) throws Exception;

	int getClauseGroupListCount(PolicyGetClauseGroupListRequestDto dto);

	List<PolicyGetClauseGroupListResultVo> getClauseGroupList(PolicyGetClauseGroupListRequestDto dto);

	void addClauseGroup(PolicySaveClauseGroupRequestSaveDto vo);

	void putClauseGroup(PolicySaveClauseGroupRequestSaveDto vo);

	int getDuplicateClauseGroupCount(PolicySaveClauseGroupRequestSaveDto vo);

	List<PolicyGetClauseGroupNameListResultVo> getClauseGroupNameList(PolicyGetClauseGroupNameListRequestDto dto);

	List<PolicyGetClauseListResultVo> getClauseList(PolicyGetClauseListRequestDto dto);

	PolicyGetClauseViewResultVo getClauseView(PolicyGetClauseViewRequestDto dto);

	PolicyGetClauseResultVo getPolicyClause(PolicyGetClauseRequestDto dto);

	PolicyGetClauseModifyViewResultVo getClauseModifyView(PolicyGetClauseModifyViewRequestDto dto);

	void addClause(PolicyAddClauseRequestDto dto);

	void addClauseUpdateHistory(PolicyPutClauseRequestDto dto);

	void putUpdateClause(PolicyPutClauseRequestDto dto);

	void addClauseDeleteHistory(PolicyDelClauseRequestDto dto);

	void putDeleteClause(PolicyDelClauseRequestDto dto);

	void delClause(PolicyDelClauseRequestDto dto);
}
