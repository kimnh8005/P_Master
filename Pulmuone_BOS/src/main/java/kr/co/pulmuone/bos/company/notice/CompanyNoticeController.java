package kr.co.pulmuone.bos.company.notice;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.company.notice.dto.*;
import kr.co.pulmuone.v1.company.notice.dto.vo.*;
import kr.co.pulmuone.v1.company.notice.service.CompanyNoticeBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller("noticeControllerHangaram")
public class CompanyNoticeController {

	@Autowired
	private CompanyNoticeBiz companyNoticeBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@PostMapping(value = "/admin/cs/notice/addNotice")
	@ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = AddNoticeResultVo.class),
            @ApiResponse(code = 901, message = "" +
                    "[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
            )
    })
	public ApiResult<?> addNotice(AddNoticeRequestDto dto) throws Exception {

		//binding data
		dto.setAddFileList(BindUtil.convertJsonArrayToDtoList(dto.getAddFile(), FileVo.class));

		return companyNoticeBiz.addNotice(dto);
	}

	@PostMapping(value = "/admin/cs/notice/putNotice")
	@ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = PutNoticeResultVo.class),
            @ApiResponse(code = 901, message = "" +
                    "[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
            )
    })
	public ApiResult<?> putNotice(PutNoticeRequestDto dto) throws Exception {

		//binding data
		dto.setAddFileList(BindUtil.convertJsonArrayToDtoList(dto.getAddFile(), FileVo.class));

		return companyNoticeBiz.putNotice(dto);
	}

	@PostMapping(value = "/admin/cs/notice/getNotice")
	@ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetNoticeResultVo.class)
            , @ApiResponse(code = 901, message = "response data", response = GetNoticeAttachResultVo.class)
    })
	public ApiResult<?> getNotice(GetNoticeRequestDto dto) throws Exception {

		return ApiResult.success(companyNoticeBiz.getNotice(dto));
	}

	@PostMapping(value = "/admin/cs/notice/getNoticeList")
	@ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = GetNoticeListResultVo.class)
    })
	public ApiResult<?> getNoticeList(GetNoticeListRequestDto dto) throws Exception {

		return ApiResult.success(companyNoticeBiz.getNoticeList((GetNoticeListRequestDto) BindUtil.convertRequestToObject(request, GetNoticeListRequestDto.class)));
	}

  /**
   * 관리자공지사항리스트조회 - 대시보드용
   * @param dto
   * @return
   * @throws Exception
   */
  @PostMapping(value = "/admin/cs/notice/getDashboardNoticeList")
  @ResponseBody
  @ApiResponses(value = {
    @ApiResponse(code = 900, message = "response data List<>", response = GetNoticeListResultVo.class)
  })
  public ApiResult<?> getDashboardNoticeList(GetNoticeListRequestDto dto) throws Exception {

    GetNoticeListRequestDto getNoticeListRequestDto = (GetNoticeListRequestDto) BindUtil.convertRequestToObject(request, GetNoticeListRequestDto.class);
    getNoticeListRequestDto.setDashboardCallYn(true);
    return ApiResult.success(companyNoticeBiz.getNoticeList(getNoticeListRequestDto));
  }

	@PostMapping(value = "/admin/cs/notice/delNotice")
	@ResponseBody
	public ApiResult<?> delNotice(DelNoticeRequestDto dto) throws Exception {

		dto.setDeleteRequestDtoList(BindUtil.convertJsonArrayToDtoList(dto.getDeleteData(), DelNoticeRequestSaveDto.class));

		return companyNoticeBiz.delNotice(dto);
	}

	@PostMapping(value = "/admin/cs/notice/putNoticeSet")
	@ResponseBody
	public ApiResult<?> putNoticeSet(PutNoticeSetRequestDto dto) throws Exception {

		dto.setUpdateRequestDtoList(BindUtil.convertJsonArrayToDtoList(dto.getUpdateData(), PutNoticeSetRequestSaveDto.class));

		return companyNoticeBiz.putNoticeSet(dto);
	}

	@PostMapping(value = "/admin/cs/notice/getNoticePreNextList")
	@ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = GetNoticePreNextListResultVo.class)
    })
	public ApiResult<?> getNoticePreNextList(GetNoticePreNextListRequestDto dto) throws Exception {

		return ApiResult.success(companyNoticeBiz.getNoticePreNextList(dto));
	}

	@PostMapping(value = "/admin/cs/notice/delAttc")
	@ResponseBody
	public ApiResult<?> delAttc(NoticeAttachParamDto noticeAttachParamDto) throws Exception {

		return companyNoticeBiz.delAttc(noticeAttachParamDto);
	}
}
