require("dotenv").config();

const express = require("express");
const path = require("path");
const logger = require("morgan");
const session = require("client-sessions");
const DB = require("./controllers/DB");
const cors = require("cors");

const auth = require("./routes/auth");
const user = require("./routes/user");
const recipe = require("./routes/recipe");
const info = require("./routes/info");

const app = express();
const port = process.env.PORT || "3003";
const corsConfig = {
  origin: true,
  credentials: true,
};

app.use(cors(corsConfig));
app.options("*", cors(corsConfig));
app.use(logger("dev"));
app.use(express.json());

app.use(
  session({
    cookieName: "session",
    secret: process.env.COOKIE_SECRET,
    duration: 4 * 60 * 60 * 1000,
    activeDuration: 5 * 60 * 1000,
    cookie: {
      httpOnly: false,
    },
  })
);
app.use(express.urlencoded({ extended: false }));

app.use(function (req, res, next) {
  if (req.session && req.session.userID) {
    DB.getUser(req.session.userID)
      .then((user) => {
        if (user) {
          req.userID = req.session.userID;
        }
        next();
      })
      .catch((error) => next());
  } else {
    next();
  }
});

app.get("/", (req, res) => {
  res.status(200).send({ message: ["RecipeSite"], success: true });
});

app.use("/auth", auth);
app.use("/user", user);
app.use("/recipe", recipe);
app.use("/info", info);

app.use((err, req, res, next) => {
  console.error(err.message);
  res.status(err.status || 500).send({ result: ["Error"], success: false });
});

const server = app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});

process.on("SIGINT", () => {
  if (server) {
    server.close(() => console.log("server closed"));
  }
  process.exit();
});
