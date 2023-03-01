package kr.co.pulmuone.v1.comm.exception;

public class MallCustomException extends Exception{

	private static final long serialVersionUID = -4751808644804611423L;

	public MallCustomException(){
		super();
	}

	public MallCustomException(String message){
		super(message);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
