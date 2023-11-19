package entities.enemy;

import static helperMethods.Constants.Enemies.COP;

import managers.EnemyManager;

public class Cop extends AEnemy {
    public Cop(float x, float y, int ID, EnemyManager em) {
        super(x, y, ID, COP, em);
    }
}
