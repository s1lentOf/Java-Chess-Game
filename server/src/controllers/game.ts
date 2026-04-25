import { Request, Response, NextFunction } from "express";
import { spawn, ChildProcessWithoutNullStreams } from "child_process";
import readline from "readline";

const STOCKFISH_PATH = "/opt/homebrew/bin/stockfish";

type Engine = {
  process: ChildProcessWithoutNullStreams;
  rl: readline.Interface;
};

let engine: Engine | null = null;
let moveHistory: string[] = [];

function send(cmd: string): void {
  if (!engine) throw new Error("Engine is not running");
  engine.process.stdin.write(cmd + "\n");
}

function waitFor(prefix: string): Promise<string> {
  return new Promise((resolve, reject) => {
    if (!engine) return reject(new Error("Engine is not running"));
    const rl = engine.rl;
    const onLine = (line: string) => {
      if (line.startsWith(prefix)) {
        rl.off("line", onLine);
        resolve(line);
      }
    };
    rl.on("line", onLine);
  });
}

export const startGame = async (
  req: Request,
  res: Response,
  next: NextFunction,
) => {
  if (engine) {
    return res.status(409).json({ message: "A game is already running." });
  }

  const requestedLevel = Number(req.body?.level ?? 20);
  const level = Math.max(0, Math.min(20, Math.round(requestedLevel)));

  const child = spawn(STOCKFISH_PATH);
  const rl = readline.createInterface({ input: child.stdout });
  engine = { process: child, rl };
  moveHistory = [];

  child.on("exit", () => {
    engine = null;
  });

  send("uci");
  await waitFor("uciok");
  send(`setoption name Skill Level value ${level}`);
  send("isready");
  await waitFor("readyok");
  send("ucinewgame");

  res.status(200).json({ message: "Engine started and ready.", level });
};

export const stopGame = async (
  req: Request,
  res: Response,
  next: NextFunction,
) => {
  if (!engine) {
    return res.status(409).json({ message: "No game is running." });
  }

  send("quit");
  engine.process.kill();
  engine = null;
  moveHistory = [];

  res.status(200).json({ message: "Engine stopped." });
};

export const requestNextMove = async (
  req: Request,
  res: Response,
  next: NextFunction,
) => {
  if (!engine) {
    return res.status(409).json({ message: "No game is running." });
  }

  const userMove: string | undefined = req.body?.move;
  const movetime: number = req.body?.movetime ?? 500;

  if (userMove) moveHistory.push(userMove);

  const positionCmd =
    moveHistory.length > 0
      ? `position startpos moves ${moveHistory.join(" ")}`
      : "position startpos";

  send(positionCmd);
  send(`go movetime ${movetime}`);
  const line = await waitFor("bestmove");
  const bestmove = line.split(" ")[1];

  if (bestmove && bestmove !== "(none)") moveHistory.push(bestmove);

  res.status(200).json({ bestmove, history: moveHistory });
};