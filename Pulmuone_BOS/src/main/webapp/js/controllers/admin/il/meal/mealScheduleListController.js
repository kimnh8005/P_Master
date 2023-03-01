/**-----------------------------------------------------------------------------
 * system           : 식단스케쥴 관리
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.09.09		최윤지		최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var serverUrlObj;
$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : "true" });

        fnPageInfo({
            PG_ID  : "mealScheduleList",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){
        serverUrlObj = fnGetServerUrl();
        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitOptionBox();//Initialize Option Box ------------------------------------
        fnInitGrid();   //Initialize Grid ------------------------------------
        fnSearch();
    };

	//--------------------------------- Button Start---------------------------------
    // 버튼 초기화
	function fnInitButton(){
		$("#fnSearch, #fnClear, #fnNewAdd").kendoButton();
	};

    // 초기화
    function fnClear() {
        $("#searchForm").formClear(true);
		setDefaultDatePicker();
    };

	// 데이트피커 컨트롤러 초기화 함수
	function setDefaultDatePicker() {
		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})

		//$("button[data-id='fnDateBtn3']").attr("fb-btn-active", true);
	}

    // 조회
    function fnSearch(){
        let data = $("#searchForm").formSerialize(true);
        // if(data['conditionType'] != '' && data['conditionValue'].length < 2){
        // 	fnKendoMessage({ message : "검색어를 최소1글자 이상 입력해주세요." });
		// 	return false;
        // }
        data.mallDiv = data.mallDivNm;
        let searchData = fnSearchData(data);
        let query = { page : 1,
                      pageSize : PAGE_SIZE,
                      filterLength : searchData.length,
                      filter : { filters : searchData }
        };
        aGridDs.query(query);
    };

    // 신규
    function fnNewAdd() {
    	fnMealPatternPopUp();
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

    	aGridDs = fnGetPagingDataSource({
             url      : "/admin/item/meal/getMealPatternList",
            pageSize : PAGE_SIZE
        });

    	aGridOpt = {
            dataSource : aGridDs,
            pageable : { pageSizes: [20, 30, 50], buttonCount : 20 },
            navigatable: true,
            columns   : [
		            	{ title: "No", width: "50px", attributes : {style : "text-align:center"},
		                    template : "<span class='row-number'></span>"
		                  }
		                , { field : "mallDivNm", title: "식단분류", width: "50px", attributes : {style : "text-align:center"} }
		                , { field : "patternCd", title: "패턴코드", width: "80px", attributes : {style : "text-align:center"} }
		                , { field : "patternNm", title: "패턴명", width: "120px", attributes : {style : "text-align:center"} }
		                , { field : "patternStartDt", title : "패턴 시작일</br>패턴 종료일", width : "100px", attributes : {style : "text-align:center"},
                            template : function(dataItem) {
                                    return dataItem.patternStartDt + "</br>" + dataItem.patternEndDt;
                            }
                          }
		                , { field : "patternGoods", title: "연결상품", width: "150px", attributes : {style : "text-align:center"},
                            template : function(dataItem) {
                                        let ilGoodsIdInfoStr = stringUtil.getString(dataItem.ilGoodsIdInfo, "");
                                        let ilGoodsIdInfoArr = [];
                                        let ilGoodsIdInfoArrList = [];
                                        let arr = [];
                                        let str = "";
                                        arr = ilGoodsIdInfoStr.split('∀'); //'∀'로 split
                                        for(let i=0; i < arr.length; i ++ ) {
                                            let ilGoodsIdInfoList = [];
                                            ilGoodsIdInfoList = arr[i].split(',');
                                            ilGoodsIdInfoArr.push(ilGoodsIdInfoList);
                                        }
                                        if(fnNvl(ilGoodsIdInfoArr) != "") {
                                           for(let i=0; i< ilGoodsIdInfoArr.length; i++) {
                                            str = '<div name="patternGoodsDetail" id= "patternGoodsDetail_'+i+'" style="text-align:center; text-decoration: underline; color: #0000ff; cursor: pointer;" data-ilGoodsId="' + ilGoodsIdInfoArr[i][0]+'">'
                                            str +=  ilGoodsIdInfoArr[i][0] + " {" + ilGoodsIdInfoArr[i][1] + "}" + "</div>";
                                           ilGoodsIdInfoArrList.push(str);
                                         }
                                         str = stringUtil.getString(ilGoodsIdInfoArrList.join(""));
                                        }

                                         return str;
                                }
                        }
		                , { field : "createDt", title: "등록일 / 최근수정일", width: "100px", attributes : {style : "text-align:center"},
							template : function(dataItem) {
		                            let createDtStr = dataItem.createDt;
		                            if(fnNvl(dataItem.modifyDt) != '') {
		                             createDtStr += " /</br>" + dataItem.modifyDt;
                                    }
                                    return createDtStr;
                            }
						}
		               , { field:'management'      , title: '관리'         , width:"200px" , attributes: {style:'text-align:center;'}
                                                                          , template: function(dataItem) {
                                                                                        let delAbleYn = true;
                                                                                        let delDisableStr = '';
                                                                                        let btnStr = '<div id="pageMgrButtonArea" class="textCenter">';
                                                                                        //console.log('# 삭제가능여부 :: ', delAbleYn);
                                                                                        if (delAbleYn == true) {
                                                                                          delDisableStr = '';
                                                                                        }
                                                                                        else {
                                                                                          delDisableStr = 'disabled';
                                                                                        }
                                                                                         btnStr += '&nbsp;'
                                                                                                  +'<button type="button" class="btn-gray btn-s" kind="btnExcelDownload">엑셀 다운로드</button>';
                                                                                         btnStr += '&nbsp;'
                                                                                                  +'<button type="button" class="btn-gray btn-s" kind="btnPatternSet">패턴설정</button>';
                                                                                         btnStr += '&nbsp;'
                                                                                                  +'<button type="button" class="btn-gray btn-s" kind="btnScheduleSet" '+delDisableStr+'>스케쥴설정</button>';
                                                                                          btnStr +='&nbsp;'
                                                                                                + '<button type="button" class="btn-red btn-s"  kind="btnPatternDel">삭제 </button>';
                                                                                        btnStr += '</div>';

                                                                                        return btnStr;
                                                                                      }
                        }
            ]
        };

    	aGrid = $("#aGrid").initializeKendoGrid( aGridOpt ).cKendoGrid();

    	aGrid.bind("dataBound", function(){
    		var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
    		$("#aGrid tbody > tr .row-number").each(function(index){
    			$(this).html(row_num);
    			row_num--;
    		});
    		$("#countTotalSpan").text(aGridDs._total );
    	});
    };

    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {

        // 코드검색
		fnKendoDropDownList({
			id    : 'searchSelect',
			data  : [{"CODE":"SEARCH_SELECT.PATTERN_CD","NAME":'패턴코드'}
					,{"CODE":"SEARCH_SELECT.IL_GOODS_ID","NAME":'상품코드'}
					]
		});

    	// 식단분류
        fnKendoDropDownList({
            id : "mallDivNm",
            tagId : "mallDivNm",
            data : [ {"CODE" : "", "NAME" : "전체"},
                     {"CODE" : "MALL_DIV.BABYMEAL", "NAME" : "베이비밀"},
                     {"CODE" : "MALL_DIV.EATSLIM", "NAME" : "잇슬림"}
                   ]
        });

        //기간검색 타입
		fnKendoDropDownList({
            id : "dateSearchType",
            tagId : "dateSearchType",
            data : [ {"CODE" : "CREATE_DT", "NAME" : "등록일"}
                   ]
        });

        //시작일
		fnKendoDatePicker({
			id    : 'dateSearchStart',
			format: 'yyyy-MM-dd',
			btnStartId : 'dateSearchStart',
			btnEndId : 'dateSearchEnd',
			//defVal : fnGetDayMinus(fnGetToday(),6),
			//defType : 'oneWeek',
			change: function() {
				fnInputValidateUse('dateSearchStart', 'dateSearchEnd', 'start');
			}
		});

		//종료일
		fnKendoDatePicker({
			id    : 'dateSearchEnd',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'dateSearchStart',
			btnEndId : 'dateSearchEnd',
			//defVal : fnGetToday(),
			//defType : 'oneWeek',
			change: function() {
				fnInputValidateUse('dateSearchStart', 'dateSearchEnd', 'end');

			}
		});

    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------
    function fnInputValidateUse(dateSearchStart, dateSearchEnd, picker) {
    //달력 조회 기간 체크
    if(!fnIsEmpty($("#"+dateSearchStart).val()) && !fnIsEmpty($("#"+dateSearchEnd).val())) {
        //달력 시작일 종료일 체크
        if($("#"+ dateSearchStart).val() > $("#"+dateSearchEnd).val()) {
            fnKendoMessage({message : "조회 시작일보다 과거로 설정할 수는 없습니다.", ok : function() {
                    $('#'+dateSearchStart).val(fnGetDayMinus(fnGetToday(), 6));
                    $('#'+dateSearchEnd).val(fnGetToday());
                    picker == 'start' ?  $("#"+dateSearchStart).focus() :  $("#"+dateSearchEnd).focus();
                }});
            return false;
        }
    }
        return true;
    }

    // 패턴등록 팝업
    function fnMealPatternPopUp(patternCd){
		var param  = {'patternCd' : patternCd };
		var winMsg = '식단패턴 / 연결상품 등록';
		fnKendoPopup({
            id         : "mealPatternMgm",
    		title      : winMsg,
            width      : "1000px",
            height     : "900px",
            src        : "#/mealPatternMgm",
            param      : param,
            success    : function( id, data ){
            	aGridDs.read();
            }
        });
    };
    
    // 스케쥴설정 팝업
    function fnMealSchedulePopUp(patternCd){
		var param  = {'patternCd' : patternCd };
		var winMsg = '스케쥴 상세정보';
		fnKendoPopup({
            id         : "mealScheduleMgm",
    		title      : winMsg,
            width      : "1070px",
            height     : "900px",
            src        : "#/mealScheduleMgm",
            param      : param,
            success    : function( id, data ){
            	aGridDs.read();
            }
        });
    };

    //관리 > 엑셀다운로드버튼
    $('#aGrid').on("click", "button[kind=btnExcelDownload]", function(e) {
        e.preventDefault();
        let dataItem = aGrid.dataItem($(e.currentTarget).closest('tr'));
            fnKendoPopup({
                id         : "mealInfoExcelDownloadPopup",
                title      : '식단정보 다운로드',
                width      : "970px",
                height     : "500px",
                src        : "#/mealInfoExcelDownloadPopup",
                param      : dataItem,
                success    : function( id, data ){
                    aGridDs.read();
                }
            });
    });

    //관리 > 패턴설정버튼
    $('#aGrid').on("click", "button[kind=btnPatternSet]", function(e) {
        e.preventDefault();
        let dataItem = aGrid.dataItem($(e.currentTarget).closest('tr'));
        fnMealPatternPopUp(dataItem.patternCd);
    });
    
    //관리 > 스케쥴설정버튼
    $('#aGrid').on("click", "button[kind=btnScheduleSet]", function(e) {
        e.preventDefault();
        let dataItem = aGrid.dataItem($(e.currentTarget).closest('tr'));
        fnMealSchedulePopUp(dataItem.patternCd);
    });

    //관리 > 삭제버튼
    $('#aGrid').on("click", "button[kind=btnPatternDel]", function(e) {
        e.preventDefault();
        let dataItem = aGrid.dataItem($(e.currentTarget).closest('tr'));

        //해당 패턴 기간이 유효할 경우
        if(dataItem.patternEndDt > fnGetToday()) {
            fnKendoMessage({message : "해당 패턴이 진행기간이 유효할 경우 삭제가 불가합니다."});
			return false;
        }

        fnKendoMessage({message : '삭제하시겠습니까?', type : "confirm", ok : function(){
				fnAjax({
					url     : '/admin/item/meal/delMealPattern?patternCd='+dataItem.patternCd,
					success :
						function( data ){
							fnKendoMessage({message : '삭제되었습니다.'});
							fnSearch();
						},
					isAction : 'delete'
				});
			}});

    });

    $('#ng-view').on("click","div[name=patternGoodsDetail]",function(e){
        var ilGoodsId = e.currentTarget.textContent.substring(0, e.currentTarget.textContent.indexOf(' '));
        window.open(serverUrlObj.mallUrl+"/shop/goodsView?goods="+ilGoodsId ,"_blank");
    });

    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSearch = function() { fnSearch(); }; // 조회
    $scope.fnClear = function() { fnClear(); }; // 초기화
    $scope.fnNewAdd = function() { fnNewAdd(); }; // 신규

    //------------------------------- Html 버튼 바인딩  End -------------------------------
     fnInputValidationForAlphabetHangulNumberSpace('patternNm');
}); // document ready - END
