//!!!!!!!!!! NOT MY CODE, FOUND ONLINE !!!!!!!!!!//
class Hitbox {
    int x, y, width, height;
    public Hitbox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public boolean intersects(Hitbox other) {
        return (x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y);
    }
}
//!!!!!!!!!! NOT MY CODE, FOUND ONLINE !!!!!!!!!!//