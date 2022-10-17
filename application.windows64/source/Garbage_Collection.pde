void garbageCollection() {
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
