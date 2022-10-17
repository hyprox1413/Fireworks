float gravity = .15;
ArrayList<Particle> particles = new ArrayList<Particle>();
ArrayList<Firework> fireworks = new ArrayList<Firework>();

void setup() {
  fullScreen(P2D);
  noStroke();
}

void draw() {
  background(0);
  garbageCollection();
  if (random(1) < 0.08) {
    fireworks.add(new FireworkPrismatic(random(width), height + 50, random(10) - 5, -1 * random(25), 255, 255, 255, 100, 12, 8));
  }
  if (random(1) < 0.08) {
    colorMode(HSB, 255);
    color iclr = color(random(255), 255, 255);
    fireworks.add(new FireworkSparkler(random(width), height + 50, random(10) - 5, -1 * random(25), iclr, 30, 8, 3));
  }
  //saveFrame();
}
