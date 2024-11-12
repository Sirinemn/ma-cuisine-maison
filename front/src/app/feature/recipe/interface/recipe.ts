import { Ingredient } from "./ingredient";

export interface Recipe{
    id: number,
    title: string,
    description: string,
    cookingTime: number,
    serving: number,
    userId: number,
    userPseudo: string,
    categoryName: string,
    ingredients: Ingredient[],   
    imageUrl: string,
    imageThumbUrl: string
}