package com.example.liuhui.lottery.utils;

/**
 * TODO: description
 * Date: 2017-05-24
 *
 * @author
 */
public class LotteryUtils {
    /**
     * 大乐透可能组合数
     *
     * @param redBalls
     * @param blues
     * @return
     */
    public static long getBigLottoMaxResult(int redBalls, int blues) {
        if (redBalls < 5 || blues < 2) {
            return 0;
        }

        return combination(redBalls, 5) * combination(blues, 2);
    }


    /**
     * 计算阶乘数，即n! = n * (n-1) * ... * 2 * 1
     *
     * @param n
     * @return
     */
    private static long factorial(int n) {
        return (n > 1) ? n * factorial(n - 1) : 1;
    }

    /**
     * 计算排列数，即A(n, m) = n!/(n-m)!
     *
     * @param n
     * @param m
     * @return
     */
    private static long arrangement(int n, int m) {
        return (n >= m) ? factorial(n) / factorial(n - m) : 0;
    }

    /**
     * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
     *
     * @param n
     * @param m
     * @return
     */
    private static long combination(int n, int m) {
        return (n >= m) ? factorial(n) / factorial(n - m) / factorial(m) : 0;
    }
}
