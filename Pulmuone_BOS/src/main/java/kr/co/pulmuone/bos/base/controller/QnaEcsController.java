package kr.co.pulmuone.bos.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.api.ecs.service.EcsBiz;
import kr.co.pulmuone.v1.base.dto.EcsResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;

@RestController
public class QnaEcsController {

	@Autowired
	private EcsBiz ecsBiz;

	@GetMapping(value = "/admin/comn/getEcsCodeList")
	@ApiOperation(value = "ECS 코드 조회 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = EcsResponseDto.class)
	})
	public ApiResult<?> getEcsCodeList(String hdBcode, String hdScode) throws Exception {

		EcsResponseDto dto = new EcsResponseDto();

		dto.setRows(ecsBiz.getEcsCodeList(hdBcode, hdScode));

		return ApiResult.success(dto);
	}

}
