/**-----------------------------------------------------------------------------
 * description 		 : 쇼핑몰 혜택관리 - 임직원 혜택 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.30		박승현          최초생성
 * @
 * **/
'use strict';


var FULL_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

$(document).ready(function () {
	var $document = $(document);
	var $noRecords = $("#grid-norecords");

	fnInitialize();

	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'policyBenefitEmployeeBrandGroup',
			callback : fnUI
		});
	};

	function fnUI(){

		setDefault();

		fnGetLastModifyDate();

//		fnInitButton();	//Initialize Button  ---------------------------------

//		fnInitOptionBox();//Initialize Option Box ------------------------------------

//		fnInitGrid();	//Initialize Grid ------------------------------------

	}
	function fnInitOptionBox(){
		// 브랜드 데이터소스
		var apiData = new kendo.data.DataSource({
			transport: {
				read: {
					url: "/admin/policy/benefit/getPolicyBenefitEmployeeGroupList",
					type: "POST",
					dataType: "json",
                    beforeSend: function(req) {
                        req.setRequestHeader('authMenuID', CLICKED_MENU_ID);
                    }
				}
			},
			schema: {
				data: function (response) {
					return response.data.rows;
				},
				model : {
					fields : {
						companyList : {
							defaultValue : [],
						},
						groupList : {
							defaultValue : [],
						}
					}
				}
			}
		})
		window.apiData = apiData;

		apiData.read();
		return apiData;
	};

	/************************** functions **************************/

	function setDefault() {
		var DS = setDataSource();

		var apiData = fnInitOptionBox();
		var viewModelOption = setViewModel({
//			apiData: apiData,
			regalDs: DS.regal,
			brandGroupDs: DS.brandGroup,
			cycleDataDs: DS.cycleData,
		})
		var viewModel = kendo.observable(viewModelOption);

		DS.cycleData.fetch(function () {
			var _data = this.data();
			viewModel.set("cycleDataDs", _data);
		});
		apiData.fetch(function () {
			var _data = this.data();
			viewModel.set("apiData", _data);
			showNorecord(_data.length);
		});

		window.viewModel = viewModel;
//		apiDataSet();
		bindViewModel("#view", viewModel);
	}

	// 뷰모델 설정
	function setViewModel(option) {
		return {
			recentUpdated: kendo.toString(new Date(), FULL_DATE_FORMAT),
			apiData: option.apiData || [],
			cycleDataDs: option.cycleDataDs,
			regalDs: option.regalDs,
			brandGroupDs: option.brandGroupDs,
			list: function () {
				return this.get("apiData");
			},
			onAddLegal: onAddLegal,
			onRemoveLegal: onRemoveLegal,
			onAddGroupItem: onAddGroupItem,
			onRemoveGroupItem: onRemoveGroupItem,
			onAddGroupData: onAddGroupData,
			onRemoveGroupData: onRemoveGroupData,
			fnCreate: fnCreate,
			onRemoveRow: onRemoveRow,
			onSave: onSave,
			onCheckAmmount: onCheckAmmount,
			onCheckGroupName: onCheckGroupName,
			onChange: function (e) {
			},
			isNewData: false,
		}
	}

	// Viewmodel 화면에 바인딩
	function bindViewModel(target, viewModel) {
		var $target = $(target);
		kendo.bind(target, viewModel);
	}

	// 켄도 datasource 세팅
	function setDataSource() {

		var brandGroupDs = new kendo.data.DataSource({
			transport: {
				read: {
					url: "/admin/policy/benefit/getPolicyBenefitEmployeeBrandGroupList",
					type: "POST",
					dataType: "json",
					data: { "searchType" : "BRANDGROUP"},
                    beforeSend: function(req) {
                        req.setRequestHeader('authMenuID', CLICKED_MENU_ID);
                    }
				}
			},
			schema: {
				data: function (response) {
					return response.data.rows;
				}
			}
		})

		// 법인 데이터소스
		var regalDs = fnGetDataSource({
			url : '/admin/user/employee/getPulmuoneRegalList'
		});

		// 한도액 주기 공통코드
		var cycleDataDs = new kendo.data.DataSource({
			transport: {
				read: {
					url: "/admin/comn/getCodeList",
					type: "GET",
					dataType: "json",
					data: { "stCommonCodeMasterCode" : "EMPL_DISC_LIMIT_CYCLE_TP", "useYn" : "Y" },
                    beforeSend: function(req) {
                        req.setRequestHeader('authMenuID', CLICKED_MENU_ID);
                    }
				}
			},
			schema: {
				data: function (response) {
					return response.data.rows;
				}
			}
		})

		brandGroupDs.read();
		regalDs.read();
		cycleDataDs.read();

		var DATA_SOURCE = {
			brandGroup: brandGroupDs,
			regal: regalDs,
			cycleData: cycleDataDs,
		}
		return DATA_SOURCE;
	}


	function fnDsExtract(operation ){

		var rtnInsertRec = [];
		var rtnUpdateRec = [];
		var rtnDeleteRec = [];

		var list = viewModel.get("apiData");

		for(var i = 0; i < list.length; i++) {
			if(list[i].isNew() && (list[i].psEmplDiscMasterId == null || list[i].psEmplDiscMasterId == ''
				|| list[i].psEmplDiscMasterId == 'undefined' || list[i].psEmplDiscMasterId == undefined)){
				rtnInsertRec.push(list[i].toJSON());
			}else if(list[i].dirty){
				rtnUpdateRec.push(list[i].toJSON());
			}
		}

		if( operation == 'insert' ){
			return rtnInsertRec;
		}else if( operation == 'update' ){
			return rtnUpdateRec;
		}else if( operation == 'delete' ){
            return rtnDeleteRec;
        }
	}

	function onSave(e) {
		var data = this.list();
		if (!data) {
			fnKendoMessage({ message: "데이터가 없습니다." });
			return;
		}
		var changeCnt =0;
        var insertData = fnDsExtract('insert');
        var updateData = fnDsExtract('update');
        changeCnt = insertData.length + updateData.length;

		var check = isEmptyData(data.toJSON());

		if (check.isEmpty) {
			fnKendoMessage({ message: "\"" + check.field + "\"을 입력해주세요." });
			return;
		}
		if(changeCnt ==0){
            fnKendoMessage({message :  '데이터 변경사항이 없습니다.'});
            return ;
        }else{
        	var param = {"insertData" : kendo.stringify(insertData)
        			,"updateData" : kendo.stringify(updateData)
        	};
        	fnAjax({
        		url     : '/admin/policy/benefit/putPolicyBenefitEmployeeGroup',
        		params  : param,
        		success :
        			function( data ){
        			fnKendoMessage({  message : "저장이 완료되었습니다."
                        , ok : function(){
                        	var apiData = fnInitOptionBox();
                    		apiData.fetch(function () {
                    			var _data = this.data();
                    			viewModel.set("apiData", _data);
                    			showNorecord(_data.length);
                    		});
        	    			viewModel.get("apiData").trigger("change", {
        	    				field: "apiData"
        	    			})
                          }
                       });
        		},
        		isAction : 'save'
        	});
        }
		return;
	}
	// 법인 추가 버튼 이벤트
	function onAddLegal(e) {
		// 현재 포함되어있는 tr 태그의 인덱스

		var rowIndex = $(e.target).closest(".outer-row").index();

		var $input = $(e.target).parent().find("input[data-role='dropdownlist']");

		if (!$input.length || typeof rowIndex == "undefined") {
			console.error("no dropdownlist or rowIndex");
			return;
		}

		var $kendoList = $input.data("kendoDropDownList");

		var _erpRegalName = $kendoList.text();
		var _erpRegalCode = $kendoList.value();

		var rowData = this.list()[rowIndex];

		// companyList 존재 여부 체크
		var companyList = rowData.companyList ? rowData.companyList.toJSON().slice() : [];

		if (!_erpRegalName || !_erpRegalCode) {
			fnKendoMessage({ message: "법인을 선택해주세요." });
			return;
		}

		var apiData = this.list();

		var _isCompany = checkRedupliacte(_erpRegalCode, apiData.toJSON(), rowIndex, "erpRegalCode", "companyList");

		if (_isCompany == -2) {
			fnKendoMessage({
				message: "이미 존재하는 법인입니다.",
			})
			return;
		}
		var newData = {
				erpRegalName: _erpRegalName,
				erpRegalCode: _erpRegalCode,
			};
		if(_isCompany == -1){
			companyList.push(newData);
			rowData.set("companyList", companyList);
		}
		if(_isCompany > -1){
			fnKendoMessage({message: "이미 다른 혜택그룹별 법인 목록에 포함되어 있습니다. 이동하시겠습니까?", type: "confirm"
				,ok : function(){
	    			var dataItemDuplicate = apiData[_isCompany];
	    			var orgCompanyList = dataItemDuplicate.companyList.toJSON().slice();
	    			if (!Array.isArray(orgCompanyList)) return;
	    			var index = orgCompanyList.findIndex(function (item) {
	    				return item.erpRegalCode == _erpRegalCode;
	    			})
	    			var newCompanyList = [].concat(orgCompanyList.slice(0, index), orgCompanyList.slice(index + 1));
	    			dataItemDuplicate.set("companyList", newCompanyList);
	    			companyList.push(newData);
	    			rowData.set("companyList", companyList);

	            }
				,cancel  : function(){
                }
			});
		}
		this.trigger("change", {
			field: "apiData[" + rowIndex + "]",
		})
		$kendoList.text("");
		$kendoList.select(-1);
	}



	// 법인 삭제 버튼 이벤트
	function onRemoveLegal(e) {
		var self = this;
		fnKendoMessage({
			message: "법인그룹을 삭제하시겠습니까?",
			type: "confirm",
			ok: function (event) {
				removeRegal.call(self, e);
				return true;
			},
			cancel: function (event) {
				return false;
			}
		})
	}

	function removeRegal(e) {
		// 현재 포함되어있는 tr 태그의 인덱스
		var rowIndex = $(e.target).closest(".outer-row").index();

		var $input = $(e.target).parent().find("input[name^='regalName']");

		var _code = $input.data().regalCode;

		var companyList = this.list()[rowIndex].companyList.slice();

		let _index = "";

		companyList.some(function (c, index) {
			if (c.erpRegalCode == _code) {
				_index = index;
			}
			return (c.erpRegalCode == _code);
		})

		companyList.splice(_index, 1);
		this.list()[rowIndex].set("companyList", companyList);

		this.trigger("change", {
			field: "apiData[" + rowIndex + "].companyList",
		})
	}

	// 할인 그룹 아이템 추가 이벤트
	function onAddGroupItem(e) {
		// 가장 바깥의 행을 통해서 배열의 인덱스를 가져온다.
		var rowIndex = $(e.target).closest("tr.outer-row").index();

		var _data = this.list()[rowIndex];

		// apiData[index].groupList[index].brandGroupList ==> 데이터 추가
		// 각 groupList는 table내의 한 행에 매칭되므로  "tr.groupList-row"의 데이터셋을 통해 인덱스를 가져온다.
		var _groupListIdx = $(e.target).closest("tr.groupList-row").data().grouplistIdx;

		var _groupList = _data.groupList[_groupListIdx];


		// 데이터를 추가할 리스트
		var newBrandGroupList = _groupList.brandGroupList.toJSON().slice();

		// 중복 체크, 선택되었는지 체크
		var $input = $(e.target).parent().find("input[data-role='dropdownlist']");
		var $kendoList = $input.data("kendoDropDownList");

		var _groupName = $kendoList.text();
		var _psEmplDiscBrandGrpId = $kendoList.value();

		if (!_psEmplDiscBrandGrpId || !_groupName) {
			fnKendoMessage({ message: "브랜드그룹을 선택해주세요." });
			return;
		}

		var apiData = _data.groupList;

		var isbrandGroup = checkRedupliacte(_psEmplDiscBrandGrpId, _data.groupList.toJSON(), _groupListIdx, "psEmplDiscBrandGrpId", "brandGroupList");

		if (isbrandGroup == -2) {
			fnKendoMessage({ message: "이미 존재하는 브랜드그룹입니다." });
			return;
		}

		var discountRatio = $kendoList.dataSource.data().filter(function (v) {
			return v.psEmplDiscBrandGrpId === _psEmplDiscBrandGrpId
		}
		)[0].discountRatio || "50%";
		// 새로 추가할 데이터 객체 생성
		var newData = {
			psEmplDiscBrandGrpId: _psEmplDiscBrandGrpId,
			groupName: _groupName,
			discountRatio: discountRatio,
		};

		if(isbrandGroup == -1){
			newBrandGroupList.push(newData);
			viewModel.set("apiData[" + rowIndex + "].groupList[" + _groupListIdx + "].brandGroupList", newBrandGroupList);
			this.list().trigger("change", {
				field: "apiData[" + rowIndex + "].groupList[" + _groupListIdx + "].brandGroupList"
			});
		}

		if(isbrandGroup > -1){
			fnKendoMessage({message: "이미 다른 할인그룹별 할인율 브랜드 그룹 목록에 포함되어 있습니다. 이동하시겠습니까?", type: "confirm"
				,ok : function(){
	    			var dataItemDuplicate = apiData[isbrandGroup];
	    			var orgBrandGroupList = dataItemDuplicate.brandGroupList.toJSON().slice();
	    			if (!Array.isArray(orgBrandGroupList)) return;
	    			var index = orgBrandGroupList.findIndex(function (item) {
	    				return item.psEmplDiscBrandGrpId == _psEmplDiscBrandGrpId;
	    			})
	    			var copyBrandGroupList = [].concat(orgBrandGroupList.slice(0, index), orgBrandGroupList.slice(index + 1));

	    			_data.set("groupList[" + isbrandGroup + "].brandGroupList", copyBrandGroupList);

	    			newBrandGroupList.push(newData);
	    			viewModel.set("apiData[" + rowIndex + "].groupList[" + _groupListIdx + "].brandGroupList", newBrandGroupList);
	    			viewModel.get("apiData").trigger("change", {
	    				field: "apiData[" + rowIndex + "]",
	    			})
//	    			_data.set("groupList[" + _groupListIdx + "].brandGroupList", newBrandGroupList);
	            }
				,cancel  : function(){
                }
			});
		}
		// NOTE 데이터가 추가되면 trigger를 통해 화면을 렌더링한다.
		this.trigger("change", {
			field: "apiData[" + rowIndex + "]",
		})
		$kendoList.text("");
		$kendoList.select(-1);
	}

	// 할인 그룹 아이템 삭제 이벤트
	function onRemoveGroupItem(e) {
		var self = this;
		fnKendoMessage({
			message: "해당 그룹을 삭제하시겠습니까?",
			type: "confirm",
			ok: function (event) {
				removeGroupItem.call(self, e);
				return true;
			},
			cancel: function (event) {
				return false;
			}
		})
	}

	function removeGroupItem(e) {
		var rowIndex = $(e.target).closest("tr.outer-row").index();

		var _data = this.list()[rowIndex];

		// apiData[index].groupList[index].brandGroupList ==> 데이터 추가
		// 각 groupList는 table내의 한 행에 매칭되므로  "tr.groupList-row"의 데이터셋을 통해 인덱스를 가져온다.
		var _groupListIdx = $(e.target).closest("tr.groupList-row").data().grouplistIdx;

		var _groupList = _data.groupList[_groupListIdx];

		// 데이터를 추가할 리스트
		var newBrandGroupList = _groupList.brandGroupList.toJSON().slice();

		var _psEmplDiscBrandGrpId = $(e.target).closest(".group__item__select").data().brandGroupId;

		// 삭제할 아이템의 인덱스를 담을 변수
		var _deleteIdx = "";

		newBrandGroupList.some(function (v, index) {
			if (v.psEmplDiscBrandGrpId == Number(_psEmplDiscBrandGrpId)) {
				_deleteIdx = index;
			}
			return v.psEmplDiscBrandGrpId === _psEmplDiscBrandGrpId;
		});

		console.log("삭제한 아이템은 " + JSON.stringify(newBrandGroupList[_deleteIdx]));

		// 인덱스에 해당하는 아이템 삭제
		newBrandGroupList.splice(_deleteIdx, 1);

		this.set("apiData[" + rowIndex + "].groupList[" + _groupListIdx + "].brandGroupList", newBrandGroupList);

		// NOTE 데이터가 추가되면 trigger를 통해 화면을 렌더링한다.
		this.list().trigger("change", {
			field: "apiData[" + rowIndex + "].groupList[" + _groupListIdx + "].brandGroupList"
		});
	}

	// 할인 그룹 추가 이벤트
	function onAddGroupData(e) {
		var self = this;
		var rowIndex = $(e.target).closest("tr.outer-row").index();

		var _data = this.list()[rowIndex];

		// groupList 존재 여부 체크
		var groupList = _data.groupList ? _data.groupList.toJSON().slice() : [];

		var groupListItem = {
				brandGroupList: [],
			cycle: null,
			limitAmt: "",
		};

		groupList.push(groupListItem);

		_data.set("groupList", groupList);

		this.list().trigger("change", {
			field: "apiData[" + rowIndex + "]"
		});
	}

	// 할인 그룹 삭제 이벤트
	function onRemoveGroupData(e) {
		var self = this;
		fnKendoMessage({
			message: "할인그룹을 삭제하시겠습니까?",
			type: "confirm",
			ok: function (event) {
				removeGroupData.call(self, e);
				return true;
			},
			cancel: function (event) {
				return false;
			}
		})
	}

	function removeGroupData(e) {
		var groupListIdx = $(e.target).data().grouplistIdx * 1;

		var rowIndex = $(e.target).closest("tr.outer-row").index();

		var rowData = this.list()[rowIndex];

		var newGroupList = rowData.groupList.slice();

		var _length = newGroupList.length;

		newGroupList.splice(groupListIdx, 1);

		rowData.set("groupList", newGroupList);

		//데이터가 추가되면 trigger를 통해 화면을 렌더링한다.
		this.list().trigger("change", {
			field: "apiData[" + rowIndex + "]"
		});
	}

	// 혜택그룹 신규생성 버튼 이벤트
	function fnCreate(e) {
		var self = this;

		var apiData = this.list() ? this.list().slice() : [];
		var ApiItem = kendo.data.Model.define({
			fields: {
				companyList: {
					editable: true
				},
				groupData: {
					editable: true,
				}
			}
		});
		//masterName
		var apiItem = new ApiItem({
			companyList: [],
			groupList: [],
		})
		var masterGroupNo = apiData.length + 1;
		apiItem.masterName = "혜택그룹 " + masterGroupNo + "그룹";
		apiData.unshift(apiItem);

		showNorecord(apiData.length);
		this.set("apiData", apiData);
		this.trigger("change", {
			field: "apiData"
		});
	}

	// 혜택 그룹 행 삭제
	function onRemoveRow(e) {
		e.preventDefault();
		var self = this;
		/*
		 * var $target = $(event.target);
			var $row = $target.closest("tr");
			var rowIdx = $("tr", grid.tbody).index($row);
		 */

		fnKendoMessage({
			message: "혜택그룹을 삭제하시겠습니까?",
			type: "confirm",
			ok: function (event) {
				removeRow.call(self, e);
				return true;
			},
			cancel: function (event) {
				return false;
			}
		})
	}
	// 행 삭제
	function removeRow(e) {
		var $target = $(e.target);
		var $row = $target.closest("tr");
		var dataItem = grid.dataItem($row);

		if(dataItem.psEmplDiscBrandGrpId != null && dataItem.psEmplDiscBrandGrpId != ''
			&& dataItem.psEmplDiscBrandGrpId != 'undefined' && dataItem.psEmplDiscBrandGrpId != undefined){
			var url  = "/admin/policy/benefit/delPolicyBenefitEmployeeBrandGroup";
			var param  = {'psEmplDiscBrandGrpId' : dataItem.psEmplDiscBrandGrpId };
				fnAjax({
					url     : url,
					params  : param,
					success :
						function(result){
							fnKendoMessage({message : "삭제되었습니다."});
							grid.removeRow($row);
                          },
                      isAction : 'update'
                  });
		}else{
			grid.removeRow($row);
		}
	}

	function removeRow(e) {
		var rowIndex = $(e.target).closest("tr.outer-row").index();
		var rowData = this.list()[rowIndex].toJSON();
		var apiData = this.list().slice();


		/*
		 * var _data = this.list()[rowIndex];

		// apiData[index].groupList[index].brandGroupList ==> 데이터 추가
		// 각 groupList는 table내의 한 행에 매칭되므로  "tr.groupList-row"의 데이터셋을 통해 인덱스를 가져온다.
		var _groupListIdx = $(e.target).closest("tr.groupList-row").data().grouplistIdx;

		var _groupList = _data.groupList[_groupListIdx];


		// 데이터를 추가할 리스트
		var newBrandGroupList = _groupList.brandGroupList.toJSON().slice();


		 */
		if(rowData.psEmplDiscMasterId != null && rowData.psEmplDiscMasterId != ''
			&& rowData.psEmplDiscMasterId != 'undefined' && rowData.psEmplDiscMasterId != undefined){
			var rtnDeleteRec = [];
			rtnDeleteRec.push(rowData);
			var url  = "/admin/policy/benefit/putPolicyBenefitEmployeeGroup";
			var param = {"deleteData" : kendo.stringify(rtnDeleteRec)};
			fnAjax({
				url     : url,
				params  : param,
				success :
					function(result){
					fnKendoMessage({message : "삭제되었습니다."});
					apiData.splice(rowIndex, 1);
					viewModel.set("apiData", apiData);
				},
				isAction : 'update'
			});
		}else{
			apiData.splice(rowIndex, 1);
		}

		showNorecord(apiData.length);
		this.set("apiData", apiData);
		this.list().trigger("change");
	}

	function onCheckAmmount(e) {
		var num_check = /[^0-9]/g

		var value = $(e.target).val().trim();

		if (!value.length) return;

		if (num_check.test(value)) {
			value = value.replace(num_check, "");
			$(e.target).val(value);
		}
		if(parseInt(value) > 99999999){
			value = value.substring(0,8);
			$(e.target).val(value);
		}
		//$(e.target).val(kendo.format("{0:\#\#,\#}", Number(value)) );
	}
	function onCheckGroupName(e) {
		var value = $(e.target).val().trim();

		if (!value.length) return;

		if (value.length > 10) {
			value = value.substring(0,10);
			$(e.target).val(value);
		}

		if (/[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]/g.test(value)) {
			value = value.replace(/[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]/g, "");
			$(e.target).val(value);
		}
	}

	// 그룹 데이터 중복 체크
	// data : 추가한 데이터, arr : 검사할 배열, prop : 검사할 프로퍼티
	function checkRedupliacte(data, arr, rowIdx, field, listName) {
		var result = -1;

		if (arr && arr.length) {
			arr.forEach(function (item, index) {
				if (item[listName]) {
					var list = item[listName];
					for (var i = 0; i < list.length; i++) {
						if (list[i][field] == data) {
							if (rowIdx == index) {
								result = -2;
							}else{
								result = index;
							}
							break;
						}
					}
				}
			})
		} else {
			result = -2;
		}
		return result;
	}


	//데이터 입력 검사
	function isEmptyData(obj) {
		var isEmpty = false;
		var field = "";
		var list = viewModel.get("apiData");

		if (!list || !list.length) return false;

		list = list.toJSON();

		for (var i = 0; i < list.length; i++) {
			var item = list[i];
			if(!item.masterName) {
				field = "혜택 그룹명";
				isEmpty = true;
				break;
			}
			if((!item.companyList || !item.companyList.length)) {
				field = "법인 그룹";
				isEmpty = true;
				break;
			}

			if((!item.groupList || !item.groupList.length)) {
				field = "할인 그룹";
				isEmpty = true;
				break;
			}

			if(item.groupList.length > 0) {
				for(var j = 0; j < item.groupList.length; j++) {
					var groupItem = item.groupList[j];
					if(!groupItem.brandGroupList.length) {
						field = "할인율 브랜드 그룹";
						isEmpty = true;
						break;

					}else if(groupItem.limitAmt == null || groupItem.limitAmt < 0 ) {
						field = "한도액";
						isEmpty = true;
						break;
					} else if (!groupItem.emplDiscLimitCycleTp) {
						field = "한도액 주기";
						isEmpty = true;
						break;
					}
				}
			}
		}

		return {
			isEmpty: isEmpty,
			field: field,
		};
	}

	function fnGetLastModifyDate(){
		fnAjax({
			url     : "/admin/policy/benefit/getLastModifyDatePolicyBenefitEmployeeGroup",
			method : "GET",
			success : function( data ){
				$("#lastUpdateDate").html(data.modifyDt);
			},
			isAction : "select"
		});
	}

	window.isEmptyData = isEmptyData;

	// grid-norecords 영역 display
	function showNorecord(length) {
		var _display = !length ? "block" : "none";
		$noRecords.css("display", _display);
	}

	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Clear*/
	//$scope.fnClear =function(){	fnClear();};
	/** Common Save*/
	//$scope.fnSave = function(){	fnSave();};
	/** Common Delete*/
	//$scope.fnDelInit = function(){	fnDelInit();};
	$scope.fnCreate = function(){	fnCreate();};

	//$scope.fnGridAdd = function(){	fnGridAdd();};
	//$scope.fnGridAddNull = function(){	fnGridAddNull();};
	//$scope.fnGridDel = function(){	fnGridDel();};
	//$scope.fnDataUp = function(){	fnDataUp();};
	//$scope.fnDataDown = function(){	fnDataDown();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END