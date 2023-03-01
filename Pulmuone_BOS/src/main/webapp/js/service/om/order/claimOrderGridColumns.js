/**-----------------------------------------------------------------------------
 * description 		 : 외부몰클레임 주문 리스트 목록 컬럼 정보
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.07		김명진   최초생성
 * @
 * **/

var outmallClaimOrderGridUtil = {
		claimOrderList : function() {
		return [
			    { field:'chk'			        ,title : { key : '4346'		, nullMsg :'체크박스'}		, width:'40px',attributes:{ style:'text-align:center' },
			    	template : function(dataItem){
					    if(dataItem.processCode == "W" || dataItem.processCode == "I"){
					    	return "<input type='checkbox' class='aGridCheckbox' name='aGridCheck'/>";
						}
					    return "";
			    	}, headerTemplate : "<input type='checkbox' id='checkBoxAll1' />", filterable: false}
				,{ field:'no'           ,title : 'No'	            , width:'50px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'shopName'	    ,title : '외부몰'            , width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'seq'	        ,title : '수집몰<BR>주문상세번호'	, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'orderIdName'	,title : '외부몰<BR>주문상세번호'	, width:'70px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						if(dataItem.orderIdSeq1 != "") {
							if(dataItem.orderIdSeq2 != ""){
								return dataItem.orderId + "<BR>" + "(" + dataItem.orderIdSeq1 + " / " + dataItem.orderIdSeq2 + ")";
							}else{
								return dataItem.orderId + "<BR>" + "(" + dataItem.orderIdSeq1 + ")";
							}
						}
						else {
					    	return dataItem.orderId;
					    }
				    }
				}
				,{ field:'orderName'    ,title : '주문자명'	        , width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'orderMobile'	,title : '주문자연락처'	    , width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'receiverName'	,title : '수취인명'		    , width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'receiverMobile'   ,title : '수취인연락처'	, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'orderCsName'		,title : '주문상태'		, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'orderDate'		,title : '주문일'		, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'productId'		,title : '수집몰<BR>상품코드'	, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'orderDetailId'	,title : '주문상세번호'	, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'masterProductCodeName'    ,title : '마스터품목코드<BR>(품목바코드)'   , width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'shopProductId'	,title : '상품코드'		, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'enableSaleName'	,title : '상품판매상태'		, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'name'			    ,title : '상품명'		, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'qty'			    ,title : '수량'		, width:'70px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						return kendo.toString(kendo.parseInt(dataItem.qty), "n0");
				    }
				}
				,{ field:'shopPrice'		,title : '판매가'		, width:'70px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						return kendo.toString(kendo.parseInt(dataItem.shopPrice), "n0");
				    }
				}
				,{ field:'processStatus'	,title : '처리상태'	, width:'70px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						if(dataItem.processCode == "W"){
							return dataItem.processCodeName;
						}else{
							return dataItem.processCodeName + "(" + dataItem.adminName + " / " + dataItem.adminId + ")";
				        }
				    }
				}
				,{ field:'management'		,title : '관리'		, width:'100px', attributes : { style : "text-align:center" },
					template : function(dataItem){
						let processCode = dataItem.processCode;
						let processName = "처리중";
						let rtnValue = '';
						if(processCode == 'W' && fnIsProgramAuth("PROC_ING")){
							processName = "처리중";
							rtnValue = '<button type="button" class="k-button k-button-icontext" kind="btnProgress">' + processName + '</button>';
						}else if(processCode == 'I' && fnIsProgramAuth("PROC_COMPLETE")){
							processName = "처리완료";
							rtnValue = '<button type="button" class="k-button k-button-icontext" kind="btnProgress">' + processName + '</button>';
						}else{
							return "";
						}

				        return rtnValue;
				    }
				}
				,{ field:'ifEasyadminOrderClaimId', hidden:true}
				,{ field:'processCode', hidden:true}
			]
		}
}