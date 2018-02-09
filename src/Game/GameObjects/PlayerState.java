package Game.GameObjects;

public enum PlayerState {
    PLAYER_IDLING_RIGHT(0, true),
    PLAYER_IDLING_LEFT(1, false),
    PLAYER_RUNNING_RIGHT(2, true),
    PLAYER_RUNNING_LEFT(3, false),
    PLAYER_JUMPING_RIGHT(4, true),
    PLAYER_JUMPING_LEFT(5, false),
    PLAYER_FALLING_RIGHT(6, true),
    PLAYER_FALLING_LEFT(7, false);

    private int id;
    private boolean isRight;

    PlayerState(int id, boolean isRight){
        this.id = id;
        this.isRight = isRight;
    }

    public int getId() {
        return id;
    }

    public boolean isRight() {
        return isRight;
    }
}
