/**
 * This class represents an application option dto
 */
export class ApplicationOption {
    id: number;
    name: string;
    defaultValue: string;
    value: string;
    type: ApplicationOptionType;
}

/**
 * This enum contains all option types available to the application
 */
export enum ApplicationOptionType {
    boolean = 'BOOLEAN',
    longRange = 'LONG_RANGE',
    shortRange = 'SHORT_RANGE',
    string = 'STRING'
}
