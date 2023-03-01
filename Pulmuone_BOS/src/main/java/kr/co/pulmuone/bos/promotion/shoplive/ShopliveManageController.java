package kr.co.pulmuone.bos.promotion.shoplive;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveInfoResponseDto;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveRequestDto;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveResponseDto;
import kr.co.pulmuone.v1.promotion.shoplive.service.ShopliveManageBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class ShopliveManageController {
    final String JOB_ADD = "ADD";
    final String JOB_PUT = "PUT";

    @Autowired(required = true)
    private HttpServletRequest request;

    @Autowired
    private ShopliveManageBiz shopliveManageBiz;

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @ 조회 - 이벤트
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * 샵라이브 목록 조회
     *
     * @param shopliveRequestDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/pm/shoplive/selectShopliveList")
    @ApiOperation(value = "샵라이브 목록 조회")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data", response = ShopliveResponseDto.class),
            @ApiResponse(code = 901, message = ""
                    + "EVENT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
            )
    })
    @ResponseBody
    public ApiResult<?> selectShopliveList(ShopliveRequestDto shopliveRequestDto) throws Exception {
        log.debug("# ######################################");
        log.debug("# ShopliveManageController.selectShopliveList Start");
        log.debug("# ######################################");

        return shopliveManageBiz.selectShopliveList(shopliveRequestDto);
    }

    /**
     * 샵라이브 정보 조회
     *
     * @param shopliveRequestDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/pm/shoplive/selectShopliveInfo")
    @ApiOperation(value = "샵라이브정보조회")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data", response = ShopliveInfoResponseDto.class),
            @ApiResponse(code = 901, message = ""
                    + "EVENT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
            )
    })
    @ResponseBody
    public ApiResult<?> selectShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        log.debug("# ######################################");
        log.debug("# ShopliveManageController.selectShopliveInfo Start");
        log.debug("# ######################################");

        return shopliveManageBiz.selectShopliveInfo(shopliveRequestDto);
    }

    /**
     * 샵라이브 정보 등록
     * @param shopliveRequestDto
     * @return BaseResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/pm/shoplive/addShopliveInfo")
    public ApiResult<?> addShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        return shopliveManageBiz.addShopliveInfo(shopliveRequestDto);
    }

    /**
     * 샵라이브 정보 수정
     * @param shopliveRequestDto
     * @return BaseResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/pm/shoplive/putShopliveInfo")
    public ApiResult<?> putShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        return shopliveManageBiz.putShopliveInfo(shopliveRequestDto);
    }


    /**
     * 샵라이브 정보 삭제
     * @param shopliveRequestDto
     * @return BaseResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/pm/shoplive/delShopliveInfo")
    public ApiResult<?> delShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        return shopliveManageBiz.delShopliveInfo(shopliveRequestDto);
    }

    /**
     * 샵라이브 정보 가져오기
     * @param AdminPointSettingRequestDto
     * @return BaseResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/pm/shoplive/getRemoteShopliveInfo")
    public ApiResult<?> getRemoteShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        return shopliveManageBiz.getRemoteShopliveInfo(shopliveRequestDto);
    }}