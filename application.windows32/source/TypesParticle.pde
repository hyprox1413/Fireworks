class Particle {
  float x, y, dx, dy, s;
  int r, g, b, trailRes;
  boolean active, useColor;
  color clr;
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

  Particle(float ix, float iy, float idx, float idy, float is, color iclr, int it) {
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

  void render() {
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

  void renderEffect() {
    colorMode(RGB, 255);
    float is = timer <= timerThreshold ? s * (timer / timerThreshold) : s;
    fill(255);
    ellipse(xTrail[0], yTrail[0], is/1.5, is/1.5);
  }

  void move() {
  }
}

class ParticleFalling extends Particle {
  ParticleFalling(float ix, float iy, float idx, float idy, float is, int ir, int ig, int ib, int it) {
    super(ix, iy, idx, idy, is, ir, ig, ib, it);
    timer = 100;
  }

  ParticleFalling(float ix, float iy, float idx, float idy, float is, color iclr, int it) {
    super(ix, iy, idx, idy, is, iclr, it);
    timer = 100;
  }

  void move() {
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
  ParticleSparkler(float ix, float iy, float idx, float idy, float is, color iclr, int it) {
    super(ix, iy, idx, idy, is, iclr, it);
    timer = 50;
  }

  void move() {
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
    dx /= 1.05;
    y += dy;
    dy += gravity / 5;
    dy /= 1.1;
    if(timer < 0){
      active = false;
    }
    timer --;
  }
}
