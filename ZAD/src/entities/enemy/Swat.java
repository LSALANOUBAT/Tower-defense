package entities.enemy;

import static helperMethods.Constants.Enemies.SWAT;

import managers.EnemyManager;

public class Swat extends AEnemy {
    public Swat(float x, float y, int ID, EnemyManager em) {
        super(x, y, ID, SWAT, em);
    }
}
