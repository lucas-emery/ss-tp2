import java.util.List;
import java.util.Locale;

public class Particle {

    private int id;
    public static double INT_RADIUS_SQ, NOISE_RATIO, MAX_X, MAX_Y, MAX_Z;
    public double x, y, z, v, u_vx, u_vy, u_vz, next_u_vx, next_u_vy, next_u_vz;

    public Particle(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Particle(int id, double x, double y, double z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void move(double dt) {
        u_vx = next_u_vx;
        u_vy = next_u_vy;
        u_vz = next_u_vz;

        x += v * u_vx * dt;
        y += v * u_vy * dt;
        z += v * u_vz * dt;

        if (x > MAX_X) {
            x -= MAX_X;
        } else if (x < 0) {
            x += MAX_X;
        }

        if (y > MAX_Y) {
            y -= MAX_Y;
        } else if (y < 0) {
            y += MAX_Y;
        }

        if (z > MAX_Z) {
            z -= MAX_Z;
        } else if (z < 0) {
            z += MAX_Z;
        }
    }

    public void adjustAngle(List<Particle> neighbors) {
        double sum_u_vx = 0;
        double sum_u_vy = 0;
        int count = 0;

        for (Particle p : neighbors) {
            if (distance_sq(p) <= INT_RADIUS_SQ) {
                sum_u_vx += p.u_vx;
                sum_u_vy += p.u_vy;
                count++;
            }
        }

        double theta = FastAtan.atan2((float) sum_u_vy / count, (float) sum_u_vx / count);
        theta += NOISE_RATIO * (Math.random() - 0.5) * 2 * Math.PI;

        next_u_vx = Math.cos(theta);
        next_u_vy = Math.sin(theta);
    }

    public double distance_sq(Particle other) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        double dz = other.z - this.z;

        // Continuous space
        if (dx > MAX_X / 2)
            dx -= MAX_X / 2;

        if (dy > MAX_Y / 2)
            dy -= MAX_Y / 2;

        if (dz > MAX_Z / 2)
            dz -= MAX_Z / 2;

        return dx*dx + dy*dy;
    }

    // Angle in radians
    public void setVelocity(double v, double theta, double phi) {
        this.v = v;
        double phi_cosine = Math.cos(phi);
        next_u_vx = Math.cos(theta) * phi_cosine;
        next_u_vy = Math.sin(theta) * phi_cosine;
        next_u_vz = Math.sin(phi);
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%d %.6f %.6f %.6f %.6f %.6f %.6f %.6f %.6f %.6f",
                id, x, y, z, next_u_vx, next_u_vy, next_u_vz, (next_u_vx + 2) / 3, (next_u_vy + 2) / 3, (next_u_vz + 2) / 3);
    }
}