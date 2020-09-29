require("dotenv").config();
const sql = require("mssql");

const config = {
  user: process.env.tedious_userName,
  password: process.env.tedious_password,
  server: process.env.tedious_server,
  database: process.env.tedious_database,
  connectionTimeout: 1500000,
  options: {
    encrypt: true,
    enableArithAbort: true,
  },
};

const pool = new sql.ConnectionPool(config);
const poolConnect = pool
  .connect()
  .then(() => console.log("new connection pool Created"))
  .catch((err) => console.log(err));

exports.execQuery = async function (query) {
  await poolConnect;
  try {
    var result = await pool.request().query(query);
    return result.recordset;
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.getUser = async function (userID) {
  await poolConnect;
  query = `SELECT * from Users WHERE UserID = '${userID}'`;
  try {
    let result = await pool.request().query(query);
    return result.recordset[0];
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.getUserByName = async function (userName) {
  await poolConnect;
  query = `SELECT * from Users WHERE UserName = '${userName}'`;
  console.log(query);
  try {
    let result = await pool.request().query(query);
    return result.recordset[0];
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.getUsers = async function () {
  await poolConnect;
  query = "SELECT UserName from Users";
  try {
    let result = await pool.request().query(query);
    return result.recordset;
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.insertUser = async function (
  userName,
  password,
  firstName,
  lastName,
  country,
  gmail,
  imageURL
) {
  await poolConnect;
  query = `INSERT INTO [dbo].[Users]
                ([UserName]
                ,[Password]
                ,[FirstName]
                ,[LastName]
                ,[Country]
                ,[Email]
                ,[ImageURL])
            VALUES
                ('${userName}'
                ,'${password}'
                ,'${firstName}'
                ,'${lastName}'
                ,'${country}'
                ,'${gmail}'
                ,'${imageURL}')`;
  try {
    let result = await pool.request().query(query);
    return result.recordset;
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.getUserSaveWatchRecipes = async function (userID, recipesIDS) {
  await poolConnect;
  query =
    " SELECT [RecipeID],[Watched],[Saved] FROM [dbo].[UserSaveWatchRecipes] where ";
  if (recipesIDS && recipesIDS.length > 0) {
    recipesIDS.forEach((element) => {
      query = query + `( UserID = '${userID}' and RecipeID = ${element}) or `;
    });
    query = query.substring(0, query.lastIndexOf("o"));
  } else {
    query = query + ` UserID = '${userID}'`;
  }
  try {
    let result = await pool.request().query(query);
    return result.recordset;
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.getUserFavourites = async function (userID) {
  await poolConnect;
  query = `SELECT [RecipeID] FROM [dbo].[UserSaveWatchRecipes] where [UserID] = '${userID}' and [Saved] = 1`;
  try {
    let result = await pool.request().query(query);
    return result.recordset;
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.getUserWatch = async function (userID) {
  await poolConnect;
  query = `SELECT [RecipeID] FROM [dbo].[UserSaveWatchRecipes]  where [UserID] = '${userID}' and [Watched] = 1`;
  try {
    let result = await pool.request().query(query);
    return result.recordset;
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.getUserRecipeWatch = async function (userID, recipeID) {
  await poolConnect;
  query = `SELECT * FROM [dbo].[UserSaveWatchRecipes]  where [UserID] = '${userID}' and [RecipeID] = ${recipeID}`;
  try {
    let result = await pool.request().query(query);
    return result.recordset[0];
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.getUserLastWatch = async function (userID, numOfRec) {
  await poolConnect;
  query = `SELECT TOP(${numOfRec}) [RecipeID],[WatchedDate] FROM [dbo].[UserSaveWatchRecipes] where [UserID] = '${userID}' and [Watched] = 1 ORDER BY [WatchedDate] DESC`;
  try {
    let result = await pool.request().query(query);
    return result.recordset;
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.getUserSelfRecipes = async function (userID) {
  await poolConnect;
  query = `SELECT [UserID],[RecipeID],[RecipeName],[ImageURL],[TimeToCook],[Popularity],[Vegan]
          ,[vegetarian],[GlutenFree],[Instructions],[NumberOfPlates] FROM [dbo].[SelfRecipes]
          WHERE [UserID] = '${userID}'`;
  try {
    let queryResult = await pool.request().query(query);
    let promises = [];
    let newQuery = null;
    let result = {};
    let promisesRes = null;
    queryResult.recordset.forEach((element) => {
      result[element.RecipeID] = {
        previewInfo: {
          id: element.RecipeID,
          title: element.RecipeName,
          image: element.ImageURL,
          readyInMinutes: element.TimeToCook,
          aggregateLikes: element.Popularity,
          vegetarian: element.Vegetarian,
          vegan: element.Vegan,
          glutenFree: element.GlutenFree,
        },
      };

      result[element.RecipeID].instructions = element.Instructions;
      result[element.RecipeID].numOfPlates = element.NumberOfPlates;

      newQuery = `SELECT [RecipeID],[IngridientName] as 'name',[IngridientAmount] as 'amount'
        FROM [dbo].[SelfRecipesIngridients] WHERE [UserID] = '${userID}' and [RecipeID] = ${element.RecipeID}`;

      promises.push(pool.request().query(newQuery));
    });
    promisesRes = await Promise.all(promises);

    promisesRes.forEach((recipe) => {
      result[recipe.recordset[0].RecipeID].ingridients = recipe.recordset;
    });

    return Object.keys(result).map((key) => result[key]);
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.getUserFamilyRecipes = async function (userID) {
  await poolConnect;
  query = `SELECT [UserID],[RecipeID],[RecipeName],[ImageURL],[TimeToCook],[Vegan]
          ,[vegetarian],[GlutenFree],[Instructions],[NumberOfPlates],[Creator],[CustomDate] FROM [dbo].[FamilyRecipes]
          WHERE [UserID] = '${userID}'`;
  try {
    let result = {};
    let queryResult = await pool.request().query(query);
    let promisesRes = null;
    let promises = [];
    let newQuery = null;
    queryResult.recordset.forEach((element) => {
      result[element.RecipeID] = {
        previewInfo: {
          ID: element.RecipeID,
          title: element.RecipeName,
          image: element.ImageURL,
          readyInMinutes: element.TimeToCook,
          vegetarian: element.Vegetarian,
          vegan: element.Vegan,
          glutenFree: element.GlutenFree,
          creator: element.Creator,
          customDate: element.CustomDate,
        },
      };

      result[element.RecipeID].instructions = element.Instructions;
      result[element.RecipeID].numOfPlates = element.NumberOfPlates;

      newQuery = `SELECT [RecipeID],[IngridientName] as 'name',[IngridientAmount] as 'amount'
        FROM [dbo].[FamilyRecipesIngridients] WHERE [UserID] = '${userID}' and [RecipeID] = ${element.RecipeID}`;

      promises.push(pool.request().query(newQuery));
    });
    promisesRes = await Promise.all(promises);

    promisesRes.forEach((recipe) => {
      result[recipe.recordset[0].RecipeID].ingridients = recipe.recordset;
    });
    return Object.keys(result).map((key) => result[key]);
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.updateUserRecipeWatched = async function (
  userID,
  recipeID,
  watchedDate
) {
  await poolConnect;
  let date = watchedDate.toISOString().slice(0, 19).replace("T", " ");
  query = `UPDATE [dbo].[UserSaveWatchRecipes] SET [WatchedDate] = '${date}',[Watched] = 1
          WHERE [UserID] = '${userID}' and [RecipeID] = ${recipeID}`;
  try {
    await pool.request().query(query);
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.insertUserRecipeWatched = async function (
  userID,
  recipeID,
  watchedDate
) {
  await poolConnect;
  let date = watchedDate.toISOString().slice(0, 19).replace("T", " ");
  query = `INSERT INTO [dbo].[UserSaveWatchRecipes] ([UserID],[RecipeID],[WatchedDate],[Watched],[Saved])
            VALUES ('${userID}',${recipeID},'${date}',1,0)`;
  try {
    await pool.request().query(query);
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.updateUserRecipeSaved = async function (userID, recipeID, like) {
  await poolConnect;
  query = `UPDATE [dbo].[UserSaveWatchRecipes] SET [Saved] = '${like}'
          WHERE [UserID] = '${userID}' and [RecipeID] = ${recipeID}`;
  try {
    await pool.request().query(query);
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};

exports.insertUserRecipeSaved = async function (userID, recipeID) {
  await poolConnect;
  let date = new Date().toISOString().slice(0, 19).replace("T", " ");
  query = `INSERT INTO [dbo].[UserSaveWatchRecipes] ([UserID],[RecipeID],[WatchedDate],[Watched],[Saved])
            VALUES ('${userID}',${recipeID},'${date}',0,1)`;
  try {
    await pool.request().query(query);
  } catch (err) {
    console.error("SQL error", err);
    throw err;
  }
};
