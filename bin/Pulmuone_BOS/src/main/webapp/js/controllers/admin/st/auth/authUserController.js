/*
* description 		 : 사용자권한관리
* @
* @ 수정일			수정자          수정내용
* @ ------------------------------------------------------
* @ 2017.01.10		최봉석          최초생성
* @
* */
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var bGridDs, bGridOpt, bGrid , bGridClick = false;
var cGridDs, cGridOpt, cGrid , cGridClick = false;
var selectedRow;
var ENTER = 13;
var pageParam = fnGetPageParam();
$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {flag: 'true'});

		fnPageInfo({
			PG_ID: 'authUser',
			callback: fnUI
		});

		//$('div#ng-view').css('width', '100%');
	}

	function fnUI() {
		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		setGridHeaderScrollWidth(); // 그리드 스크롤 너비만큼 패딩 적용

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton() {
		$('#fnSearch, #fnNew,#fnSave,  #fnClear,#fnRightGridDblClick,#fnGridDblClick').kendoButton();
		$('#fnDel').kendoButton({enable: false});
	}

	function fnSearch() {
		var data = {};
		data["useYn"] = 'Y';
		aGridDs.read(data);
	}

	function fnClear() {
		$('#searchForm').formClear(true);
	}

	function fnNew() {
		aGrid.clearSelection();
		$('#inputForm').formClear(true);
		inputFocus();
	}

	function fnSave() {

        fnKendoPopup({
            id: "itgcPopup",
            title: "ITSM 계정",
            src: "#/itgcPopup",
            param: {},
            width: "450px",
            height: "150px",
            success: function(id, data) {
                if( data.itsmId == undefined ){
                    fnKendoMessage({message : 'ITSM 계정을 입력해 주세요.'});
                    return;
                }
                fnSaveProcess(data);
            }
        });

	}

	function fnSaveProcess(data) {
        var map = aGrid.dataItem(aGrid.select());
        var selectRows = bGrid.tbody.find('input[class=bGridCheckbox]:checked').closest('tr');
        var insertArray = new Array();
        for (var i = 0; i < selectRows.length; i++) {
            var dataRow = bGrid.dataItem($(selectRows[i]));
            insertArray.push(dataRow);
        }

		fnAjax({
			url: '/admin/st/auth/addAuthUser',
			//params  : {insertData :kendo.stringify(cGridDs._data)  ,"stRoleTypeId" : map.stRoleTypeId },
			params: {insertData: kendo.stringify(cGridDs._data)
			    , "stRoleTypeId" : map.stRoleTypeId
			    , "roleName" : map.roleName
			    , "itsmId" : data.itsmId
			    },
			success:
				function (data) {
					fnBizCallback("save", data);
				},
			isAction: 'save' //save
		});
    }

	function fnDel() {
		fnKendoMessage({key: "4489", message: '삭제 하시겠습니까?', type: "confirm", ok: fnDelApply});
	}

	function fnDelApply() {
		alert("fnDelApply");
		var url = '/biz/st/auth/delAuth';
		var cbId = 'delete';
		var data = $('#inputForm').formSerialize(true);
		var map = aGrid.dataItem(aGrid.select());
		if (data.rtnValid) {
			fnAjax({
				url: url,
				params: data,
				success:
					function (data) {
						fnBizCallback(cbId, map);
					}
			});
		} else {

		}
	}

	function fnClose() {

	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid() {

		aGridDs = fnGetDataSource({
			url: '/admin/st/auth/getRoleListWithoutPaging'
		});

		aGridOpt = {
			dataSource: aGridDs
			, navigatable: true
			, scrollable: true
			, columns: [
				{
					field: 'roleName',
					title: {key: '4345', nullMsg: '역할명'},
					width: '160px',
					attributes: {style: 'text-align:left'}
				},
				{filed: 'stRoleTypeId', hidden: true}

			]
			, bindStatus: 'rebind'
			, frAtEvent: fnGridClick
		};
		aGrid = $('#aGrid').initializeKendoGrid(aGridOpt).cKendoGrid();
		//------------------------------- 왼쪽그리드 E -------------------------------
		$("#aGrid").on("click", "tbody>tr", function () {
			fnGridClick();
		});

		bGridDs = fnGetPagingDataSource({
			url: '/admin/st/auth/getAuthUserOutList',
			//pageSize: PAGE_SIZE,
			schema : {
				model : {
					id : "urUserId",
					fields : {
						loginId : {
							type : "string",
							nullable : false,
							editable : false,
						},
						userName : {
							type : "string",
							editable : false,
						},
						regalName : {
							type : "string",
						},
						organizationName : {
							type : "string",
						},
						email : {
							type : "string"
						}
						, compTpNm : {
							type : "string"
						}
					}
				}
			},
			filter: {
				filters: [
					{
						field: "stRoleTypeId", operator: "eq", value: function (e) {
							var aMap = aGrid.dataItem(aGrid.select());
							if (aMap) {
								return aMap.stRoleTypeId;
							} else {
								return null;
							}
						}
					}
					,{
						field: "notUrUserIds", operator: "neq", value: function (e) {
							var list = cGridDs.data();
							if(list.length > 0) {
								var returnData = [];
								for (var i = 0; i < list.length; i++) {
									returnData.push(list[i].urUserId);
								}
								return returnData;
							}  else {
								return [];
							}
						}
					}
				]
			}
		});

		bGridOpt = {
			dataSource: bGridDs
			//, pageable: {
			//	pageSizes: [20, 30, 50],
			//	buttonCount: 5,
			//	responsive: false
			//}
			, filterable: {
				mode: "row",
				showOperators: false
			}
			, navigatable: true
			, scrollable: true
			, serverFiltering : true
			, columns: [
				{
					field: 'chk',
					title: {key: '4346', nullMsg: '체크박스'},
					width: '36px',
					attributes: {style: 'text-align:center'}
					,
					template: "<input type='checkbox' class='bGridCheckbox' name='BGRID'/>"
					,
					headerTemplate: "<input type='checkbox' id='checkBoxAll1' />"
					,
					filterable: false
				}
				, {
					field: 'loginId',
					title: {key: '4560', nullMsg: '아이디'},
					width: '120px',
					attributes: {style: 'text-align:left'},
					filterable: {
						cell: {
							delay : 1500,
							suggestionOperator: "contains",
							showOperators: false
						}
					}
				}
				, {
					field: 'userName',
					title: {key: '4343', nullMsg: '관리자 명'},
					width: '115px',
					attributes: {style: 'text-align:left'},
					filterable: {
						cell: {
							delay : 1500,
							suggestionOperator: "contains",
							showOperators: false
						}
					}
				}
				, {
					field: 'regalName',
					title: {key: '4871', nullMsg: '법인'},
					width: '180px',
					attributes: {style: 'text-align:left'}
                    , template : function(dataItem) {
                        if (dataItem.compTp == "COMPANY_TYPE.HEADQUARTERS") {
                            return dataItem.regalName;
                        }
                        else {
                            return '(' + dataItem.clientTpNm + ') ' + dataItem.compNm;
                        }
                    }
					, filterable: false
//					, filterable: {
//						cell: {
//							suggestionOperator: "contains"
//							, showOperators: false
//							, template: function (e) {
//								e.element.attr('id', 'regalFilter')
//								e.element.kendoDropDownList({
//									dataSource: {
//										serverFiltering: true,
//										transport: {
//											read: {
//												url: "/admin/user/employee/getPulmuoneRegalListWithoutPaging"
//											}
//										},
//										schema: {
//											data: function (response) {
//												return response.data.rows
//											}
//										}
//									},
//									dataTextField: "erpRegalName",
//									dataValueField: "erpRegalCode",
//									valuePrimitive: true,
//									optionLabel: "All"
//								});
//							}
//						}
//					}
				}
				, {
					field: 'organizationName',
					title: {key: '4871', nullMsg: '부서'},
					width: '180px',
					attributes: {style: 'text-align:left'}
					, filterable: false
//					, filterable: {
//						cell: {
//							suggestionOperator: "contains"
//							, showOperators: false
//							, template: function (e) {
//
//								e.element.attr("id", "organizationFilter");
//
//								e.element.kendoDropDownList({
//									dataSource: {
//										serverFiltering: true,
//										transport: {
//											read: {
//												url: "/admin/user/employee/getPulmuoneOrganizationList"
//												, dataType: 'json'
//											}
//										},
//										schema: {
//											data: function (response) {
//												return response.data.rows
//											}
//										}
//									},
//									cascadeFrom: "regalFilter",
//									cascadeFromField: "erpRegalCode",
//									dataTextField: "erpOrganizationName",
//									dataValueField: "erpOrganizationName",
//									valuePrimitive: true,
//									optionLabel: "All"
//								});
//							}
//						}
//					}
				}
				, {
					field: 'email',
					title: {key: '2839', nullMsg: '이메일'},
					width: '174px',
					attributes: {style: 'text-align:left'},
					filterable: {
						cell: {
							delay : 1500,
							suggestionOperator: "contains",
							showOperators: false
						}
					}
				}
				, {
					field: 'compTpNm',
					title: '관리자유형',
					width: '70px',
					attributes: {style: 'text-align:left'},
					filterable: false
//					filterable: {
//						cell: {
//							delay : 1500,
//							suggestionOperator: "contains",
//							showOperators: false
//						}
//					}
				}
				, {field: 'urUserId', hidden: true}
				, {field: 'stRoleTypeId', hidden: true}
			]
		};


		bGrid = $('#bGrid').initializeKendoGrid(bGridOpt).cKendoGrid();

		bGrid.bind("dataBound", function () {
			if (bGrid.dataSource && bGrid.dataSource._view.length > 0) {
				fnSetAllCheckbox('bGridCheckbox', 'checkBoxAll1');
				$('input[name=BGRID]').on("change", function () {
					fnSetAllCheckbox('bGridCheckbox', 'checkBoxAll1');
				});
			}
		});
		$(bGrid.tbody).on("click", "td", function (e) {
			var row = $(this).closest("tr");
			var rowIdx = $("tr", aGrid.tbody).index(row);
			var colIdx = $("td", row).index(this);
			if (colIdx > 0) {
				fnBGridClick($(e.target).closest('tr'));
			}
		});


		cGridDs = fnGetDataSource({
			url: '/admin/st/auth/getAuthUserInList',
			// filter: {
			// 	filters: [
			// 		{
			// 			field: "stRoleTypeId", operator: "eq", value: function (e) {
			// 				var aMap = aGrid.dataItem(aGrid.select());
			// 				if (aMap) {
			// 					return aMap.stRoleTypeId;
			// 				} else {
			// 					return null;
			// 				}
			// 			}
			// 		}
			// 	]
			// }
		});

		cGridOpt = {
			dataSource: cGridDs
			, navigatable: true
			, scrollable: true
			, filterable: {
				mode: "row",
				showOperators: false,
				operators: {
					string: {
						contains: "contains"
					}
				}
			}
			, columns: [
				{
					field: 'chk',
					title: {key: '4346', nullMsg: '체크박스'},
					width: '36px',
					attributes: {style: 'text-align:center'}
					,
					template: "<input type='checkbox' class='cGridCheckbox' name='CGRID'/>"
					,
					headerTemplate: "<input type='checkbox' id='checkBoxAll2' />"
					,
					filterable: false
				}
				//,{ field:'LOGIN_ID'		,title : { key : '4560'	, nullMsg :'아이디'}			, width:'100px',attributes:{ style:'text-align:left' }}
				//,{ field:'userName'		,title : { key : '4343'		, nullMsg :'사용자명'}		, width:'100px',attributes:{ style:'text-align:left' }}
				//,{ field:'companyName'	,title : { key : '4871'		, nullMsg :'회사명'}		, width:'100px',attributes:{ style:'text-align:left' }}
				//,{ field:'companyName'	,title : { key : '2839'		, nullMsg :'이메일'}		, width:'100px',attributes:{ style:'text-align:left' }}


				// , {
				// 	field: 'userName',
				// 	title: {key: '4344', nullMsg: '사용자'},
				// 	width: '250px',
				// 	attributes: {style: 'text-align:left'},
				// 	template: kendo.template($("#urInfoTpl").html()),
				// 	filterable: {
				// 		cell: {
				// 			delay : 1500,
				// 			suggestionOperator: "contains",
				// 			showOperators: false
				// 		}
				// 	}
				// }

				,{
					field: 'loginId',
					title: {key: '4560', nullMsg: '아이디'},
					width: '90px',
					attributes: {style: 'text-align:center'},
					filterable: {
						cell: {
							delay : 1500,
							suggestionOperator: "contains",
							showOperators: false
						}
					}
				}
				, {
					field: 'userName',
					title: {key: '4343', nullMsg: '관리자 명'},
					width: '90px',
					attributes: {style: 'text-align:center'},
					filterable: {
						cell: {
							delay : 1500,
							suggestionOperator: "contains",
							showOperators: false
						}
					}
				}
				, {
					field: 'companyName', title: '추가정보', width: '270px', attributes: {style: 'text-align:left'}
//					, template: kendo.template($("#urInfoTpl").html())
	                , template : function(dataItem) {
	                	var html = '';
	                	if (dataItem.compTp == "COMPANY_TYPE.HEADQUARTERS") {
		                	html += '법인 : ' + dataItem.regalName + ' / 부서 : ' + dataItem.organizationName
	                    }
	                    else {
		                	html += '법인 : (' + dataItem.clientTpNm + ') ' + dataItem.compNm + ' / 부서 : ' + dataItem.organizationName
	                    }
	                	html += '<br/> Email : ' + dataItem.email;
	                	return html;
	                }
					, filterable: false
				}
				, {field: 'urUserId', hidden: true}
			]
			,key: '5920', noRecordMsg:'검색결과가 없습니다.'
		};


		cGrid = $('#cGrid').initializeKendoGrid(cGridOpt).cKendoGrid();

		cGrid.bind("dataBound", function () {
			if (cGrid.dataSource && cGrid.dataSource._view.length > 0) {
				fnSetAllCheckbox('cGridCheckbox', 'checkBoxAll2');
				$('input[name=CGRID]').on("change", function () {
					fnSetAllCheckbox('cGridCheckbox', 'checkBoxAll2');
				});

			}
		});
		$(cGrid.tbody).on("click", "td", function (e) {
			var row = $(this).closest("tr");
			var rowIdx = $("tr", aGrid.tbody).index(row);
			var colIdx = $("td", row).index(this);
			if (colIdx > 0) {
				fncGridClick($(e.target).closest('tr'));
			}
		});


		fnSearch();

	}

	function fnGridClick() {
		let isbGridChanged = $("#bGrid").data('kendoGrid').dataSource.hasChanges();
		let iscGridChanged = $("#cGrid").data('kendoGrid').dataSource.hasChanges();

		if (isbGridChanged || iscGridChanged) {
			fnKendoMessage({
				message: '변경된 데이터가 있습니다 저장하지 않고 나가시겠습니까?', type: "confirm", ok: fnMoveRoleType, cancel: function () {
					$("#aGrid").data("kendoGrid").select("tr:eq(" + selectedRow + ")");
				}
			})
		} else {
			fnMoveRoleType();
		}
	}

	function fnMoveRoleType() {

		//bGridDs 필터때문에 cGridDs data 초기화
		cGridDs.data([]);
		bGridDs.read();
		var aMap = aGrid.dataItem(aGrid.select());
		cGridDs.read({stRoleTypeId : aMap.stRoleTypeId});

		// selectedRow = $("#aGrid").data("kendoGrid").select().index()
	}

	function fnBGridClick(param) {
		var clickRowCheckBox = param.find('input[type=checkbox]');
		if (clickRowCheckBox.prop('checked')) {
			clickRowCheckBox.prop('checked', false);
		} else {
			clickRowCheckBox.prop('checked', true);
		}
		fnSetAllCheckbox('bGridCheckbox', 'checkBoxAll1');
	}

	function fncGridClick(param) {
		var clickRowCheckBox = param.find('input[type=checkbox]');
		if (clickRowCheckBox.prop('checked')) {
			clickRowCheckBox.prop('checked', false);
		} else {
			clickRowCheckBox.prop('checked', true);
		}
		fnSetAllCheckbox('cGridCheckbox', 'checkBoxAll2');
	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox() {
		fnTagMkRadioYN({id: "intputActive", tagId: "useYn", chkVal: 'Y'});
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus() {
		$('#input1').focus();
	};

	function condiFocus() {
		$('#condition1').focus();
	};

	/**
	 * 콜백합수
	 */
	function fnBizCallback(id, data) {
		switch (id) {
			case 'select':
				//form data binding
				$('#searchForm').bindingForm(data, 'rows', true);
				break;
			case 'insert':
				if (data.rows == "DUP_DATA") {
					fnKendoMessage({key: "368", message: '중복입니다.', ok: inputFocus});
				} else {
					aGridDs.insert(data.rows);
					;
					fnKendoMessage({key: "369", message: '입력되었습니다.', ok: fnNew});
				}
				break;
			case 'save':
				bGridDs.fetch();
				cGridDs.fetch();
				fnKendoMessage({key: "368", message: '저장되었습니다.', ok: fnMoveRoleType});
				break;
			case 'update':
				fnUpdateGrid(data, $("#aGrid"), "rows");
				fnKendoMessage({key: "367", message: '수정되었습니다.'});
				break;
			case 'delete':
				aGridDs.remove(data);
				fnNew();
				//aGridDs.total = aGridDs.total-1;
				fnKendoMessage({key: "366", message: '삭제되었습니다.'});
				break;

		}
	}

	function fnGridDblClick() {
		var aMap = aGrid.dataItem(aGrid.select());
		var selectRows = bGrid.tbody.find('input[class=bGridCheckbox]:checked').closest('tr');
		if (selectRows.length > 0) {
			for (var i = 0; i < selectRows.length; i++) {
				var bMap = bGrid.dataItem($(selectRows[i]));


				bMap.each
				var insertData = {
					"loginId": bMap.loginId,
					"userName": bMap.userName,
					"regalName": bMap.regalName,
					"organizationName": bMap.organizationName,
					"email": bMap.email,
					"urUserId": bMap.urUserId,
					"stRoleTypeId": aMap.stRoleTypeId
				};

				cGridDs.add(insertData);
			}
				for (var i = 0; i < selectRows.length; i++) {
					bGrid.removeRow($(selectRows[i]));
			}
		}else {
			return fnKendoMessage({message: "관리자를 선택해주세요."});
		}
	};
	function fnRightGridDblClick(){
		var aMap = aGrid.dataItem(aGrid.select());
		var selectRows 	= cGrid.tbody.find('input[class=cGridCheckbox]:checked').closest('tr');
		if(selectRows.length > 0) {
			for (var i = 0; i < selectRows.length; i++) {
				var cMap = cGrid.dataItem($(selectRows[i]));
				var insertData = {
					"loginId": cMap.loginId,
					"userName": cMap.userName,
					"regalName": cMap.regalName,
					"organizationName": cMap.organizationName,
					"email": cMap.email,
					"urUserId": cMap.urUserId,
					"stRoleTypeId": aMap.stRoleTypeId
				};

				bGridDs.add(insertData);
			}
			for (var i = 0; i < selectRows.length; i++) {
				cGrid.removeRow($(selectRows[i]));
			}
		}else {
			return fnKendoMessage({message: "권한설정 된 관리자를 선택해주세요."});
			}
	};

	// Resize Scrollbar function & event
	var resizingTimer = null;

	window.addEventListener("resize", function(e) {

		if( resizingTimer ) {
			clearTimeout(resizingTimer);
		}

		resizingTimer = setTimeout(function(){

			setGridHeaderScrollWidth();
			clearTimeout(resizingTimer);

		}, 200);

	})

	function setGridHeaderScrollWidth() {
			var scrollBarWidth = fbGetScrollBarWidth();

			$(".k-grid-header").css("padding-right", scrollBarWidth - 1);
	}



	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnGridDblClick = function( ) {fnGridDblClick();};
	$scope.fnRightGridDblClick = function( ) {fnRightGridDblClick();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END

