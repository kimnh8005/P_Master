(function(t){function e(e){for(var i,s,r=e[0],l=e[1],c=e[2],d=0,h=[];d<r.length;d++)s=r[d],Object.prototype.hasOwnProperty.call(o,s)&&o[s]&&h.push(o[s][0]),o[s]=0;for(i in l)Object.prototype.hasOwnProperty.call(l,i)&&(t[i]=l[i]);u&&u(e);while(h.length)h.shift()();return a.push.apply(a,c||[]),n()}function n(){for(var t,e=0;e<a.length;e++){for(var n=a[e],i=!0,r=1;r<n.length;r++){var l=n[r];0!==o[l]&&(i=!1)}i&&(a.splice(e--,1),t=s(s.s=n[0]))}return t}var i={},o={"mobile/events/convertReserves/index":0},a=[];function s(e){if(i[e])return i[e].exports;var n=i[e]={i:e,l:!1,exports:{}};return t[e].call(n.exports,n,n.exports,s),n.l=!0,n.exports}s.m=t,s.c=i,s.d=function(t,e,n){s.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:n})},s.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},s.t=function(t,e){if(1&e&&(t=s(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var n=Object.create(null);if(s.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var i in t)s.d(n,i,function(e){return t[e]}.bind(null,i));return n},s.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return s.d(e,"a",e),e},s.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},s.p="/";var r=window["webpackJsonp"]=window["webpackJsonp"]||[],l=r.push.bind(r);r.push=e,r=r.slice();for(var c=0;c<r.length;c++)e(r[c]);var u=l;a.push([23,"chunk-commons"]),n()})({"052f":function(t,e,n){},"0697":function(t,e,n){"use strict";var i=n("94fe"),o=n.n(i);o.a},"0fe2":function(t,e,n){},23:function(t,e,n){t.exports=n("8886")},"38de":function(t,e,n){"use strict";var i=n("052f"),o=n.n(i);o.a},"68be":function(t,e,n){"use strict";var i=n("6a72"),o=n.n(i);o.a},"6a72":function(t,e,n){},"77f2":function(t,e,n){},"7ae4":function(t,e,n){},"83b0":function(t,e,n){},8886:function(t,e,n){"use strict";n.r(e);var i=n("5530"),o=(n("e260"),n("e6cf"),n("cca6"),n("a79d"),function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("main",{staticClass:"convert"},[n("member-layout",{attrs:{header:t.header}},[t.eventTerm?[n("section",{staticClass:"convert-header"},[n("h3",{staticClass:"convert-title"},[t._v("#풀무원 통합 적립금 전환 서비스")]),n("p",{staticClass:"convert-description"},[t._v("기존 풀무원샵과 올가에서 적립한 적립금과 포인트를"),n("br"),t._v("#풀무원 적립금으로 전환하여 사용 할 수 있습니다.")])]),n("form",{staticClass:"point-form"},[n("form-fieldset-wrap",{attrs:{legend:"구)풀무원샵 & 구)올가 포인트 조회"}},[n("input-text",{attrs:{inputId:"loginId",value:t.oldAccount.loginId,placeholder:t.placeholderId,disabled:t.authState},on:{updateText:function(e){return t.dataUpdate("loginId",e)}}}),n("input-text",{attrs:{inputId:"password",value:t.oldAccount.password,placeholder:t.placeholderPw,password:"",disabled:t.authState},on:{updateText:function(e){return t.dataUpdate("password",e)},inputEnter:t.setCurrentPoint}}),t.authState?n("p",{staticClass:"confirm-description"},[t._v(" 인증 되었습니다. ")]):t._e(),n("button-default",{attrs:{text:"조회하기",full:"",green:"",disabled:t.authState},on:{customClick:t.setCurrentPoint}}),t.authState?n("button-default",{attrs:{text:"다른 계정 조회하기",line:"",full:"",green:""},on:{customClick:t.setResetInfo}}):t._e()],1),t.authState?[n("form-fieldset-wrap",{staticClass:"convert",attrs:{legend:"#풀무원 통합 적립금 전환"}},[n("form-point-set",{attrs:{label:"구)풀무원샵 보유 적립금",checkLabel:"전액",inputId:"pulmuone",originalPoint:t.currentPulmuonePoint,childDisabled:""}}),n("form-point-set",{attrs:{label:"구)올가 보유 적립금",checkLabel:"전액",inputId:"orga",originalPoint:t.currentOrgaPoint},on:{deliveryPoint:t.deliveryPoint}})],1),n("p",{staticClass:"total-point"},[t._v(" 총 전환 예정 적립금 : "),n("strong",{staticClass:"price"},[t._v(t._s(t.totalPoint))]),t._v(" 원 ")]),n("notice-list-template",{attrs:{notice:t.notice}}),n("input-checkbox",{attrs:{label:"상기 유의사항을 확인하였으며, 적립금 전환에 동의합니다.",inputId:"agreeAll",value:t.agreeCheck},on:{updateChecked:t.setAgreeCheck}}),n("button-default",{attrs:{text:"전환하기",full:"",green:""},on:{customClick:t.setConvertPoint}})]:t._e()],2),t.fetching?n("div",{staticClass:"fb__fetching"}):t._e()]:[n("section",{staticClass:"convert-header"},[n("h3",{staticClass:"convert-title"},[t._v("#풀무원 통합 적립금 전환 서비스")]),n("p",{staticClass:"convert-description"},[t._v("적립금 전환 이벤트가 종료되었습니다.")])])]],2),n("fb-alert",{attrs:{open:t.alert.open,message:t.alert.message},on:{"close-alert":function(e){return t.closeAlert(e)}}}),n("fb-confirm",{attrs:{open:t.confirm.open,width:t.confirm.width,height:t.confirm.height,title:t.confirm.title,message:t.confirm.message,ok:t.confirm.ok,cancel:t.confirm.cancel,"is-background":t.confirm.isBackground,"is-background-close":t.confirm.isBackgroundClose},on:{"close-confirm":function(e){return t.closeConfirm(e)}}}),n("fb-modal",{attrs:{open:t.modals.signIn.open,classes:t.modals.signIn.classes,width:t.modals.signIn.width,height:t.modals.signIn.height,background:t.modals.signIn.background,"is-close-button":t.modals.signIn.isCloseButton},on:{"close-modal":function(e){return t.closeModal("signIn")}}},[[n("sign-in-modal",{attrs:{type:t.$FB_CODES.SIGN_IN.TYPE_NON_USER_ORDER},on:{"non-user-order":function(e){return t.handleNonUserOrder(e)},"sign-in-finished":function(e){return t.handleSignInFinished(e)},"sign-in-close-modal":function(e){return t.closeModal("signIn")}}})]],2)],1)}),a=[],s=(n("a9e3"),n("ac1f"),n("5319"),n("498a"),n("ade3")),r=n("2f62"),l=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"member-wrap"},[t.isDesktop?n("header",[n("a",{staticClass:"logo",attrs:{href:t.$APP_CONFIG.PAGE_URL.ROOT}},[n("img",{attrs:{src:t.$APP_CONFIG.IMAGES.LOGO,alt:"#풀무원"}})])]):t.isMobile?n("fb-mobile-header",{attrs:{title:t.header.title,buttons:t.header.buttons,actions:t.header.actions},on:{"handle-back":function(e){return t.handleBack(e)}}}):t._e(),t._t("default")],2)},c=[],u=n("8e32"),d={name:"member-layout",components:{fbMobileHeader:u["a"]},props:{header:{type:Object,default:{}}},data:function(){return{}}},h=d,p=(n("def8"),n("2877")),f=Object(p["a"])(h,l,c,!1,null,"47d0d1b0",null),m=f.exports,b=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("fieldset",{staticClass:"form-fieldset"},[n("legend",[t._v(" "+t._s(t.legend)+" ")]),t._t("default")],2)},g=[],S={name:"formFieldsetWrap",props:{legend:{type:String,default:""}}},v=S,_=(n("38de"),Object(p["a"])(v,b,g,!1,null,null,null)),C=_.exports,P=n("f5d9"),E=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("label",{staticClass:"checkbox",class:t.size},[n("input",{directives:[{name:"model",rawName:"v-model",value:t.model.value,expression:"model.value"}],attrs:{type:"checkbox",id:t.inputId,disabled:t.disabled},domProps:{checked:Array.isArray(t.model.value)?t._i(t.model.value,null)>-1:t.model.value},on:{change:[function(e){var n=t.model.value,i=e.target,o=!!i.checked;if(Array.isArray(n)){var a=null,s=t._i(n,a);i.checked?s<0&&t.$set(t.model,"value",n.concat([a])):s>-1&&t.$set(t.model,"value",n.slice(0,s).concat(n.slice(s+1)))}else t.$set(t.model,"value",o)},t.inputChange]}}),n("span",[t._v(t._s(t.label))])])},k=[],I={name:"inputCheckbox",props:{inputId:{type:String,default:""},size:{type:String,default:""},value:{type:Boolean,default:!1},label:{type:String,default:""},disabled:{type:Boolean,default:!1}},data:function(){return{model:{value:!1}}},watch:{value:{immediate:!0,handler:function(t){this.model.value=t}}},methods:{inputChange:function(){this.$emit("updateChecked",this.model.value)}}},A=I,O=(n("b179"),Object(p["a"])(A,E,k,!1,null,null,null)),y=O.exports,M=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"form-point"},[n("label",{staticClass:"form-point-title",attrs:{for:t.inputId}},[t._v(" "+t._s(t.label)+" ")]),n("input-text",{ref:"pointInput",attrs:{inputId:"text-"+t.inputId,value:t.stringCurrent,disabled:t.childDisabled},on:{updateText:t.onChangePoint}}),n("input-checkbox",{attrs:{inputId:"check-"+t.inputId,value:t.isChecked,label:t.checkLabel,size:"medium",disabled:t.childDisabled},on:{updateChecked:t.onChecked}})],1)},T=[],x={name:"formPointSet",components:{inputText:P["a"],inputCheckbox:y},props:{inputId:{type:String,default:""},label:{type:String,default:""},checkLabel:{type:String,default:""},childDisabled:{type:Boolean,default:!1},minPoint:{type:Number,default:0},originalPoint:{type:Number,default:0}},data:function(){return{currentPoint:0,stringCurrent:0,isChecked:!0}},watch:{originalPoint:{immediate:!0,handler:function(t){this.currentPoint=String(t),this.isChecked=!0}},currentPoint:{immediate:!0,handler:function(t){this.stringCurrent=this.setCommas(t),this.$refs.pointInput&&(this.$refs.pointInput.model.value=this.setCommas(t))}}},methods:{setCommas:function(t){return String(t).replace(/\B(?=(\d{3})+(?!\d))/g,",")},onChangePoint:function(t){var e=Number(t.replace(/[^0-9.]/g,""));this.$refs.pointInput.model.value=this.setCommas(e),Number(e)<this.minPoint?(this.isChecked=!1,this.$emit("deliveryPoint",this.minPoint)):Number(e)>this.originalPoint?(this.isChecked=!0,this.currentPoint=this.originalPoint,this.$refs.pointInput.model.value=this.setCommas(this.originalPoint),this.$emit("deliveryPoint",this.originalPoint)):Number(e)===this.originalPoint?(this.isChecked=!0,this.$emit("deliveryPoint",e)):(this.isChecked=!1,this.$emit("deliveryPoint",e))},onChecked:function(t){this.isChecked=t,this.$refs.pointInput.model.value=this.setCommas(this.originalPoint),this.$emit("deliveryPoint",this.originalPoint),this.$emit("deliveryCheck",this.isChecked)}}},$=x,w=(n("b18c"),Object(p["a"])($,M,T,!1,null,null,null)),N=w.exports,L=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("button",{staticClass:"btn",class:{full:t.full,line:t.line,green:t.green},attrs:{type:"button",tabindex:"0"},on:{click:t.buttonClick}},[t._v(" "+t._s(t.text)+" ")])},R=[],F={name:"buttonDefault",props:{text:{type:String,default:""},line:{type:Boolean,default:!1},full:{type:Boolean,default:!1},green:{type:Boolean,default:!1}},methods:{buttonClick:function(){this.$emit("customClick")}}},G=F,B=(n("0697"),Object(p["a"])(G,L,R,!1,null,null,null)),D=B.exports,j=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"notice-wrap"},[t.notice.title?n("strong",{staticClass:"notice-title",domProps:{innerHTML:t._s(t.notice.title)}}):t._e(),n("ul",{staticClass:"notice-list"},t._l(t.notice.option,(function(e,i){return n("li",{key:i,domProps:{innerHTML:t._s(e)}})})),0)])},U=[],Y={name:"noticeListTemplate",props:{notice:{type:Object,default:{}}}},z=Y,H=(n("68be"),Object(p["a"])(z,j,U,!1,null,null,null)),X=H.exports,J=n("face"),V=n("2479"),W=n("5f72"),K=n("479c"),Z=Object(r["a"])("convert"),q={components:{MemberLayout:m,formFieldsetWrap:C,inputText:P["a"],inputCheckbox:y,formPointSet:N,buttonDefault:D,noticeListTemplate:X,fbModal:V["a"],fbAlert:J["a"],fbConfirm:W["a"],signInModal:K["a"]},data:function(){return{header:{title:"적립금 전환 안내",buttons:["back"],actions:[]},placeholderId:"구)풀무원샵 / 구)올가 ID",placeholderPw:"구)풀무원샵 / 구)올가 비밀번호",oldAccount:{loginId:"",password:""},authState:!1,pulmuone:{point:"0"},orga:{point:"0"},totalPoint:"0",notice:{title:"전환 전 꼭! 확인하세요",option:["#풀무원 통합 적립금으로 전환된 적립금은 전환일로부터 1년간 사용 가능합니다.","전환 즉시 풀무원샵 적립금과 올가 포인트는 차감되며, 전환된 적립금은 다시 환원되지 않습니다.","#풀무원 최대 적립금액은 500,000원을 초과 할 수 없으므로 초과된 경우 적립금 사용 후 다시 적립 해주세요."]},agreeCheck:!1,modals:Object(s["a"])({},this.$FB_CODES.MODAL.KEYS.SIGN_IN,this.makeModal({classes:"sign-in-modal__wrapper",width:"100%",height:"100%",background:"#fafafa"})),fetching:!1,resetStep:!1,eventTerm:!1}},computed:Object(i["a"])({},Z.mapGetters(["currentPulmuonePoint","currentOrgaPoint"])),watch:{currentPulmuonePoint:{immediate:!0,handler:function(){this.pulmuone.point=String(this.currentPulmuonePoint)}},currentOrgaPoint:{handler:function(){this.orga.point=String(this.currentOrgaPoint)}}},methods:Object(i["a"])({},Z.mapActions(["fetchUserPoint","fetchConvertPoint","setResetPointInfo"]),{dataUpdate:function(t,e){this.oldAccount[t]="loginId"===t?e.trim():e},setCurrentPoint:function(){var t=this;this.fetching=!0,this.fetchUserPoint(this.oldAccount).then((function(e){switch(t.fetching=!1,e.messageEnum){case"SUCCESS":t.authState=!0;break;case"NEED_LOGIN":t.needLogin();break;case"LOGIN_FAIL":t.loginFail();break}t.totalPoint=t.setCommas(t.currentPulmuonePoint+t.currentOrgaPoint)}))},deliveryPoint:function(t){this.orga.point=String(t),this.totalPoint=this.setCommas(Number(this.pulmuone.point)+Number(t))},setCommas:function(t){return String(t).replace(/\B(?=(\d{3})+(?!\d))/g,",")},setAgreeCheck:function(t){this.agreeCheck=t},setConvertPoint:function(){var t=this;if(this.agreeCheck)if(this.agreeCheck&&0===Number(this.pulmuone.point+this.orga.point))this.openAlert(this.$FB_MESSAGES.SYSTEM.ALERT_297);else if(this.agreeCheck){this.fetching=!0;var e=Object(i["a"])({},this.oldAccount,{pulmuonePoint:Number(this.pulmuone.point),orgaPoint:Number(this.orga.point)});this.fetchConvertPoint(e).then((function(e){switch(t.fetching=!1,e.messageEnum){case"SUCCESS":t.openAlert(t.$FB_MESSAGES.SYSTEM.ALERT_277),t.resetStep=!0;break;case"NEED_LOGIN":t.needLogin();break;case"LOGIN_FAIL":t.loginFail();break;case"PARTIAL_DEPOSIT_OVER_LIMIT":var n=t.setCommas(e.data.pointPartialDeposit);t.exceedConvertPoint(n),t.resetStep=!0;break;case"DEPOSIT_POINT_EXCEEDED":t.exceedConvertPoint("0"),t.resetStep=!0;break;case"MAXIMUM_DEPOSIT_POINT_EXCEEDED":t.exceedConvertPoint("0"),t.resetStep=!0;break;case"ASIS_OVER_POINT":t.openAlert(t.$FB_MESSAGES.SYSTEM.ALERT_276),t.resetStep=!0;break;case"ASIS_API_ERROR":t.openAlert(t.$FB_MESSAGES.SYSTEM.ALERT_276),t.resetStep=!0;break;case"ASIS_POINT_ZERO":t.openAlert(t.$FB_MESSAGES.SYSTEM.ALERT_276),t.resetStep=!0;break;default:t.openAlert(t.$FB_MESSAGES.ERROR.DEFAULT);break}}))}else this.openAlert(this.$FB_MESSAGES.SYSTEM.ALERT_23);else this.openAlert(this.$FB_MESSAGES.SYSTEM.ALERT_298)},loginFail:function(){this.openAlert(this.$FB_MESSAGES.SYSTEM.ALERT_01)},needLogin:function(){this.openConfirm({message:this.$FB_MESSAGES.SYSTEM.CONFIRM_84,type:"sign-in"})},exceedConvertPoint:function(t){this.openAlert(this.replaceTemplateString(this.$FB_MESSAGES.SYSTEM.ALERT_301,{N:t}))},setResetInfo:function(){var t="",e=!1;this.oldAccount.loginId=t,this.oldAccount.password=t,this.authState=e,this.setResetPointInfo()},closeAlert:function(){this.resetStep&&(this.setResetInfo(),this.resetStep=!1),this.alert.open=!1},closeConfirm:function(t){var e=this.confirm;if(e){switch(e.type){case"sign-in":t.value&&this.openModal("sign-in");break;default:console.warn("not found type...",e.type);break}this.resetConfirm()}},handleSignInFinished:function(t){this.closeModal("sign-in")}})},Q=q,tt=(n("e6f9"),Object(p["a"])(Q,o,a,!1,null,"276227b1",null)),et=tt.exports,nt=n("633d"),it=n("79a5"),ot=n("caf9");nt["a"].use(it["a"]),nt["a"].use(ot["a"],{error:nt["b"].IMAGES.NOT_FOUND,loading:nt["b"].IMAGES.IMG_LOADING}),nt["c"].components=Object(i["a"])({App:et},nt["c"].components),nt["d"].dispatch("init").then((function(){return new nt["a"](nt["c"])}))},"94fe":function(t,e,n){},b179:function(t,e,n){"use strict";var i=n("7ae4"),o=n.n(i);o.a},b18c:function(t,e,n){"use strict";var i=n("83b0"),o=n.n(i);o.a},def8:function(t,e,n){"use strict";var i=n("77f2"),o=n.n(i);o.a},e6f9:function(t,e,n){"use strict";var i=n("0fe2"),o=n.n(i);o.a}});