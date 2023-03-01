/**
 * 20.09.02
 * 프론트엔드 공통함수
 */

/**
 * api 연동하기
 */
const fbAjax = function fbAjax(params, callback) {
    $.ajax({
        url: params.url,
        data: params.data,
        type: params.method ? params.method : "POST",
        dataType: "json",

        success: function (data) {
            callback(data.data);
        },

        error: function (error) {
            console.error(params.url + ' has exception...', error);
        },
    });
};

/**
 * 카테고리관련 공통 이벤트 함수
 * 각각의 함수에 접근하려면 fbCategories.함수명()
 */
const fbCategories = {
    /**
     * 데이터
     */
    fbData: {
        // 분류타입: 0 / 뎁스는 1부터
        depth: 0,

        //로딩
        fetches: {
            category: false,
        },

        //표준카테고리 default
        apiUrl: "/admin/comn/getDropDownCategoryStandardList",

        //요청 파라미터
        requests: {
            type: {
                depth: 1,
                mallDiv: null,
                authMenuID: 160,
            },

            category: {
                categoryId: null,
                authMenuID: 160,
            },
        },

        //사용은 안하지만 디버깅용, 개발자도구에서 확인 가능
        categories: {},
    },

    /**
     * 표준카테고리, 이외 카테고리 구분
     * fbCategoryType
     */
    requestBigCategories: function (type, callback) {
        //default 설정
        const self = this;
        this.fbData.depth = 0;
        const fnCallback = callback ? callback : fbCategories.customData;

        let parameter = {};

        if (type == "STANDARD") {
            //표준카테고리
            self.fbData.apiUrl = "/admin/comn/getDropDownCategoryStandardList";
            parameter = this.fbData.requests.type;
        } else {
            //전시카테고리, 몰인몰카테고리
            self.fbData.apiUrl = "admin/comn/getDropDownCategoryList";
            parameter = Object.assign(this.fbData.requests.type, {
                mallDiv: type,
            });
        }

        fbAjax(
            {
                url: self.fbData.apiUrl,
                method: "GET",
                data: parameter,
            },
            fnCallback
        );
    },

    /**
     * request 요청
     * 뎁스
     */
    requestDepthCategories: function (depth, callback, selectedId) {
        //default 설정
        const self = this;
        this.fbData.depth = depth ? depth : 1;
        const fnCallback = callback ? callback : fbCategories.customData;

        //파라미터
        const parameter = Object.assign(this.fbData.requests.category, {
            categoryId: selectedId,
        });

        //연동
        fbAjax(
            {
                url: self.fbData.apiUrl,
                method: "GET",
                data: parameter,
            },
            fnCallback
        );
    },

    /**
     * 받은 데이터 가공
     * 초기 option값 추가 ex) 1차분류선택
     */
    customData: function (data, callback) {
        const self = fbCategories;
        const _depth = self.fbData.depth + 1; //다음 뎁스를 바꿔야하기때문에

        const response = data.rows;

        let $target = $("#fbCategory" + _depth);

        if (response && response.length) {
            response.unshift({
                categoryName: _depth + "차 분류 선택",
                categoryId: "",
            });

            self.fbData.categories[_depth] = response;

            if (callback) callback(response);
            else self.renderSelectBox($target, response);
        } else {
            //다음뎁스 데이터 없는 경우
            self.makeDisabled($target, true);
        }

        self.fbData.fetches.category = false;
        fbLoading(self.fbData.fetches.category);
    },

    /**
     * select박스에 데이터 연동
     * option 태그 append
     */
    renderSelectBox : function($target, _data) {
        if (!$target || !_data) return;

        $target.empty();

        this.makeDisabled($target, false);

        _data.forEach(function (value) {
            const $option = "<option value=" + value.categoryId + ">" + value.categoryName + "</option>";
            $target.append($option);
        });
    },

    /**
     * 상위뎁스 선택한 경우 전체 하위뎁스 초기화
     * $target은 선택한 셀렉트 박스
     * */
    clearData: function($target) {
        const $siblings = $target.siblings();

        if ($siblings && $siblings.length) {
            $siblings.each(function (i, el) {
                const $el = $(el);
                const _index = $el.index();

                if (_index > $target.index()) {
                    $el.empty().append(
                        "<option>" + _index + "차 분류 선택</option>"
                    );
                }
            });
        }
    },

    /**
     * 하위뎁스 없는경우 disabled 활성화
     * 하위뎁스 있는경우 disabled 비활성화
     */
    makeDisabled: function ($target, _disable) {
        const $siblings = $target.parent().children();

        if ($siblings && $siblings.length) {
            $siblings.each(function (i, el) {
                const $el = $(el);

                if ($el.index() >= $target.index()) {
                    $el.prop("disabled", _disable);
                }
            });
        }
    },

    /**
     * 카테고리 변경 시
     */
    changeCategory: function () {
        const self = fbCategories;
        self.fbData.fetches.category = true;
        fbLoading(self.fbData.fetches.category);

        $("[name^=fbCategory]").on("change", function (e) {
            const $this = $(this);
            const _value = $this.val();

            if ($this.attr("name") == "fbCategoryType") {
                //카테고리 타입변경
                self.requestBigCategories(_value);
            } else {
                //카테고리 뎁스 변경
                const _depth = $this.index();
                self.requestDepthCategories(_depth, null, _value);
            }

            self.clearData($this);
        });
    },

    /**
     * 초기실행함수
     */
    init: function (type) {
        const _type = $("#fbCategoryType").val();
        this.requestBigCategories(_type);
        this.changeCategory();
    },
};

