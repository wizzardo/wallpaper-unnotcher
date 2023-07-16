package com.example.misc;

public class Vector2f {
    public float x;
    public float y;

    public Vector2f() {
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f vector2f) {
        this.x = vector2f.x;
        this.y = vector2f.y;
    }

    public Vector2f add(Vector2f vec) {
        return new Vector2f(x + vec.x, y + vec.y);
    }

    public double distance(Vector2f v) {
        return Math.sqrt(distanceSquared(v));
    }

    public float distanceSquared(Vector2f v) {
        double dx = x - v.x;
        double dy = y - v.y;
        return (float) (dx * dx + dy * dy);
    }
}
