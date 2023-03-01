package kr.co.pulmuone.v1.policy.accessibleip.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.policy.accessibleIp.PolicyAccessibleIpMapper;
import kr.co.pulmuone.v1.policy.accessibleip.dto.basic.GetPolicyAccessibleIpListRequestDto;
import kr.co.pulmuone.v1.policy.accessibleip.dto.basic.SavePolicyAccessibleIpRequestSaveDto;
import kr.co.pulmuone.v1.policy.accessibleip.dto.vo.GetPolicyAccessibleIpListResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@Service
class PolicyAccessibleIpServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private PolicyAccessibleIpService policyAccessibleIpService;

    @InjectMocks
    private PolicyAccessibleIpService mockPolicyAccessibleIpService;

    @Mock
    PolicyAccessibleIpMapper mockPolicyAccessibleIpMapper;

    @BeforeEach
    void setUp() {
        preLogin();
    }

    @Test
    void BOS접근가능IP설정조회() {

        GetPolicyAccessibleIpListRequestDto getPolicyAccessibleIpListRequestDto = new GetPolicyAccessibleIpListRequestDto();
        List<GetPolicyAccessibleIpListResultVo> getPolicyAccessibleIpListResultList = policyAccessibleIpService.getPolicyAccessibleIpList(getPolicyAccessibleIpListRequestDto);

        assertTrue(CollectionUtils.isNotEmpty(getPolicyAccessibleIpListResultList));
    }

    @Test
    void BOS접근가능IP설정저장성공() {
        SavePolicyAccessibleIpRequestSaveDto savePolicyAccessibleIpRequestSaveDto = new SavePolicyAccessibleIpRequestSaveDto();

        savePolicyAccessibleIpRequestSaveDto.setIpAddress("1.212.111.1");
        savePolicyAccessibleIpRequestSaveDto.setStartApplyDate("2020-10-28 00:00");
        savePolicyAccessibleIpRequestSaveDto.setEndApplyDate("2020-10-29 00:00");
        List<SavePolicyAccessibleIpRequestSaveDto> insertRequestDtoList = new ArrayList<SavePolicyAccessibleIpRequestSaveDto>();
        insertRequestDtoList.add(savePolicyAccessibleIpRequestSaveDto);
        System.out.println(insertRequestDtoList);
        int count = policyAccessibleIpService.addPolicyAccessibleIp(insertRequestDtoList);

        assertTrue(count > 0);
    }

    @Test
    void BOS접근가능IP설정삭제성공() {
        SavePolicyAccessibleIpRequestSaveDto savePolicyAccessibleIpRequestSaveDto = new SavePolicyAccessibleIpRequestSaveDto();

        savePolicyAccessibleIpRequestSaveDto.setIpAddress("1.212.111.1");
        savePolicyAccessibleIpRequestSaveDto.setStartApplyDate("2020-10-28 00:00");
        savePolicyAccessibleIpRequestSaveDto.setEndApplyDate("2020-10-29 00:00");
        List<SavePolicyAccessibleIpRequestSaveDto> deleteRequestDtoList = new ArrayList<SavePolicyAccessibleIpRequestSaveDto>();
        deleteRequestDtoList.add(savePolicyAccessibleIpRequestSaveDto);
        System.out.println(deleteRequestDtoList);
        int count = policyAccessibleIpService.addPolicyAccessibleIp(deleteRequestDtoList);

        assertTrue(count > 0);
    }

    @Test
    void BOS접근가능IP설정수정성공() {
        SavePolicyAccessibleIpRequestSaveDto savePolicyAccessibleIpRequestSaveDto = new SavePolicyAccessibleIpRequestSaveDto();

        savePolicyAccessibleIpRequestSaveDto.setIpAddress("1.212.111.1");
        savePolicyAccessibleIpRequestSaveDto.setStartApplyDate("2020-10-28 00:00");
        savePolicyAccessibleIpRequestSaveDto.setEndApplyDate("2020-10-29 00:00");
        List<SavePolicyAccessibleIpRequestSaveDto> updateRequestDtoList = new ArrayList<SavePolicyAccessibleIpRequestSaveDto>();
        updateRequestDtoList.add(savePolicyAccessibleIpRequestSaveDto);
        System.out.println(updateRequestDtoList);
        int count = policyAccessibleIpService.addPolicyAccessibleIp(updateRequestDtoList);

        assertTrue(count > 0);
    }

    @Test
    void 중복데이터체크성공() {
        //given
        SavePolicyAccessibleIpRequestSaveDto savePolicyAccessibleIpRequestSaveDto = new SavePolicyAccessibleIpRequestSaveDto();
        savePolicyAccessibleIpRequestSaveDto.setPsAccessibleIpId("111.111.111.111");
        savePolicyAccessibleIpRequestSaveDto.setIpAddress("127.0.0.1");
        List<SavePolicyAccessibleIpRequestSaveDto> voList = new ArrayList<>();
        voList.add(savePolicyAccessibleIpRequestSaveDto);

        //when
        int count = policyAccessibleIpService.checkPolicyAccessibleIpDuplicate(voList);

        //then
        assertTrue(count > 0);
    }

    @Test
    void 중복데이터체크실패() {

        SavePolicyAccessibleIpRequestSaveDto savePolicyAccessibleIpRequestSaveDto = new SavePolicyAccessibleIpRequestSaveDto();

        savePolicyAccessibleIpRequestSaveDto.setPsAccessibleIpId(null);

        List<SavePolicyAccessibleIpRequestSaveDto> voList = new ArrayList<SavePolicyAccessibleIpRequestSaveDto>();

        voList.add(savePolicyAccessibleIpRequestSaveDto);

        int count =policyAccessibleIpService.checkPolicyAccessibleIpDuplicate(voList);

        assertEquals(0, count);
    }

    @Test
    void BOS_접근_IP_저장() {

        List<SavePolicyAccessibleIpRequestSaveDto> insertRequestDtoList = new ArrayList<>();
        SavePolicyAccessibleIpRequestSaveDto savePolicyAccessibleIpRequestSaveDto = new SavePolicyAccessibleIpRequestSaveDto();
        savePolicyAccessibleIpRequestSaveDto.setIpAddress("127.0.0.1");
        savePolicyAccessibleIpRequestSaveDto.setStartApplyDate("20201201");
        savePolicyAccessibleIpRequestSaveDto.setEndApplyDate("20201201");
        insertRequestDtoList.add(savePolicyAccessibleIpRequestSaveDto);

        given(mockPolicyAccessibleIpService.addPolicyAccessibleIp(any())).willReturn(1);

        //when
        int result = mockPolicyAccessibleIpMapper.addPolicyAccessibleIp(insertRequestDtoList);

        //then
        Assertions.assertEquals(1, result);
    }

    @Test
    void BOS_접근_IP_수정() {
        List<SavePolicyAccessibleIpRequestSaveDto> insertRequestDtoList = new ArrayList<>();
        SavePolicyAccessibleIpRequestSaveDto savePolicyAccessibleIpRequestSaveDto = new SavePolicyAccessibleIpRequestSaveDto();
        savePolicyAccessibleIpRequestSaveDto.setIpAddress("127.0.0.1");
        savePolicyAccessibleIpRequestSaveDto.setStartApplyDate("20201201");
        savePolicyAccessibleIpRequestSaveDto.setEndApplyDate("20201201");
        insertRequestDtoList.add(savePolicyAccessibleIpRequestSaveDto);

        given(mockPolicyAccessibleIpService.putPolicyAccessibleIp(any())).willReturn(1);

        //when
        int result = mockPolicyAccessibleIpMapper.putPolicyAccessibleIp(insertRequestDtoList);

        //then
        Assertions.assertEquals(1, result);
    }

    @Test
    void BOS_접근_IP_삭제() {
        List<SavePolicyAccessibleIpRequestSaveDto> insertRequestDtoList = new ArrayList<>();
        SavePolicyAccessibleIpRequestSaveDto savePolicyAccessibleIpRequestSaveDto = new SavePolicyAccessibleIpRequestSaveDto();
        savePolicyAccessibleIpRequestSaveDto.setIpAddress("127.0.0.1");
        savePolicyAccessibleIpRequestSaveDto.setStartApplyDate("20201201");
        savePolicyAccessibleIpRequestSaveDto.setEndApplyDate("20201201");
        insertRequestDtoList.add(savePolicyAccessibleIpRequestSaveDto);

        given(mockPolicyAccessibleIpService.delPolicyAccessibleIp(any())).willReturn(1);

        //when
        int result = mockPolicyAccessibleIpMapper.delPolicyAccessibleIp(insertRequestDtoList);

        //then
        Assertions.assertEquals(1, result);
    }
}

