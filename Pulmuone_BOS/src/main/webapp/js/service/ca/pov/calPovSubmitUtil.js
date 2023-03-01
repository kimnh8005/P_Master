/**-----------------------------------------------------------------------------
 * description 		 : 주문정산 관련 Submit
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.05		이명수          최초생성
 * **/
var calPovSubmitUtil = {

	clear : function() {
		var yearDropdownlist = $("#findYear").data("kendoDropDownList")
		yearDropdownlist.value('');
		yearDropdownlist.trigger("change");

		calPovProcessDisplayUtil.clear();
    },

    search : function() {
        if( !this.fnInputValidate() ) return;

        var grid = $("#defaultGrid").data("kendoGrid");
        var gridDs = grid.dataSource;

        var form = $("#searchForm");

        var reqData = form.formSerialize(true);

        fnAjax({
			url : "/admin/calculate/pov/getPovList",
			params : reqData,
			success : function(data) {
				//데이터
				gridDs.data(data.list);
				//프로세스 처리
				calPovProcessDisplayUtil.setStatus(data.process);

				calPovSearchUtil.setGridTitle(reqData.findYear, reqData.findMonth);
			},
			isAction : 'select'
		});
    },

    getReqData : function (){
    	var form = $("#searchForm");
        return form.formSerialize(true);
    },

    fnInputValidate : function () {
        let findYear   = $.trim($("#findYear").val());
        let findMonth  = $.trim($("#findMonth").val());

    	// 조회기간 체크
    	if(findYear == '') {
    		fnKendoMessage({message : "년도를 선택해주세요.", ok : function() {
    			$("#findYear").focus();
    		}});
    		return false;
    	}
    	// 달력 시작일 종료일 체크
    	if(findMonth == '') {
    		fnKendoMessage({message : "월을 선택해주세요.", ok : function() {
    			$("#findMonth").focus();
    		}});
    		return false;
        }

        return true;
    },

    excelDownload: function(){
    	if( !this.fnInputValidate() ) return;

        var reqData = this.getReqData();

    	fnExcelDownload('/admin/calculate/pov/getPovListExportExcel', reqData);
    },

    fnInterfaceRun : function(interfaceType) {
        if( !this.fnInputValidate() ) return;

        var reqData = this.getReqData();

        reqData['scenario'] = interfaceType;

        fnKendoMessage({ type : 'confirm', message : "ERP I/F를 실행하면 되돌릴 수 없습니다.\n실행 하시겠습니까?", ok : function (){
        	fnAjax({
    			url : "/admin/calculate/pov/odPovInterface",
    			params : reqData,
    			success : function(data) {
    				fnKendoMessage({
						message : "I/F가 성공했습니다." , ok : function() {
							calPovSubmitUtil.search();
			    		}
					});
    			},
    			fail: function (data, resultcode){
    				fnKendoMessage({
						message : resultcode.message , ok : function() {
							calPovSubmitUtil.search();
			    		}
					});
    			},
    			isAction : 'update'
    		});
		}});
		return;
    },
}