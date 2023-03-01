/**-----------------------------------------------------------------------------
 * description 		 : 외부목 주문상세 리스트 그리드 컬럼 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.17		안지영   최초생성
 * @
 * **/

var outmallOrderGridUtil = {
	    outmallOrderDetailList: function(){
	        return [
				{title : 'No.'		, width:'60px'	,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'userType'			,title : '주문상세번호'	, width:'90px'	,attributes:{ style:'text-align:center' }, template: "#=(employeeYn=='Y') ? '임직원' : '일반'#"}
				,{ field:'userName'			,title : '주문상태'		, width:'80px'	,attributes:{ style:'text-align:center;text-decoration: underline;' }}
				,{ field:'loginId'			,title : '주문I/F<br/>출고예정일'		, width:'100px'	,attributes:{ style:'text-align:center;text-decoration: underline;' }}
				,{ field:'mobile'			,title : '도착예정일'		, width:'120px'	,attributes:{ style:'text-align:center' }
					, template: function(dataItem) {
						var mobile = kendo.htmlEncode(dataItem.mobile);
						return fnPhoneNumberHyphen(mobile);
	                }
				}
				,{ field:'mail'				,title : '상품유형<br/>(배송방법)'	, width:'120px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '마스터상품코드<br/>(품목바코드)'		, width:'120px'	,attributes:{ style:'text-align:center' }}
				,{ field:'modifyDate'		,title : '상품코드'	, width:'120px'	,attributes:{ style:'text-align:center' }}
				,{ field:'modifyDate'		,title : '할인유형'	, width:'120px'	,attributes:{ style:'text-align:center' }}
				,{ field:'modifyDate'		,title : '상품명'	, width:'120px'	,attributes:{ style:'text-align:center' }}
				,{ field:'modifyDate'		,title : '상세정보'	, width:'120px'	,attributes:{ style:'text-align:center' }}
				,{ field:'modifyDate'		,title : '보관방법'	, width:'120px'	,attributes:{ style:'text-align:center' }}
				,{ field:'modifyDate'		,title : '수량'	, width:'120px'	,attributes:{ style:'text-align:center' }}
				,{ field:'modifyDate'		,title : '배송번호'	, width:'120px'	,attributes:{ style:'text-align:center' }}
				,{ field:'modifyDate'		,title : '송장번호<br/>(택배사)'	, width:'120px'	,attributes:{ style:'text-align:center' }}
				,{ field:'urUserId'			,hidden: true}
			]
	    }
	}