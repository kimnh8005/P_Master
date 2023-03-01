/**-----------------------------------------------------------------------------
 * description 		 : 프로그램관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.10     신성훈          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var selectedHolidayDate = [];
var deletedHolidayDate = [];

$(document).ready(function() {

    fnInitialize();

    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : 'true' });

        fnPageInfo({
              PG_ID    : 'holidayGroup'
            , callback : fnUI
        });
    }

	function fnUI(){
		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

	}


	//--------------------------------- Button Start---------------------------------
	function fnInitButton() {
		$('#fnSearch, #fnNew,#fnSave,  #fnClear, #fnClose').kendoButton();
		//$('#fnDel').kendoButton({ enable: false });
	}

    //--- 검색  -----------------
    function fnSearch() {
        $('#inputForm').formClear(false);
        var data = $('#searchForm').formSerialize(true);
        var query = {
                       page         : aGridDs.page()
                     , pageSize     : PAGE_SIZE
                     , filterLength : fnSearchData(data).length
                     , filter :  {
                           filters : fnSearchData(data)
                       }
        };
        aGridDs.query( query );
    }

    //-- 초기화 버튼 -----------------
	function fnClear() {
		$('#searchForm').formClear(true);
		$("span#addHolidayCondition input:radio").eq(0).click();
		$("span#commonHolidayCondition input:radio").eq(0).click();
	}

    //-- 추가 팝업창
    function fnNew() {
        aGrid.clearSelection();
        $('#inputForm').formClear(true);
        $("span#addHolidayYn input:radio").eq(0).click();
		$("span#commonHolidayYn input:radio").eq(0).click();
		$(".dateBox__list__item").remove();
    	selectedHolidayDate = [];
    	deletedHolidayDate = [];

        inputFocus();
        fnKendoInputPoup({height:"375px" ,width:"700px", title:{ nullMsg :'휴일 그룹' } });
    }

    //--- 저장한다. -----------------
    function fnSave() {
    	var url  = '/admin/policy/holidayGroup/addHolidayGroup';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/policy/holidayGroup/putHolidayGroup';
			cbId= 'update';
		}
		var data = $('#inputForm').formSerialize(true);

		let dateList= new Array();
		for(let i = 0; i < selectedHolidayDate.length; i++) {
			let dateDataset = new Object();
			dateDataset.holidayDateList = selectedHolidayDate[i];
			dateList.push(dateDataset);

		}
		let selectedHolidayDateJson = JSON.stringify(dateList);

		let deletedDateList= new Array();
		for(let i = 0; i < deletedHolidayDate.length; i++) {
			let dateDataset = new Object();
			dateDataset.holidayDateList = deletedHolidayDate[i];
			deletedDateList.push(dateDataset);

		}
		let deletedHolidayDateJson = JSON.stringify(deletedDateList);

		data["holidayDateList"] = selectedHolidayDateJson;
		data["deletedDateList"] = deletedHolidayDateJson;

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
					isAction : 'update'
			});
		}
    }

	function fnClose() {
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}
	//--------------------------------- Button End---------------------------------


	//--------------------------------------------------------------------------
	//------------------------------- Grid Start -------------------------------
	//--------------------------------------------------------------------------
    function fnInitGrid() {
        aGridDs = fnGetPagingDataSource({
             url      : "/admin/policy/holidayGroup/getHolidayGroupList"
             , pageSize : PAGE_SIZE
        });

        aGridOpt = {
              dataSource: aGridDs
            , pageable  : {
                pageSizes   : [20, 30, 50],
                buttonCount : 10
              }
            , navigatable: true
            , columns : [
                    {field : 'psHolidayGroupId'        , title : 'No'   , width : '110px' , attributes : {style : 'text-align:center'}}
                    , {field : 'holidayGroupName' , title : '그룹명' , width : '520px' , attributes : {style : 'text-align:center'}}
                    , {field : 'commonHolidayYn' , title : '공통 휴일' , width : '250px' , attributes : {style : 'text-align:center'},
                    	template:kendo.template("#if(commonHolidayYn == 'Y')  {# #='적용'# #} else {# #='미적용' # #} #")}
                    , {field : 'addHolidayYn' , title : '추가 휴일' , width : '250px' , attributes : {style : 'text-align:center'},
                    	template:kendo.template("#if(addHolidayYn == 'Y' && addHolidayCount > 0) {# #='설정'# #} else {# #='미설정' # #} #")}
                    , {field : 'modifyDate' , title : '일자' , width : '300px' , attributes : {style : 'text-align:center'}
                    	, template:kendo.template("#if(modifyDate)  {# #=modifyDate# #} else {# #= createDate # #} #")}
                    , {field : "button"    , title : "관리"       , width : "120px"  , attributes : { style : "text-align:center" }
                    , command : [{ name: "수정",
                    	click: function(e) {
                    		e.preventDefault();

                            var tr = $(e.target).closest("tr");
                            var data = this.dataItem(tr);

                            fnAjax({
                                  url     : '/admin/policy/holidayGroup/getHolidayGroup'
                                , params  : {"psHolidayGroupId" : data.psHolidayGroupId}
                                , success : function (data) {
                                                fnBizCallback("select", data);
                                            }
                                , isAction : 'select'
                            });
                    	}
                    }] }
              ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
			//total count
            $('#listCount').text(aGridDs._total);
        });
	}

    //---- Kendo Grid Cell Click 이벤트 ------------------
    function fnGridClick(clickedRow, rowIdx, clickedColIdx) {
    	 var grid = $("#aGrid").data("kendoGrid");
         var fieldName = grid.options.columns[clickedColIdx].field;

         if (fieldName == "button") {
             var rowData = grid.dataItem(clickedRow) ;
             fnAjax({
                   url     : '/admin/policy/holidayGroup/getHolidayGroup'
                 , params  : {"psHolidayGroupId" : rowData.psHolidayGroupId}
                 , success : function (data) {
                                 fnBizCallback("select", data);
                             }
                 , isAction : 'select'
             });
         }
    }


    //==================================================================================
    //--------------- Initialize Option Box Start --------------------------------------
    //==================================================================================
    function fnInitOptionBox() {
        $('#kendoPopup').kendoWindow({
              visible: false
            , modal  : true
        });

        //-----------------------------------------------------------------------
        //-- 검색조건 : 사용여부 라디오
        //-----------------------------------------------------------------------
        fnTagMkRadio({
              id    : 'commonHolidayCondition'
            , tagId : 'commonHolidayCondition'
            , chkVal: ""
            , data  : [
                         {"CODE" : "" , "NAME" : '전체'     }
                       , {"CODE" : "Y", "NAME" : '적용'     }
                       , {"CODE" : "N", "NAME" : '미적용' }
                      ]
            , style : {}
        });

        fnTagMkRadio({
			id    :  'addHolidayCondition',
			tagId : 'addHolidayCondition',
			chkVal: "",
			data  : [   { "CODE" : ""	, "NAME":'전체' },
						{ "CODE" : "Y"	, "NAME":'설정' },
						{ "CODE" : "N"	, "NAME":'미설정' }
					],
			style : {}
        });

        fnTagMkRadio({
            id    : 'commonHolidayYn'
          , tagId : 'commonHolidayYn'
          , chkVal: "Y"
          , data  : [
        	  			{"CODE" : "Y", "NAME" : '적용' },
						{"CODE" : "N", "NAME" : '미적용' }
                    ]
        });

        fnTagMkRadio({
			id    :  'addHolidayYn',
			tagId : 'addHolidayYn',
			chkVal: "Y",
			data  : [
						{ "CODE" : "Y"	, "NAME":'설정' },
						{ "CODE" : "N"	, "NAME":'미설정' }
					]
			, change : function(e){
			    const selectedValue = e.target.value;

			    if(selectedValue === "Y"){
			        $('.selectHolidayDate-container').show();
			    }else{
			        $('.selectHolidayDate-container').hide();
			    }
			}
        });

        //================팝업 창 데이트 피커===============
        fnKendoDatePicker({
			id    : 'selectHolidayDate',
			format: 'yyyy-MM-dd',
			change: function(e){

				const datePicker = $('#selectHolidayDate').data('kendoDatePicker');
				const li = $('<li class="dateBox__list__item"></li>');
				let checkDateDuplicate = true;

				const selectedValue = datePicker._oldText;
			    //추가되어있는 날짜들을 확인하여 값이 중복되는지 확인
			    $('input[name="selectedDate"]').each(function(){
			        if(this.value === selectedValue){
			            checkDateDuplicate = false;
			        }
			    })
			    if(selectedValue < fnGetToday()) {
			    	fnKendoMessage({message:'과거 날짜는 선택 불가능합니다.'});
			        datePicker.value('');
			        return;
			    }
			    //중복된 날짜를 선택한 경우
			    if(checkDateDuplicate){
			    	selectedHolidayDate.push($("#selectHolidayDate").val())

			        li.append(`<input type="text" name="selectedDate" value="${selectedValue}" readonly>`);
			        li.append(`<button class="btn-removeDateBoxItem" type="button">x</button>`);

			        const target = $('.dateBox__list');

			        target.append(li);

			        //remove 버튼 이벤트 추가
			        li.on('click','.btn-removeDateBoxItem',function(e){
			        	const selectedDate = this.defaultValue;
	        			selectedHolidayDate = selectedHolidayDate.filter(function(e) { return e !== selectedDate });
	        			deletedHolidayDate.push(selectedDate);
			        	$(this).parent().remove();
			        });
			        //데이트피커의 값을 초기화
			        datePicker.value('');
			    } else {
			    	//중복된 날짜 선택
			        fnKendoMessage({message:'이미 선택된 날짜입니다.'});
			        datePicker.value('');
			        return;
			    }
			},
			defVal: fnGetToday()
		});


	}
    //==================================================================================
	//---------------Initialize Option Box End ------------------------------------------------
    //==================================================================================



    //==================================================================================
    //-------------------------------  Common Function start -------------------------------
    //==================================================================================
    function inputFocus() {
        $('#brandName').focus();
    };

    //-------------------------------  콜백합수 -----------------------------
    function fnBizCallback (id, data) {

        switch (id) {
            case 'select':
            	/* popup 오픈 시 데이터 초기화 */
            	$(".dateBox__list__item").remove();
            	selectedHolidayDate = [];
            	deletedHolidayDate = [];

            	$('#inputForm').bindingForm(data.rows, "0", true);

        		let date;

        		for(let i = 0; i < data.rows.length; i++) {
        			if(data.rows[i].holidayDate) {
    					date = data.rows[i].holidayDate.split(' ')

        				selectedHolidayDate[i] = date[0];

    					const li = $('<li class="dateBox__list__item"></li>');

    					li.append(`<input type="text" name="selectedDate" value="${date[0]}" readonly>`);
    			        li.append(`<button class="btn-removeDateBoxItem" type="button">x</button>`);

    			        const target = $('.dateBox__list');

    			        target.append(li);

    			        //remove 버튼 이벤트 추가
    			        li.on('click','.btn-removeDateBoxItem',function(e){
    			        	const selectedDate = this.defaultValue;
    	        			selectedHolidayDate = selectedHolidayDate.filter(function(e) { return e !== selectedDate });
    	        			deletedHolidayDate.push(selectedDate);
    			        	$(this).parent().remove();
    			        });
    					if(data.rows[i].addHolidayYn == 'N') {
    				        $('.selectHolidayDate-container').hide();
    					}
        			} else {
    			        $('.selectHolidayDate-container').hide();
        			}
        		}

        		fnKendoInputPoup({height:"550px" ,width:"700px",title:{key :"5876",nullMsg :'휴일 그룹' } });
                break;
			case 'insert':
			case 'update':
				fnKendoMessage({
                        message:"저장되었습니다."
                        , ok : function() {
                                      fnSearch();
                                      fnClose();
                               }
				});
			    break;

            case 'delete':
                fnKendoMessage({  message : '삭제되었습니다.'
                                , ok      : function(){
                                                fnSearch();
                                                fnClose();
                                            }
                });
                break;
        }
    }

    //-- Alert 메세지
	function fnAlertMessage(msg, id) {
		fnKendoMessage(
			{  message : msg
				, ok      : function focusValue() { $("#" + id).focus(); }
			}
		);
        return false;
    }
    //==================================================================================
    //-------------------------------  Common Function end -----------------------------
    //==================================================================================


    //==================================================================================
	//------------------------------- Html 버튼 바인딩  Start -------------------------------
    //==================================================================================
    $scope.fnSearch     = function () { fnSearch(); };
    $scope.fnClear      = function () { fnClear() ; };
    $scope.fnNew        = function () { fnNew()   ; };
    $scope.fnSave       = function () { fnSave()  ; };
    $scope.fnClose      = function () { fnClose() ; };

    $scope.fnFileDelete    = function (param) { fnFileDelete   (param); };
    $scope.fnShowLogoImage = function (param) { fnShowLogoImage(param); };
	$scope.fnPopupButton   = function (param) { fnPopupButton  (param); };

	//마스터코드값 입력제한 - 영문대소문자 & 숫자
	fnInputValidationByRegexp("input1", /[^a-z^A-Z^0-9]/g);

	//마스터코드값 입력제한 - 영문대소문자 & /
	fnInputValidationByRegexp("htmlPath", /[^a-z^A-Z^/]/g);
    //==================================================================================
	//------------------------------- Html 버튼 바인딩  End -------------------------------
    //==================================================================================

}); // document ready - END
