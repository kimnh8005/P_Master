/**-----------------------------------------------------------------------------
 * description 		 : BOS 사유 관리 팝업 그리트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.21		천혜현   최초생성
 * @
 * **/


var gridUtil = {
	toolbar : function(){
		return [
			{ name: 'create', text: '신규', className: "btn-point btn-s"},
			{ name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'},
			{ name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
		]
	}
	,claimNameOption : function(){
		return { editable: true	, type: 'string', validation: { required: true, maxLength:"20"
			, stringValidation: function (input) {
//                if (input.is("[name='claimName']") && input.val() != "") {
//                    input.attr("data-stringValidation-msg", '한글만 등록하실 수 있습니다');
//                    return !validateFormat.typeNotKorean.test(input.val());
//                }
            return true;
		}}}
	}
	,claimNameBySCtgryOption : function(){
		return { editable: true	, type: 'string', validation: {maxLength:"20"
			, stringValidation: function (input) {
                if (input.is("[name='claimName']") && input.val() == "") {
                    input.attr("data-stringValidation-msg", '귀책처를 입력해주세요.');
                    return false;
                }
            return true;
		}}}
	}
	,targetTypeOption : function(){
		return {
            editable: true, type: 'string', validation: {
                required: true,
                stringValidation(input) {
                    // input이 .k-edit-cell 또는 #targetTypeDropDown input가 되는 두가지의 경우가 있습니다.
                    if (input.is("#targetTypeDropDown") && input.val() == "") {
                        const $editCell = input.closest("td.k-edit-cell");
                        if ($editCell.length > 0) {
                            // tooltip box가 dropdownlist span 영역 안에 들어가서 보이지 않음
                            // td의 overflow 속성을 visible로 바꾸니 해결되었습니다.
                            // 따라서 동적으로 overflow : visible로 바꿔주고 변경 성공시 hidden으로 만들었습니다.
                            $editCell.css("overflow", "visible");
                        }
                        input.attr("data-stringValidation-msg", '이영역을 선택해주세요.');
                        return false;
                    }
                    input.closest('.k-edit-cell').css("overflow", "hidden");
                    return true;
                }
            }
        }
	}
    ,aList : function(){
        return [
			{ title : 'No'				, width	:'60px'				, attributes:{ style:'text-align:center' }, template: function (dataItem){
				return fnKendoGridPagenation(aGrid.dataSource,dataItem);
			}}
			,{ field :'claimName'		, title : '클레임 사유 (대)'		, width:'220px',attributes:{ style:'text-align:center' }}
			,{ title : '관리'				, command: {name:'destroy'	, text:'삭제', className: 'btn-red btn-s'}		, width:'120px', attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
			,{ field :'psClaimCtgryId'  , hidden:true}
			,{ field :'categoryCode'    , hidden:true, defaultValue: '10'}
		]
    }
    ,bList : function(){
        return [
			{ title : 'No'				, width	:'60px'				, attributes:{ style:'text-align:center' }, template: function (dataItem){
				return fnKendoGridPagenation(bGrid.dataSource,dataItem);
			}}
			,{ field :'claimName'		, title : '클레임 사유 (중)'		, width:'220px',attributes:{ style:'text-align:center' }}
			,{ title : '관리'			    , command : {name:'destroy'	, text:'삭제', className: 'btn-red btn-s'}		, width:'120px', attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
			,{ field :'psClaimCtgryId'  , hidden : true}
			,{ field :'categoryCode'    , hidden:true, defaultValue: '20'}
		]
    }
    ,cList : function(){
        return [
			{ title : 'No'				, width	:'60px'				, attributes:{ style:'text-align:center' }, template: function (dataItem){
				return fnKendoGridPagenation(cGrid.dataSource,dataItem);
			}}
			,{ field :'claimName'		, title : '귀책처'		, width:'170px'	,attributes:{ style:'text-align:center' }}
			,{ field :'targetTypeName'	, title : '귀책 유형'		, width:'170px'	,attributes:{style: 'text-align:center'/*, class:'forbiz-cell-readonly' */}
				,editor: function (container, options) {
					var input = "";
					if (options.model.useYn == 'Y') {
						var input = $("<input id='targetTypeDropDown' />");
					} else {
						var input = $("<input id='targetTypeDropDown' />");
					}
					input.appendTo(container);

					fnKendoDropDownList({
						id: 'targetTypeDropDown',
						data : [
							{"CODE":"B", "NAME":"구매자 귀책"},
							{"CODE":"S", "NAME":"판매자 귀책"}
						],
						textField :"NAME",
						valueField : "CODE",
						blank : ' ',
						/* HGRM-2013 - dgyoun : selectbox 수정 선택 시 기존 값 선택  Start */
						// Databound : 렌더링 된 후 발생하는 이벤트
						dataBound: function (e) {
							// 현재 선택된 값을 선택된 열에서 찾는다
							const FIELD_NAME = "targetTypeName";
							const _currentVal = cGrid.dataItem(cGrid.select())[FIELD_NAME];
							const self = e.sender;
							const data = e.sender.dataSource.data();

							if (!data.length) {
								return;
							}

							//드롭다운 리스트에서 선택된 값과 같은 값을 가진 데이터의 index값을 찾고,
							//드롭다운 리스트의 선택된 인덱스를 변경한다.
							data.forEach(function (d, index) {
								if (d.NAME === _currentVal) {
									self.select(index+1);
								}
							})
						},
						/* HGRM-2013 - dgyoun : selectbox 수정 선택 시 기존 값 선택  End */

					});

					$('#targetTypeDropDown').unbind('change').on('change', function () {
						var dataItem = cGrid.dataItem($(this).closest('tr'));
						var shopDropDownList = $('#targetTypeDropDown').data('kendoDropDownList');
						dataItem.set('targetTypeName', shopDropDownList.text());
						dataItem.set('targetTypeCode', shopDropDownList.value());
					});
				}
			}
			,{ title : '관리'			    , command : {name:'destroy'	, text:'삭제', className: 'btn-red btn-s'}		, width:'120px', attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
			,{ field :'psClaimCtgryId'  , hidden : true}
			,{ field :'categoryCode'    , hidden:true, defaultValue: '30'}
			,{ field :'targetTypeCode'    , hidden:true}
		]
    }
}