/**
 * 	유효성검사 이벤트 (returns Boolean)
 *  1. 유효성 검사가 필요한 태그에 두개의 속성 추가
 *  data-validator-required="true" - 필수값 여부(이 속성이 없으면 false로 간주)
 *  data-validator-reg="\d" - 정규식 여부 (필요한 경우 추가 없으면 빈 값만 체크)
 */
// const fbValidator = function fbValidator(requestObj) {
//     let result = true;

//     for (let [key, value] of Object.entries(requestObj)) {
//         const $target = $("[name=" + key + "]");
//         let isRequired = $target.attr("data-validator-required");

//         if (isRequired) {
//             if (value == "") {
//                 result = false;
//             } else {
//                 let isReg = $target.attr("data-validator-reg");
//                 let makeReg = new RegExp(isReg, "g");

//                 if (isReg && !makeReg.test(value)) {
//                     result = false;
//                 }
//             }
//         }
//     }

//     return result;
// };

/**
 * loading bar
 * 로딩바 노출 : fbLoading(true)
 * 로딩바 비노출 : fbLoading(false)
 */
const fbLoading = function fbLoading(boolean) {
    const _display = boolean ? "block" : "none";
    const loadingHtml = "<div class='loading'></div>";
    const $loading = $("body").find(".loading");

    if (!$loading.length) {
        // html에 로딩 쓰는게 없으면 body에 element 삽입
        $("body").append(loadingHtml);

        $(".loading").css({
            display: _display,
            position: "fixed",
            top: "0",
            left: "0",
            right: "0",
            bottom: "0",
            margin: "auto",
            width: "50px",
            height: "50px",
            background:
                "url(../contents/images/loader.gif) no-repeat center center",
            zIndex: "1",
        });
    } else {
        // html에 로딩이 있으면 display만 컨트롤
        $loading.css({
            display: _display,
        });
    }
};

/**
 * form data안에 값들을 객체로 리턴하는 함수
 * form serialize
 */
const fbSerializeForm = function fbSerializeForm($form) {
    if (!$form) return;

    let requestObj = {};
    const formData = $form.serialize().split("&");

    // 모든 입력 값 데이터객체화
    formData.forEach(function (d) {
        const reg = d.split(/\=/);

        if (reg) {
            const key = reg[0];
            const value = reg[1];

            //한글이거나 특수문자의 경우 decode
            requestObj[key] = decodeURIComponent(value);
        }
    });

    //체크박스 배열화
    $form.find("input[type=checkbox]").each(function () {
        const $this = $(this);

        if ($this.is(":checked")) {
            const _name = $this.attr("name");
            //result안에 해당 프로퍼티가 존재하고, 배열일 경우에만 값을 push
            if (requestObj[_name] && Array.isArray(requestObj[_name])) {
                requestObj[_name].push($this.val());
            } else {
                requestObj[_name] = [];
                requestObj[_name].push($this.val());
            }
        }
    });

    return requestObj;
};

/**
 * form 입력 값들 리셋하는 함수
 */
const fbResetForm = function fbResetForm($area, callback) {
    if (!$area) return;

    $area.find("input,select,textarea").each(function () {
        const self = $(this);

        //textarea 태그인 경우
        if (self.is("textarea")) {
            self.val("");
        }
        //input 태그 인 경우
        else if (self.is("input")) {
            switch (self.attr("type")) {
                case "text":
                case "email":
                case "password":
                    self.val("");
                    break;
                case "checkbox":
                case "radio":
                    self.prop("checked", false);
                    break;
                default:
                    break;
            }
        }
        //select 태그인 경우
        else if (self.is("select")) {
            self.find("option").eq(0).prop("selected", true);
        }
        // 그 외의 경우
        else {
            self.val("");
        }
    });
    if (callback) callback();
};

/**
 <가이드 - 라디오 탭 콘텐츠 js>
 1. html에 작성 (id에는 사용할 아이디 작성)
 <div id="selectConditionType" class="radios-wrapper fb__custom__radio"></div>

 2.  js 생성 시 option 추가
 tab: true와
 TAB_CONTENT_NAME 을 각각에 추가

 tab: true,
 data: [{
                CODE: "singleSection",
                NAME: "단일조건 검색",
                TAB_CONTENT_NAME: "singleSection"
            },{
                CODE: "multiSection",
                NAME: "복수조건 검색",
                TAB_CONTENT_NAME: "multiSection"
            }]

 3. 각각의 탭 콘텐츠에 div로 감싸고 class에 "사용할아이디", TAB_CONTENT_NAME를 추가하고 첫번째 보여져야하는애 말고는 display: none추가

 탭콘텐츠1
 <div class="selectConditionType singleSection">
 탭콘텐츠2
 <div class="selectConditionType multiSection" style="display: none;">
 */
