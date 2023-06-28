import { Amount } from './amount';
import { User } from './user';
import { Note } from './note';
import { WineCategory } from './wine';
import {Author} from './author';

/**
 * This class represents a recipe details dto, that is used to display information about a recipe in the detail view.
 */
export class RecipeDetails {
    id: number;
    name: string;
    shortDescription: string;
    description: string;
    owner: User;
    difficulty: Difficulty;
    ingredients: Amount[];
    note?: Note;
    favorite?: boolean;
    recommendationConfidence: number;
    recommendedCategory: WineCategory;
    author?: Author;
    imageId: number;
}

export enum Difficulty {
    easy = 'EASY',
    medium = 'MEDIUM',
    hard = 'HARD'
}
