const api_recipes = "https://api.spoonacular.com/recipes";
const axios = require("axios");
const user = require("./user");

exports.getRecipeInformation = async function (recipeID, userID) {
  try {
    let response_api = await axios.get(
      `${api_recipes}/${recipeID}/information`,
      {
        params: {
          includeNutrition: false,
          apiKey: process.env.spooncular_apiKey,
        },
      }
    );
    let previewInfo = getPreview(response_api.data);
    let ingredients = getIngredients(response_api.data);
    let instructions = getInstructions(response_api.data);
    let numOfPlates = response_api.data.servings;
    if (userID) {
      console.log("what the fuck");
      await user.addUserWatchedRecipe(userID, recipeID);
    }
    return {
      previewInfo: previewInfo,
      ingredients: ingredients,
      instructions: instructions,
      numOfPlates: numOfPlates,
    };
  } catch (error) {
    throw error;
  }
};

exports.getRecipePreview = async function (recipeID) {
  try {
    let response_api = await axios.get(
      `${api_recipes}/${recipeID}/information`,
      {
        params: {
          includeNutrition: false,
          apiKey: process.env.spooncular_apiKey,
        },
      }
    );
    return getPreview(response_api.data);
  } catch (err) {
    throw err;
  }
};

exports.getRandomRecipes = async function (numberOfRecipes) {
  try {
    let recipes = [];
    const random_recipes = await axios.get(
      `${api_recipes}/random?number=${numberOfRecipes}`,
      {
        params: {
          includeNutrition: false,
          apiKey: process.env.spooncular_apiKey,
        },
      }
    );
    random_recipes.data.recipes.forEach((element) => {
      recipes.push(getPreview(element));
    });
    return recipes;
  } catch (e) {
    console.log(e);
    throw e;
  }
};

exports.getSearchedRecipes = async function (paramaters) {
  try {
    paramaters.apiKey = process.env.spooncular_apiKey;
    const searchResults = await axios.get(`${api_recipes}/search`, {
      params: paramaters,
    });

    let recipesIDs = getRecipeIDOfSearch(searchResults);
    let recipesInfo = await getRecipesInformation(recipesIDs);
    let result = [];
    recipesInfo.forEach((element) => {
      result.push({
        previewInfo: getPreview(element.data),
        instructions: element.data.instructions,
        time: element.data.preparationMinutes,
        popularity: element.data.aggregateLikes,
      });
    });

    return result;
  } catch (error) {
    console.log(error);
  }
};

let getRecipeIDOfSearch = function (searchResults) {
  let searchRecipes = searchResults.data.results;
  recipesIDs = [];
  searchRecipes.map((sRecipe) => recipesIDs.push(sRecipe.id));
  return recipesIDs;
};

let getRecipesInformation = async function (recipesIDs) {
  let promises = [];
  recipesIDs.map((id) => {
    promises.push(
      axios.get(
        `${api_recipes}/${id}/information/?apiKey=${process.env.spooncular_apiKey}`
      )
    );
  });

  let apiResponse = await Promise.all(promises);
  return apiResponse;
};

let randomRecipeInformation = function (recipe) {
  var recipe_id = recipe["id"];
  var recipe_image = recipe["image"];
  var recipe_title = recipe["title"];
  let recipe_info_json = {
    id: recipe_id,
    image: recipe_image,
    title: recipe_title,
  };
  return recipe_info_json;
};

let getPreview = function (recipe) {
  const {
    id,
    title,
    image,
    readyInMinutes,
    aggregateLikes,
    vegetarian,
    vegan,
    glutenFree,
  } = recipe;
  let res_JSON = {
    id: id,
    title: title,
    image: image,
    readyInMinutes: readyInMinutes,
    aggregateLikes: aggregateLikes,
    vegetarian: vegetarian,
    vegan: vegan,
    glutenFree: glutenFree,
  };
  return res_JSON;
};

let getIngredients = function (recipe) {
  let ingredients = [];
  recipe.extendedIngredients.forEach((element) =>
    ingredients.push({
      name: element.name,
      amount: element.amount,
      unit: element.unit,
    })
  );
  return ingredients;
};

let getInstructions = function (recipe) {
  let instructions = [];
  recipe.analyzedInstructions[0].steps.forEach((element) =>
    instructions.push(element.step)
  );
  return instructions;
};
