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

  void render() {
    colorMode(RGB, 255);
    fill(255);
    ellipse(x, y, 10, 10);
  }
  
  void renderEffect(){}

  void move() {
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

  void render() {
    colorMode(RGB, 255);
    fill(r, g, b);
    ellipse(x, y, 10, 10);
  }

  void move() {
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

  void explode() {
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

  void explode() {
    active = false;
    float angle = random(TAU);
    float iclrAngle = random(255);
    for (int i = 0; i < children; i ++) {
      float ispeed = random(speed);
      colorMode(HSB, 255);
      color iclr = color((iclrAngle + ispeed * 10) % 255, 255, 255);
      particles.add(new ParticleFalling(x, y, dx + ispeed*cos(angle + TAU * i / children), dy + ispeed*sin(angle + TAU * i / children), cs, iclr, 10));
    }
  }
}

class FireworkSparkler extends Firework{
  color clr;
  float children, childSize, childSpeed;
  
  FireworkSparkler(float ix, float iy, float idx, float idy, color iclr, int ichildren, float ics, float ispeed) {
    super(ix, iy, idx, idy);
    clr = iclr;
    children = ichildren;
    childSize = ics;
    childSpeed = ispeed;
  }
  
  void renderEffect(){
    colorMode(RGB, 255);
    fill(255);
    ellipse(x, y, 8, 8);
  }
  
  void move(){
    if (x < -100 || x > width + 100 || y > height + 100) {
      active = false;
    }
    for(int i = 0; i < children; i ++){
      if(random(1) < .05){
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
