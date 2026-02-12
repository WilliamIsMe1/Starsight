package william.starsight.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import william.coreutils.MathUtils;

/**
 * The utility class for screen coordinate stuff in Starsight
 *
 * @author William
 */
public final class ScreenUtils {
	@Contract(" -> fail")
	private ScreenUtils() {
	    throw new AssertionError("This class must not be instantiated.");
	}
	
	/**
	 * Used to convert pixel coords to screen coords
	 *
	 * @param x The x coordinate of the mouse in pixel coordinates
	 * @param y The y coordinate of the mouse in pixel coordinates
	 * @param width The width of the window receiving the clicks
	 * @param height The height of the window receiving the clicks
	 * @return The coordinates openGL expects
	 */
	@Contract(pure = true, value = "_,_,_,_ -> new")
	public static @NotNull Pair<Float> getNormalizedDeviceCoords(int x, int y, int width, int height) {
		return new Pair<>(
			MathUtils.remap(x,
				0, width - 1,
				1.0f, -1.0f
			),
			MathUtils.remap(y,
				0, height - 1,
				1.0f, -1.0f
			)
		);
	}
	
	/**
	 * Used to convert pixel coords to screen coords
	 *
	 * @param inCoordinates The coordinates of the mouse in pixel coordinates
	 * @param width The width of the window receiving the clicks
	 * @param height The height of the window receiving the clicks
	 * @return The coordinates openGL expects
	 */
	@Contract(pure = true, value = "_, _, _ -> new")
	public static @NotNull Pair<Float> getNormalizedDeviceCoords(@NotNull Pair<Integer> inCoordinates, int width, int height) {
		return getNormalizedDeviceCoords(inCoordinates.one(), inCoordinates.two(), width, height);
	}
	
	/**
	 * Used to convert pixel coords to screen coords
	 *
	 * @param x The x coordinate of the mouse in pixel coordinates
	 * @param y The y coordinate of the mouse in pixel coordinates
	 * @param dimensions The dimensions of the window receiving the clicks
	 * @return The coordinates openGL expects
	 */
	@Contract(pure = true, value = "_, _, _ -> new")
	public static @NotNull Pair<Float> getNormalizedDeviceCoords(int x, int y, @NotNull Pair<Integer> dimensions) {
		return getNormalizedDeviceCoords(x, y, dimensions.one(), dimensions.two());
	}
	
	/**
	 * Used to convert pixel coords to screen coords
	 *
	 * @param inCoordinates The coordinates of the mouse in pixel coordinates
	 * @param dimensions The dimensions of the window receiving the clicks
	 * @return The coordinates openGL expects
	 */
	@Contract(pure = true, value = "_, _ -> new")
	public static @NotNull Pair<Float> getNormalizedDeviceCoords(@NotNull Pair<Integer> inCoordinates, @NotNull Pair<Integer> dimensions) {
		return getNormalizedDeviceCoords(inCoordinates.one(), inCoordinates.two(), dimensions.one(), dimensions.two());
	}
}
