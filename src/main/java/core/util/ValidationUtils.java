package core.util;

/**
 * Utility class for common validation operations.
 * Provides centralized validation methods to ensure consistency across the application.
 */
public final class ValidationUtils {
    
    private ValidationUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }
    
    /**
     * Validates that an object is not null.
     *
     * @param obj the object to validate
     * @param message the error message to throw if validation fails
     * @param <T> the type of the object
     * @return the validated object
     * @throws IllegalArgumentException if the object is null
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }
    
    /**
     * Validates that a string is not null or blank.
     *
     * @param str the string to validate
     * @param message the error message to throw if validation fails
     * @return the validated string
     * @throws IllegalArgumentException if the string is null or blank
     */
    public static String requireNonBlank(String str, String message) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return str;
    }
    
    /**
     * Validates that a numeric value is positive (greater than zero).
     *
     * @param value the value to validate
     * @param message the error message to throw if validation fails
     * @return the validated value
     * @throws IllegalArgumentException if the value is not positive
     */
    public static int requirePositive(int value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
    
    /**
     * Validates that a numeric value is non-negative (greater than or equal to zero).
     *
     * @param value the value to validate
     * @param message the error message to throw if validation fails
     * @return the validated value
     * @throws IllegalArgumentException if the value is negative
     */
    public static double requireNonNegative(double value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}

