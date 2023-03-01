package kr.co.pulmuone.v1.policy.benefit.dto;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.github.pagehelper.util.StringUtil;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "임직원  혜택 관리 저장 Dto")
public class PolicyBenefitEmployeeGroupSaveDto extends BaseRequestDto{

	String insertData;
    String updateData;
    String deleteData;

    List<PolicyBenefitEmployeeVo> insertList;
    List<PolicyBenefitEmployeeVo> updateList;
    List<PolicyBenefitEmployeeVo> deleteList;

    public boolean hasInsertData() {
        return CollectionUtils.isNotEmpty(insertList);
    }
    public boolean hasUpdateData() {
        return CollectionUtils.isNotEmpty(updateList);
    }
    public boolean hasDeleteData() {
    	return CollectionUtils.isNotEmpty(deleteList);
    }
    public void convertDataList() throws Exception {
    	if(StringUtil.isNotEmpty(this.insertData))
    		this.insertList = BindUtil.convertJsonArrayToDtoList(this.insertData, PolicyBenefitEmployeeVo.class);
    	if(StringUtil.isNotEmpty(this.updateData))
    		this.updateList = BindUtil.convertJsonArrayToDtoList(this.updateData, PolicyBenefitEmployeeVo.class);
    	if(StringUtil.isNotEmpty(this.deleteData))
    		this.deleteList = BindUtil.convertJsonArrayToDtoList(this.deleteData, PolicyBenefitEmployeeVo.class);
    }
}
