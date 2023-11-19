package managers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.enemy.AEnemy;
import helperMethods.LoadSave;
import objects.Projectile;
import entities.tower.ATower;
import scenes.Playing;

import static helperMethods.Constants.Towers.*;
import static helperMethods.Constants.Projectiles.*;

public class ProjectileManager {

    private Playing playing;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private ArrayList<Explosion> explosions = new ArrayList<>();
    private BufferedImage[] proj_imgs, explo_imgs;
    private int proj_id = 0;

    // Temp variables
    private boolean callTrue;
    private long lastCall;

    public ProjectileManager(Playing playing) {
        this.playing = playing;
        importImgs();

    }

    private void importImgs() {
        BufferedImage atlas = LoadSave.getSpriteAtlas();
        proj_imgs = new BufferedImage[3];

        for (int i = 0; i < 3; i++)
            proj_imgs[i] = atlas.getSubimage((6 + i) * 75, 0, 75, 75);
        importExplosion(atlas);
    }

    private void importExplosion(BufferedImage atlas) {
        explo_imgs = new BufferedImage[7];

        for (int i = 0; i < 7; i++)
            explo_imgs[i] = atlas.getSubimage(i * 75, 75 * 2, 75, 75);

    }

    public void newProjectile(ATower t, AEnemy e) {
        int type = getProjType(t);

        int xDist = (int) (t.getX() - e.getX());
        int yDist = (int) (t.getY() - e.getY());
        int totDist = Math.abs(xDist) + Math.abs(yDist);

        float xPer = (float) Math.abs(xDist) / totDist;

        float xSpeed = xPer * helperMethods.Constants.Projectiles.GetSpeed(type);
        float ySpeed = helperMethods.Constants.Projectiles.GetSpeed(type) - xSpeed;

        if (t.getX() > e.getX())
            xSpeed *= -1;
        if (t.getY() > e.getY())
            ySpeed *= -1;

        float rotate = 0;

        if (type == ARROW) {
            float arcValue = (float) Math.atan(yDist / (float) xDist);
            rotate = (float) Math.toDegrees(arcValue);

            if (xDist < 0)
                rotate += 180;
        }

        for (Projectile p : projectiles)
            if (!p.isActive())
                if (p.getProjectileType() == type) {
                    p.reuse(t.getX() + 37, t.getY() + 37, xSpeed, ySpeed, t.getDmg(), rotate);
                    return;
                }

        projectiles.add(new Projectile(t.getX() + 37, t.getY() + 37, xSpeed, ySpeed, t.getDmg(), rotate, proj_id++, type));

    }

    public void update() {
        for (Projectile p : projectiles)
            if (p.isActive()) {
                p.move();
                if (isProjHittingEnemy(p)) {
                    p.setActive(false);
                    if (p.getProjectileType() == BOMB) {
                        explosions.add(new Explosion(p.getPos()));
                        explodeOnEnemies(p);
                    }
                } else if (isProjOutsideBounds(p)) {
                    p.setActive(false);
                }
            }

        for (Explosion e : explosions)
            if (e.getIndex() < 7)
                e.update();

    }

    private void explodeOnEnemies(Projectile p) {
        for (AEnemy e : playing.getEnemyManger().getEnemies()) {
            if (e.isAlive()) {
                float radius = 40.0f;

                float xDist = Math.abs(p.getPos().x - e.getX());
                float yDist = Math.abs(p.getPos().y - e.getY());

                float realDist = (float) Math.hypot(xDist, yDist);

                if (realDist <= radius)
                    e.hurt(p.getDmg());
            }

        }

    }

    private boolean isProjHittingEnemy(Projectile p) {
        for (AEnemy e : playing.getEnemyManger().getEnemies()) {
            if (e.isAlive())
                if (e.getBounds().contains(p.getPos())) {
                    e.hurt(p.getDmg());
                    if (p.getProjectileType() == CHAINS)
                        e.slow();

                    return true;
                }
        }
        return false;
    }

    private boolean isProjOutsideBounds(Projectile p) {
        if (p.getPos().x >= 0)
            if (p.getPos().x <= 750)
                if (p.getPos().y >= 0)
                    if (p.getPos().y <= 910)
                        return false;
        return true;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (Projectile p : projectiles)
            if (p.isActive()) {
                if (p.getProjectileType() == ARROW) {
                    g2d.translate(p.getPos().x, p.getPos().y);
                    g2d.rotate(Math.toRadians(p.getRotation()));
                    g2d.drawImage(proj_imgs[p.getProjectileType()], -37, -37, null);
                    g2d.rotate(-Math.toRadians(p.getRotation()));
                    g2d.translate(-p.getPos().x, -p.getPos().y);
                } else {
                    g2d.drawImage(proj_imgs[p.getProjectileType()], (int) p.getPos().x - 37, (int) p.getPos().y - 37, null);
                }
            }

        drawExplosions(g2d);

    }

    private void drawExplosions(Graphics2D g2d) {
        for (Explosion e : explosions)
            if (e.getIndex() < 7)
                g2d.drawImage(explo_imgs[e.getIndex()], (int) e.getPos().x- 75, (int) e.getPos().y -75, null);
    }

    private int getProjType(ATower t) {
        switch (t.getTowerType()) {
            case ARCHER:
                return ARROW;
            case CANNON:
                return BOMB;
            case WIZARD:
                return CHAINS;
        }
        return 0;
    }

    public class Explosion {

        private Point2D.Float pos;
        private int exploTick, exploIndex;

        public Explosion(Point2D.Float pos) {
            this.pos = pos;
        }

        public void update() {
            exploTick++;
            if (exploTick >= 6) {
                exploTick = 0;
                exploIndex++;
            }
        }

        public int getIndex() {
            return exploIndex;
        }

        public Point2D.Float getPos() {
            return pos;
        }
    }

    public void reset() {
        projectiles.clear();
        explosions.clear();

        proj_id = 0;
    }

}