const fbTabChange = function fbTabChange(_selected) {

    $(document).on("change", ".js__tab", function () {
        const $this = $(this);
        const _name = $this.attr("name");

        //js__tabs__wrapper안의 모든 탭 콘텐츠
        const $allTabContents = $("." + _name);

        //해당 탭의 콘텐츠
        let $tabContent = $("." + $this.data("tab-content"));

        //모든 탭 콘텐츠 비노출
        $allTabContents.hide();

        //해당 탭 콘텐츠 노출
        $tabContent.show();

        //탭 변경시 이전 탭 영역 초기화
        if( typeof $scope.fnClear === 'function' ) {
            $scope.fnClear();
        }

    })
}

/**
 * 체크박스 공통 이벤트
 * 1. checkbox-wrapper 클래스 추가
 */

const fbCheckboxChange = function onChangeCheckbox() {
    $(document).on("change", ".checkbox-wrapper input[type=checkbox]", function (e) {
        const $this = $(this);
        const $area = $this.closest(".checkbox-wrapper");
        const $inputList = $area.find("input[type=checkbox]");
        const _isChecked = $this.is(":checked");

        let $allInput = null;

        if ($inputList && $inputList.length) {
            $inputList.each(function (i, element) {
                if ($(element).val() == "ALL") {
                    $allInput = $(element);
                }
            })
        }

        //전체선택버튼이 있으면
        if ($allInput) {
            if ($this.val() == "ALL") {
                $allInput.parent("label").siblings().each(function () {
                    $(this).find("input").prop("checked", _isChecked);
                });
            }
            else {
                const maxLength = $inputList.length - 1;

                if (!_isChecked) {
                    $allInput.prop("checked", false);
                }
                else {
                    const checkedLength = $area.find("input[type=checkbox]:checked").length;
                    const _isCheckedAll = checkedLength == maxLength;

                    if (_isCheckedAll) {
                        $allInput
                            .prop("checked", _isCheckedAll)
                    }
                }
            }
        }
    })
}




const fbMakeTimeArr = function fbMakeTimeArr(mode){
    var i = 0;
    var MAX_TIME = mode === "hour" ? 24 : 60;
    var data = [];

    for (i; i < MAX_TIME; i++) {
        i = i < 10 ? "0" + i : i.toString();

        const newData = {
            CODE: i,
            NAME: i
        }
        data.push(newData);
    }
    return data;
}

/**
 * kendo dropdown 형식 timepicker 만드는 함수
 * @param {string} selector : "#"을 포함한 selector
 * @param {string} mode : "start/end"
 * @param {string} type : "hour/min",
 */
const fbMakeTimePicker = function fbMakeTimePicker(selector, mode, type, onChange) {
    if (!selector || $(selector).length <= 0 || selector.indexOf("#") < 0) {
        console.error("ID 값에 #을 붙여서 입력해주세요. + ", selector);
        return;
    }

    const self = $(selector);

    self.attr("data-time-type", type);
    self.attr("data-mode", mode);
    self.attr("data-default", "timepicker");

    if( !self.closest(".fb__timepicker-wrapper").length ) {
        self.wrap("<span class='fb__timepicker-wrapper'>");
    }

    selector = selector.split("#")[1];

    const data = fbMakeTimeArr(type);

    const dropDownList =  fnKendoDropDownList({
        id: selector,
        data: data,
        valueField: "CODE",
        textField: "NAME",
        popup: {
            appendTo: self.closest('.fb__timepicker-wrapper'),
        }
    });

    if( onChange && typeof onChange === "function" ) {
        dropDownList.bind("change", function(e) {
            onChange(e);
        })
    }

    return dropDownList;
}

/**
 *
 * @param {string} container : 제이쿼리 객체
 */
function fbClearTimePicker($container) {
    const $timePickers =  $container.find("[data-default='timepicker']");

    if( $timePickers.length ) {
        $timePickers.each(function() {
            const self = $(this);
            const kendoList = self.data("kendoDropDownList");

            if( self.data("time-type") === "min" ) {
                if( self.data("mode") === "start" ) {
                    kendoList.select(0);
                } else {
                    kendoList.select(59);
                }
            } else if ( self.data("time-type") === "hour" ) {
                if( self.data("mode") === "start" ) {
                    kendoList.select(0);
                } else {
                    kendoList.select(23);
                }
            }
        })
    }
}



