package kr.co.pulmuone.v1.comm.mapper.policy.accessibleIp;


import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.policy.accessibleip.dto.basic.GetPolicyAccessibleIpListRequestDto;
import kr.co.pulmuone.v1.policy.accessibleip.dto.basic.SavePolicyAccessibleIpRequestSaveDto;
import kr.co.pulmuone.v1.policy.accessibleip.dto.vo.GetPolicyAccessibleIpListResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface PolicyAccessibleIpMapper {

    /**
     * @Desc BOS 접근 IP 조회
     * @param GetPolicyAccessibleIpListResultVo
     * @return Page<GetPolicyAccessibleIpListResultVo>
     */
    Page<GetPolicyAccessibleIpListResultVo> getPolicyAccessibleIpList(GetPolicyAccessibleIpListRequestDto getPolicyAccessibleIpListRequestDto);

    /**
     * @Desc BOS 접근 IP 저장
     * @param SavePolicyAccessibleIpRequestSaveDto
     * @return int
     */
    int addPolicyAccessibleIp(List<SavePolicyAccessibleIpRequestSaveDto> insertRequestDtoList);

    /**
     * @Desc BOS 접근 IP 수정
     * @param SavePolicyAccessibleIpRequestSaveDto
     * @return int
     */
    int putPolicyAccessibleIp(List<SavePolicyAccessibleIpRequestSaveDto> updateRequestDtoList);

    /**
     * @Desc BOS 접근 IP 삭제
     * @param SavePolicyAccessibleIpRequestSaveDto
     * @return int
     */
    int delPolicyAccessibleIp(List<SavePolicyAccessibleIpRequestSaveDto> deleteRequestDtoList);

    /**
     * @Desc 중복 데이터 체크
     * @param SavePolicyAccessibleIpRequestSaveDto
     * @return int
     */
    int checkPolicyAccessibleIpDuplicate(@Param("list") List<SavePolicyAccessibleIpRequestSaveDto> voList);

}
