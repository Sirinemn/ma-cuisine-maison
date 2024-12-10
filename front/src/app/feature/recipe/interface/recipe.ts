import { Ingredient } from "./ingredient";

export interface Recipe {
    id?: number,
    title: string;
    description: string;
    cookingTime: number;
    servings: number;
    userId: number;
    userPseudo: string;
    categoryName: string;
    ingredients: [Ingredient];
    imageId: number;
    imageUrl: string;
    imageThumbUrl: string;
}