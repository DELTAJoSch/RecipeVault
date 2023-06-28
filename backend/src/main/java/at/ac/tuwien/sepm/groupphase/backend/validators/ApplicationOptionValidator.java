package at.ac.tuwien.sepm.groupphase.backend.validators;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationOptionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationOption;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * This class contains the validators for ApplicationOptions.
 */
public class ApplicationOptionValidator {
    /**
     * Validate an ApplicationOption for update.
     *
     * @param newOption The new applicationOption
     * @param current   The old applicationOption
     * @throws ValidationException Thrown if the application option does not conform.
     */
    public static void validateForUpdate(ApplicationOptionDto newOption, ApplicationOption current) throws ValidationException {
        var errors = new ArrayList<String>();

        var newValue = newOption.getValue();
        if (newValue != null) {
            if (newValue.strip().length() == 0) {
                errors.add("Der Wert darf nicht leer sein.");
            } else {
                switch (current.getType()) {
                    case BOOLEAN -> {
                        Pattern pattern = Pattern.compile("^(TRUE|FALSE|True|False|true|false)$");
                        var matcher = pattern.matcher(newValue);

                        if (!matcher.matches()) {
                            errors.add("Der Wert entspricht nicht dem booleschen Wert TRUE oder FALSE");
                        }
                    }
                    case SHORT_RANGE -> {
                        Pattern pattern = Pattern.compile("^(0|[1-9][0-9]?|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$");
                        var matcher = pattern.matcher(newValue);

                        if (!matcher.matches()) {
                            errors.add("Der Wert entspricht nicht dem SHORT RANGE-Wertebereich von 0 bis 255");
                        }
                    }
                    case LONG_RANGE -> {
                        Pattern pattern = Pattern.compile("^(0|[1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");
                        var matcher = pattern.matcher(newValue);

                        if (!matcher.matches()) {
                            errors.add("Der Wert entspricht nicht dem LONG RANGE-Wertebereich von 0 bis 65535");
                        }
                    }
                    case STRING -> {
                        if (newValue.length() > 254) {
                            errors.add("Der neue Wert ist zu lang");
                        }
                    }
                    default -> {

                    }
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
