package kr.co.pulmuone.v1.comm.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * 최상위 RequestDto 객체
 *
 * @author 오영민
 */
@Getter
@Setter
public class BaseRequestDto extends BaseDto {

	@Value("${database.encryption.key}")
	@ApiModelProperty(value = "암호화키", hidden = true)
	private String DATABASE_ENCRYPTION_KEY;

	// 두군데서 사용하여 오류방지위해 임시 사용..  나중에 하나로 통일..  (최용호)
	@Value("${database.encryption.key}")
	@ApiModelProperty(value = "암호화키", hidden = true)
	private String databaseEncryptionKey;
}