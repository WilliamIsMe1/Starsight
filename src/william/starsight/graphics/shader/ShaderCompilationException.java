package william.starsight.graphics.shader;

/**
 * Thrown when a shader fails compilation
 *
 * @author William
 */
public class ShaderCompilationException extends Exception {

    /**
     * Constructs the error message
     *
     * @param message The message to send up the stack
     */
	public ShaderCompilationException(String message) {
		super(message);
	}
	
	/**
	 * Constructs an exception to throw
	 *
	 * @param cause The cause of the error
	 */
	public ShaderCompilationException(Throwable cause) {
        super(cause);
    }
}
