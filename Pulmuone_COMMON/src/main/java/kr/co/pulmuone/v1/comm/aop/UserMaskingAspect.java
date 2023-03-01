package kr.co.pulmuone.v1.comm.aop;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.aop.service.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.MaskingUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class UserMaskingAspect {

    /**
     * annotation 따른 Point cut 실행
     *
     * @param pjp ProceedingJoinPoint
     * @return Object
     * @throws Throwable throws anything
     */
    @Around("@annotation(kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun)")
    public Object userMaskingRun(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        UserMaskingRun custom = methodSignature.getMethod().getAnnotation(UserMaskingRun.class);
        if (custom.system().equals("BOS")) {
            UserVo userVo = SessionUtil.getBosUserVO();
            if (userVo == null || userVo.getPersonalInformationAccessYn().equals("Y")) {
                return result;
            }
        }

        checkFieldType(result);
        return result;
    }

    /**
     * fieldType 값 에 따른 분기
     *
     * @param object Object
     */
    private void checkFieldType(Object object) throws NoSuchFieldException {
        if (object == null) return;
        if (object instanceof ArrayList) {  // for Page
            List<?> list = (List<?>) object;
            for (Object obj : list) {
                checkFieldType(obj);
            }
            return;
        }
        if (object instanceof ApiResult) {
            ApiResult<?> apiResult = (ApiResult<?>) object;
            checkFieldType(apiResult.getData());
            return;
        }

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(List.class)) {
                List<?> list = (List<?>) getObjectField(object, object.getClass().getDeclaredField(field.getName()));
                if (list == null) continue;
                for (Object obj : list) {
                    checkFieldType(obj);
                }
            }
            if (field.getType().getName().contains(".pulmuone.")) {
                checkFieldType(getObjectField(object, object.getClass().getDeclaredField(field.getName())));
            }
            if (field.getType().isAssignableFrom(Page.class)) {
                checkFieldType(getObjectField(object, object.getClass().getDeclaredField(field.getName())));
            }
            if (field.getType().isAssignableFrom(String.class)) {
                checkField(object, field);
            }
        }
    }

    /**
     * field 값 annotation 에 따른 분기
     *
     * @param obj   Object
     * @param field Field
     */
    private void checkField(Object obj, Field field) {
        Object newValue;
        if (field.isAnnotationPresent(UserMaskingUserName.class)) {
            Object temp = getObjectField(obj, field);
            if (Objects.isNull(temp)) return;
            newValue = MaskingUtil.name((String) temp);
            setObjectField(obj, field, newValue);
        } else if (field.isAnnotationPresent(UserMaskingMobile.class)) {
            Object temp = getObjectField(obj, field);
            if (Objects.isNull(temp)) return;
            newValue = MaskingUtil.cellPhone((String) temp);
            setObjectField(obj, field, newValue);
        } else if (field.isAnnotationPresent(UserMaskingEmail.class)) {
            Object temp = getObjectField(obj, field);
            if (Objects.isNull(temp)) return;
            newValue = MaskingUtil.email((String) temp);
            setObjectField(obj, field, newValue);
        } else if (field.isAnnotationPresent(UserMaskingAddress.class)) {
            Object temp = getObjectField(obj, field);
            if (Objects.isNull(temp)) return;
            newValue = MaskingUtil.address((String) temp);
            setObjectField(obj, field, newValue);
        } else if (field.isAnnotationPresent(UserMaskingAddressDetail.class)) {
            Object temp = getObjectField(obj, field);
            if (Objects.isNull(temp)) return;
            newValue = MaskingUtil.addressDetail((String) temp);
            setObjectField(obj, field, newValue);
        } else if (field.isAnnotationPresent(UserMaskingTelePhone.class)) {
            Object temp = getObjectField(obj, field);
            if (Objects.isNull(temp)) return;
            newValue = MaskingUtil.telePhone((String) temp);
            setObjectField(obj, field, newValue);
        } else if (field.isAnnotationPresent(UserMaskingAccountNumber.class)) {
            Object temp = getObjectField(obj, field);
            if (Objects.isNull(temp)) return;
            newValue = MaskingUtil.accountNumber((String) temp);
            setObjectField(obj, field, newValue);
        } else if (field.isAnnotationPresent(UserMaskingBirth.class)) {
            Object temp = getObjectField(obj, field);
            if (Objects.isNull(temp)) return;
            newValue = MaskingUtil.birth((String) temp);
            setObjectField(obj, field, newValue);
        } else if (field.isAnnotationPresent(UserMaskingLoginId.class)) {
            Object temp = getObjectField(obj, field);
            if (Objects.isNull(temp)) return;
            newValue = MaskingUtil.loginId((String) temp);
            setObjectField(obj, field, newValue);
        } else if (field.isAnnotationPresent(UserMaskingUserNameLoginId.class)) {
          Object temp = getObjectField(obj, field);
          if (Objects.isNull(temp)) return;
          newValue = MaskingUtil.nameLoginId((String) temp);
          setObjectField(obj, field, newValue);
        } else if (field.isAnnotationPresent(UserMaskingSerialNumber.class)) {
            Object temp = getObjectField(obj, field);
            if (Objects.isNull(temp)) return;
            newValue = MaskingUtil.serialNumber((String) temp);
            setObjectField(obj, field, newValue);
        }
    }

    /**
     * field 값 추출
     *
     * @param obj   Object
     * @param field Field
     * @return Object
     */
    private Object getObjectField(Object obj, Field field) {
        Object result = null;
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            log.error("마스킹 실패:: [{}]", field.getName());
        }
        field.setAccessible(accessible);
        return result;
    }

    /**
     * field 값 변경
     *
     * @param obj      Object
     * @param field    Field
     * @param newValue Object
     */
    private void setObjectField(Object obj, Field field, Object newValue) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(obj, newValue);
        } catch (IllegalAccessException e) {
            log.error("마스킹 실패:: [{}]", field.getName());
        }
        field.setAccessible(accessible);
    }
}
