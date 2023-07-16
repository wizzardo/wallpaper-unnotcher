package com.example.misc;

public class Vector4f {
    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4f() {
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(Vector4f vector2f) {
        this.x = vector2f.x;
        this.y = vector2f.y;
        this.z = vector2f.z;
        this.w = vector2f.w;
    }

    public Vector4f set(Vector4f q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
        return this;
    }

    public void set(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f normalizeLocal() {
        float n = invSqrt(norm());
        x *= n;
        y *= n;
        z *= n;
        w *= n;
        return this;
    }

    public float norm() {
        return w * w + x * x + y * y + z * z;
    }

    public static float invSqrt(float fValue) {
        return (float) (1.0f / Math.sqrt(fValue));
    }

    public Vector4f slerp(Vector4f q1, Vector4f q2, float t) {
        if (q1.x == q2.x && q1.y == q2.y && q1.z == q2.z && q1.w == q2.w) {
            this.set(q1);
            return this;
        }

        float result = (q1.x * q2.x) + (q1.y * q2.y) + (q1.z * q2.z)
                + (q1.w * q2.w);

        if (result < 0.0f) {
            q2.x = -q2.x;
            q2.y = -q2.y;
            q2.z = -q2.z;
            q2.w = -q2.w;
            result = -result;
        }

        double scale0 = 1 - t;
        double scale1 = t;

        if ((1 - result) > 0.1f) {
            double theta = Math.acos(result);
            double invSinTheta = 1f / Math.sin(theta);

            scale0 = Math.sin((1 - t) * theta) * invSinTheta;
            scale1 = Math.sin((t * theta)) * invSinTheta;
        }

        this.x = (float) ((scale0 * q1.x) + (scale1 * q2.x));
        this.y = (float) ((scale0 * q1.y) + (scale1 * q2.y));
        this.z = (float) ((scale0 * q1.z) + (scale1 * q2.z));
        this.w = (float) ((scale0 * q1.w) + (scale1 * q2.w));

        // Return the interpolated quaternion
        return this;
    }
}
