package Services;

import Constants.ColorForChessPieces;

import javax.swing.*;

public class GameManager {
    private GameService gameService;
    private BotService botService;
    private ColorForChessPieces botColor;



    public GameManager(GameService gameService) {
        this(gameService, null, null);
    }

    public GameManager(GameService gameService, BotService botService, ColorForChessPieces humanColor) {
        this.gameService = gameService;
        this.botService = botService;
        this.botColor = (humanColor == null) ? null
                : (humanColor == ColorForChessPieces.WHITE ? ColorForChessPieces.BLACK : ColorForChessPieces.WHITE);
    }

    public GameService getGameService() {
        return gameService;
    }

    public boolean isBotGame(){
        return botService!=null;
    }

    public ColorForChessPieces getBotColor(){
        return botColor;
    }


    public void start() {
        if(botService!=null){
            botService.startSession();
            if(botColor==ColorForChessPieces.WHITE){

                playBotMove();
            }
        }
    }


    public void playBotMove(){
        if(isGameOver()){
            return;
        }
        new SwingWorker<EngineMove, Void>() {
            @Override
            protected EngineMove doInBackground() {
                return botService.getBestMove(gameService.getMoveStorage());
            }
            @Override
            protected void done() {
                try{
                    EngineMove move = get();
                    if(move!=null){
                        gameService.applyBotMove(move);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }.execute();
    }

    public void onHumanMove(){
        if(isGameOver()){
            return;
        }
        if(isBotGame()){
            playBotMove();
        }
    }

    public void stop(){
        if(isBotGame()){
            botService.endSession();
        }
    }

    public boolean isGameOver(){
        return gameService.isCheckmate() || gameService.isDraw();
    }

}