const fbCustomFormHandler = {
    /**
     *
     * @param {*} targetForm : 대상이 될 Form jquery 엘리먼트
     * @param {*} rules      : validation 규칙, rules가 null 값일 경우, html 코드에 있는 옵션들에 해당하는 엘리먼트들만 검사한다.
     * @param {*} messages   : 특정 엘리먼트를 검사할 때 표시할 메시지
     * @param {*} ignore     : 검사를 하지 않을 엘리먼트
     */
    setup : function( targetForm, rules, messages, ignore ){
        this.el = targetForm;
        // this.el.validate({
        //     debug : true,
        //     rules : rules,
        //     messages : messages,
        //     ignore : ignore,
        // })

        return this;
    },
    /**
     * setup메소드에서 설정한 값으로 form의 기본적은 validation check를 하는 메소드
     */
    validate : function(){
        return this.el.valid();
    },
    // serialize : function(){
    //     const self = this.el;
    //     let params = {};
    //     try{
    //         self.find('input, textarea, select, radio, checkbox').each(function() {
    //             var formID = $((this).form).attr('id');
    //             if ($(this).is("textarea")) {
    //                 params[$(this).attr('name')] = $(this).val()
    //             }
    //             switch($(this).attr('type')) {
    //             case'text':
    //                 var kdObj;
    //                 if ($(this).data('role') == 'dropdownlist') {
    //                     kdObj = $(this).data("kendoDropDownList");
    //                     params[$(this).attr('name')] = this.value
    //                 } else if ($(this).data('role') == 'datepicker') {
    //                     kdObj = $(this).data("kendoDatePicker");
    //                     if (kdObj != undefined) {
    //                         params[$(this).attr('name')] = fnFormatDate(this.value, 'yyyyMMdd')
    //                     } else {
    //                         params[$(this).attr('name')] = fnFormatDate(this.value, 'yyyyMMdd')
    //                     }
    //                 } else if ($(this).data('role') == 'maskedtextbox') {
    //                     kdObj = $(this).data("kendoMaskedTextBox");
    //                     params[$(this).attr('name')] = this.value.replace(/-/g, '')
    //                 } else if ($(this).data('role') == 'money2') {
    //                     params[$(this).attr('name')] = this.value.replace(/,/g, '')
    //                 } else {
    //                     params[$(this).attr('name')] = $(this).val()
    //                 }
    //                 break;
    //             case'password':
    //                 params[$(this).attr('name')] = $(this).val();
    //                 break;
    //             case'hidden':
    //                 params[$(this).attr('name')] = $(this).val();
    //                 break;
    //             case'textarea':
    //                 params[$(this).attr('name')] = $(this).val();
    //                 break;
    //             case'radio':
    //                 var val = $('#' + formID + ' :radio[name="' + $(this).attr("name") + '"]:checked').val();
    //                 params[$(this).attr('name')] = (val == null) ? '' : val;
    //                 break;
    //             case'select':
    //                 params[$(this).attr('name')] = $(this).val();
    //                 break;
    //             case'checkbox':
    //                 var value = "";
    //                 $('form[id=' + formID + '] :checkbox[name=' + $(this).attr("name") + ']:checked').each(function() {
    //                     value += $(this).val() + "∀"
    //                 });
    //                 if (value == null)
    //                     value = "";
    //                 value = value.substring(0, value.length - 1);
    //                 params[$(this).attr("name")] = value;
    //                 break;
    //             }
    //         });
    //         return params;
    //     } catch(e){
    //      return params['jsErrMsg'] = e.toString()
    //     }
    // }
    /**
     * Form data json 형태로 리턴하는 메소드
     */
    serializeObject : function() {
        var obj = null;
        const form = this.el;
        try {
            if (form[0].tagName && form[0].tagName.toUpperCase() == "FORM") {
                var arr = form.serializeArray();
                if (arr) {
                    obj = {};
                    jQuery.each(arr, function() {
                        obj[this.name] = typeof this.value == "string" ? this.value.trim() : this.value;
                    });
                }
            }
        } catch (e) {
            alert(e.message);
        } finally {
        }
        return obj;
    },
}

/**
 * Date 객체 Format에 따라 출력하는 함수
 * @params : f => "yyyy.MM"
 */

