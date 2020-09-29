var express = require("express");
var router = express.Router();
const auth = require("../controllers/auth");

//Register
router.post("/register", async (req, res, next) => {
  try {
    const register = await auth.register(req.body);
    if (register.success)
      res.status(201).send({ result: register.messages, success: true });
    else {
      let registrationError = new Error(register.messages.join(","));
      registrationError.status = 409;
      next(registrationError);
    }
  } catch (error) {
    console.log(error);
    next(error);
  }
});

// Login
router.post("/login", async (req, res, next) => {
  try {
    const login = await auth.login(req.body);
    if (login.success) {
      req.session.userID = login.userID;
      res.status(200).send({ result: login.messages, success: true });
    } else {
      let loginError = new Error(login.messages.join(","));
      loginError.status = login.status;
      next(loginError);
    }
  } catch (error) {
    next(error);
  }
});

//Logout
router.get("/logout", async (req, res, next) => {
  if (req.session && req.session.userID) {
    req.session.reset();
  }
  res
    .status(200)
    .send({ result: ["User logged out successfully"], success: true });
});

module.exports = router;
