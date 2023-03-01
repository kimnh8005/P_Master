package kr.co.pulmuone.batch.infra.mapper.send;

import kr.co.pulmuone.batch.domain.service.send.template.dto.BatchSmsIssueRequestDto;
import kr.co.pulmuone.batch.domain.service.send.template.dto.vo.BatchSmsTargetVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BatchSendTemplateMapper {

    String getPsValue(@Param("psKey") String psKey);

    void addSmsIssue(@Param("list") List<BatchSmsIssueRequestDto> list);

    List<BatchSmsTargetVo> getSmsTargetList(@Param("batchNo") Long batchNo);

}
