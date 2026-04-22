import { Request, Response, NextFunction } from "express";

// TODO: Start the execution of the stockfish .exe.
export const startGame = async (
  req: Request,
  res: Response,
  next: NextFunction,
) => {
  res.status(200).json({
    message: "Start the game succesfully.",
  });
};

// TODO: Stop the execution of the stockfish .exe.
export const stopGame = async (
  req: Request,
  res: Response,
  next: NextFunction,
) => {
  res.status(200).json({
    message: "Stoped the game succesfully.",
  });
};

// TODO: Get the next move from the stockfish server.
export const requestNextMove = async (
  req: Request,
  res: Response,
  next: NextFunction,
) => {
  res.status(200).json({
    message: "Requested the next move succesfully.",
  });
};
