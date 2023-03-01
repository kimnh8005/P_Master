package kr.co.pulmuone.v1.policy.bbs.service;

import kr.co.pulmuone.v1.comm.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums.EnumSiteType;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsBannedWordDto;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PolicyBbsBannedWordBizImpl implements PolicyBbsBannedWordBiz {

    @Autowired
    private PolicyBbsBannedWordService policyBbsBannedWordService;

    /**
     * 게시판금칙어 설정 조회
     *
     * @param PolicyBbsBannedWordDto
     * @return PolicyBbsBannedWordDto
     * @throws	Exception
     */
    @Override
    public ApiResult<?> getPolicyBbsBannedWord(PolicyBbsBannedWordDto dto) {
    	PolicyBbsBannedWordDto rtnDto = policyBbsBannedWordService.getPolicyBbsBannedWord(dto);
    	if(rtnDto == null) {
    		rtnDto = new PolicyBbsBannedWordDto();
    		rtnDto.setRETURN_CODE("0000");
    		rtnDto.setRETURN_MSG("NOT_DEFINE_설정된 값이 없어 기본 세팅됩니다.");
    	}
    	return ApiResult.success(rtnDto);
    }
    /**
     * 게시판금칙어 설정 저장
     *
     * @param PolicyBbsBannedWordDto
     * @return int
     * @throws	Exception
     */
    @Override
    public ApiResult<?> putPolicyBbsBannedWord(PolicyBbsBannedWordDto dto) {
    	policyBbsBannedWordService.putPolicyBbsBannedWord(dto);
    	return ApiResult.success();
    }

    @Override
    public List<String> getSpam(String siteType) {
        return policyBbsBannedWordService.getSpam(siteType);
    }

    @Override
	public String filterSpamWord(String contents, EnumSiteType site) {
        if (StringUtil.isEmpty(contents)) {
            return contents;
        }

        // 금칙어 확인
        List<String> spamList = getSpam(site.getCode());

        if (spamList == null || spamList.size() <= 0) {
        	return contents;
        }

        String resultString = contents;
        for (String spam : spamList) {
            String removeSpace = spam.replaceAll("\\s+", "");
            String compilePattern = removeSpace.replaceAll(",", "|");
            Pattern p = Pattern.compile(compilePattern, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(resultString);

            StringBuffer sb = new StringBuffer();
            while (m.find()) {
            	m.appendReplacement(sb, maskWord(m.group()));
            }
            m.appendTail(sb);
            resultString = sb.toString();
        }

        return resultString;
    }

    private String maskWord(String word) {
    	StringBuffer sb = new StringBuffer();
    	char[] ch = word.toCharArray();

    	for (int i = 0; i < ch.length; i++) {
    		sb.append("*");
    	}

    	return sb.toString();
    }

}
