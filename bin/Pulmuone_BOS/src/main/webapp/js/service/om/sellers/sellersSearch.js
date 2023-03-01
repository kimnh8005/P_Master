/**-----------------------------------------------------------------------------
 * description 		 : 판매처관련 검색 항목
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.12		이명수   최초생성
 * @
 * **/

var sellersSearchUtil = {
    searchTypeKeyword: function(){
        var searchConditionDropDown =  fnKendoDropDownList(
            {
                id : "searchCondition",
                data : [
	                	{"CODE" : "SELLERS_NM", "NAME" : "외부몰명"},
                        {"CODE" : "LOGISTICS_GUBUN", "NAME" : "물류구분"},
                        {"CODE" : "SELLERS_CD", "NAME" : "외부몰코드"},
	                	{"CODE" : "SUPPLIER_CD", "NAME" : "공급업체코드"}
                ],
                valueField : "CODE",
                textField : "NAME",
            });

        // 검색구분 DropDown 변경 이벤트
        searchConditionDropDown.bind("change", function() {
            if( $("#searchCondition").val() == "" ){
                $("#findKeyword").val("").attr("disabled", true);
            }else{
                $("#findKeyword").attr("disabled", false);
            }
        });

        // 입력제한
        //fnInputValidationForHangulAlphabetNumber("findKeyword"); // 검색어
    },
    searchInterfaceData: function(){
        return [
            { "CODE" : ""	, "NAME":'전체' },
            { "CODE" : "Y"	, "NAME":'예' },
            { "CODE" : "N"	, "NAME":'아니오' }
        ];
    },
    searchCalcTypeData: function(){
        return [
            { "CODE" : "S"	, "NAME":'판매가 정산' },
            { "CODE" : "B"	, "NAME":'공급가 정산' }
        ];
    },
    searchInterfaceYnData: function(){
        return [
            { "CODE" : "Y"	, "NAME":'예' },
            { "CODE" : "N"	, "NAME":'아니오' }
        ];
    },
    searchUseData: function(){
        return [
            { "CODE" : ""	, "NAME":'전체' },
            { "CODE" : "Y"	, "NAME":'예' },
            { "CODE" : "N"	, "NAME":'아니오' }
        ];
    },
    searchUseYnData: function(){
        return [
            { "CODE" : "Y"	, "NAME":'예' },
            { "CODE" : "N"	, "NAME":'아니오' }
        ];
    },
}