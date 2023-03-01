package kr.co.pulmuone.mall.policy.clause;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.clause.dto.GetClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetClauseHistoryResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetClauseResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetLatestJoinClauseListResultVo;
import kr.co.pulmuone.v1.policy.clause.service.PolicyClauseBiz;
import kr.co.pulmuone.v1.user.warehouse.service.WarehouseBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
/*
 * SpringBootBosApplication.java에 선언하지 말고 Controller에서 필요한 Domain의 Package를
 * Scan해서 사용해야 함 kr.co.pulmuone.mall는 명시해도 되고 안해도 됨
 */

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
 *  1.0    20200617   	 	김경민            최초작성
 * =======================================================================
 * </PRE>
 */
@ComponentScan(basePackages = { "kr.co.pulmuone.common" })
@RestController
public class PolicyClauseController {

    @Autowired
    PolicyClauseBiz policyClauseBiz;

    @Autowired
    private WarehouseBiz warehouseBiz;

    /**
     * 회원가입 최신 약관 리스트
     *
     * @param
     * @return ResponseEntity<ResponseDto<GetLatestClauseResponseDto>>
     * @throws Exception
     */
    @PostMapping(value = "/config/clause/getLatestJoinClauseList")
    @ApiOperation(value = "회원가입 최신 약관 리스트", httpMethod = "POST", notes = "회원가입 최신 약관 리스트")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = GetLatestJoinClauseListResultVo.class)
    })
    public ApiResult<?> getLatestJoinClauseList() throws Exception {

        return policyClauseBiz.getLatestJoinClauseList();
    }

    /**
     * 최신 약관 내용 정보
     * @param
     * @return ResponseEntity<ResponseDto<GetLatestClauseResponseDto>>
     * @throws Exception
     */
    @PostMapping(value = "/config/clause/getLatestClause")
    @ApiOperation(value = "약관 내용 정보", httpMethod = "POST", notes = "약관 내용 정보")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : GetClauseResultVo", response = GetClauseResultVo.class)
    })
    public ApiResult<?> getLatestClause(@RequestParam(value = "psClauseGrpCd") String psClauseGrpCd) throws Exception{

        return policyClauseBiz.getLatestClause(psClauseGrpCd);
    }

    /**
     * 약관 변경이력 리스트
     * @param
     * @return	ResponseEntity<GetClauseHistoryListResponseDto>
     * @throws Exception
     */
    @PostMapping(value = "/config/clause/getClauseHistoryList")
    @ApiOperation(value = "약관 변경이력 리스트", httpMethod = "POST", notes = "약관 변경이력 리스트")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = GetClauseHistoryResultVo.class)
    })
    public ApiResult<?> getClauseHistoryList(@RequestParam(value = "psClauseGrpCd") String psClauseGrpCd) throws Exception{

        return policyClauseBiz.getClauseHistoryList(psClauseGrpCd);
    }

    /**
     * 버전별 약관 내용 정보
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/config/clause/getClause")
    @ApiOperation(value = "버전별 약관 내용 정보", httpMethod = "POST", notes = "버전별 약관 내용 정보")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : GetClauseResultVo", response = GetClauseResultVo.class)
    })
    public ApiResult<?> getClause(@RequestParam(value ="psClauseGrpCd") String psClauseGrpCd, @RequestParam(value = "executeDate") String executeDate) throws Exception{

        GetClauseRequestDto getClauseRequestDto = new GetClauseRequestDto();
        getClauseRequestDto.setPsClauseGrpCd(psClauseGrpCd);
        getClauseRequestDto.setExecuteDate(executeDate);

        return policyClauseBiz.getClause(getClauseRequestDto);
    }

    @PostMapping(value = "/config/clause/getWarehouse")
    @ApiOperation(value = "약관 출고처 정보", httpMethod = "POST", notes = "약관 출고처 정보")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : ", response = String.class)
    })
    public ApiResult<?> getWarehouse() throws Exception{
        return ApiResult.success(warehouseBiz.getWarehouseCompanyName());
    }

}
