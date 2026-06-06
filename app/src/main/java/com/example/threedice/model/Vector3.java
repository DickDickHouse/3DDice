package com.example.threedice.model;

/**
 * 3D Vector utility class
 */
public class Vector3 {
    public float x, y, z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(float[] values) {
        this.x = values[0];
        this.y = values[1];
        this.z = values[2];
    }

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3 copy() {
        return new Vector3(x, y, z);
    }

    public Vector3 add(Vector3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    public Vector3 subtract(Vector3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    public Vector3 multiply(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    public float dot(Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector3 cross(Vector3 other) {
        float nx = this.y * other.z - this.z * other.y;
        float ny = this.z * other.x - this.x * other.z;
        float nz = this.x * other.y - this.y * other.x;
        this.x = nx;
        this.y = ny;
        this.z = nz;
        return this;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 normalize() {
        float len = length();
        if (len > 0) {
            this.x /= len;
            this.y /= len;
            this.z /= len;
        }
        return this;
    }

    public float[] toArray() {
        return new float[]{x, y, z};
    }

    @Override
    public String toString() {
        return String.format("Vector3(%.2f, %.2f, %.2f)", x, y, z);
    }
}