Date.prototype.format = function (f) {

    if (!this.valueOf()) return " ";



    var weekKorName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];

    var weekKorShortName = ["일", "월", "화", "수", "목", "금", "토"];

    var weekEngName = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

    var weekEngShortName = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

    var d = this;



    return f.replace(/(yyyy|yy|MM|dd|KS|KL|ES|EL|HH|hh|mm|ss|a\/p)/gi, function ($1) {

        switch ($1) {

            case "yyyy": return d.getFullYear(); // 년 (4자리)

            case "yy": return (d.getFullYear() % 1000).zf(2); // 년 (2자리)

            case "MM": return (d.getMonth() + 1).zf(2); // 월 (2자리)

            case "dd": return d.getDate().zf(2); // 일 (2자리)

            case "KS": return weekKorShortName[d.getDay()]; // 요일 (짧은 한글)

            case "KL": return weekKorName[d.getDay()]; // 요일 (긴 한글)

            case "ES": return weekEngShortName[d.getDay()]; // 요일 (짧은 영어)

            case "EL": return weekEngName[d.getDay()]; // 요일 (긴 영어)

            case "HH": return d.getHours().zf(2); // 시간 (24시간 기준, 2자리)

            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2); // 시간 (12시간 기준, 2자리)

            case "mm": return d.getMinutes().zf(2); // 분 (2자리)

            case "ss": return d.getSeconds().zf(2); // 초 (2자리)

            case "a/p": return d.getHours() < 12 ? "오전" : "오후"; // 오전/오후 구분

            default: return $1;

        }

    });

  };


  String.prototype.string = function (len) { var s = '', i = 0; while (i++ < len) { s += this; } return s; };
  String.prototype.zf = function (len) { return "0".string(len - this.length) + this; };
  Number.prototype.zf = function (len) { return this.toString().zf(len); };


  //숫자 비교 함수

  function compare(a, b) {
    return a - b;
  }

  function isEqual(a, b) {
    return compare(a, b) === 0;
  }

//   Date 객체 다음 달/이전 달
/**
 * date : date object
 */
Date.prototype.getNextMonth = function() {
    if( !this || !this instanceof Date ) {
        throw new Error(this + "type Error");
    }

    var t = this;

    return new Date(t.getFullYear(), t.getMonth() + 1, 1);
}

Date.prototype.getPrevMonth = function() {
    if( !this || !this instanceof Date ) {
        throw new Error(this + "type Error");
    }

    var t = this;

    return new Date(t.getFullYear(), t.getMonth() - 1, 1);
}

/**
 * FullCalendar 라이브러리
 * @param {*} target : html 엘리먼트
 * @param {*} options : obj
*/
function FbCalendar({ target, options }) {
    var todayDate = new Date();

    this.el = target
    this.calendar = null;
    this.options = {
        events : [],
        height: 860,
        initialView: 'dayGridMonth',
        initialDate: todayDate.oFormat("yyyy-MM-dd"),
        locale : "kr",
        // headerToolbar: {
        // 	// left: 'prev,next today',
        // 	left: "",
        // 	center: 'prev,title,next',
        // 	// right: 'dayGridMonth,timeGridWeek,timeGridDay',
        // 	right: "",
        // },
        selectable: true,
        eventClassNames: "fb__calendar__event",
        // views: {
        // 	dayGridMonth: {
        // 		titleFormat: { year: "numeric", month: "numeric", day: "numeric" }
        // 	}
        // },
        titleFormat: {
            year: "numeric", month: "2-digit", day: "2-digit", range: null
        }
    }

    if (options && typeof options === "object") {
        $.extend(this.options, options);
    }

    // 달력 인스턴스 생성 및 렌더링
    this.init = function () {
        this.calendar = new FullCalendar.Calendar(this.el, this.options);
        return this.render();
    }


    /**
     * 이벤트 추가
     * @param {*} title : string
     * @param {*} date : string "yyyy-MM-dd"
     * @param {*} source : string, 이벤트 소스 아이디 값
     */
    this.addEvent = function (title, date, source) {

        var currentEvent = this.getEventsByDate(date);
        title = title.split(",");

        // 해당 날짜에 출고처가 있을 경우 기존 객체의 title에 값 추가
        if( currentEvent ) {
            var currentTitle = currentEvent.title.split(",");
            var newTitle = [].concat(currentTitle, title);

            // currentEvent.setProp("title", currentEvent.title + "," + title);
            currentEvent.setProp("title", newTitle);
        } else {
        // 해당 날짜에 출고처가 없을 경우 새로운 객체를 생성
            this.calendar.addEvent({
                title : title,
                start : date,
            }, source);
        }

        return this;
    }

    /**
     * 이벤트를 새로운 값으로 갱신
     * @param {*} title : string
     * @param {*} date : string "yyyy-MM-dd"
     * @param {*} source : string, 이벤트 소스 아이디 값
     */
    this.setEvent = function (title, date, source) {

        var currentEvent = this.getEventsByDate(date);

        title = title.split(",");

        // 해당 날짜에 출고처가 있을 경우 기존 객체의 title값 변경
        if( currentEvent ) {
            currentEvent.setProp("title", title);
        } else {
        // 해당 날짜에 출고처가 없을 경우 새로운 객체를 생성
            this.calendar.addEvent({
                title : title,
                start : date,
            }, source);
        }

        return this;
    }

    /**
     * 이벤트 소스 추가
     * @param {*} events : array
     * @param {*} id : string
     */
    this.addEventSource = function (events, id) {
        if(id) {
            this.calendar.addEventSource({
                id : id,
                events : events
            })
        } else {
            this.calendar.addEventSource(events)
        }
        return this;
    }

    // 달력 렌더링
    this.render = function () {
        this.calendar.render();
        return this;
    }

    // 달력 삭제
    this.destroy = function () {
        this.calendar.destroy();
        return this;
    }

    // 다음 달로 이동
    this.next = function () {
        this.calendar.next();
        return this;
    }

    // 이전 달로 이동
    this.prev = function () {
        this.calendar.prev();
        return this;
    }


    /**
     * 달력 날짜 변경
     * @param {*} date : date Obj
     */
    this.setDate = function(date) {
        this.calendar.gotoDate(date);
        return this;
    }

    this.today = function() {
        this.calendar.today();
    }

    // 이벤트 리스트 전체 가져오기
    this.getEvents = function() {
        return this.calendar.getEvents();
    }

    /**
     * 날짜를 통해 이벤트 데이터 가져오기
     * @param {*} date : string "yyyy-MM-dd"
     */
    this.getEventsByDate = function(date) {
        return this.getEvents().filter(function(item) {
            return item.startStr === date;
        })[0];
    }

    // 해당 월 이벤트 개수
    this.getEventsByYear = function(year, month) {
        return this.getEvents().filter(function(e) {
        	console.log(e.start.getYear());
            return e.start.getMonth() === month && e.start.getYear() === year;
        })
    }

    // 해당 월 이벤트 개수
    this.getEventsByMonth = function(month) {
        return this.getEvents().filter(function(e) {
        	console.log(e);
        	window.test = e;
            return e.start.getMonth() === month;
        })
    }

    /**
     * 아이디로 이벤트소스 가져오기
     * @param {*} id : string, 이벤트 소스 아이디값
     */
    this.getEventSourceById = function(id) {
        return this.calendar.getEventSourceById(id);
    }

    /**
     * 이벤트소스 제거
     * @param {*} id : string, 이벤트 소스 아이디값
     */
    this.removeEventSource = function(id) {
        var eventSource = this.getEventSourceById(id);

        if( eventSource ) {
            eventSource.remove();
        }
    }

    //달력에 설정된 모든 이벤트 제거
    this.removeAllEvents = function() {
        this.calendar.removeAllEvents();
    }

    // 달력 이벤트 데이터 다시 가져오기
    this.refetch = function() {
        this.calendar.refetchEvents();
        return this;
    }

    /**
     * 달력 뷰 타입 변경
     * @param {*} type : string
     */
    this.changeView = function(type) {
        this.calendar.changeView(type);
        return this;
    }

    // 달력 현재 뷰 타입 가져오기
    this.getViewType = function() {
        return this.calendar.view.type;
    }

    // 달력 현재(선택된) 날짜 가져오기
    this.getCurrentDate = function() {
        return this.calendar.getDate();
    }

    // init 함수 실행
    this.init();
}

