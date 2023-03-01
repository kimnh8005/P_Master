/**-----------------------------------------------------------------------------
 * description 		 : 판매처관련 그리드 컬럼 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.12		이명수   최초생성
 * @
 * **/


var sellersGridUtil = {
    sellersList: function(){
        return [
            { field : "erpInterfaceYn", headerTemplate : '',
                template : '<input type="checkbox" name="rowCheckbox" class="k-checkbox" />',
                width : "32px", attributes : {style : "text-align:center;"}, locked: true, lockable: false
            }
            , { field : "omSellersId"			, title : "판매처코드<br/>(No)"		, width: "40px"		, attributes : {style : "text-align:center;"} }
            , { field : "sellersName"			, title : "판매처명"			, width: "80px"		, attributes : {style : "text-align:center;text-decoration: underline;"} }
            , { field : "logisticsGubun"		, title : "물류구분"			, width: "80px"	, attributes : {style : "text-align:center;"} }
            , { field : "sellersGroupName"		, title : "판매처그룹"			, width: "60px"		, attributes : {style : "text-align:center;"} }
            , { field : "sellersSupplierCount"	, title : "공급업체"			, width: "40px"		, attributes : {style : "text-align:center;text-decoration: underline;color: blue;"}
            	, template: function(dataItem){ return dataItem.sellersSupplierCount + "개"; }}
            , { field : "sellersUrl"			, title : "판매처 URL"			, width: "100px"	, attributes : {style : "text-align:center; word-break: break-all;text-decoration: underline;color: blue;"}
                , template: function(dataItem){ return dataItem.sellersUrl != "" ? "<a href='"+dataItem.sellersUrl+"' target='_blank'>"+dataItem.sellersUrl+"</a>" : "";}}
            , { field : "sellersAdminUrl"		, title : "판매처 관리자 URL"	, width: "100px"	, attributes : {style : "text-align:center; word-break: break-all;text-decoration: underline;color: blue;"}, lockable: false
                , template: function(dataItem){ return dataItem.sellersAdminUrl != "" ? "<a href='"+dataItem.sellersAdminUrl+"' target='_blank'>"+dataItem.sellersAdminUrl+"</a>" : "";}}
            , { field : "interfaceYn"			, title : "수집몰<br/>연동여부"			, width: "40px"		, attributes : {style : "text-align:center;"}
                , template: function(dataItem){ return dataItem.interfaceYn == "Y" ? "예" : "아니요";}}
            , { field : "erpInterfaceYn"			, title : "물류I/F<br/>연동여부"			, width: "40px"		, attributes : {style : "text-align:center;"}
                , template: function(dataItem){ return dataItem.erpInterfaceYn == "Y" ? "예" : "아니요";}}
            , { field : "useYn"			, title : "사용여부"			, width: "40px"		, attributes : {style : "text-align:center;"}
        		, template: function(dataItem){ return dataItem.useYn == "Y" ? "예" : "아니요";}}
            , { field : "createDate"			, title : "등록일자"			, width: "60px"		, attributes : {style : "text-align:center;"} }
        ]
    },
    sellerSuppliersList:function(){
    	return [
            { field : "urSupplierName"			, title : "공급업체"			, width: "80px"		, attributes : {style : "text-align:center;"} }
          , { field : "calcType"				, title : "정산방식"			, width: "60px"		, attributes : {style : "text-align:center;"}
        		, template: function(dataItem){ return dataItem.calcType == "S" ? "판매가정산" : "공급가정산" ;}}
          , { field : "urSupplierCode"			, title : "납품처코드"			, width: "80px"		, attributes : {style : "text-align:center;"} }
      ]
    }
}