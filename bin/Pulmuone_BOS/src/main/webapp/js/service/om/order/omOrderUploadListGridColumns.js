﻿/**-----------------------------------------------------------------------------
 * description 		 : 외부몰클레임 주문 리스트 목록 컬럼 정보
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.07		김명진   최초생성
 * @
 * **/

var outmallOrderUploadListGridUtil = {
		orderUploadList : function() {
		return [
				{ field:'no'                   ,title : 'No'	, width:'50px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'outMallTypeName'	    ,title : '엑셀 형식',width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'uploadInfo'   ,title : '업로드'	, width:'150px',attributes:{ style:'text-align:center' },
					template : function(dataItem){

						return "성공 : " + dataItem.successCount + " 건 / 실패 : " + dataItem.failCount + " 건";
					}
				}
				,{ field:'batchInfo'   ,title : '업데이트'	, width:'150px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						if(dataItem.batchStatusCode == '22'){
							if(dataItem.batchEndDateTime == "") {
								return "-";
							}
							return "성공 : " + dataItem.orderCreateDetlCount + " 건 / 실패 : " + (dataItem.successCount - dataItem.orderCreateDetlCount) + " 건";
						}
						return "-";
					}
				}

	            ,{ field:'uploadStatusCodeName' ,title : '등록상태',width:'90px',attributes:{ style:'text-align:center' }}

				,{ field:'batchEnd'   ,title : '주문생성 Batch 종료일자<BR>(소요시간)'	, width:'100px',attributes:{ style:'text-align:center' },
	                template : function(dataItem){
	                    if(dataItem.batchStatusCode == '22'){
	                    	if(dataItem.batchEndDateTime == "") {
	                    		return "-";
	                    	}
	                        return dataItem.batchEndDateTime + "<br/>(" + dataItem.batchExecutionTime + ")";
	                    }
	                    return "-";
	                }
	            }
				,{ field:'admin'	,title : '관리자'	, width:'120px',attributes:{ style:'text-align:center' },
	                template : function(dataItem){
	                    return dataItem.createName + " / " + dataItem.loginId;
	                }
	            }
	            ,{ field:'createDateName' ,title : '등록일자',width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'fileNm' ,title : '업로드 파일명',width:'300px',attributes:{ style:'text-align:center; text-decoration: underline; cursor: pointer;' },
						template : function(dataItem){
							let rtnValue = "";
							if(dataItem.fileNm != "") {
								if(dataItem.uploadJsonData != "" && fnIsProgramAuth("EXCELDOWN")) {
									rtnValue += "<div class='divExcelDownClick'  data-failtype='O'>" + dataItem.fileNm + "</div>";
								} else {
									rtnValue += dataItem.fileNm;
								}
							} else {
								if(dataItem.uploadJsonData != "" && fnIsProgramAuth("EXCELDOWN")) {
									rtnValue += '<button type="button" class="k-button k-button-icontext btn-s k-grid-업로드내역다운로드" kind="btnExcelDown" data-failtype="O">다운로드</button>';
								}
							}
							return rtnValue;
						}
					}
				,{ field:'management'		    ,title : '관리'		, width:'150px', attributes : { style : "text-align:center" },
	                template : function(dataItem){
	                	let rtnValue = "";
	                    if(dataItem.uploadFailCount > 0) {
							if (fnIsProgramAuth("EXCELDOWN")) {
								rtnValue += '<button type="button" class="k-button k-button-icontext btn-s k-grid-실패내역다운로드" kind="btnExcelDown" data-failtype="U">업로드 실패내역</button>';
							}
						}

	                    if(dataItem.batchStatusCode == '22' && dataItem.batchFailCount > 0) { // 배치상태 완료 이며 실패내역이 존재 할 경우
							if (fnIsProgramAuth("EXCELDOWN")) {
								rtnValue += '&nbsp;&nbsp;<button type="button" class="k-button k-button-icontext btn-s k-grid-실패내역다운로드" kind="btnExcelDown" data-failtype="B">업데이트 실패내역</button>';
							}
	                    } else {
	                    	//rtnValue += '<div style="height:30px;"></div>';
	                    }
	                    return rtnValue;
	                }
	            }
				,{ field:'ifOutmallExcelInfoId', title:'', hidden:true}
			]
		}
}