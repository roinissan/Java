const recipe = require("./recipe");
const DB = require("./DB");

exports.getUserLastWatched = async function (userName, numberOfRecipes) {
  try {
    const recipes = await DB.getUserLastWatchedRecipes(userID);
    let recipesPreview = [];
    if (recipes && recipes.length < numberOfRecipes)
      numberOfRecipes = recipes.length;

    for (let index = 0; index < numberOfRecipes; index++) {
      recipesPreview.push(
        await recipe.getRecipePreview(recipes[index].RecipeID)
      );
    }
    return recipesPreview;
  } catch (err) {
    throw err;
  }
};

exports.getUserSaveWatchRecipes = async function (userID, recipesIDs) {
  let dbResult = await DB.getUserSaveWatchRecipes(userID, recipesIDs);
  let finalResult = {};
  if (dbResult.length > 0)
    dbResult.forEach((element) => {
      finalResult[element.RecipeID] = {
        watched: element.Watched,
        saved: element.Saved,
      };
    });
  else {
    recipesIDs.forEach((element) => {
      finalResult[element] = { watched: false, saved: false };
    });
  }
  return finalResult;
};

exports.getUserFavourites = async function (userID) {
  let result = await DB.getUserFavourites(userID);
  let previewPromises = [];
  result.forEach((element) =>
    previewPromises.push(recipe.getRecipePreview(element.RecipeID))
  );

  let previewResult = await Promise.all(previewPromises);
  console.log("dsad");
  return previewResult;
};

exports.getUserWatch = async function (userID) {
  return await DB.getUserWatch(userID);
};

exports.getUserLastWatch = async function (userID, numOfRec) {
  return await DB.getUserLastWatch(userID, numOfRec);
};

exports.getUserSelfRecipes = async function (userID) {
  return await DB.getUserSelfRecipes(userID);
};
exports.getUserSelfRecipe = async function (userID, recipeID) {
  let result = await DB.getUserSelfRecipes(userID, recipeID);
  let selfRecipe = {};
  result.forEach((element) => {
    if (element.previewInfo.id === recipeID) {
      selfRecipe = element;
    }
  });
  return selfRecipe;
};

exports.getUserFamilyRecipes = async function (userID) {
  return await DB.getUserFamilyRecipes(userID);
};

exports.getUserFamilyRecipe = async function (userID, recipeID) {
  let result = await DB.getUserFamilyRecipes(userID, recipeID);
  let familyRecipe = {};
  result.forEach((element) => {
    if (element.previewInfo.ID === recipeID) {
      familyRecipe = element;
    }
  });
  return familyRecipe;
};

exports.addUserWatchedRecipe = async function (userID, recipeID) {
  let exists = await DB.getUserRecipeWatch(userID, recipeID);
  if (exists) {
    await DB.updateUserRecipeWatched(userID, recipeID, new Date());
  } else {
    await DB.insertUserRecipeWatched(userID, recipeID, new Date());
  }
};

exports.addUserLikedRecipe = async function (userID, recipeID, like) {
  let exists = await DB.getUserRecipeWatch(userID, recipeID);
  if (exists) {
    await DB.updateUserRecipeSaved(userID, recipeID, like);
  } else {
    await DB.insertUserRecipeSaved(userID, recipeID);
  }
};
