package kr.co.pulmuone.v1.comm.exception;


import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.MessageSource;


/* S-정부프레임워크 삭제 테스트(수정-정부프레임워크 BaseException 대체 상속관계 변경) */
public class UserException extends BaseException{
/* E-정부프레임워크 삭제 테스트(수정-정부프레임워크 BaseException 대체 상속관계 변경) */

	//private static final long serialVersionUID = -1989742136286962539L;
	private static final long serialVersionUID = 1L;
	private String userExceptionCode;
	private String userExceptionMessage;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public Object[] getMessageParameters() {
		return messageParameters;
	}

	public void setMessageParameters(Object[] messageParameters) {
		this.messageParameters = messageParameters;
	}
	//-------------------------------------------------------------------
	/*
	public Throwable getWrappedException() {
		return wrappedException;
	}

	public void setWrappedException(Exception wrappedException) {
		this.wrappedException = wrappedException;
	}
	*/
    //-------------------------------------------------------------------
	public UserException() {
		this("UserException without message", null, null);
	}

	public UserException(String defaultMessage) {
		this(defaultMessage, null, null);
	}

	public UserException(Throwable wrappedException) {
		this("BaseException without message", null, wrappedException);
	}

	public UserException(String defaultMessage, Throwable wrappedException) {
		this(defaultMessage, null, wrappedException);
	}

	public UserException(String defaultMessage, Object[] messageParameters, Throwable wrappedException) {
		super(wrappedException);

		String userMessage = defaultMessage;
		if (messageParameters != null) {
			userMessage = MessageFormat.format(defaultMessage, messageParameters);
		}
		this.message = userMessage;

	}

	public UserException(MessageSource messageSource, String messageKey) {
		this(messageSource, messageKey, null, null, Locale.getDefault(), null);
	}

	public UserException(MessageSource messageSource, String messageKey, Throwable wrappedException) {
		this(messageSource, messageKey, null, null, Locale.getDefault(), wrappedException);
	}

	public UserException(MessageSource messageSource, String messageKey, Locale locale, Throwable wrappedException) {
		this(messageSource, messageKey, null, null, locale, wrappedException);
	}

	public UserException(MessageSource messageSource, String messageKey, Object[] messageParameters, Locale locale,
	        Throwable wrappedException) {
		this(messageSource, messageKey, messageParameters, null, locale, wrappedException);
	}

	public UserException(MessageSource messageSource, String messageKey, Object[] messageParameters,
	        Throwable wrappedException) {
		this(messageSource, messageKey, messageParameters, null, Locale.getDefault(), wrappedException);
	}

	public UserException(MessageSource messageSource, String messageKey, Object[] messageParameters,
	        String defaultMessage, Throwable wrappedException) {
		this(messageSource, messageKey, messageParameters, defaultMessage, Locale.getDefault(), wrappedException);
	}

	public UserException(MessageSource messageSource, String messageKey, Object[] messageParameters,
	        String defaultMessage, Locale locale, Throwable wrappedException) {
		super(wrappedException);

		this.messageKey = messageKey;
		this.messageParameters = messageParameters;
		this.message = messageSource.getMessage(messageKey, messageParameters, defaultMessage, locale);

	}

	public UserException(String code, String message) {
		this("", null, null);
		setUserExceptionCode(code);
		setUserExceptionMessage(message);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUserExceptionCode() {
		return userExceptionCode;
	}

	public void setUserExceptionCode(String code) {
		this.userExceptionCode = code;
	}

	public String getUserExceptionMessage() {
		return userExceptionMessage;
	}

	public void setUserExceptionMessage(String message) {
		this.userExceptionMessage = message;
	}

}
