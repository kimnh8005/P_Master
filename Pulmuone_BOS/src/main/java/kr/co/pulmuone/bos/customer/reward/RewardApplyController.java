package kr.co.pulmuone.bos.customer.reward;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyRequestBosDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyResponseBosDto;
import kr.co.pulmuone.v1.customer.reward.service.RewardBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RewardApplyController {

    @Autowired
    private RewardBiz rewardBiz;

    @Autowired(required=true)
    private HttpServletRequest request;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    /**
     * 고객보상제 신청관리 조회
     * @param rewardApplyRequestBosDto
     * @return RewardApplyResponseBosDto
     * @throws Exception
     */
    @ApiOperation(value = "고객보상제 신청관리 조회")
    @PostMapping(value = "/admin/customer/reward/getRewardApplyList")
    @ApiResponse(code = 900, message = "response data", response = RewardApplyResponseBosDto.class)
    public ApiResult<?> getRewardApplyList(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {
        return rewardBiz.getRewardApplyList((RewardApplyRequestBosDto) BindUtil.convertRequestToObject(request, RewardApplyRequestBosDto.class));
    }

    /**
     * 보상제 조회
     * @param rewardApplyRequestBosDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/customer/reward/getRewardNmList")
    @ApiOperation(value = "공급업체 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = RewardApplyResponseBosDto.class)
    })
    @ResponseBody
    public ApiResult<?> getRewardNmList(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {
        return rewardBiz.getRewardNmList(rewardApplyRequestBosDto);
    }


    /**
     * 고객보상제 신청관리 상세
     * @param rewardApplyRequestBosDto
     * @return RewardApplyResponseBosDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/customer/reward/getRewardApplyDetail")
    @ApiOperation(value = "고객보상제 신청관리 상세")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = RewardApplyResponseBosDto.class)
    })
    public ApiResult<?> getRewardApplyDetail(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {
        return rewardBiz.getRewardApplyDetail(rewardApplyRequestBosDto);
    }



    /**
     * 고객보상제 신청 상세 첨부파일 이미지
     * @param csRewardApplyId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "고객보상제 신청 상세 첨부파일 이미지")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = QnaBosDetailVo.class)
    })
    @PostMapping(value = "/admin/customer/reward/getImageList")
    @ResponseBody
    public ApiResult<?> getImageList(@RequestParam(value = "csRewardApplyId", required = true) String csRewardApplyId) throws Exception {
        return rewardBiz.getImageList(csRewardApplyId);
    }



    /**
     * 처리진행 상태변경
     * @param rewardApplyRequestBosDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/customer/reward/putRewardApplyConfirmStatus")
    @ApiOperation(value = "처리진행 상태변경")
    @ResponseBody
    public ApiResult<?> putRewardApplyConfirmStatus(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {
        return rewardBiz.putRewardApplyConfirmStatus(rewardApplyRequestBosDto);
    }


    /**
     * 보상제 신청 상세 수정
     * @param rewardApplyRequestBosDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/customer/reward/putRewardApplyInfo")
    @ApiOperation(value = "처리진행 상태변경")
    @ResponseBody
    public ApiResult<?> putRewardApplyInfo(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {
        return rewardBiz.putRewardApplyInfo(rewardApplyRequestBosDto);
    }


    /**
     * 고객보상제 신청관리 내역 엑셀 다운로드
     * @param rewardApplyRequestBosDto
     * @return
     */
    @PostMapping(value = "/admin/customer/reward/getRewardApplyListExportExcel")
    public ModelAndView getRewardApplyListExportExcel(@RequestBody RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, rewardBiz.getRewardApplyListExportExcel(rewardApplyRequestBosDto));

        return modelAndView;
    }
}
