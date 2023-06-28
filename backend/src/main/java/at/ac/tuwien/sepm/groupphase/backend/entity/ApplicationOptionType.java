package at.ac.tuwien.sepm.groupphase.backend.entity;

/**
 * Different types of ApplicationOptions.
 * Boolean: Boolean value (stored as TRUE/FALSE)
 * SHORT_RANGE: Value 0 - 255
 * LONG_RANGE: Value 0 - 65535
 * STRING: Arbitrary String value, max. 255 Char.
 */
public enum ApplicationOptionType {
    BOOLEAN,
    SHORT_RANGE,
    LONG_RANGE,
    STRING
}
