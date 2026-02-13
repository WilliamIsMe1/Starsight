package william.starsight.util;

/**
 * Stores two objects in a pair cause Java don't have tuples
 *
 * @param one The first in the pair
 * @param two The second in the pair
 * @param <T> The type both elements in the pair
 * @author William
 */
@SuppressWarnings("QuestionableName") // It actually makes sense here
public record Pair<T>(T one, T two) {
}
