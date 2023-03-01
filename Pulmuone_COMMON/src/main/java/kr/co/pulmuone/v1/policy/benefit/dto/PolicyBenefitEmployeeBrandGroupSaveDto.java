package kr.co.pulmuone.v1.policy.benefit.dto;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeBrandGroupVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "임직원 할인율 브랜드 그룹 관리 저장 Dto")
public class PolicyBenefitEmployeeBrandGroupSaveDto extends BaseRequestDto{

	String insertData;
    String updateData;
    String deleteData;

    List<PolicyBenefitEmployeeBrandGroupVo> insertList;
    List<PolicyBenefitEmployeeBrandGroupVo> updateList;
    List<PolicyBenefitEmployeeBrandGroupVo> deleteList;

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
        this.insertList = BindUtil.convertJsonArrayToDtoList(this.insertData, PolicyBenefitEmployeeBrandGroupVo.class);
        this.updateList = BindUtil.convertJsonArrayToDtoList(this.updateData, PolicyBenefitEmployeeBrandGroupVo.class);
        this.deleteList = BindUtil.convertJsonArrayToDtoList(this.deleteData, PolicyBenefitEmployeeBrandGroupVo.class);
    }
}
