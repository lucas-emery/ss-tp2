import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Simulation {

    private static final int N = 300;
    private static final double L = 25;
    private static final double INT_RADIUS = 1;
    private static final double VELOCITY = 0.03;
    private static final double NOISE_RATIO = 0.1;
    private static final double DT = 1;
    private static final double MAX_TIME = 600;
    private static final double ANIMATION_DT = 1;
    private static final double WIDTH = L, HEIGHT = L, DEPTH = L;

    private static final int INDEX_X_DIM = (int) Math.ceil(WIDTH / INT_RADIUS);
    private static final int INDEX_Y_DIM = (int) Math.ceil(HEIGHT / INT_RADIUS);

    private static List<Particle> particles = new LinkedList<>();
    private static List<Particle>[][] neighborIndex = new List[INDEX_X_DIM][INDEX_Y_DIM];
    private static List<Double> times = new LinkedList<>();
    private static List<Double> order_measurements = new LinkedList<>();

    public static void main(String[] args) {
        PrintWriter writer = null;

        Particle.INT_RADIUS_SQ = INT_RADIUS * INT_RADIUS;
        Particle.NOISE_RATIO = NOISE_RATIO;
        Particle.MAX_X = WIDTH;
        Particle.MAX_Y = HEIGHT;
        Particle.MAX_Z = DEPTH;

        initParticles(N, WIDTH, HEIGHT, DEPTH, VELOCITY);

        try {
            System.out.println("Starting simulation");
            writer = new PrintWriter("data/" + N + "_" + L + "_" + NOISE_RATIO + "_" + VELOCITY + "_" + DT + "_" + (DEPTH == 0? "2D" : "3D") + "_simulation.xyz");
            writeState(writer);

            double totalTime = 0;
            int lastFrame = 1;

            saveMeasures(totalTime);
            while (totalTime < MAX_TIME) {
                rebuildIndex();

                for (Particle p : particles) {
                    p.move(DT);
                    List<Particle> neighbors = neighborIndex[(int)(p.x / INT_RADIUS)][(int)(p.y / INT_RADIUS)];
                    p.adjustAngle(particles);
                }

                totalTime += DT;

                if (totalTime / ANIMATION_DT > lastFrame) {
                    writeState(writer);
                    lastFrame++;
                }

                saveMeasures(totalTime);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("Finished simulation");
            if (writer != null)
                writer.close();
        }

        printList(times, "data/" + N + "_" + L + "_" + NOISE_RATIO + "_" + VELOCITY + "_" + DT + "_" + (DEPTH == 0? "2D" : "3D") + "_times.csv");
        printList(order_measurements, "data/" + N + "_" + L + "_" + NOISE_RATIO + "_" + VELOCITY + "_" + DT + "_" + (DEPTH == 0? "2D" : "3D") + "_order.csv");
    }

    private static void initParticles(int n, double width, double height, double depth, double v) {
        while (particles.size() < n) {
            double x = Math.random() * width;
            double y = Math.random() * height;
            double z = Math.random() * depth;

            Particle newParticle = new Particle(particles.size(), x, y, z);
            double theta = Math.random() * 2 * Math.PI;
            double phi = depth == 0 ? 0 : Math.random() * 2 * Math.PI;
            newParticle.setVelocity(v, theta, phi);

            particles.add(newParticle);
        }
    }

    private static void rebuildIndex() {
        for (int i = 0; i < INDEX_X_DIM; i++) {
            for (int j = 0; j < INDEX_Y_DIM; j++) {
                neighborIndex[i][j] = new LinkedList<>();
            }
        }

        for (Particle p : particles) {
            int centerX = (int)(p.x / INT_RADIUS);
            int leftX = centerX > 0 ? centerX - 1 : INDEX_X_DIM - 1;
            int rightX = centerX < INDEX_X_DIM - 1 ? centerX + 1 : 0;

            int centerY = (int)(p.y / INT_RADIUS);
            int leftY = centerY > 0 ? centerY - 1 : INDEX_Y_DIM - 1;
            int rightY = centerY < INDEX_Y_DIM - 1 ? centerY + 1 : 0;

            neighborIndex[leftX][leftY].add(p);
            neighborIndex[leftX][centerY].add(p);
            neighborIndex[leftX][rightY].add(p);
            neighborIndex[centerX][leftY].add(p);
            neighborIndex[centerX][centerY].add(p);
            neighborIndex[centerX][rightY].add(p);
            neighborIndex[rightX][leftY].add(p);
            neighborIndex[rightX][centerY].add(p);
            neighborIndex[rightX][rightY].add(p);
        }
    }

    private static void writeState(PrintWriter writer) {
        writer.println(particles.size() + 2);
        writer.println();
        writer.println(new Particle(-1, 0, 0, 0));
        writer.println(new Particle(-2, WIDTH, HEIGHT, DEPTH));
        for (Particle p : particles) {
            writer.println(p);
        }
    }

    private static void saveMeasures(double time) {
        times.add(time);

        double sum_vx = 0, sum_vy = 0, sum_vz = 0;
        for (Particle p : particles) {
            sum_vx += p.u_vx;
            sum_vy += p.u_vy;
            sum_vz += p.u_vz;
        }

        order_measurements.add(Math.sqrt(sum_vx*sum_vx + sum_vy*sum_vy + sum_vz*sum_vz) / particles.size());
    }

    private static void printList(List<Double> list, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            for (double d : list) {
                writer.println(d);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}