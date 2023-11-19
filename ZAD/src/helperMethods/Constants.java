package helperMethods;

public class Constants {

    public static class Projectiles {
        public static final int ARROW = 0;
        public static final int CHAINS = 1;
        public static final int BOMB = 2;

        public static float GetSpeed(int type) {
            switch (type) {
                case ARROW:
                    return 4f;
                case BOMB:
                    return 2f;
                case CHAINS:
                    return 1f;
            }
            return 0f;
        }
    }

    public static class Towers {
        public static final int CANNON = 0;
        public static final int ARCHER = 1;
        public static final int WIZARD = 2;

        public static int GetTowerCost(int towerType) {
            switch (towerType) {
                case CANNON:
                    return 65;
                case ARCHER:
                    return 35;
                case WIZARD:
                    return 50;
            }
            return 0;
        }

        public static String GetName(int towerType) {
            switch (towerType) {
                case CANNON:
                    return "Cannon";
                case ARCHER:
                    return "Archer";
                case WIZARD:
                    return "Wizard";
            }
            return "";
        }

        public static int GetStartDmg(int towerType) {
            switch (towerType) {
                case CANNON:
                    return 15;
                case ARCHER:
                    return 5;
                case WIZARD:
                    return 0;
            }

            return 0;
        }

        public static float GetDefaultRange(int towerType) {
            switch (towerType) {
                case CANNON:
                    return 110;
                case ARCHER:
                    return 140;
                case WIZARD:
                    return 130;
            }

            return 0;
        }

        public static float GetDefaultCooldown(int towerType) {
            switch (towerType) {
                case CANNON:
                    return 120;
                case ARCHER:
                    return 35;
                case WIZARD:
                    return 50;
            }

            return 0;
        }
    }

    public static class Direction {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class Enemies {

        public static final int COP = 0;
        public static final int SWAT = 1;
        public static final int CAR = 2;


        public static int GetReward(int enemyType) {
            switch (enemyType) {
                case COP:
                    return 5;
                case SWAT:
                    return 5;
                case CAR:
                    return 25;


            }
            return 0;
        }

        public static float GetSpeed(int enemyType) {
            switch (enemyType) {
                case COP:
                    return 0.5f;
                case SWAT:
                    return 0.7f;
                case CAR:
                    return 0.45f;


            }
            return 0;
        }

        public static int GetStartHealth(int enemyType) {
            switch (enemyType) {
                case COP:
                    return 85;
                case SWAT:
                    return 100;
                case CAR:
                    return 400;
            }
            return 0;
        }
    }

    public static class Tiles {
        public static final int WATER_TILE = 0;
        public static final int GRASS_TILE = 1;
        public static final int ROAD_TILE = 2;
    }

}
