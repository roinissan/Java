const bcrypt = require("bcrypt");
const DB = require("../controllers/DB");

exports.register = async function (requestBody) {
  let registerErrors = [];
  let query = "";
  let userDS = null;
  if (!checkRegisterFieldsFilled(requestBody)) {
    registerErrors.push("Please fill all fields");
  }
  if (!checkPasswordsMatch(requestBody)) {
    registerErrors.push("Password and password confirm does not match");
  }
  try {
    userDS = await DB.getUserByName(requestBody.username);
    if (userDS) {
      registerErrors.push("User with this user name is already exist");
    }
  } catch (err) {
    registerErrors.push("server error");
  }

  if (registerErrors.length > 0) {
    return { success: false, messages: registerErrors };
  } else {
    let pass_hash = bcrypt.hashSync(requestBody.password, 10);
    try {
      userDS = await DB.insertUser(
        requestBody.username,
        pass_hash,
        requestBody.firstname,
        requestBody.lastname,
        requestBody.country,
        requestBody.email,
        requestBody.userImage
      );
      return { success: true, messages: ["New user added successfully"] };
    } catch (error) {
      throw error;
    }
  }
};

exports.login = async function (requestBody) {
  let loginError = null;
  if (!checkLoginFieldsFilled(requestBody)) {
    return {
      success: false,
      status: 409,
      messages: ["Please fill all fields"],
    };
  } else {
    let user = await DB.getUserByName(requestBody.username);
    console.log(requestBody.password, requestBody.username);
    console.log(user);
    if (
      !user ||
      user.length == 0 ||
      !bcrypt.compareSync(requestBody.password, user.Password)
    ) {
      return {
        success: false,
        status: 401,
        messages: ["Wrong Username or Password"],
      };
    } else {
      return {
        messages: ["User logged in successfuly"],
        success: true,
        userID: user.UserID,
        userImg: user.ImageURL,
      };
    }
  }
};

let checkRegisterFieldsFilled = function (user) {
  if (
    user.username == "" ||
    user.password == "" ||
    user.passwordConfirmation == "" ||
    user.firstname == "" ||
    user.lastname == "" ||
    user.country == "" ||
    user.email == "" ||
    !user.userImage
  ) {
    return false;
  }
  return true;
};

let checkLoginFieldsFilled = function (user) {
  if (user.username == "" || user.password == "") {
    return false;
  }
  return true;
};

let checkPasswordsMatch = function (user) {
  if (user.password != user.passwordConfirmation) {
    return false;
  }
  return true;
};
