import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {
        double INT_RADIUS = 1;
        double VELOCITY = 0.03;
        double DT = 1;
        double MAX_TIME = 600;
        double ANIMATION_DT = 10;

        List<Config> configurations = Arrays.asList(
                new Config(40, 3.16),
                new Config(100, 5),
                new Config(400, 10),
                new Config(1000, 15.81)
        );

//        for (Config c : configurations) {
//            for (int noise_ratio_it = 0; noise_ratio_it <= 50; noise_ratio_it++) {
//                double noise_ratio = noise_ratio_it / 50.0; // To remove floating point error
//                System.out.println(String.format("N: %d - L: %.2f - noise: %.2f", c.n, c.l, noise_ratio));
//                new Simulation(c.n, c.l, INT_RADIUS, VELOCITY, noise_ratio, DT, MAX_TIME, ANIMATION_DT, false)
//                        .run();
//            }
//        }
//
//        for (int n = 50; n <= 1000; n += 50) {
//            for (double noise_ratio : Arrays.asList(0.0, 0.1, 0.2, 0.4, 0.8, 1.0)) {
//                double l = 20;
//                System.out.println(String.format("N: %d - L: %.2f - noise: %.2f", n, l, noise_ratio));
//                new Simulation(n, l, INT_RADIUS, VELOCITY, noise_ratio, DT, MAX_TIME, ANIMATION_DT, false)
//                        .run();
//            }
//        }

        int n = 0;
        while (n < 1000) {
            if (n < 200) {
                n += 10;
            } else if (n < 500){
                n += 20;
            } else {
                n += 50;
            }

            double l = 10;
            double noise_ratio = 0.4;
            System.out.println(String.format("N: %d - L: %.2f - noise: %.2f", n, l, noise_ratio));
            new Simulation(n, l, INT_RADIUS, VELOCITY, noise_ratio, DT, MAX_TIME, ANIMATION_DT, false)
                    .run();
        }
    }

    private static class Config {
        public int n;
        public double l;
        public double n_r;

        public Config(int n, double l) {
            this.n = n;
            this.l = l;
        }

        public Config(int n) {
            this.n = n;
        }
    }
}