// Tooltip
/**
 *
 * @param target : html id selector, string
 * @param title : string
 * @param message : string
 * @param width : number, string
 * @param height : number, string
 * @param onEnter : function
 * @param onLeave : function
 */
function Tooltip({ target, title, message, onEnter, onLeave, width, height }) {
    if(!target) {
        throw new Error("Parameter Error. target");
    }

    this.$target = $(target);
    this.$target.addClass("tooltip-container");


    this.title = title ? title : "도움말 제목을 설정하세요.";
    this.message = message ? message : "도움말 내용을 설정하세요.";
    this.onEnter = onEnter || function() {};
    this.onLeave = onLeave || function() {};
    // this.width = width ? width : 150;
    // this.height = height ? height : 80;

    // 툴팁 초기 설정
    this.init = function() {
        var $icon = $("<button class='tooltip-icon'></button>");
        this.$icon = $icon;

        var $box = $("<div class='tooltip-box'></div>")
        this.$box = $box;

        var $title = $("<span class='tooltip__title'></span>");
        this.$title = $title;

        var $message = $("<span class='tooltip__message'></span>");
        this.$message = $message;

        this.$box.append(this.$title);
        this.$box.append(this.$message);

        this.$target.append(this.$icon);
        this.$target.append(this.$box);

        this.render();
        // this.$box.css("width", this.width + "px");
        // this.$box.css("height", this.height + "px");
    }

    this.render = function() {
        this.$title.html(this.title);
        this.$message.html(this.message);
    }

    this.bindEvents = function() {
        var self = this;

        if( this.onEnter && typeof this.onEnter === "function") {

            this.$box.on("mouseenter", function(e) {
                self.onEnter();
            })
        }

        if( this.onLeave && typeof this.onLeave === "function") {
            this.$box.on("mouseenter", function(e) {
                self.onLeave();
            })
        }
    }
    this.init();
}

var helpDataArray = [];
var fetchData = function(key) {

    var _helpDataArray = helpDataArray.find(function(h) {
        return h.id == key;
    })

    return  _helpDataArray;
}


/**
 * 도움말 생성
 * @param {string} target : jquery 엘리먼트
 */
