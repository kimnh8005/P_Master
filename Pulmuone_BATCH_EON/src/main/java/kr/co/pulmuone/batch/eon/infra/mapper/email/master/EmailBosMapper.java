package kr.co.pulmuone.batch.eon.infra.mapper.email.master;

import kr.co.pulmuone.batch.eon.domain.model.email.eon.dto.vo.EmailBosVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmailBosMapper {

    List<EmailBosVo> getList(@Param("senderMail") String senderMail, @Param("senderNm") String senderNm, @Param("returnMail") String returnEmail);

    void update(EmailBosVo vo);

}
