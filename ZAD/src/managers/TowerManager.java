package managers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.enemy.AEnemy;
import entities.tower.TowerDmg;
import entities.tower.TowerExplose;
import entities.tower.TowerSlow;
import helperMethods.LoadSave;
import entities.tower.ATower;
import scenes.Playing;

public class TowerManager {

    private Playing playing;
    private BufferedImage[] towerImgs;
    private ArrayList<ATower> towers = new ArrayList<>();
    private int towerAmount = 0;

    public TowerManager(Playing playing) {
        this.playing = playing;
        loadTowerImgs();
    }

    private void loadTowerImgs() {
        BufferedImage atlas = LoadSave.getSpriteAtlas();
        towerImgs = new BufferedImage[3];
        for (int i = 0; i < 3; i++)
            towerImgs[i] = atlas.getSubimage((3 + i) * 75, 75, 75, 75);
    }

    public void addTower(ATower selectedATower, int xPos, int yPos) {
        switch (selectedATower.getTowerType()) {
            case 0:
                towers.add(new TowerDmg(xPos, yPos, towerAmount++, selectedATower.getTowerType()));
            case 1:
                towers.add(new TowerExplose(xPos, yPos, towerAmount++, selectedATower.getTowerType()));
            case 2:
                towers.add(new TowerSlow(xPos, yPos, towerAmount++, selectedATower.getTowerType()));


        }
    }

    public void removeTower(ATower displayedATower) {
        for (int i = 0; i < towers.size(); i++)
            if (towers.get(i).getId() == displayedATower.getId())
                towers.remove(i);
    }

    public void upgradeTower(ATower displayedATower) {
        for (ATower t : towers)
            if (t.getId() == displayedATower.getId())
                t.upgradeTower();
    }

    public void update() {
        for (ATower t : towers) {
            t.update();
            attackEnemyIfClose(t);
        }
    }

    private void attackEnemyIfClose(ATower t) {
        for (AEnemy e : playing.getEnemyManger().getEnemies()) {
            if (e.isAlive())
                if (isEnemyInRange(t, e)) {
                    if (t.isCooldownOver()) {
                        playing.shootEnemy(t, e);
                        t.resetCooldown();
                    }
                } else {
                    // we do nothing
                }
        }

    }

    private boolean isEnemyInRange(ATower t, AEnemy e) {
        int range = helperMethods.Utilz.GetHypoDistance(t.getX(), t.getY(), e.getX(), e.getY());
        return range < t.getRange();
    }

    public void draw(Graphics g) {
        for (ATower t : towers)
            g.drawImage(towerImgs[t.getTowerType()], t.getX(), t.getY(), null);
    }

    public ATower getTowerAt(int x, int y) {
        for (ATower t : towers)
            if (t.getX() == x)
                if (t.getY() == y)
                    return t;
        return null;
    }

    public BufferedImage[] getTowerImgs() {
        return towerImgs;
    }

    public void reset() {
        towers.clear();
        towerAmount = 0;
    }

}
