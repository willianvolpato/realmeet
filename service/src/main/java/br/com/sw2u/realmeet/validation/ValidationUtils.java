package br.com.sw2u.realmeet.validation;

import static java.util.Objects.isNull;

import br.com.sw2u.realmeet.exception.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static void thrownOnError(ValidationErrors validationErrors) {
        if (validationErrors.hasErrors()) throw new InvalidRequestException(validationErrors);
    }

    public static boolean validateRequired(String field, String value, ValidationErrors validationErrors) {
        if (StringUtils.isBlank(value)) {
            validationErrors.add(field, "Missing value on field: " + field);
            return false;
        }
        return true;
    }

    public static boolean validateRequired(String field, Object value, ValidationErrors validationErrors) {
        if (isNull(value)) {
            validationErrors.add(field, "Missing value on field: " + field);
            return false;
        }
        return true;
    }

    public static boolean validateMaxLength(
        String field,
        String value,
        int maxLength,
        ValidationErrors validationErrors
    ) {
        if (!isNull(value) && value.trim().length() > maxLength) {
            validationErrors.add(field, "Exceeded max length on field: " + field);
            return false;
        }
        return true;
    }

    public static boolean validateValue(
        String field,
        Integer value,
        int maxValue,
        int minValue,
        ValidationErrors validationErrors
    ) {
        if (!isNull(value) && (value > maxValue || value < minValue)) {
            validationErrors.add(
                field,
                "The field " +
                field +
                " has value " +
                value +
                ", but the field allows max " +
                maxValue +
                " and min " +
                minValue
            );
        }
        return true;
    }
}
