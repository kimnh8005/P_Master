/**-----------------------------------------------------------------------------
 * description 		 : 쇼핑몰 혜택관리 - 할인율 그룹 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.30		박승현          최초생성
 * @
 * **/
'use strict';

var grid = null;
var gridDs = null;
var gridOpt = null;
var brandDs = null;

$(document).ready(function () {
	var todayDate = new Date();

	fnInitialize();

	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'policyBenefitEmployeeBrandGroup',
			callback : fnUI
		});
	};

	function fnUI(){

		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

	}
	function fnInitButton(){
		$('#fnSave, #fnCreate, #onRemoveBrand').kendoButton();
	};
	function fnInitOptionBox(){
		// 브랜드 데이터소스
		brandDs = fnGetDataSource({
			url : '/admin/ur/brand/getBrandList'
		});
		brandDs.read();
	};
	var onCheckGroupName = function(e) {
		var kor_check = /([^a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ0-9!@\#$%^&*\(\)\-_,.\x20])/i;
		var value = $(e.target).val().trim();
		if (!value.length) return;
		if (value.length > 15) {
			value = value.substring(0,15);
			$(e.target).val(value);
		}
		if (kor_check.test(value)) {
			value = value.replace(kor_check, "");
			$(e.target).val(value);
		}
	};

	var onCheckAmmount = function(e) {
		var num_check = /[^0-9]/g;

		var value = $(e.target).val().trim();

		if (!value.length) return;

		if (num_check.test(value)) {
			value = value.replace(num_check, "");
			$(e.target).val(value);
		}
		if(parseInt(value) > 70){
			if(value.length>2)
				value = value.substring(0,2);
			else
				value = value.substring(0,1);
			$(e.target).val(value);
		}
	}

	function fnInitGrid() {

		var gridDs =	fnGetEditDataSource({
            url      : "/admin/policy/benefit/getPolicyBenefitEmployeeBrandGroupList"
            	, model_id : 'psEmplDiscBrandGrpId'
            	, model_fields : {
            			groupName      : { editable: true  , nullable: false, type: 'string'}
						, brandList : { nullable: true}
						, discountRatio : { type: "number", nullable: false, defaultValue: 10, editable: true}
						, modifyDt : { nullable: false, type: "string", defaultValue: todayDate.oFormat("yyyy-MM-dd hh:mm:ss")	}
						, psEmplDiscBrandGrpId : { nullable: true}
            	}
        	});

		var gridOpt = {
			dataSource: gridDs,
			editable: {
			     mode: "incell",
			     confirmation: false,
			},
			navigatable: false,
			scrollable: false,
			selectable: true,
			columns: [{
				field: "groupName",
				title: "임직원 할인율 브랜드 그룹명",
				editor: function (container, options) {
					var $input = $("<input type='text' class='comm-input'>");
					$input.attr("name", options.field);
					$input.keyup(onCheckGroupName);
					$input.focusout(onCheckGroupName);
					$input.appendTo(container);
				},
				width: "15%",
				attributes: {
					style: "text-align : center;"
				},
				headerAttributes: {
					style: "text-align: center;"
				}
			}, {
				field: "brandList",
				title: "그룹별 브랜드",
				template: brandListTemplate,
				editable: function () {
					return false;
				},
				width: "*",
				attributes: {
					style: "text-align : center;"
				},
				headerAttributes: {
					style: "text-align: center;"
				}
			}, {
				field: "discountRatio",
				title: "할인율",
				template : function(dataItem) {
					if(dataItem.discountRatio != '' && dataItem.discountRatio != 'undefined' && dataItem.discountRatio != undefined){
						return "<span class='marginR5'>"+dataItem.discountRatio+"</span>%";
					}
					else{
						return "";
					}
            	},
				editor: function (container, options) {
					var $input = $("<input type='text' class='comm-input'>");
					$input.attr("name", options.field);
					$input.keyup(onCheckAmmount);
					$input.focusout(onCheckAmmount);
					$input.appendTo(container);
				},
				width: 150,
				attributes: {
					style: "text-align : center;"
				},
				headerAttributes: {
					style: "text-align: center;"
				}
			}, {
				field: "modifyDt",
				title: "최근 업데이트일",
				width: "15%",
				editable: function () {
					return false;
				},
				attributes: {
					style: "text-align : center;"
				},
				headerAttributes: {
					style: "text-align: center;"
				}
			},
			{
				title: '관리',
				width: "10%",
				attributes: { style: 'text-align:center', class: 'forbiz-cell-readonly' },
				headerAttributes: {
					style: "text-align: center;"
				},
				command: [{
					name: 'remove', text: '삭제',
					imageClass: "k-i-custom",
					className: "btn-red btn-s",
					iconClass: "k-icon",
					click: onRemoveRow
				}]
			}
			, { field:'psEmplDiscBrandGrpId'		,hidden:true}

			],
			dataBound : onDataBound
		}

		$("#grid").kendoGrid(gridOpt);
		grid = $("#grid").data("kendoGrid");

		fnGetLastModifyDate();

		$('#ng-view').on("click", ".brandList__removeItem", onRemoveBrand);
	}
	function fnGetLastModifyDate(){
		fnAjax({
			url     : "/admin/policy/benefit/getLastModifyDatePolicyBenefitEmployeeBrandGroup",
			method : "GET",
			success : function( data ){
				$("#lastUpdateDate").html(data.modifyDt);
			},
			isAction : "select"
		});
	}

	// 입력값 검증
    function fnSaveValid(){
    	if( $("#groupName").val().length < 1 ){
    		fnKendoMessage({ message : "타입 미선택",
    			ok : function(e) { $("#bbsTp").focus(); }
    		});
    		return false;
    	}
    	if( $("#categoryNm").val().length < 1 ){
    		fnKendoMessage({ message : "분류명 미입력",
    						ok : function(e) { $("#categoryNm").focus(); }
    		});
    		return false;
    	}
    	return true;
    };

	// 브랜드 목록 템플릿
	function brandListTemplate(dataItem) {
		var brandList = [];

		if (dataItem && dataItem.brandList) {
			brandList = dataItem.brandList.toJSON().slice();
		}
		var $container = $("<div class='complex-condition'></div>")
		var $ul = $("<ul class='brandList'></ul>")

		var $input = $("<div class='brandList__item'><input class='dropDownTemplate'>"
			+ "<button  type='button' class='btn-white btn-s addBrand-btn'>추가</button></div>");

		$input.appendTo($container);
		$ul.appendTo($container);

		var template = "<li class='brandList__item'>"
			+ "<input type='text' class='comm-input disabled' name='brandName' data-brandName='#= brandName #' data-brandId='#= urBrandId#' value='#= brandName #' >"
			+ "<button type='button' class='btn-s btn-red brandList__removeItem'>&times;</button>"
			+ "</li>"

		template = kendo.template(template);

		var templateString = "";

		if (Array.isArray(brandList) && brandList.length) {
			for (var i = 0; i < brandList.length; i++) {
				templateString += template(brandList[i]);
			}
			$ul.html(templateString);
		}
		return $container.html();
	}

	// 그리드 dataBound 이벤트 핸들러
	function onDataBound(e) {
		var grid = e.sender;
		var items = e.sender.items();

		items.each(function (e) {
			var dataItem = grid.dataItem(this);
			var ddt = $(this).find('.dropDownTemplate');

			$(ddt).kendoDropDownList({
				optionLabel: "선택해주세요",
				dataSource: brandDs,
				dataTextField: "brandName",
				dataValueField: "urBrandId",
			});

			var $addBrand = $(this).find(".addBrand-btn");

			$addBrand.on("click", function (event) {
				onAddBrand(event, ddt, dataItem);
			})
		});
	}

	// 그룹 데이터 중복 체크
	// data : 추가한 데이터, arr : 검사할 배열, prop : 검사할 프로퍼티
	function checkRedupliacte(data, arr, rowIdx) {
		var result = -1;

		if (arr && arr.length) {
			arr.forEach(function (item, index) {
				if (item.brandList) {
					var list = item.brandList;
					for (var i = 0; i < list.length; i++) {
						if (list[i]["urBrandId"] == data) {
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

	// 변경사항 저장
	function fnSave(e) {
		var selector = "grid";
		var changeCnt =0;
        var insertData = fnEGridDsExtract(selector, 'insert');
        var updateData = fnEGridDsExtract(selector, 'update');
        var deleteData = fnEGridDsExtract(selector, 'delete');
        changeCnt = insertData.length + updateData.length +deleteData.length ;

        var allData = grid.dataSource.data();
		for (var i=0; i < allData.length; i++) {

			let groupName = allData[i].groupName;
			if(fnIsEmpty(allData[i].groupName)){
				fnKendoMessage({message : "브랜드 그룹명을 입력해주세요."});
				return;
			}
			if(fnIsEmpty(allData[i].discountRatio)){
				fnKendoMessage({message : "할인율을 입력해주세요."});
				return;
			}
			if(!allData[i].brandList || !Array.isArray(allData[i].brandList.toJSON()) || !allData[i].brandList.toJSON().length ){
				fnKendoMessage({message : "그룹별 브랜드를 선택해주세요."});
				return;
			}
		}

        if(changeCnt ==0){
            fnKendoMessage({message :  '데이터 변경사항이 없습니다.'});
            return ;
        }else{
            var data = {"insertData" : kendo.stringify(insertData)
                ,"updateData" : kendo.stringify(updateData)
                ,"deleteData" : kendo.stringify(deleteData)
            };
            fnAjax({
                url     : '/admin/policy/benefit/putPolicyBenefitEmployeeBrandGroup',
                params  : data,
                refreshGrid : gridDs,
                success :
                    function( data ){
	                	fnKendoMessage({  message : "저장이 완료되었습니다."
	                        , ok : function(){
	                        	fnUI();
	                          }
	                       });
                    },
                isAction : 'save'
            });
        }
		return false;
	}

	// 행 추가
	function fnCreate(e) {
		var grid = $("#grid").data("kendoGrid");
		grid.addRow();
	}

	// 행 삭제 이벤트
	function onRemoveRow(event) {
		event.preventDefault();

		var $target = $(event.target);
		var $row = $target.closest("tr");

		var rowIdx = $("tr", grid.tbody).index($row);


		var _gridData =  grid.dataSource.data().toJSON();
		if(_gridData.length==1) {
			fnKendoMessage({message :  '최소 1개이상의 할인율 브랜드 그룹이 존재해야 합니다.'});
			return;
		}


		var dataItem = grid.dataItem($row);

		if(dataItem.psEmplDiscBrandGrpId != null && dataItem.psEmplDiscBrandGrpId != ''
			&& dataItem.psEmplDiscBrandGrpId != 'undefined' && dataItem.psEmplDiscBrandGrpId != undefined){
			var param  = {'psEmplDiscBrandGrpId' : dataItem.psEmplDiscBrandGrpId };
			var url  = "/admin/policy/benefit/getRegistDiscMasterPolicyBenefitEmployeeBrandGroup";
				fnAjax({
					url     : url,
					params  : param,
					success :
						function(result){
							if(result.psEmplDiscBrandGrpId != null && result.psEmplDiscBrandGrpId != ''){
								fnKendoMessage({message :  '임직원 혜택관리 메뉴에 등록된 정보가 존재합니다. 해당정보 삭제 후 그룹삭제가 가능합니다.'});
							}else{
								removeRowConfirm(event);
							}
						},
                      isAction : 'select'
				});
		}else{
			removeRowConfirm(event);
		}
	}

	function removeRowConfirm(event) {

		fnKendoMessage({
			message: "삭제하시겠습니까?",
			type: "confirm",
			ok: function (e) {
				removeRow(event);
				return true;
			},
			cancel: function (e) {
				return false;
			}
		})

	}

	// 브랜드 목록 추가 이벤트
	function onAddBrand(ev, ddt, dataItem) {
		var selectedVal = $(ddt).data("kendoDropDownList").value();
		var selectedText = $(ddt).data("kendoDropDownList").text();
		var targetGrid = $('#grid').data('kendoGrid');

		var brandList = [];

		if (dataItem.brandList && Array.isArray(dataItem.brandList.toJSON())) {
			brandList = dataItem.brandList.toJSON().slice();
		}

		if (!selectedVal || !selectedText) {
			fnKendoMessage({
				message: "브랜드를 선택해주세요",
			})
			return;
		}
		var idx = fnGridRowNumRtn(grid);

		var _gridData = targetGrid.dataSource.data().toJSON();

		var _check = checkRedupliacte(selectedVal, _gridData, idx);

		if (_check == -2) {
			fnKendoMessage({
				message: "브랜드가 중복되었습니다.",
			})
			return;
		}
		if(_check == -1){
			brandList.push({
				brandName: selectedText,
				urBrandId: selectedVal
			})
			dataItem.set("brandList", brandList);
		}
		if(_check > -1){
			fnKendoMessage({message: "이미 다른 그룹별 브랜드 목록에 포함되어 있습니다. 이동하시겠습니까?", type: "confirm"
				,ok : function(){
	    			var dataItemDuplicate = targetGrid.dataSource.at(_check);
	    			var orgBrandList = dataItemDuplicate.brandList.toJSON().slice();
	    			if (!Array.isArray(orgBrandList)) return;
	    			if (selectedVal.length <= 0) {
	    				console.error("onRemoveBrand");
	    				return;
	    			}
	    			var index = orgBrandList.findIndex(function (item) {
	    				return item.urBrandId == selectedVal;
	    			})
	    			var newBrandList = [].concat(orgBrandList.slice(0, index), orgBrandList.slice(index + 1));
	    			dataItemDuplicate.set("brandList", newBrandList);
	    			brandList.push({
	    				brandName: selectedText,
	    				urBrandId: selectedVal
	    			})
	    			dataItem.set("brandList", brandList);
	            }
				,cancel  : function(){
                }
			});
		}
	}

	// 브랜드 목록 삭제 이벤트
	function onRemoveBrand(event) {
		event.preventDefault();
		fnKendoMessage({
			message: "삭제하시겠습니까?",
			type: "confirm",
			ok: function (e) {
				removeBrand(event);
				return true;
			},
			cancel: function (e) {
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

	// 브랜드 목록 삭제
	function removeBrand(e) {
		var $target = $(e.target);
		var $row = $target.closest("tr");
		var grid = $("#grid").data("kendoGrid");

		var dataItem = grid.dataItem($row);

		var brandList = dataItem.brandList.toJSON().slice();

		if (!Array.isArray(brandList)) return;

		var $brandItem = $target.parent().find("input[name='brandName']");

		if ($brandItem.length <= 0) {
			console.error("onRemoveBrand");
			return;
		}

		var _text = $brandItem.data("brandname");
		var _value = $brandItem.data("brandid");
		var idx = brandList.findIndex(function (item) {
			return item.urBrandId == _value;
		})

		var newBrandList = [].concat(brandList.slice(0, idx), brandList.slice(idx + 1));

		dataItem.set("brandList", newBrandList);
	}

	function fnGridRowNumRtn(targetGrid){
		var targetGrid = targetGrid;
		var row = targetGrid.select().closest("tr");
		var rowIdx = $("tr", targetGrid.tbody).index(row);

		return rowIdx;
	};

	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Clear*/
	//$scope.fnClear =function(){	fnClear();};
	/** Common Save*/
	$scope.fnSave = function(){	fnSave();};
	/** Common Delete*/
	$scope.fnCreate = function(){	fnCreate();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END