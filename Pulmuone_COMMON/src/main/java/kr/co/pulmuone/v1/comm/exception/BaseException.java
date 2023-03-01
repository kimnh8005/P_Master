package kr.co.pulmuone.v1.comm.exception;

import java.text.MessageFormat;
import java.util.Locale;

import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import org.springframework.context.MessageSource;

/**
 * BaseException 은 EgovBizException의 상위클래스이다.
 *
 * <p><b>NOTE:</b> Exception Handling 상의 BaseException 은
 * EgovBizException, RuntimeException, FdlException을 제외한
 * 나머지 Exception 이 발생하는 경우 BaseException 을 바뀌도록 되어 있다.
 *
 * @author Judd Cho (horanghi@gmail.com)
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.05.30  Judd Cho        최초 생성
 *   2015.01.31 Vincent Han		코드 품질 개선
 *
 * </pre>
 */

/* S-정부프레임워크 삭제 테스트(추가-정부프레임워크 BaseException 대체) */
public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;

	protected String message = null;
	protected String messageKey = null;
	protected Object[] messageParameters = null;
	protected Exception wrappedException = null;
	protected MessageCommEnum messageEnum = null;
	protected Object returnObj = null;

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

	public Throwable getWrappedException() {
		return wrappedException;
	}

	public void setWrappedException(Exception wrappedException) {
		this.wrappedException = wrappedException;
	}

	public MessageCommEnum getMessageEnum() {
		return messageEnum;
	}

	public Object getReturnObj(){
		return returnObj;
	}

	/**
	 * BaseException 기본 생성자.
	 */
	public BaseException() {
		this("BaseException without message", null, null);
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param defaultMessage 메세지 지정
	 */
	public BaseException(String defaultMessage) {
		this(defaultMessage, null, null);
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param defaultMessage 메세지 지정
	 */
	public BaseException(MessageCommEnum messageEnum) {
		this.messageEnum = messageEnum;
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param wrappedException  원인 Exception
	 */
	public BaseException(Throwable wrappedException) {
		this("BaseException without message", null, wrappedException);
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param defaultMessage 메세지 지정
	 * @param wrappedException 원인 Exception
	 */
	public BaseException(String defaultMessage, Throwable wrappedException) {
		this(defaultMessage, null, wrappedException);
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param defaultMessage 메세지 지정(변수지정)
	 * @param messageParameters 치환될 메세지 리스트
	 * @param wrappedException 원인 Exception
	 */
	public BaseException(String defaultMessage, Object[] messageParameters, Throwable wrappedException) {
		super(wrappedException);

		String userMessage = defaultMessage;
		if (messageParameters != null) {
			userMessage = MessageFormat.format(defaultMessage, messageParameters);
		}

		this.message = userMessage;
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param messageSource 메세지 리소스
	 * @param messageKey 메세지키값
	 */
	public BaseException(MessageSource messageSource, String messageKey) {
		this(messageSource, messageKey, null, null, Locale.getDefault(), null);
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param messageSource 메세지 리소스
	 * @param messageKey 메세지키값
	 */
	public BaseException(MessageSource messageSource, String messageKey, Throwable wrappedException) {
		this(messageSource, messageKey, null, null, Locale.getDefault(), wrappedException);
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param messageSource 메세지 리소스
	 * @param messageKey 메세지키값
	 * @param locale 국가/언어지정
	 * @param wrappedException 원인 Exception
	 */
	public BaseException(MessageSource messageSource, String messageKey, Locale locale, Throwable wrappedException) {
		this(messageSource, messageKey, null, null, locale, wrappedException);
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param messageSource 메세지 리소스
	 * @param messageKey 메세지키값
	 * @param messageParameters 치환될 메세지 리스트
	 * @param locale 국가/언어지정
	 * @param wrappedException 원인 Exception
	 */
	public BaseException(MessageSource messageSource, String messageKey, Object[] messageParameters, Locale locale, Throwable wrappedException) {
		this(messageSource, messageKey, messageParameters, null, locale, wrappedException);
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param messageSource 메세지 리소스
	 * @param messageKey 메세지키값
	 * @param messageParameters 치환될 메세지 리스트
	 * @param wrappedException 원인 Exception
	 */
	public BaseException(MessageSource messageSource, String messageKey, Object[] messageParameters, Throwable wrappedException) {
		this(messageSource, messageKey, messageParameters, null, Locale.getDefault(), wrappedException);
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param messageSource 메세지 리소스
	 * @param messageKey 메세지키값
	 * @param messageParameters 치환될 메세지 리스트
	 * @param defaultMessage 메세지 지정(변수지정)
	 * @param wrappedException 원인 Exception
	 */
	public BaseException(MessageSource messageSource, String messageKey, Object[] messageParameters, String defaultMessage, Throwable wrappedException) {
		this(messageSource, messageKey, messageParameters, defaultMessage, Locale.getDefault(), wrappedException);
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param messageSource 메세지 리소스
	 * @param messageKey 메세지키값
	 * @param messageParameters 치환될 메세지 리스트
	 * @param defaultMessage 메세지 지정(변수지정)
	 * @param locale 국가/언어지정
	 * @param wrappedException 원인 Exception
	 */
	public BaseException(MessageSource messageSource, String messageKey, Object[] messageParameters, String defaultMessage, Locale locale, Throwable wrappedException) {
		super(wrappedException);

		this.messageKey = messageKey;
		this.messageParameters = messageParameters;
		this.message = messageSource.getMessage(messageKey, messageParameters, defaultMessage, locale);
	}

	/**
	 * BaseException 생성자.
	 *
	 * @param message 메세지
	 * @param returnObj 객체
	 */
	public BaseException(String message, Object returnObj) {
		this.message = message;
		this.returnObj = returnObj;
	}
}
/* E-정부프레임워크 삭제 테스트(추가-정부프레임워크 BaseException 대체) */