function fnHelp(target) {

    target.each(function(index) {

        var self = this;
        var _key = $(this).data("helpKey");
        var _title = $(this).data("helpTitle");
        var _message = $(this).data("helpMessage");

        // key 값이 있는 경우 helpDataArray에서 해당하는 데이터를 조회
        if( _key ) {

            var _data = fetchData(_key);

            if( _data === undefined || _data === null ) {
                return;
            }

            // 조회 결과가 없을 경우 툴팁을 생성하지 않는다.
            new Tooltip({
                target : self,
                title : _data.title,
                message : _data.message,
            })

        } else  {
            // key 값이 없는 경우
            new Tooltip({
                target : self,
                title : _title ? _title : "",
                message : _message ? _title : "",
            })
        }
    })
}

/**
 * 도움말 생성 공통함수 가이드
 * 1. <div data-help-key="1" data-help-title="help title 1" data-help-message="help-message 1"></div> 처럼 data-help-title, data-help-message를 지정하면 이 값을 우선적으로 사용해서 제목과 메시지를 출력합니다.
 * 2. help-key에 해당 도움말의 "id값"을 넣어주면 ajax를 통해서 제목과 메시지 내용을 가져옵니다.
 * 3. layout.html, modal.html의 document.ready()함수 안에서 호출하면 자동적으로 도움말 태그가 생성됩니다.
 * 도움말 데이터 받기
 * @param {string} target(optional) : 도움말을 설정할 jQuery 엘리먼트
 */
function fnGetHelpData(target) {

    var array = [];
    var data;
    var $target;

    // 타겟을 파라미터로 전달 받을 경우
    if( target && target.length ) {

        $target = target;

    } else {
        // layout.html, modal.html에서 페이지 최초 실행 시 호출한 경우
        $target = $("[data-help-key]");
    }

    // $target의 data-help-key값이 있는지 체크해서, ajax로 전달할 데이터 객체를 생성
    var isHelp = false;
    $target.each(function(index) {
    	var helpKey = $(this).data("helpKey");
        if(helpKey != ""){
        	isHelp = true;
        	if(typeof fetchData(helpKey) == "undefined"){
        		array.push(helpKey);
        	}
        }
    });

    if(array.length > 0){
    	data = {"systemHelpId": array.toString()};

    	$.ajax({
    		url     : '/admin/comn/help/getHelpListByArray',
    		data  	: data,
    		type	: 'POST',
    		dataType: 'json',
    		success :
    			function( data ) {
                    const rows = data.data.rows;

    				rows.forEach(function (value) {
    			        const eachData = { "id" : value.id, "title" : value.title, "message" : fnTagConvert(value.content) }
    			        helpDataArray.push(eachData);
                    });

                    // 조회 결과가 있으면 fnHelp함수를 호출
    				if(isHelp) {
                        fnHelp($target);
                    }
    			}
    	});
    } else {
    	if(isHelp) {
            fnHelp($target);
        }
    }
}

// 브라우저 사이즈에따라서 스크롤바 너비 구하기
function fbGetScrollBarWidth () {

    var inner = document.createElement('p');
    inner.style.width = "100%";
    inner.style.height = "200px";

    var outer = document.createElement('div');
    outer.style.position = "absolute";
    outer.style.top = "0px";
    outer.style.left = "0px";
    outer.style.visibility = "hidden";
    outer.style.width = "200px";
    outer.style.height = "150px";
    outer.style.overflow = "hidden";
    outer.appendChild (inner);

    document.body.appendChild (outer);
    var w1 = inner.offsetWidth;
    outer.style.overflow = 'scroll';
    var w2 = inner.offsetWidth;
    if (w1 == w2) w2 = outer.clientWidth;

    document.body.removeChild (outer);

    return (w1 - w2);
};


const REG_EX = {
    num : {
        regExp : /[^0-9]/gi,
        message : "숫자만 입력하실 수 있습니다.",
    },

    eng : {
        regExp : /[^a-zA-Z]/gi,
        message : "영어만 입력하실 수 있습니다.",
    },
    engNum : {
        regExp : /[^a-zA-Z0-9]/gi,
        message : "영어, 숫자만 입력하실 수 있습니다.",
    },
    numEngKor : {
        regExp : /[^a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\s]/gi,
        message : "영어, 숫자, 한글만 입력하실 수 있습니다.",
    },
    numEngLowPoint : {
        regExp : /[^a-z0-9\.]/g,
        message : "영어소문자, 숫자, '.'만 입력하실 수 있습니다.",
    }
}

