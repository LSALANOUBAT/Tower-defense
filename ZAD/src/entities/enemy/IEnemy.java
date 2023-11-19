package entities.enemy;

public interface IEnemy {

    default void setStartHealth(){};
    default void hurt(int dmg) {};

    default void kill() {};

    default void slow() {};
    default void updateHitbox() {};
    default void move(float speed, int dir) {};
    default void setPos(int x, int y) {};

    default void setLastDir(int newDir) {};


}
