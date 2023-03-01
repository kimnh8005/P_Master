/**-----------------------------------------------------------------------------
 * description 		 : 회원그룹설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.21		강윤경          최초생성
 * @ 2021.02.05     이원호          수정 - 회원등급 관리 방식 변경 분
 * **/
'use strict';


var PAGE_SIZE = 10;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	let nowYear = new Date().getFullYear();
	const nowMonth = new Date().getMonth() +1;
	if(nowMonth == 12) nowYear++;
	let startYY = [];
	let startMM = [];
	const dbStartDt = [];

	$("#startDateYear").on("change", function() {
		fnShowPerion();
	})

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'userGroup',
			callback : fnUI
		});
	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnSave').kendoButton();
	}
	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);
		var query = {
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					}
		};
		aGridDs.query( query );
		//	aGridDs.read(data);
	}
	function fnClear(){
		$('#searchForm').formClear(true);
	}

	//신규등록
	function fnNew(){
		aGrid.clearSelection();
		$('#inputForm').formClear(true);
		OPER_TP_CODE = "N";

		fnShowPerion();
		$('#calculatePeriod').data('kendoDropDownList').enable(false);
		fnKendoInputPoup({height:"255px" ,width:"400px",title:{key :"4368",nullMsg :'회원그룹 설정' } });
	}

	function fnPopupModify(urGroupMasterId){
        fnAjax({
            url     : '/admin/ur/userGroup/getUserGroupMaster',
            params  : {urGroupMasterId : urGroupMasterId},
            success :
                function( data ){
                    OPER_TP_CODE = 'U';

                    $('#inputForm [name="popupUrGroupMasterId"]').val(data.rows.urGroupMasterId);
                    $('#inputForm [name="groupMasterName"]').val(data.rows.groupMasterName);
                    $("#calculatePeriod").data('kendoDropDownList').value(data.rows.calculatePeriod);
                    $("#startDateYear").data('kendoDropDownList').value(data.rows.startDateYear);
                    fnShowPerion();
                    $('#calculatePeriod').data('kendoDropDownList').enable(false);
                    $("#startDateMonth").data('kendoDropDownList').value(data.rows.startDateMonth);
                    fnKendoInputPoup({height:"255px" ,width:"400px",title:{key :"4368",nullMsg :'회원그룹 설정' } });
                },
            isAction : 'select'
        });
	}

	//저장
	function fnSave(){
		var url  = '/admin/ur/userGroup/addUserMasterGroup';
		var cbId = 'insert';
		var data = $('#inputForm').formSerialize(true);
		data.urGroupMasterId = $('#inputForm [name="popupUrGroupMasterId"]').val();

		if(OPER_TP_CODE == 'U'){
		    url = '/admin/ur/userGroup/putUserMasterGroup';
		    cbId = 'update';
		}

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
				isAction : 'batch'
			});
		}
	}
	//close
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//가능 적용기간 체크
	function fnShowPerion() {
		startMM = [];
		let month = 1;
		if($("#startDateYear").val() == '' || $("#startDateYear").val() == nowYear) {
			month = nowMonth + 1;
		}

		//12월일 경우
		if(month == 13) {
			month = 1;
		}

		for(var i = month; i < 13; i++) {
			startMM.push({"CODE":('00'+ i).slice(-2)	,"NAME":i+"월"})
		}

		//적용기간(월)
		fnKendoDropDownList({
			id    : 'startDateMonth',
			data  : startMM,
			textField :"NAME",
			valueField : "CODE",
			value : ""
		});
	}

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/userGroup/getUserGroupMasterList',
			requestEnd : function(e){
			    $("#fnNew").prop("disabled", false);
			    for(var data of e.response.data.rows){
			        if(data.state == "예약"){
			            $("#fnNew").prop("disabled", true);
			        }
			    }
			}
		});
		aGridOpt = {
				dataSource: aGridDs
			,	navigatable: true
			,	columns   : [
					 { field:'urGroupMasterId',hidden:true}
					,{ field:'groupMasterName'	,title : '그룹명'		, width:'40px'	,attributes:{ style:'text-align:center' }
					    ,template : function(dataItem){
					        var returnValue = "";
					        if(dataItem.state == '예약'){
                                return "<div style = 'text-align:center;text-decoration: underline; color:blue;'>" + dataItem.groupMasterName + "</div>";
                            }
                            return dataItem.groupMasterName;
					    }
					}
					,{ field:'groupLevelCount'	,title : '등급 수'		, width:'40px'	,attributes:{ style:'text-align:center' }}
					,{ field:'startDate'		,title : '적용 시작일'		, width:'100px'	,attributes:{ style:'text-align:center' }
						,template: "#if(startDate){# #=kendo.toString(kendo.parseDate(startDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') # #} else {# #=startDate# #} #"}
					,{ field:'endDate'			,title : '적용 종료일'		, width:'100px'	,attributes:{ style:'text-align:center' }
						,template: "#if(endDate){# #=kendo.toString(kendo.parseDate(endDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') # #} else {# #='-'# #} #"}
					,{ field:'state'			,title : '상태'			, width:'40px'	,attributes:{ style:'text-align:center' }}
				    ,{ field : "management", title : "관리", width : "60px", attributes : {style : "text-align:center"},
		              template : function(dataItem){
		                var button = "";
                        if(dataItem.state == '예약'){
                            button = "<a href='#' role='button' class='btn-s btn-blue middle' kind='manage' >관리</a>";
                            button += "<a href='#' role='button' class='btn-s btn-red middle' kind='delete' >삭제</a>";
                        } else {
                            button = "<a href='#' role='button' class='fb-btn btn-gray inputForm__button' kind='manage' >보기</a>";
                        }
		            	return button;
		             }}
				]
			};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		// 수정 팝업 호출
		$("#aGrid").on("click", "tbody>tr>td", function() {
            var index = $(this).index();

            if($(this).closest('table').find('th').eq(index).text() == '그룹명'){
                var map = aGrid.dataItem(aGrid.select());
                if(map.state == "예약"){
                    fnPopupModify(map.urGroupMasterId);
                }
            }
        });

		// 관리
        $('#aGrid').on("click", "a[kind=manage]", function(e) {
            e.preventDefault();

            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
            let param = {};

    		var option = new Object();
    		option.url = "#/userGroupDetail";
    		option.menuId = 785;
    		option.data = {
    						editMode		  : dataItem.state == '예약' ? "EDIT" : "VIEW",
    						urGroupMasterId : dataItem.urGroupMasterId
    						};
    		$scope.$emit('goPage', option);

        });

        // 삭제 버튼 클릭
        $('#aGrid').on("click", "a[kind=delete]", function(e) {
            e.preventDefault();
            fnKendoMessage({message:'삭제하시면 예약한 등급도<BR>모두 함께 삭제됩니다.<BR>정말로 삭제 하시겠습니까?', type : "confirm" , ok : function(){

	            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
	            var url  = '/admin/ur/userGroup/delUserGroupMaster';
	    		var cbId = 'delete';

				fnAjax({
					url     : url,
					params  : {urGroupMasterId:dataItem.urGroupMasterId},
					success :
						function( data ){
							fnBizCallback(cbId, data);
						},
					isAction : 'batch'
				});
            }});

        });
	}


	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

        //등급산정기간
        let calculatePeriodParam = [];
        for(var i = 1; i < 13; i++) {
			calculatePeriodParam.push({"CODE":i,"NAME":i+"개월"})
		}
		fnKendoDropDownList({
			id    : 'calculatePeriod',
			data  : calculatePeriodParam,
		});

        //적용기간(년)
		for(var i=nowYear; i < nowYear+11; i++) {
			startYY.push({"CODE":i	,"NAME":i+"년"})
		}
		fnKendoDropDownList({
			id    : 'startDateYear',
			data  : startYY,
			value : ""
		});
	}


	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'insert':
				fnKendoMessage({
					message:"등록 되었습니다.",
					ok:function(){
						fnSearch();
						fnClose();
					}
				});
				break;

            case 'update':
				fnKendoMessage({
					message:"수정 되었습니다.",
					ok:function(){
						fnSearch();
						fnClose();
					}
				});
				break;

			case 'delete':
				fnKendoMessage({
					message:"삭제 되었습니다.",
					ok:function(){
						fnSearch();
					}
				});
				break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Cancel*/
    $scope.fnClose = function(){ fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	fnInputValidationForHangulAlphabetNumberSpace("groupMasterName");	// 한글 + 영문대소문자 + 숫자 + 빈칸


}); // document ready - END
