package kr.co.pulmuone.bos.system.help;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.system.help.dto.DictionarySearchRequestDto;
import kr.co.pulmuone.v1.system.help.service.NationBiz;
import kr.co.pulmuone.v1.system.help.vo.DictionaryMasterVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
 *  1.0    20200605		        천혜현           최초작성
 *  2.0    20200826             정길준           패키지 변경 리팩토링
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/system/help/nation")
public class NationController {

	@NotNull
	private final NationBiz nationBiz;

	@Autowired
	private HttpServletRequest request;

	@ApiOperation(value = "표준용어 추가")
	@PostMapping(value = "/saveDictionaryMaster")
	public ApiResult<?> saveDictionaryMaster(DictionarySaveRequestDto dto) throws Exception {
		return nationBiz.saveDictionaryMaster(dto.convert());
	}

	@ApiOperation(value = "표준용어 변경")
	@PostMapping(value = "/updateDictionaryMaster")
	public ApiResult<?> updateDictionaryMaster(DictionarySaveRequestDto dto) throws Exception {
		return nationBiz.updateDictionaryMaster(dto.getId(), dto.getBaseName());
	}

	@ApiOperation(value = "표준용어 상세조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "표준용어 PK", dataType = "Long")
	})
	@PostMapping(value = "/getDictionaryMaster")
	public ApiResult<?> getDictionaryMaster(@RequestParam(value = "id") Long id) {
		return ApiResult.success(
			new DictionaryMasterResponseDto(Optional.ofNullable(nationBiz.findDictionaryMasterById(id))
				.map(DictionarySaveRequestDto::new)
				.orElse(null))
		);
	}

	@ApiOperation(value = "표준용어 리스트조회")
	@PostMapping(value = "/getDictionaryMasterList")
	public ApiResult<?> getDictionaryMasterList(DictionarySearchRequestDto dto) throws Exception {
		Page<DictionaryMasterVo> result = nationBiz.findDictionaryMasterList(
			BindUtil.bindDto(request, DictionarySearchRequestDto.class));

		return ApiResult.success(
			DictionaryMasterListResponseDto.builder()
				.total((int) result.getTotal())
				.rows(result.getResult().stream()
					.map(DictionaryMasterDto::new)
					.collect(Collectors.toList())
				).build()
		);
	}
}
