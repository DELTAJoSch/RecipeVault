import { Ingredient } from './ingredient';

/**
 * This class represents an amount dto.
 */
export class Amount {
    ingredient: Ingredient;
    amount: number;
    unit: AmountUnit;
}

export enum AmountUnit {
    g = 'G',
    piece = 'PIECE',
    lb = 'LB',
    tsp = 'TSP',
    tbsp = 'TBSP',
    kg = 'KG',
    fl = 'FL',
    oz = 'OZ',
    ml = 'ML',
    cup = 'CUP'
}
