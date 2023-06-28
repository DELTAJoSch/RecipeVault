import { WineCategory } from './wine';

/**
 * This class represents a wine search dto
 */
export class WineSearch {
    name: string;
    vinyard: string;
    country: string;
    category: WineCategory;
}
