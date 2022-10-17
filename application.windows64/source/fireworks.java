import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class fireworks extends PApplet {

float gravity = .15f;
ArrayList<Particle> particles = new ArrayList<Particle>();
ArrayList<Firework> fireworks = new ArrayList<Firework>();

public void setup() {
  
  noStroke();
}

public void draw() {
  background(0);
  garbageCollection();
  if (random(1) < 0.08f) {
    fireworks.add(new FireworkPrismatic(random(width), height + 50, random(10) - 5, -1 * random(25), 255, 255, 255, 100, 12, 8));
  }
  if (random(1) < 0.08f) {
    colorMode(HSB, 255);
    int iclr = color(random(255), 255, 255);
    fireworks.add(new FireworkSparkler(random(width), height + 50, random(10) - 5, -1 * random(25), iclr, 30, 8, 3));
  }
  //saveFrame();
}
public void garbageCollection() {
  for (Particle p : particles) {
    p.move();
  }
  for (Firework f : fireworks) {
    f.move();
  }
  for (Particle p : particles){
    p.render();
    p.renderEffect();
  }
  for(Firework f : fireworks){
    f.render();
    f.renderEffect();
  }
  for (int i = 0; i < particles.size(); i ++) {
    if (!particles.get(i).active) {
      particles.remove(i);
      i--;
    }
  }

  for (int i = 0; i < fireworks.size(); i ++) {
    if (!fireworks.get(i).active) {
      fireworks.remove(i);
      i--;
    }
  }
}
class Firework {
  float x, y, dx, dy;
  boolean active;

  Firework(float ix, float iy, float idx, float idy) {
    x = ix;
    y = iy;
    dx = idx;
    dy = idy;
    active = true;
  }

  public void render() {
    colorMode(RGB, 255);
    fill(255);
    ellipse(x, y, 10, 10);
  }
  
  public void renderEffect(){}

  public void move() {
    x += dx;
    y += dy;
  }
}

class FireworkBasic extends Firework {
  int r, g, b, children;
  float cs, speed;
  FireworkBasic(float ix, float iy, float idx, float idy, int ir, int ig, int ib, int ichildren, float ics, float ispeed) {
    super(ix, iy, idx, idy);
    r = ir;
    g = ig;
    b = ib;
    children = ichildren;
    cs = ics;
    speed = ispeed;
  }

  public void render() {
    colorMode(RGB, 255);
    fill(r, g, b);
    ellipse(x, y, 10, 10);
  }

  public void move() {
    if (x < -100 || x > width + 100 || y > height + 100) {
      active = false;
    }
    if (dy >= 0) {
      explode();
    }
    x += dx;
    y += dy;
    dy += gravity;
  }

  public void explode() {
    active = false;
    float angle = random(TAU);
    for (int i = 0; i < children; i ++) {
      float ispeed = random(speed);
      particles.add(new ParticleFalling(x, y, dx + ispeed*cos(angle + TAU * i / children), dy + ispeed*sin(angle + TAU * i / children), cs, r, g, b, 10));
    }
  }
}

class FireworkPrismatic extends FireworkBasic {
  FireworkPrismatic(float ix, float iy, float idx, float idy, int ir, int ig, int ib, int ichildren, float ics, float ispeed) {
    super(ix, iy, idx, idy, ir, ig, ib, ichildren, ics, ispeed);
  }

  public void explode() {
    active = false;
    float angle = random(TAU);
    float iclrAngle = random(255);
    for (int i = 0; i < children; i ++) {
      float ispeed = random(speed);
      colorMode(HSB, 255);
      int iclr = color((iclrAngle + ispeed * 10) % 255, 255, 255);
      particles.add(new ParticleFalling(x, y, dx + ispeed*cos(angle + TAU * i / children), dy + ispeed*sin(angle + TAU * i / children), cs, iclr, 10));
    }
  }
}

class FireworkSparkler extends Firework{
  int clr;
  float children, childSize, childSpeed;
  
  FireworkSparkler(float ix, float iy, float idx, float idy, int iclr, int ichildren, float ics, float ispeed) {
    super(ix, iy, idx, idy);
    clr = iclr;
    children = ichildren;
    childSize = ics;
    childSpeed = ispeed;
  }
  
  public void renderEffect(){
    colorMode(RGB, 255);
    fill(255);
    ellipse(x, y, 8, 8);
  }
  
  public void move(){
    for(int i = 0; i < children; i ++){
      if(random(1) < .05f){
        float angle = random(TAU);
        particles.add(new ParticleSparkler(x + dx + childSpeed*cos(angle + TAU * i / children), y + dy + childSpeed * sin(angle + TAU * i / children), dx + childSpeed*cos(angle + TAU * i / children), dy + childSpeed * sin(angle + TAU * i / children), childSize, clr, 1));
      }
    }
    colorMode(HSB, 255);
    clr = color(hue(clr) + 1, 255, 255);
    x += dx;
    y += dy;
    dy += gravity;
  }
}
class Particle {
  float x, y, dx, dy, s;
  int r, g, b, trailRes;
  boolean active, useColor;
  int clr;
  float[] xTrail;
  float[] yTrail;
  float timer, timerThreshold;

  Particle(float ix, float iy, float idx, float idy, float is, int ir, int ig, int ib, int it) {
    x = ix;
    y = iy;
    dx = idx;
    dy = idy;
    s = is;
    r = ir;
    g = ig;
    b = ib;
    active = true;
    useColor = false;
    trailRes = it;
    xTrail = new float[trailRes];
    yTrail = new float[trailRes];
    for (int i = 0; i < trailRes; i++) {
      xTrail[i] = x;
      yTrail[i] = y;
    }
    timerThreshold = 20;
  }  

  Particle(float ix, float iy, float idx, float idy, float is, int iclr, int it) {
    x = ix;
    y = iy;
    dx = idx;
    dy = idy;
    s = is;
    clr = iclr;
    active = true;
    useColor = true;
    trailRes = it;
    xTrail = new float[trailRes];
    yTrail = new float[trailRes];
    for (int i = 0; i < trailRes; i++) {
      xTrail[i] = x;
      yTrail[i] = y;
    }
    timerThreshold = 20;
  }

  public void render() {
    if (!useColor) {
      colorMode(RGB, 255);
      fill(r, g, b, 255);
    } else {
      fill(clr, 255);
    }
    float is = timer <= timerThreshold ? s * (timer / timerThreshold) : s;
    for (int i = 0; i < trailRes; i ++) {
      ellipse(xTrail[i], yTrail[i], is * (trailRes - i) / trailRes, is * (trailRes - i) / trailRes);
    }
  }

  public void renderEffect() {
    colorMode(RGB, 255);
    float is = timer <= timerThreshold ? s * (timer / timerThreshold) : s;
    fill(255);
    ellipse(xTrail[0], yTrail[0], is/1.5f, is/1.5f);
  }

  public void move() {
  }
}

class ParticleFalling extends Particle {
  ParticleFalling(float ix, float iy, float idx, float idy, float is, int ir, int ig, int ib, int it) {
    super(ix, iy, idx, idy, is, ir, ig, ib, it);
    timer = 100;
  }

  ParticleFalling(float ix, float iy, float idx, float idy, float is, int iclr, int it) {
    super(ix, iy, idx, idy, is, iclr, it);
    timer = 100;
  }

  public void move() {
    if (x < -100 || x > width + 100 || y > height + 100) {
      active = false;
    }

    for (int i = 0; i < trailRes - 1; i ++) {
      xTrail[trailRes - 1 - i] = xTrail[trailRes - 2 - i];
      yTrail[trailRes - 1 - i] = yTrail[trailRes - 2 - i];
    }
    xTrail[0] = x;
    yTrail[0] = y;
    x += dx;
    y += dy;
    dy += gravity / 2;
    if(timer < 0){
      active = false;
    }
    timer --;
  }
}

class ParticleSparkler extends Particle {
  ParticleSparkler(float ix, float iy, float idx, float idy, float is, int iclr, int it) {
    super(ix, iy, idx, idy, is, iclr, it);
    timer = 50;
  }

  public void move() {
    if (x < -100 || x > width + 100 || y > height + 100) {
      active = false;
    }
    for (int i = 0; i < trailRes - 1; i ++) {
      xTrail[trailRes - 1 - i] = xTrail[trailRes - 2 - i];
      yTrail[trailRes - 1 - i] = yTrail[trailRes - 2 - i];
    }
    xTrail[0] = x;
    yTrail[0] = y;
    x += dx;
    dx /= 1.05f;
    y += dy;
    dy += gravity / 5;
    dy /= 1.1f;
    if(timer < 0){
      active = false;
    }
    timer --;
  }
}
  public void settings() {  fullScreen(P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "fireworks" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
