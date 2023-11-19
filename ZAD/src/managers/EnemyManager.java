package managers;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.enemy.*;
import helperMethods.LoadSave;
import helperMethods.Utilz;
import objects.PathPoint;
import scenes.Playing;

import static helperMethods.Constants.Direction.*;
import static helperMethods.Constants.Tiles.*;
import static helperMethods.Constants.Enemies.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[] enemyImgs;
    private ArrayList<AEnemy> enemies = new ArrayList<>();
    private PathPoint start, end;
    private int HPbarWidth = 20;
    private BufferedImage slowEffect;
    private int[][] roadDirArr;

    public EnemyManager(Playing playing, PathPoint start, PathPoint end) {
        this.playing = playing;
        enemyImgs = new BufferedImage[4];
        this.start = start;
        this.end = end;

        loadEffectImg();
        loadEnemyImgs();
        loadRoadDirArr();

    }

    private void loadRoadDirArr() {
        roadDirArr = Utilz.GetRoadDirArr(playing.getGame().getTileManager().getTypeArr(), start, end);

    }


    private void loadEffectImg() {
        slowEffect = LoadSave.getSpriteAtlas().getSubimage(75 * 7, 0, 75, 75);
    }

    private void loadEnemyImgs() {
        BufferedImage atlas = LoadSave.getSpriteAtlas();
        for (int i = 0; i < 4; i++)
            enemyImgs[i] = atlas.getSubimage(i * 75, 75, 75, 75);

    }

    public void update() {
        for (AEnemy e : enemies)
            if (e.isAlive()) {
                updateEnemyMoveNew(e);
            }

    }

    private void updateEnemyMoveNew(AEnemy e) {
        PathPoint currTile = getEnemyTile(e);
        int dir = roadDirArr[currTile.getyCord()][currTile.getxCord()];

        e.move(GetSpeed(e.getEnemyType()), dir);

        PathPoint newTile = getEnemyTile(e);

        if (!isTilesTheSame(currTile, newTile)) {
            if (isTilesTheSame(newTile, end)) {
                e.kill();
                playing.removeOneLife();
                return;
            }
            int newDir = roadDirArr[newTile.getyCord()][newTile.getxCord()];
            if (newDir != dir) {
                e.setPos(newTile.getxCord() * 75, newTile.getyCord() * 75);
                e.setLastDir(newDir);
            }
        }

    }

    private PathPoint getEnemyTile(AEnemy e) {

        switch (e.getLastDir()) {
            case LEFT:
                return new PathPoint((int) ((e.getX() + 74) / 75), (int) (e.getY() / 75));
            case UP:
                return new PathPoint((int) (e.getX() / 75), (int) ((e.getY() + 74) / 75));
            case RIGHT:
            case DOWN:
                return new PathPoint((int) (e.getX() / 75), (int) (e.getY() / 75));

        }

        return new PathPoint((int) (e.getX() / 75), (int) (e.getY() / 75));
    }

    private boolean isTilesTheSame(PathPoint currTile, PathPoint newTile) {
        if (currTile.getxCord() == newTile.getxCord())
            if (currTile.getyCord() == newTile.getyCord())
                return true;
        return false;
    }

    public void updateEnemyMove(AEnemy e) {
        if (e.getLastDir() == -1)
            setNewDirectionAndMove(e);

        int newX = (int) (e.getX() + getSpeedAndWidth(e.getLastDir(), e.getEnemyType()));
        int newY = (int) (e.getY() + getSpeedAndHeight(e.getLastDir(), e.getEnemyType()));

        if (getTileType(newX, newY) == ROAD_TILE) {
            e.move(GetSpeed(e.getEnemyType()), e.getLastDir());
        } else if (isAtEnd(e)) {
            e.kill();
            playing.removeOneLife();
        } else {
            setNewDirectionAndMove(e);
        }
    }

    private void setNewDirectionAndMove(AEnemy e) {
        int dir = e.getLastDir();

        int xCord = (int) (e.getX() / 75);
        int yCord = (int) (e.getY() / 75);

        fixEnemyOffsetTile(e, dir, xCord, yCord);

        if (isAtEnd(e))
            return;

        if (dir == LEFT || dir == RIGHT) {
            int newY = (int) (e.getY() + getSpeedAndHeight(UP, e.getEnemyType()));
            if (getTileType((int) e.getX(), newY) == ROAD_TILE)
                e.move(GetSpeed(e.getEnemyType()), UP);
            else
                e.move(GetSpeed(e.getEnemyType()), DOWN);
        } else {
            int newX = (int) (e.getX() + getSpeedAndWidth(RIGHT, e.getEnemyType()));
            if (getTileType(newX, (int) e.getY()) == ROAD_TILE)
                e.move(GetSpeed(e.getEnemyType()), RIGHT);
            else
                e.move(GetSpeed(e.getEnemyType()), LEFT);

        }

    }

    private void fixEnemyOffsetTile(AEnemy e, int dir, int xCord, int yCord) {
        switch (dir) {
            case RIGHT:
                if (xCord < 14)
                    xCord++;
                break;
            case DOWN:
                if (yCord < 9)
                    yCord++;
                break;
        }

        e.setPos(xCord * 75, yCord * 75);

    }

    private boolean isAtEnd(AEnemy e) {
        if (e.getX() == end.getxCord() * 75)
            if (e.getY() == end.getyCord() * 75)
                return true;
        return false;
    }

    private int getTileType(int x, int y) {
        return playing.getTileType(x, y);
    }

    private float getSpeedAndHeight(int dir, int enemyType) {
        if (dir == UP)
            return -GetSpeed(enemyType);
        else if (dir == DOWN)
            return GetSpeed(enemyType) + 75;

        return 0;
    }

    private float getSpeedAndWidth(int dir, int enemyType) {
        if (dir == LEFT)
            return -GetSpeed(enemyType);
        else if (dir == RIGHT)
            return GetSpeed(enemyType) + 75;

        return 0;
    }

    public void spawnEnemy(int nextEnemy) {
        addEnemy(nextEnemy);
    }

    public void addEnemy(int enemyType) {

        int x = start.getxCord() * 75;
        int y = start.getyCord() * 75;

        switch (enemyType) {
            case COP:
                enemies.add(new Cop(x, y, 0, this));
                break;
            case SWAT:
                enemies.add(new Swat(x, y, 0, this));
                break;
            case CAR:
                enemies.add(new Car(x, y, 0, this));
                break;
        }

    }

    public void draw(Graphics g) {
        for (AEnemy e : enemies) {
            if (e.isAlive()) {
                drawEnemy(e, g);
                drawHealthBar(e, g);
                drawEffects(e, g);
            }
        }
    }

    private void drawEffects(AEnemy e, Graphics g) {
        if (e.isSlowed())
            g.drawImage(slowEffect, (int) e.getX(), (int) e.getY(), null);

    }

    private void drawHealthBar(AEnemy e, Graphics g) {
        g.setColor(Color.red);
        g.fillRect((int) e.getX() + 30 - (getNewBarWidth(e) / 2), (int) e.getY() - 10, getNewBarWidth(e) + 30, 5);

    }

    private int getNewBarWidth(AEnemy e) {
        return (int) (HPbarWidth * e.getHealthBarFloat());
    }

    private void drawEnemy(AEnemy e, Graphics g) {
        g.drawImage(enemyImgs[e.getEnemyType()], (int) e.getX(), (int) e.getY(), null);
    }

    public ArrayList<AEnemy> getEnemies() {
        return enemies;
    }

    public int getAmountOfAliveEnemies() {
        int size = 0;
        for (AEnemy e : enemies)
            if (e.isAlive())
                size++;

        return size;
    }

    public void rewardPlayer(int enemyType) {
        playing.rewardPlayer(enemyType);
    }

    public void reset() {
        enemies.clear();
    }

}
