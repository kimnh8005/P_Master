/**-----------------------------------------------------------------------------
 * description 		 : 외부몰클레임 주문 리스트 목록 컬럼 정보
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.07		김명진   최초생성
 * @
 * **/

var outmallOrderGridUtil = {
		orderList : function() {
		return [
		            { field:'chk'			        ,title : { key : '4346'		, nullMsg :'체크박스'}		, width:'40px',attributes:{ style:'text-align:center' }
					, template : function(dataItem){
					    if(dataItem.processCode == "I"){
					        return "<input type='checkbox' class='aGridCheckbox' name='aGridCheck'/>";
					    }
					    return "";
					}
					, headerTemplate : "<input type='checkbox' id='checkBoxAll1' />"
					, filterable: false
				}
				,{ field:'no'                   ,title : 'No'		,width:'50px' ,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'collectCodeName'	    ,title : '수집상태'	,width:'60px' ,attributes:{ style:'text-align:center' }}
				,{ field:'uploadInfo'   ,title : '업로드'	, width:'150px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						return "성공 : " + dataItem.successCount + " 건 / 실패 : " + dataItem.failCount + " 건";
					}
				}
				,{ field:'syncCodeName'	        ,title : '연동상태'	,width:'60px' ,attributes:{ style:'text-align:center' }}
				,{ field:'batchInfo'   ,title : '업데이트'	, width:'150px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						if(dataItem.syncCodeName == '완료'){
							if(dataItem.batchEndDateTime == "") {
								return "-";
							}
							return "성공 : " + dataItem.orderSuccCnt + " 건 / 실패 : " + dataItem.orderFailCnt + " 건";
						}
						return "-";
					}
				}
				,{ field:'batchStartDateTime'	,title : '시작일자'	,width:'90px' ,attributes:{ style:'text-align:center' },
		            template : function(dataItem){
		            	if(dataItem.batchStartDateTime != null) {
		            		return dataItem.batchStartDateTime;
		            	}
		            	else {
		            		return '-';
		            	}
		            }
		        }
				,{ field:'batchEndDateTime'	    ,title : '종료일자'	,width:'90px' ,attributes:{ style:'text-align:center' },
		            template : function(dataItem){
		            	if(dataItem.batchEndDateTime != null) {
		            		return dataItem.batchEndDateTime;
		            	}
		            	else {
		            		return '-';
		            	}
		            }
		        }
				,{ field:'batchExecutionTime'   ,title : '소요 시간'	,width:'90px' ,attributes:{ style:'text-align:center' },
		            template : function(dataItem){
		            	if(dataItem.batchStartDateTime != null) {
		            		return kendo.toString(kendo.parseInt(dataItem.batchExecutionTime), "n0") + " 초";
		            	}
		            	else {
		            		return '-';
		            	}
		            }
		        }
				/*,{ field:'successCount'			,title : '정상'	    , width:'90px',attributes:{ style:'text-align:center' },
				    template : function(dataItem){
		                return kendo.toString(kendo.parseInt(dataItem.successCount), "n0") + " 건";
		            }
		        }
				,{ field:'failCount'			,title : '실패'		, width:'90px',attributes:{ style:'text-align:center' },
				    template : function(dataItem){
		                return kendo.toString(kendo.parseInt(dataItem.failCount), "n0") + " 건";
		            }
				}*/
				,{ field:'processStatus'		,title : '처리상태'	, width:'90px',attributes:{ style:'text-align:center' },
		            template : function(dataItem){
		            	if(dataItem.processCode != null) {
							if (dataItem.processCode == "W" || dataItem.processCode == "T") {
								return dataItem.processCodeName;
						    } else {
		                        return dataItem.processCodeName + "(" + dataItem.adminName + " / " + dataItem.adminLoginId + ")";
		                    }
		            	}
		            	else {
		                    return dataItem.processCodeName;
		            	}
		            }
		        }
				,{ field:'management'		    ,title : '관리'		, width:'150px', attributes : { style : "text-align:center" },
		            template : function(dataItem){
		                let processCode = dataItem.processCode;
		                if(dataItem.failCount > 0 || dataItem.orderFailCnt > 0) { // 주문수집실패count가 있거나, 주문생성실패count가 있을 경우
		                    let rtnValue = "";
		                    if(processCode == 'I' && fnIsProgramAuth("PROC_COMPLETE")){
		                        rtnValue = '<button type="button" class="k-button k-button-icontext" kind="btnProgress">처리완료</button>';
		                    }
		                    if(fnIsProgramAuth("EXCELDOWN")) {
		                        rtnValue += '  ' + '<button type="button" class="k-button k-button-icontext k-grid-실패내역다운로드" kind="btnExcelDown" data-failtype="U">업로드 실패내역</button>';
		                    }
							if(dataItem.syncCodeName == '완료' && dataItem.orderFailCnt > 0) { // 배치상태 완료 이며 실패내역이 존재 할 경우
								if (fnIsProgramAuth("EXCELDOWN")) {
									rtnValue += '<button type="button" style ="margin-top: 2px" class="k-button k-button-icontext btn-s k-grid-실패내역다운로드" kind="btnExcelDown" data-failtype="B">업데이트 실패내역</button>';
								}
							} else {
								//rtnValue += '<div style="height:30px;"></div>';
							}
		                    return rtnValue;
		                }
		                else {
		                	return '-';
		                }
		            }
		        }
				,{ field:'ifEasyadminInfoId', hidden:true}
				,{ field:'processCode', hidden:true}
			]
		}
}