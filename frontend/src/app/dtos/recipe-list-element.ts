import {Author} from './author';
import { Difficulty } from './recipe-details';
import {User} from './user';
import {OwnerInfo} from './owner-info';


/**
 * This class represents a recipe list dto, that is used to display information about a recipe in an overview mode.
 */
export class RecipeListElement {
  id: number;
  name: string;
  shortDescription: string;
  difficulty: Difficulty;
  favorite: boolean;
  owner: OwnerInfo;
  imageId: number;
}
