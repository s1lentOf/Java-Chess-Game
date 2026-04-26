import { Router } from "express";
import * as gameController from "../controllers/game.js";

const router = Router();

router.post("/start", gameController.startGame);

router.post("/stop", gameController.stopGame);

router.post("/request", gameController.requestNextMove);

export default router;
