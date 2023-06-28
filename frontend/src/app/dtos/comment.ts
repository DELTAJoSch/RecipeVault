/**
 * This class represents a comment dto
 */
export class Comment {
  creatorId: number | undefined;
  recipeId: number;
  dateTime: Date;
  content: string;
}
