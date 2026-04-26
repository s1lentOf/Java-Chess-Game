import bodyParser from "body-parser";
import express from "express";
import gameRoutes from "./routes/game.js";

const app = express();

app.use(bodyParser.json());

app.use("/api", gameRoutes);

app.get("/", (req, res) => {
  res.json({ message: "The server is running" });
});

const PORT = 3000;

app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
