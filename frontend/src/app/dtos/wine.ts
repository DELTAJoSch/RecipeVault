import { OwnerInfo } from './owner-info';

/**
 * This class represents a wine dto
 */
export class Wine {
    id: number;
    name: string;
    description: string;
    grape: string;
    link: string;
    temperature: number;
    vinyard: string;
    owner: OwnerInfo;
    country: string;
    category: WineCategory;
}

/**
 * This enum contains all wine categories available to the application
 */
export enum WineCategory {
    sparkling = 'SPARKLING',
    lightWhite = 'LIGHT_WHITE',
    fullWhite = 'FULL_WHITE',
    aromaticWhite = 'AROMATIC_WHITE',
    rose = 'ROSE',
    lightRed = 'LIGHT_RED',
    middleRed = 'MIDDLE_RED',
    fullRed = 'FULL_RED',
    dessert = 'DESSERT'
}