function FbDatePicker( id, option ) {
    this.$el = $("#" + id);

    // 선택 불가능 요일
    this.disableDay = option &&  option.disableDay ? option.disableDay : [];
    // 휴일 지정
    this.dayOff = option && option.dayOff ? option.dayOff : [];
    // 활성화 요일 지정
    this.dayOn = option && option.dayOn ? option.dayOn : [];

    // 달력 셀 UI
    this.beforeShowDay = function(date) {

    	if(option.dayOn != [] && option.dayOn != undefined){
    		if(this.isDayOn(date)){
    			return [true, "dayOn"];
    		}else{
    			return [false, ""];
    		}
    	}else{
    		 if( this.isDayOff(date)) {
	             return [false, "dayOff", "휴일"];
	         } else if( this.isDisabledDate(date) ) {
	             return [false, ""];
	         } else {
	             return [true, ""];
	         }
    	}
    };

    /**
     * 휴일 설정
     * @param {array} dayOff : 휴일 배열
     */
    this.setDayOff = function(dayOff) {
        this.dayOff = dayOff;
    }

    /**
     * 선택 불가능 요일 설정
     * @param {array} disableDay : 선택 불가능 요일 배열
     */
    this.setDisableDay = function(disableDay) {
        this.disableDay = disableDay;
    }

    /**
     * 활성화 요일 설정
     * @param {array} dayOn : 활성화 요일 배열
     */
    this.setDayOn = function(dayOn) {
        this.dayOn = dayOn;
    }

    // 데이트피커 날짜 변경 이벤트
	this.onChange = function(event) {
        var $target = $(event.target);
		let date = $target.val();

		if( !fnIsValidDate(date) ) {
            alert("올바른 날짜가 아닙니다.");
            $target.val("");
            return;
		}

		date = new Date(date);

		if( this.isDayOffOrDisabled(date) ) {
			alert("해당 날짜를 선택하실 수 없습니다.");
            $target.val("");
			return;
		}
    }

    // //  데이트피커 셀 클릭 이벤트
    // function onClickDatePicker(e) {
    //     var $td = $(e.target).closest('td');

    //     if($td.hasClass("ui-state-disabled")) {
    //     alert("해당 날짜를 선택하실 수 없습니다.");
    //     }
    // }

    // 데이트피커 옵션
    this.options = {
        dateFormat: "yy-mm-dd",
        dayNamesMin: ['일','월', '화', '수', '목', '금', '토'],
        monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
        monthNames: ['01', '02', '03', '04', '05', '06', '07', '08', '09' ,'10', '11', '12'],
        showMonthAfterYear: true,
        showOtherMonths: true, // 나머지 날짜도 화면에 표시
        showOn: "both",
        nextText: "다음 달",
        prevText: "이전 달",
        onSelect: option && option.onSelect ? option.onSelect : null,
        beforeShow: function(args) {
            $('#ui-datepicker-div').addClass("fb__datePicker");
            var options = arguments[1];
            var dpDiv = options.dpDiv;

            // dpDiv.off("click", onClickDatePicker);
            // dpDiv.on("click", onClickDatePicker);
        },
        afterShow: function(args) {
            console.log(arguments);
        },
        beforeShowDay: option && option.beforeShowDay ? option.beforeShowDay.bind(this) : this.beforeShowDay.bind(this),
    }

    // 이벤트 바인딩
    this.bindEvents = function() {
        const self = this;
        const onChange = option && option.onChange ? option.onChange : this.onChange;

        this.$el.on("change", function(e) {
            onChange.call(self, e);
        });
    }

    // 데이트피커 실행
    this.init = function() {
        this.$el.datepicker(this.options);
        this.bindEvents();
    }

    this.init();
}

/**
 * 휴일 여부 체크
 * @param {ojbect} date : Date 객체
 */
FbDatePicker.prototype.isDayOff = function(date) {
    date = date.oFormat("yyyy-MM-dd");
    return this.dayOff.includes(date);
}

/**
 * 선택 불가능한 날짜 여부 체크
 * @param {ojbect} date : Date 객체
 */
FbDatePicker.prototype.isDisabledDate = function(date) {
    return this.disableDay.includes(date.getDay());
}


FbDatePicker.prototype.isDayOffOrDisabled = function(date) {
    return this.isDayOff(date) || this.isDisabledDate(date);
}

/**
 * 활성화 요일 여부 체크
 * @param {ojbect} date : Date 객체
 */
FbDatePicker.prototype.isDayOn = function(date) {
    date = date.oFormat("yyyy-MM-dd");
    return this.dayOn.includes(date);
}

/**
* 엘리먼트를 canvas로 변환하고, 이미지 파일로 다운로드
* @param el : DOM 엘리먼트, 제이쿼리 사용시 : $('div')[0]을 인자로 넘겨야한다.
* @param name : string : 파일 명
*/
function printDiv(el, fileName) {
	fileName || (fileName = 'sample.png');

	return html2canvas(el, {
		logging: true,
		letterRendering: 1,
		allowTaint : true,
		useCORS: true,
	}).then(function(canvas){
		const myImage = canvas.toDataURL('image/png');
		downloadURI(myImage, fileName);
	});
}

/**
* dataURL로 변경된 이미지 다운로드
* @param uri : 이미지 url
* @param name : string : 파일 명
*/
function downloadURI(uri, fileName) {
	const link = document.createElement('a');
	link.download = fileName;
	link.href = uri;
	document.body.appendChild(link);
	link.click();
	link.parentNode.removeChild(link);
}