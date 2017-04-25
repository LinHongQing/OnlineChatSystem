package exception;

public final class IllegalEmptyParameterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2006311967967986291L;

	public IllegalEmptyParameterException(String msg) {
		super("非法的空字符串: " + msg);
	}
}
