(function(e){function t(t){for(var s,r,o=t[0],c=t[1],l=t[2],d=0,m=[];d<o.length;d++)r=o[d],Object.prototype.hasOwnProperty.call(i,r)&&i[r]&&m.push(i[r][0]),i[r]=0;for(s in c)Object.prototype.hasOwnProperty.call(c,s)&&(e[s]=c[s]);u&&u(t);while(m.length)m.shift()();return n.push.apply(n,l||[]),a()}function a(){for(var e,t=0;t<n.length;t++){for(var a=n[t],s=!0,o=1;o<a.length;o++){var c=a[o];0!==i[c]&&(s=!1)}s&&(n.splice(t--,1),e=r(r.s=a[0]))}return e}var s={},i={"pc/mypage/inquiry/index":0},n=[];function r(t){if(s[t])return s[t].exports;var a=s[t]={i:t,l:!1,exports:{}};return e[t].call(a.exports,a,a.exports,r),a.l=!0,a.exports}r.m=e,r.c=s,r.d=function(e,t,a){r.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:a})},r.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},r.t=function(e,t){if(1&t&&(e=r(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var a=Object.create(null);if(r.r(a),Object.defineProperty(a,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var s in e)r.d(a,s,function(t){return e[t]}.bind(null,s));return a},r.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return r.d(t,"a",t),t},r.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},r.p="/";var o=window["webpackJsonp"]=window["webpackJsonp"]||[],c=o.push.bind(o);o.push=t,o=o.slice();for(var l=0;l<o.length;l++)t(o[l]);var u=c;n.push([139,"chunk-commons"]),a()})({"053e":function(e,t,a){},139:function(e,t,a){e.exports=a("4cf9")},"1c1c":function(e,t,a){"use strict";var s=a("053e"),i=a.n(s);i.a},"4cf9":function(e,t,a){"use strict";a.r(t);var s=a("5530"),i=(a("e260"),a("e6cf"),a("cca6"),a("a79d"),function(){var e=this,t=e.$createElement,a=e._self._c||t;return e.userSession.isLogin?a("main",{staticClass:"fb__mypage fb__mypage__qna"},[a("fb-header",{attrs:{type:"sub"}}),a("div",{ref:"scrollWrapper",staticClass:"product-layout__body"},[a("section",{staticClass:"product-layout__body__left"},[a("fb-mypage-menu")],1),a("section",{staticClass:"product-layout__body__right"},[a("h2",{staticClass:"fb__mypage__title"},[e._v("상품문의 내역")]),a("div",{staticClass:"qna"},[e.$FB_CODES.FETCHES.WAIT===e.fetches.inquiryList?a("div",{staticClass:"fb__fetching"}):e.$FB_CODES.FETCHES.SUCCESS===e.fetches.inquiryList?[a("div",{staticClass:"qna__search"},[a("h3",{staticClass:"qna__search__title"},[e._v("조회기간")]),a("div",{staticClass:"qna__search__form"},[a("fb-months",{on:{"change-date":e.changeDate}}),a("button",{staticClass:"form__submit",attrs:{type:"button",disabled:e.userActionDisabled},on:{click:e.searchDate}},[e._v("조회하기")])],1)]),a("div",{ref:"qnaFilter",staticClass:"qna__filter"},[a("p",{staticClass:"qna__filter__label total"},[a("span",[e._v("총 문의 "),a("em",[e._v(e._s(e.summary.totalCount))])])]),a("p",{staticClass:"qna__filter__label register"},[a("span",[e._v("문의 접수 "),a("em",[e._v(e._s(e.summary.receptionCount))])])]),a("p",{staticClass:"qna__filter__label onGoing"},[a("span",[e._v("문의확인중 "),a("em",[e._v(e._s(e.summary.answerCheckingCount))])])]),a("p",{staticClass:"qna__filter__label done"},[a("span",[e._v("답변완료 "),a("em",[e._v(e._s(e.summary.answerCompletedCount))])])])]),e.$FB_CODES.PROCESSES.ING===e.processes.inquiryList?a("div",{staticClass:"fb__fetching"}):[e.inquiryList&&e.inquiryList.length>0?a("ul",{staticClass:"qna__list"},e._l(e.inquiryList,(function(t,s){return a("li",{key:"inquiryList-"+s,staticClass:"qna__item"},[a("div",{staticClass:"item__header"},[a("div",{staticClass:"item__header__left"},[a("span",{staticClass:"item__header__date"},[e._v("문의일 "),a("em",[e._v(e._s(t.createDate))])]),a("div",{staticClass:"item__goods"},[a("figure",{staticClass:"item__goods__thumb",class:{goodsThumbnailDimmed:e.goodsThumbnailDimmed}},[a("a",{attrs:{href:e.$APP_CONFIG.PAGE_URL.GOODS_VIEW+"?goods="+t.ilGoodsId}},["GREENJUICE"==t.packType?a("img",{directives:[{name:"lazy",rawName:"v-lazy",value:e.$APP_CONFIG.IMAGES.GREEN_JUICE,expression:"$APP_CONFIG.IMAGES.GREEN_JUICE"}],attrs:{alt:""}}):"EXHIBIT"==t.packType?a("img",{directives:[{name:"lazy",rawName:"v-lazy",value:e.$APP_CONFIG.IMAGES.FLAT_PRICE,expression:"$APP_CONFIG.IMAGES.FLAT_PRICE"}],attrs:{alt:""}}):a("img",{directives:[{name:"lazy",rawName:"v-lazy",value:e.mergeImageHost(t.thumbnailPath),expression:"mergeImageHost(item.thumbnailPath)"}],attrs:{alt:""}})])]),a("div",{staticClass:"item__goods__info"},[a("p",{staticClass:"info__title"},[e._v(e._s(t.goodsName))])])]),a("p",{staticClass:"item__header__title",class:{secret:["QNA_SECRET_TP.CLOSE_CUSTOMER","QNA_SECRET_TP.CLOSE_ADMIN"].includes(t.secretType)}},[a("span",{staticClass:"division"},[e._v(e._s(t.productTypeName))]),e._v(" "+e._s(t.title))])]),a("div",{staticClass:"item__header__right"},[["QNA_STATUS.RECEPTION"==t.status?a("span",{staticClass:"item__header__status"},[e._v("문의접수")]):"QNA_STATUS.ANSWER_CHECKING"==t.status?a("span",{staticClass:"item__header__status"},[e._v("답변확인중")]):"QNA_STATUS.ANSWER_COMPLETED"==t.status?a("span",{staticClass:"item__header__status complete"},[e._v("답변완료")]):a("span",{staticClass:"item__header__status"},[e._v(e._s(t.statusName))])],a("button",{staticClass:"item__header__toggle",class:{on:t.visible},attrs:{type:"button"},on:{click:function(a){return e.toggleVisible(t)}}},[e._v("상세내용 보이기/감추기")])],2)]),t.visible?a("div",{staticClass:"item__content"},[a("p",{staticClass:"item__detail"},[e._v(e._s(t.question))]),a("div",{staticClass:"item__modify"},["QNA_STATUS.RECEPTION"==t.status?a("button",{attrs:{type:"button"},on:{click:function(a){return e.openModifyInquiry(t)}}},[e._v("수정")]):e._e(),"QNA_SECRET_TP.OPEN"==t.secretType&&"QNA_STATUS.ANSWER_COMPLETED"==t.status?a("button",{staticClass:"secret",attrs:{type:"button"},on:{click:function(a){return e.setSecret(t)}}},[e._v("비공개")]):e._e()]),t.answer&&t.answer.length>0?a("div",e._l(t.answer,(function(t,s){return a("div",{key:"itemAnswer-"+s,staticClass:"item__answer"},[a("div",{staticClass:"item__answer__header"},[a("span",{staticClass:"item__answer__name"},[e._v("상담원")]),a("span",{staticClass:"item__answer__date"},[e._v(e._s(t.answerDate))])]),a("div",{staticClass:"item__answer__text",domProps:{innerHTML:e._s(t.answer)}})])})),0):a("div",{staticClass:"item__answer empty"},[a("p",[e._v("등록된 답변이 없습니다.")])])]):e._e()])})),0):a("div",{staticClass:"qna__list empty"},[a("p",[e._v("등록된 문의 내역이 없습니다.")])]),a("div",{staticClass:"qna__pagination"},[a("fb-pagination",{attrs:{pagination:e.pagination},on:{movePaging:e.movePaging}})],1)]]:e.$FB_CODES.FETCHES.ERROR===e.fetches.inquiryList?[a("error-layout",{attrs:{"error-type":"default"}},[[a("div",{staticClass:"fb__btn-wrap margin"},[a("button",{staticClass:"fb__btn-margin--white",attrs:{type:"button",tabindex:"0"},on:{click:function(t){return e.reload(t,"events")}}},[e._v("새로고침")]),a("button",{staticClass:"fb__btn-margin--green",attrs:{type:"button",tabindex:"0"},on:{click:function(t){return e.goToRoute(e.$APP_CONFIG.PAGE_URL.ROOT)}}},[e._v("홈으로")])])]],2)]:e._e()],2)])]),a("fb-footer"),a("fb-modal",{attrs:{open:e.modals.qaWrite.open,isBackgroundClose:!1,classes:e.modals.qaWrite.classes,width:e.modals.qaWrite.width,height:e.modals.qaWrite.height},on:{"close-modal":function(t){return e.handleCancelWrite(t)}},scopedSlots:e._u([e.modals.qaWrite.model?{key:"default",fn:function(t){var s=t.model;return void 0===s&&(s=e.modals.qaWrite.model),[a("header",[a("h4",[e._v("상품문의 작성")])]),a("simplebar",{staticClass:"goods-qa-write-modal",attrs:{"data-simplebar-auto-hide":"false"}},[a("p",{staticClass:"goods-qa-write-modal__note"},[e._v("상품에 대한 내용을 판매자에게 문의할 수 있습니다.​")]),a("form",{on:{submit:function(t){return t.preventDefault(),e.writeSubmit(t)}}},[a("fieldset",[a("legend",[e._v("입력항목")]),a("label",{staticClass:"goods-qa-write-modal__item"},[a("span",[e._v("문의유형"),a("em",[e._v("(필수)")])]),a("fb-select-box",{attrs:{classes:"full large",rows:s.types},scopedSlots:e._u([{key:"option",fn:function(t){var i=t.row;return[a("label",[a("input",{directives:[{name:"model",rawName:"v-model",value:s.items.type.code,expression:"model.items.type.code"}],staticClass:"blind",attrs:{type:"radio",tabindex:"0",disabled:e.$FB_CODES.PROCESSES.ING===e.processes.write},domProps:{value:i.code,checked:e._q(s.items.type.code,i.code)},on:{change:function(t){return e.$set(s.items.type,"code",i.code)}}}),a("span",[e._v(e._s(i.name))])])]}}],null,!0)},[a("span",{directives:[{name:"fb-validate",rawName:"v-fb-validate.focus",value:s.items.type.code,expression:"model.items.type.code",modifiers:{focus:!0}}],attrs:{"fb-validate-info":"qaCode"}},[e._v(" "+e._s(s.items.type&&s.items.type.code?s.items.type.name:"선택")+" ")])]),e.qaValidate["qaCode"]?a("p",{staticClass:"fb__required__error"},[e._v("문의유형을 선택해주세요.")]):e._e()],1),a("label",{staticClass:"goods-qa-write-modal__item fb__input-text"},[a("span",[e._v("제목"),a("em",[e._v("(필수)")])]),a("div",{staticClass:"fb__input-text__inner"},[a("input",{directives:[{name:"model",rawName:"v-model",value:s.items.title,expression:"model.items.title"},{name:"fb-validate",rawName:"v-fb-validate.focus",value:s.items.title,expression:"model.items.title",modifiers:{focus:!0}}],attrs:{type:"text",tabindex:"0",disabled:e.$FB_CODES.PROCESSES.ING===e.processes.write,placeholder:"제목을 입력해주세요.(30자 이내)",maxlength:"30","fb-validate-info":"qaTitle"},domProps:{value:s.items.title},on:{input:[function(t){t.target.composing||e.$set(s.items,"title",t.target.value)},function(t){s.items.title=e.getCuttingValue(t,30)}]}}),s.items.title&&s.items.title.length?a("button",{staticClass:"fb__input-text__clear",attrs:{type:"button",tabindex:"-1",disabled:e.$FB_CODES.PROCESSES.ING===e.processes.write},on:{click:function(e){s.items.title=null}}},[e._v("지우기")]):e._e()]),e.qaValidate["qaTitle"]?a("p",{staticClass:"fb__required__error"},[e._v("제목을 입력해주세요.")]):e._e()]),a("label",{staticClass:"goods-qa-write-modal__item fb__textarea"},[a("span",[e._v("내용"),a("em",[e._v("(필수)")])]),a("div",{staticClass:"fb__textarea__inner"},[a("textarea",{directives:[{name:"model",rawName:"v-model",value:s.items.content,expression:"model.items.content"},{name:"fb-validate",rawName:"v-fb-validate.focus",value:s.items.content,expression:"model.items.content",modifiers:{focus:!0}}],attrs:{type:"text",tabindex:"0",disabled:e.$FB_CODES.PROCESSES.ING===e.processes.write,placeholder:"내용을 입력해주세요​.","fb-validate-info":"qaContent"},domProps:{value:s.items.content},on:{input:[function(t){t.target.composing||e.$set(s.items,"content",t.target.value)},function(t){s.items.content=e.getCuttingValue(t,s.options.maxLength)}]}}),a("span",{staticClass:"fb__textarea__count"},[e._v(e._s(e.modals.qaWrite.model&&s.items&&s.items.content?s.items.content.length:0)+"/"+e._s(s.options.maxLength))])]),e.qaValidate["qaContent"]?a("p",{staticClass:"fb__required__error"},[e._v("내용을 입력해주세요.")]):e._e()]),a("label",{staticClass:"goods-qa-write-modal__item fb__checkbox private"},[a("input",{directives:[{name:"model",rawName:"v-model",value:s.items.isPrivate,expression:"model.items.isPrivate"}],attrs:{type:"checkbox",tabindex:"0",disabled:["QNA_SECRET_TP.CLOSE_CUSTOMER","QNA_SECRET_TP.CLOSE_ADMIN"].includes(s.originalItem.secretType)},domProps:{checked:Array.isArray(s.items.isPrivate)?e._i(s.items.isPrivate,null)>-1:s.items.isPrivate},on:{change:function(t){var a=s.items.isPrivate,i=t.target,n=!!i.checked;if(Array.isArray(a)){var r=null,o=e._i(a,r);i.checked?o<0&&e.$set(s.items,"isPrivate",a.concat([r])):o>-1&&e.$set(s.items,"isPrivate",a.slice(0,o).concat(a.slice(o+1)))}else e.$set(s.items,"isPrivate",n)}}}),a("span",[e._v("비공개")])])]),a("fieldset",[a("legend",[e._v("체크항목")]),a("div",{staticClass:"goods-qa-write-modal__item fb__checkbox"},[a("span",[e._v("답변수신")]),a("div",{staticClass:"goods-qa-write-modal__item__answer"},[a("label",[a("input",{directives:[{name:"model",rawName:"v-model",value:s.items.isEmail,expression:"model.items.isEmail"}],attrs:{type:"checkbox",tabindex:"0",disabled:e.$FB_CODES.PROCESSES.ING===e.processes.write},domProps:{checked:Array.isArray(s.items.isEmail)?e._i(s.items.isEmail,null)>-1:s.items.isEmail},on:{change:function(t){var a=s.items.isEmail,i=t.target,n=!!i.checked;if(Array.isArray(a)){var r=null,o=e._i(a,r);i.checked?o<0&&e.$set(s.items,"isEmail",a.concat([r])):o>-1&&e.$set(s.items,"isEmail",a.slice(0,o).concat(a.slice(o+1)))}else e.$set(s.items,"isEmail",n)}}}),a("span",[e._v("E-mail로 받기")])]),a("label",[a("input",{directives:[{name:"model",rawName:"v-model",value:s.items.isSMS,expression:"model.items.isSMS"}],attrs:{type:"checkbox",tabindex:"0",disabled:e.$FB_CODES.PROCESSES.ING===e.processes.write},domProps:{checked:Array.isArray(s.items.isSMS)?e._i(s.items.isSMS,null)>-1:s.items.isSMS},on:{change:function(t){var a=s.items.isSMS,i=t.target,n=!!i.checked;if(Array.isArray(a)){var r=null,o=e._i(a,r);i.checked?o<0&&e.$set(s.items,"isSMS",a.concat([r])):o>-1&&e.$set(s.items,"isSMS",a.slice(0,o).concat(a.slice(o+1)))}else e.$set(s.items,"isSMS",n)}}}),a("span",[e._v("SMS로 받기")])])]),a("p",{staticClass:"fb__noticecaption"},[e._v("수신 선택 시, 회원정보의 휴대폰번호 및 이메일로 답변이 발송됩니다.​")])]),a("div",{staticClass:"goods-qa-write-modal__precautions__area"},[a("span",{staticClass:"goods-qa-write-modal__precautions__heading"},[e._v("문의 시 유의해 주세요!​")]),a("p",{staticClass:"goods-qa-write-modal__precautions fb__list-item"},[e._v(" 전화번호, 이메일, 배송지 주소, 환불계좌 정보 등 개인정보가 포함된 게시글은 개인정보 도용의 위험이 있으니 비밀글로 문의해주시기 바랍니다. ")]),a("p",{staticClass:"goods-qa-write-modal__precautions fb__list-item"},[e._v(" 상품과 관계없는 내용, 비방, 욕설, 광고, 도배 등의 게시물은 예고없이 삭제될 수 있습니다. ")])])]),a("div",{staticClass:"goods-qa-write-modal__actions fb__btn-wrap"},[a("button",{staticClass:"fb__btn-margin--white",attrs:{type:"button",tabindex:"0"},on:{click:function(t){return e.handleCancelWrite(t)}}},[e._v("취소")]),a("button",{staticClass:"fb__btn-margin--green",attrs:{type:"submit",tabindex:"0",disabled:e.$FB_CODES.PROCESSES.ING===e.processes.write}},[e._v("저장")])])])])]}}:null],null,!0)}),a("fb-confirm",{attrs:{open:e.confirm.open,width:e.confirm.width,height:e.confirm.height,title:e.confirm.title,message:e.confirm.message,ok:e.confirm.ok,cancel:e.confirm.cancel,"is-background":e.confirm.isBackground,"is-background-close":e.confirm.isBackgroundClose},on:{"close-confirm":function(t){return e.closeConfirm(t)}}}),a("fb-alert",{attrs:{open:e.alert.open,message:e.alert.message},on:{"close-alert":function(t){return e.closeAlert(t)}}})],1):e._e()}),n=[],r=(a("99af"),a("7db0"),a("4160"),a("caad"),a("b0c0"),a("d3b7"),a("ac1f"),a("2532"),a("3ca3"),a("841c"),a("159b"),a("ddb0"),a("2b3d"),a("96cf"),a("1da1")),o=a("2ec4"),c=a("dcbe"),l=a("4402"),u=a("7fcc"),d=a("3af9"),m=a("7e74"),_=a("9032"),p=a("9d0b"),f=a("aa0e"),v=a("863d"),h=a("3540"),b=a("8d3b"),C=(a("f138"),a("0bf4")),g=a("c1df"),S=a.n(g),y={name:"mypage-inquiry",mixins:[C["a"],o["a"]],components:{fbHeader:c["a"],fbFooter:l["a"],fbMypageMenu:u["a"],fbSelectBox:d["a"],fbPagination:m["a"],fbMonths:_["a"],fbAlert:p["a"],fbConfirm:f["a"],fbModal:v["a"],errorLayout:h["a"],simplebar:b["a"]},data:function(){return{reservesTerm:0,reservesTermList:[{name:"3개월",value:0},{name:"6개월",value:1},{name:"12개월",value:2}],reservesDay:"​",summary:{totalCount:0,receptionCount:0,answerCheckingCount:0,answerCompletedCount:0},date:{start:S()(new Date).subtract(3,"M").format(this.$APP_CONFIG.DATE_FORMAT_DEFAULT),end:S()(new Date).format(this.$APP_CONFIG.DATE_FORMAT_DEFAULT)},modals:{qaWrite:{open:!1,classes:"goods-qa-write-modal__wrapper",width:"540px",height:"620px",model:null}},types:null,dateChange:!0,answerCheckList:[],qaValidate:{qaCode:!1,qaTitle:!1,qaContent:!1}}},created:function(){var e=this;if(this.userSession.isLogin){this.getQueryString();this.$nextTick((function(){e.searchDate()}))}},watch:{"modals.qaWrite.model.items.type.code":{handler:function(e){var t=this;if(e){var a,s=this.modals.qaWrite.model.items.type;s.name=null===(a=this.types.find((function(e){return e.code==t.modals.qaWrite.model.items.type.code})))||void 0===a?void 0:a.name}}}},computed:{reservesTermName:function(){var e=this;return this.reservesTermList.find((function(t){return t.value==e.reservesTerm})).name}},methods:{inputLimit:function(e,t){e.target.value=this.getCuttingValue(e,t)},getQueryString:function(){return new URLSearchParams(window.location.search)},openModifyInquiry:function(e){var t=this;return Object(r["a"])(regeneratorRuntime.mark((function a(){var s,i,n,r,o,c;return regeneratorRuntime.wrap((function(a){while(1)switch(a.prev=a.next){case 0:if(null===(s=t.types)||void 0===s){a.next=4;break}a.t0=s,a.next=7;break;case 4:return a.next=6,t.getQATypes();case 6:a.t0=a.sent;case 7:t.types=a.t0,c={originalItem:e,types:t.types,items:{index:e.csQnaId,type:{code:null!==(i=null===(n=t.types.find((function(t){return t.name==e.productTypeName})))||void 0===n?void 0:n.code)&&void 0!==i?i:null,name:(null===(r=t.types.find((function(t){return t.name==e.productTypeName})))||void 0===r?void 0:r.code)?e.productTypeName:null},title:e.title,content:e.question,isPrivate:["QNA_SECRET_TP.CLOSE_CUSTOMER","QNA_SECRET_TP.CLOSE_ADMIN"].includes(e.secretType),isEmail:"Y"==(null===e||void 0===e?void 0:e.answerMailYn),isSMS:"Y"==(null===e||void 0===e?void 0:e.answerSmsYn)},options:{maxLength:200}},t.openModal("qaWrite",{model:c}),(null===(o=t.types.find((function(t){return t.name==e.productTypeName})))||void 0===o?void 0:o.code)||t.openAlert('문의유형 : "'.concat(e.productTypeName,'" 이 존재하지 않습니다. <br> 문의유형을 다시 선택해주세요.'));case 11:case"end":return a.stop()}}),a)})))()},putQnaAnswerUserCheck:function(e){var t=this;return Object(r["a"])(regeneratorRuntime.mark((function a(){return regeneratorRuntime.wrap((function(a){while(1)switch(a.prev=a.next){case 0:if(a.prev=0,!(e.answer.length<1||t.answerCheckList.includes(e.csQnaId))){a.next=5;break}return a.abrupt("return",!1);case 5:t.answerCheckList.push(e.csQnaId);case 6:return a.next=8,t.$store.dispatch("network/request",{id:t.$options.name,url:"/user/buyer/putQnaAnswerUserCheck",method:"post",data:{csQnaId:e.csQnaId}});case 8:a.sent,a.next=14;break;case 11:a.prev=11,a.t0=a["catch"](0),console.error("putQnaAnswerUserCheck error...",a.t0.message);case 14:case"end":return a.stop()}}),a,null,[[0,11]])})))()},toggleVisible:function(e){e.visible=!e.visible,this.putQnaAnswerUserCheck(e)},changeDate:function(e){var t=e.start,a=e.end;this.date.start=t,this.date.end=a,this.dateChange=!0},searchDate:function(){this.clearPage(),this.getQnaInfoByUser(),this.getProductQnaListByUser()},movePaging:function(e){this.pagination.currentPage=e,this.getProductQnaListByUser()},setSecret:function(e){this.openConfirm({message:this.$FB_MESSAGES.SYSTEM.CONFIRM_57,type:"secret",model:e})},handleCancelWrite:function(e){this.openConfirm({message:this.$FB_MESSAGES.SYSTEM.CONFIRM_09,type:"writeCancel"})},writeSubmit:function(e){try{var t,a=this.modals.qaWrite.model,s=(null===a||void 0===a||null===(t=a.items)||void 0===t?void 0:t.index)?this.$FB_MESSAGES.CONFIRM.MODIFY_GOODS_QA:this.$FB_MESSAGES.SYSTEM.CONFIRM_10,i=this.$fbValidator.validate(),n=i.fails.any;i.successes.any;for(var r in this.qaValidate)this.qaValidate[r]=!1;if(Array.isArray(n)&&n.length){for(var o=0,c=n.length;o<c;o++)this.qaValidate[n[o].info]=!0;"qaCode"==n[0].info?n[0].el.closest("button").focus():n[0].el.focus()}i.passes.any&&this.openConfirm({message:s,type:"writeSubmit"})}catch(l){switch(l.code){case this.$FB_CODES.PROCESSES.ING:this.openAlert(l.message||this.$FB_MESSAGES.PROCESSES.ING);break;default:this.openAlert(l.message||this.$FB_MESSAGES.ERROR.DEFAULT);break}}},closeConfirm:function(e){var t=this,a=e.value;return Object(r["a"])(regeneratorRuntime.mark((function e(){return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:if(!a){e.next=14;break}e.t0=t.confirm.type,e.next="secret"===e.t0?4:"writeSubmit"===e.t0?6:"writeCancel"===e.t0?12:14;break;case 4:return t.putProductQnaSetSecretByUser(),e.abrupt("break",14);case 6:return t.resetConfirm(),e.next=9,t.updateQA(t.modals.qaWrite.model.items);case 9:return t.closeModal("qaWrite"),location.reload(),e.abrupt("break",14);case 12:return t.closeModal("qaWrite"),e.abrupt("break",14);case 14:t.resetConfirm();case 15:case"end":return e.stop()}}),e)})))()},getProductQnaListByUser:function(){var e=this;return Object(r["a"])(regeneratorRuntime.mark((function t(){var a,s,i,n,r,o;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return a=e.processKeys.list,t.prev=1,e.checkInProcessing(a),s=e.pagination,i=s.currentPage,n=s.goodsCount,t.next=6,e.$store.dispatch("network/request",{id:e.$options.name,url:"/user/buyer/getProductQnaListByUser",method:"post",willPrevCancel:!0,data:{startDate:e.date.start,endDate:e.date.end,page:i,limit:n}});case 6:r=t.sent,o=r.data,r.code==e.$FB_CODES.API.SUCCESS&&(e.fetches.inquiryList=e.$FB_CODES.FETCHES.SUCCESS,e.pagination.totalCount=o.total,e.inquiryList=o.list,e.inquiryList.forEach((function(t,a){e.$set(e.inquiryList[a],"visible",!1)})),e.$nextTick((function(){var t;window.scrollTo(0,null===(t=e.$refs["qnaFilter"])||void 0===t?void 0:t.offsetTop)}))),t.next=14;break;case 11:t.prev=11,t.t0=t["catch"](1),e.isNonHandlingException(t.t0)||(e.fetches.inquiryList=e.$FB_CODES.FETCHES.ERROR,console.error("getProductQnaListByUser error...",t.t0.message));case 14:return t.prev=14,e.waitProcesses(a),t.finish(14);case 17:case"end":return t.stop()}}),t,null,[[1,11,14,17]])})))()},getQnaInfoByUser:function(){var e=this;return Object(r["a"])(regeneratorRuntime.mark((function t(){var a,s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,e.$store.dispatch("network/request",{id:e.$options.name,url:"/user/buyer/getQnaInfoByUser",method:"post",willPrevCancel:!0,data:{startDate:e.date.start,endDate:e.date.end,qnaType:"QNA_TP.PRODUCT"}});case 3:a=t.sent,a.code==e.$FB_CODES.API.SUCCESS&&(e.summary=null!==(s=a.data)&&void 0!==s?s:e.summary),t.next=10;break;case 7:t.prev=7,t.t0=t["catch"](0),console.error("getQnaInfoByUser error...",t.t0.message);case 10:case"end":return t.stop()}}),t,null,[[0,7]])})))()},putProductQnaSetSecretByUser:function(e){var t=this;return Object(r["a"])(regeneratorRuntime.mark((function a(){var s;return regeneratorRuntime.wrap((function(a){while(1)switch(a.prev=a.next){case 0:return a.prev=0,a.next=3,t.$store.dispatch("network/request",{id:t.$options.name,url:"/user/buyer/putProductQnaSetSecretByUser",method:"post",data:{csQnaId:e.csQnaId}});case 3:s=a.sent,"NOT_QNA"==s.code&&t.openAlert("해당 문의가 존재하지 않습니다.<br> 관리자에게 문의하세요."),a.next=10;break;case 7:a.prev=7,a.t0=a["catch"](0),console.error("addPromotionByUser error...",a.t0.message);case 10:case"end":return a.stop()}}),a,null,[[0,7]])})))()},getQATypes:function(){var e=this;return Object(r["a"])(regeneratorRuntime.mark((function t(){var a,s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,e.$store.dispatch("network/request",{method:"get",url:"/goods/goods/getProductType"});case 3:if(a=t.sent,s=a.data,e.$FB_CODES.API.SUCCESS!==a.code){t.next=9;break}return t.abrupt("return",s);case 9:throw{code:a.code,message:a.message};case 10:t.next=15;break;case 12:throw t.prev=12,t.t0=t["catch"](0),t.t0;case 15:case"end":return t.stop()}}),t,null,[[0,12]])})))()},requestGetMoreQA:function(){var e=this;return Object(r["a"])(regeneratorRuntime.mark((function t(){var a,i,n;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(t.prev=0,e.$FB_CODES.PROCESSES.ING!==e.processes.qaList){t.next=3;break}throw{code:e.$FB_CODES.PROCESSES.ING};case 3:return e.processes.qaList=e.$FB_CODES.PROCESSES.ING,a=e.paginations.qaList,i=e.requests.qaList,t.next=8,goodsService.getQA(Object(s["a"])({page:++a.page,limit:a.rowCount},i));case 8:n=t.sent,a.max=(null===n||void 0===n?void 0:n.total)||0,(null===n||void 0===n?void 0:n.rows)&&(null===n||void 0===n?void 0:n.rows.length)&&(e.qaList=e.qaList.concat(n.rows)),t.next=16;break;case 13:throw t.prev=13,t.t0=t["catch"](0),t.t0;case 16:return t.prev=16,e.processes.qaList=e.$FB_CODES.PROCESSES.WAIT,t.finish(16);case 19:case"end":return t.stop()}}),t,null,[[0,13,16,19]])})))()},updateQA:function(e){var t=this;return Object(r["a"])(regeneratorRuntime.mark((function a(){var s,i,n;return regeneratorRuntime.wrap((function(a){while(1)switch(a.prev=a.next){case 0:return a.prev=0,s={csQnaId:e.index,productType:e.type.code,title:e.title,question:e.content,secretType:e.isPrivate?"Y":"N",answerSmsYn:e.isSMS?"Y":"N",answerMailYn:e.isEmail?"Y":"N"},a.next=4,t.$store.dispatch("network/request",{method:"post",url:"/goods/goods/putProductQna",data:s});case 4:if(i=a.sent,n=i.data,t.$FB_CODES.API.SUCCESS!==i.code){a.next=10;break}return a.abrupt("return",n);case 10:if("NOT_STATUS"!==i.code){a.next=14;break}t.openAlert("수정 가능한 상태가 아닙니다. <br> 새로고침 후 다시 시도해주세요."),a.next=15;break;case 14:throw{code:i.code,message:i.message};case 15:a.next=20;break;case 17:throw a.prev=17,a.t0=a["catch"](0),a.t0;case 20:case"end":return a.stop()}}),a,null,[[0,17]])})))()}}},E=y,w=(a("1c1c"),a("2877")),q=Object(w["a"])(E,i,n,!1,null,null,null),O=q.exports,P=a("8f7d"),A=a("79a5"),x=a("caf9");P["a"].use(A["a"]),P["a"].use(x["a"],{error:P["b"].IMAGES.NOT_FOUND,loading:P["b"].IMAGES.IMG_LOADING}),P["c"].components=Object(s["a"])({App:O},P["c"].components),P["d"].dispatch("init").then((function(){return new P["a"](P["c"])}))}});