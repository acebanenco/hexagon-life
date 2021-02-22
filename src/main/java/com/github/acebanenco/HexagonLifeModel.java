package com.github.acebanenco;

import java.util.BitSet;
import java.util.Random;
import java.util.stream.IntStream;

public class HexagonLifeModel {
    private final BitSet currentFill;
    private final LifeGenerationLogic generationLogic;

    public HexagonLifeModel(LifeGenerationLogic generationLogic, int WIDTH, int HEIGHT) {
        this.generationLogic = generationLogic;
        currentFill = new BitSet(HEIGHT * WIDTH);
    }

    BitSet getNextFill(int WIDTH, int HEIGHT) {
        BitSet nextFill = BitSet.valueOf(currentFill.toLongArray());
        int totalPoulation = WIDTH * HEIGHT / 2;

        int population = nextFill.cardinality();
        double xp = population / (double) totalPoulation;
        double r = 15;
        double xp1 = r * xp * (1 - xp);
        int maxNewPopulation = (int) (xp1 * totalPoulation);
        System.out.printf("Current population is %s%%, next population can be %s%%%n", population*100/totalPoulation, maxNewPopulation*100/totalPoulation);

        for (int yn = 0; yn < HEIGHT; yn++) {
            for (int xn = 0; xn < WIDTH; xn++) {
                int aliveNearbyNeighbours = getNeighbourCount(xn, yn, WIDTH, HEIGHT); // up to 6
                int aliveSecondRingNeighbours = getFarNeighbours(xn, yn, WIDTH, HEIGHT) / 2; // up to 14
                int totalNeighbours = aliveNearbyNeighbours;//(aliveNearbyNeighbours + aliveSecondRingNeighbours) / 2; // average

                int currentIndex = xn + yn * WIDTH;
                boolean alive = currentFill.get(currentIndex);

                if (alive && !generationLogic.shouldSurvive(totalNeighbours)) {
                    nextFill.set(currentIndex, false);
                }
                if (!alive && generationLogic.shouldBeBorn(aliveNearbyNeighbours)) {
                    nextFill.set(currentIndex, true);
                }
            }
        }
        int newPopulation = nextFill.cardinality();
        if (newPopulation > maxNewPopulation) {
            double scaleFactor = maxNewPopulation / (double) newPopulation;
            System.out.printf("Leaving %s%% of population%n", 100 * maxNewPopulation / (newPopulation));
            IntStream.range(0, WIDTH * HEIGHT)
                    .filter(nextFill::get)
                    .forEach(index -> {
                        if ( random.nextDouble() > scaleFactor ) {
                            nextFill.set(index, false);
                        }
                    });
        }

        if (nextFill.equals(currentFill)) {
            return null;
        }
        return nextFill;
    }

    void copyBits(BitSet nextFill) {
        IntStream.range(0, nextFill.size())
                .forEach(bitIndex ->
                        currentFill.set(bitIndex, nextFill.get(bitIndex)));
    }


    private int getFarNeighbours(int xn, int yn, int WIDTH, int HEIGHT) {
        int[] farNeighbours = {
                reflect(xn - 0, WIDTH) + reflect(yn - 4, HEIGHT) * WIDTH,
                reflect(xn - 1, WIDTH) + reflect(yn - 3, HEIGHT) * WIDTH,
                reflect(xn - 2, WIDTH) + reflect(yn - 2, HEIGHT) * WIDTH,
                reflect(xn - 2, WIDTH) + reflect(yn - 0, HEIGHT) * WIDTH,
                reflect(xn - 2, WIDTH) + reflect(yn + 2, HEIGHT) * WIDTH,
                reflect(xn - 1, WIDTH) + reflect(yn + 3, HEIGHT) * WIDTH,
                reflect(xn - 0, WIDTH) + reflect(yn + 4, HEIGHT) * WIDTH,

                reflect(xn + 0, WIDTH) + reflect(yn - 4, HEIGHT) * WIDTH,
                reflect(xn + 1, WIDTH) + reflect(yn - 3, HEIGHT) * WIDTH,
                reflect(xn + 2, WIDTH) + reflect(yn - 2, HEIGHT) * WIDTH,
                reflect(xn + 2, WIDTH) + reflect(yn - 0, HEIGHT) * WIDTH,
                reflect(xn + 2, WIDTH) + reflect(yn + 2, HEIGHT) * WIDTH,
                reflect(xn + 1, WIDTH) + reflect(yn + 3, HEIGHT) * WIDTH,
                reflect(xn + 0, WIDTH) + reflect(yn + 4, HEIGHT) * WIDTH,
        };
        int aliveaFarNeighbours = (int) IntStream.of(farNeighbours)
                .filter(currentFill::get)
                .count();
        return aliveaFarNeighbours;
    }

    Random random = new Random();

    int getNeighbourCount(int xn, int yn, int WIDTH, int HEIGHT) {
        int dx = -1 + yn % 2;
        byte[] bits = new byte[6];
        random.nextBytes(bits);
        int[] neighbours = {
                bits[0] < -128 ? -1 : reflect(xn + 0, WIDTH) + WIDTH * reflect(yn - 2, HEIGHT),
                bits[1] < -128 ? -1 : reflect(xn + 0, WIDTH) + WIDTH * reflect(yn + 2, HEIGHT),
                bits[2] < -128 ? -1 : reflect(xn + 1 + dx, WIDTH) + WIDTH * reflect(yn - 1, HEIGHT),
                bits[3] < -128 ? -1 : reflect(xn + 0 + dx, WIDTH) + WIDTH * reflect(yn - 1, HEIGHT),
                bits[4] < -128 ? -1 : reflect(xn + 1 + dx, WIDTH) + WIDTH * reflect(yn + 1, HEIGHT),
                bits[5] < -128 ? -1 : reflect(xn + 0 + dx, WIDTH) + WIDTH * reflect(yn + 1, HEIGHT),
        };
        int currentIndex = xn + yn * WIDTH;
        int neighboursCount = (int) IntStream.of(neighbours)
                .filter(i -> i >= 0 && i != currentIndex)
                .distinct()
                .filter(currentFill::get)
                .count();
        int mutation = 0;//Math.max(random.nextInt(1000+4)-1000, 0);
        return (neighboursCount < 2 || neighboursCount > 4) && mutation != 0 ? mutation : neighboursCount;
    }

    void clear() {
        currentFill.clear();
    }

    void set(int index) {
        currentFill.set(index, true);
    }

    boolean flip(int index) {
        currentFill.flip(index);
        return currentFill.get(index);
    }

    boolean get(int index) {
        return currentFill.get(index);
    }

    private static int reflect(int i, int n) {
        if (i < 0) {
            return -i;
        }
        if (i >= n) {
            return 2 * n - i;
        }
        return i;
    }

}
