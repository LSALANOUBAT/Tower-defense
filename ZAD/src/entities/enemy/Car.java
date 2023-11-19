package entities.enemy;

import static helperMethods.Constants.Enemies.CAR;

import managers.EnemyManager;

public class Car extends AEnemy {

    public Car(float x, float y, int ID, EnemyManager em) {
        super(x, y, ID, CAR, em);

    }

}
