/*
    This file is part of Numbermat: Math Problem Generator.
    Copyright © 2014 Valdemar Svabensky

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package cz.muni.fi.Numbermat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests for class cz.muni.fi.Numbermat.Algorithms.
 * 
 * @author Valdemar Svabensky <395868(at)mail(dot)muni(dot)cz>
 */
public final class AlgorithmsTests {
    
    /**
     * Primes from 1 to 100.
     */
    private static final int[] PRIMES = new int[] {2, 3, 5, 7, 11, 13, 17, 19,
        23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};
    
    private static final List<Integer> EMPTY = new ArrayList<>();
    
    // Linear congruences:
    private static final Pair<Integer, Integer> NO_SOLUTIONS = new Pair<>();
    private static final Pair<Integer, Integer> INF_SOLUTIONS = new Pair<>(0, 1);
    
    // Quadratic congruences:
    private static final List<Integer> INF_SOLUTIONS_Q = new ArrayList<>(Arrays.asList(0, 1));
    
    @Test
    public void testIsPrime() {
        final int[] bigPrimes = new int[] {101424101, 1500450271, 179858971, 181666181,
            189464981, 193414391, 194000491, 198565891, 199909991, 199999991};
        final int[] nonPrimes = new int[] {0, 1, 4, 6, 8, 9, 10, 12, 14, 15, 16,
            18, 20, 341, 1234567890};
        
        for (int i = 0; i < PRIMES.length; ++i)
            assertTrue(Algorithms.isPrime(PRIMES[i]));
        
        for (int i = 0; i < bigPrimes.length; ++i)
            assertTrue(Algorithms.isPrime(bigPrimes[i]));
        
        for (int i = 0; i < nonPrimes.length; ++i)
            assertFalse(Algorithms.isPrime(nonPrimes[i]));
        
        for (int i = -1; i > -11; --i)
            assertFalse(Algorithms.isPrime(i));
    }
    
    @Test
    public void testIsCoprime() {
        assertTrue(Algorithms.isCoprime(1, 3458));
        assertTrue(Algorithms.isCoprime(1, 0));
        assertTrue(Algorithms.isCoprime(3, 2));
        assertTrue(Algorithms.isCoprime(-1, 1));
        
        assertFalse(Algorithms.isCoprime(0, 0));
        assertFalse(Algorithms.isCoprime(4864, 3458));
        assertFalse(Algorithms.isCoprime(13, 169));
        assertFalse(Algorithms.isCoprime(-8, -16));
    }
    
    @Test
    public void testIsPowerOf2() {
        final int[] nonPowers = new int[] {-8, -4, -2, -1, 0, 3, 5, 6, 7, 9, 10, 11, 1022};
        for (int i = 1; i < Algorithms.FOR_LOOP_ATTEMPTS; i = 2*i)
            assertTrue(Algorithms.isPowerOf2(i));
        for (int i = 0; i < nonPowers.length; ++i)
            assertFalse(Algorithms.isPowerOf2(nonPowers[i]));
    }
    
    @Test
    public void testIsPowerOf4() {
        final int[] nonPowers = new int[] {-16, -4, -2, -1, 0, 2, 3, 5, 6, 7, 8, 9, 10, 15, 17, 1022};
        for (int i = 1; i < Algorithms.FOR_LOOP_ATTEMPTS; i = 4*i)
            assertTrue(Algorithms.isPowerOf4(i));
        for (int i = 0; i < nonPowers.length; ++i)
            assertFalse(Algorithms.isPowerOf4(nonPowers[i]));
    }
    
    @Test
    public void testGetNumberOfBits() {
        int expectedNumberOfBits = 0;
        for (int i = 1; i < Algorithms.FOR_LOOP_ATTEMPTS; ++i) {
            if (Algorithms.isPowerOf2(i))
                expectedNumberOfBits++;
            assertTrue(Algorithms.getNumberOfBits(i) == expectedNumberOfBits);
            assertTrue(Algorithms.getNumberOfBits(-i) == expectedNumberOfBits + 1);
        }
        assertTrue(Algorithms.getNumberOfBits(0) == 1);
    }
    
    @Test
    public void testRandInt() {
        for (int i = 0; i < Algorithms.FOR_LOOP_ATTEMPTS; ++i) {
            final int p = Algorithms.randInt(1, 9999);
            assertTrue((p > 0) && (p < 10000));
        }
        for (int i = 0; i < Algorithms.FOR_LOOP_ATTEMPTS; ++i) {
            final int p = Algorithms.randInt(-9999, -1);
            assertTrue((p < 0) && (p > -10000));
        }
        for (int i = 0; i < Algorithms.FOR_LOOP_ATTEMPTS; ++i) {
            final int p = Algorithms.randInt(0, 0);
            assertTrue(p == 0);
        }
        Algorithms.randInt(1, Integer.MAX_VALUE);
        Algorithms.randInt(0, Integer.MAX_VALUE - 1);
        Algorithms.randInt(-1, Integer.MAX_VALUE - 2);
    }
    
    @Test
    public void testRandIntFail() {
        try {
            Algorithms.randInt(0, 0, false);
            fail();
        } catch (IllegalArgumentException ex) {}
        testRandIntFail(1, 0);
        testRandIntFail(-4, -10);
        testRandIntFail(Integer.MIN_VALUE, Integer.MAX_VALUE);
        testRandIntFail(-951, Integer.MAX_VALUE);
        testRandIntFail(0, Integer.MAX_VALUE);
    }
    
    private void testRandIntFail(final int a, final int b) {
        try {    
            Algorithms.randInt(a, b);
            fail();
        } catch (IllegalArgumentException ex) {
        } catch (RuntimeException ex) {}
    }
    
    @Test
    public void testRandPrime() {
        assertTrue(Algorithms.randPrime(0, 2) == 2);
        for (int i = 0; i < Algorithms.FOR_LOOP_ATTEMPTS; ++i) {
            final int p = Algorithms.randPrime(0, 10000);
            assertTrue(Algorithms.isPrime(p));
        }
    }
    
    @Test
    public void testRandPrimeFail() {
        testRandPrimeFail(10, 0);   // Max bound is smaller than min
        testRandPrimeFail(-1, 10);  // Min bound is negative
        testRandPrimeFail(0, 0);    // Max bound is smaller than 2
        testRandPrimeFail(4, 4);    // No primes exist here
        testRandPrimeFail(954, 966);// And here
    }
    
    private void testRandPrimeFail(final int a, final int b) {
        try {
            Algorithms.randPrime(a, b);
            fail();
        } catch (IllegalArgumentException ex) {
        } catch (RuntimeException ex) {
        }
    }
    
    @Test
    public void testRandCoprime() {
        assertTrue(Algorithms.randCoprime(1, 1).equals(new Pair<>(1, 1)));
        for (int i = 0; i < Algorithms.FOR_LOOP_ATTEMPTS; ++i) {
            final Pair<Integer, Integer> coprimes = Algorithms.randCoprime(1, 500);
            assertTrue(Algorithms.isCoprime(coprimes.getFirst(), coprimes.getSecond()));
        }
    }
    
    @Test
    public void testRandCoprimeFail() {
        testRandCoprimeFail(10, 0); // Max bound is smaller than min
        testRandCoprimeFail(-1, 0); // Min bound is negative
        testRandCoprimeFail(8, 8);  // No coprimes exist here
    }
    
    private void testRandCoprimeFail(final int a, final int b) {
        try {
            Algorithms.randCoprime(a, b);
            fail();
        } catch (IllegalArgumentException ex) {
        } catch (RuntimeException ex) {
        }
    }
    
    @Test
    public void testGCD() {
        allPossibleGCDs(113, 50, 1);
        allPossibleGCDs(4864, 3458, 38);
        allPossibleGCDs(10175, 2277, 11);
        allPossibleGCDs(-15, -10, 5);
        allPossibleGCDs(1, 144, 1);
        allPossibleGCDs(1, 0, 1);
        allPossibleGCDs(0, 0, 0);
    }
    
    private void allPossibleGCDs(int a, int b, int gcd) {
        assertTrue(Algorithms.gcd(a, b) == gcd);
        assertTrue(Algorithms.gcd(-a, b) == gcd);
        assertTrue(Algorithms.gcd(a, -b) == gcd);
        assertTrue(Algorithms.gcd(-a, -b) == gcd);
        assertTrue(Algorithms.gcd(b, a) == gcd);
        assertTrue(Algorithms.gcd(-b, a) == gcd);
        assertTrue(Algorithms.gcd(b, -a) == gcd);
        assertTrue(Algorithms.gcd(-b, -a) == gcd);
    }
    
    @Test
    public void testLCM() {
        assertTrue(Algorithms.lcm(1, 1) == 1);
        assertTrue(Algorithms.lcm(1, 2) == 2);
        assertTrue(Algorithms.lcm(2, 3) == 6);
        assertTrue(Algorithms.lcm(2, 4) == 4);
        assertTrue(Algorithms.lcm(15, 18) == 90);
    }
    
    @Test
    public void testLCMFail() {
        testLCMFail(1, 0);
        testLCMFail(0, 0);
        testLCMFail(0, 1);
        testLCMFail(-4, 1);
        testLCMFail(4, -1);
        testLCMFail(-5, -2);
    }
    
    private void testLCMFail(final int a, final int b) {
        try {
            Algorithms.lcm(a, b);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testBezout() {
        // 98 * (-1) + 21 * 5 = 7
        testBezout(98, -1, 21, 5, 7);
        testBezout(4864, 32, 3458, -45, 38);
        testBezout(15, 0, 1, 1, 1);
        testBezout(15, 1, 0, 0, 15);
        testBezout(0, 1, 0, 0, 0);
    }
    
    // ax + by = gcd;
    private void testBezout(final int a, final int x, final int b, final int y, final int gcd) {
        final List<Integer> result = Algorithms.bezout(a, b);
        assertEquals(result, Arrays.asList(gcd, x, y));
    }
    
    @Test
    public void testBezoutFail() {
        testBezoutFail(15, -15);
        testBezoutFail(4, 15);
    }
    
    private void testBezoutFail(final int a, final int b) {
        try {
            Algorithms.bezout(a, b);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testDivisors() {
        testDivisors(1, Arrays.asList(1));
        testDivisors(2, Arrays.asList(1, 2));
        testDivisors(5, Arrays.asList(1, 5));
        testDivisors(28, Arrays.asList(1, 2, 4, 7, 14, 28));
        testDivisors(-28, Arrays.asList(1, 2, 4, 7, 14, 28));
        testDivisors(744, Arrays.asList(1, 2, 3, 4, 6, 8, 12, 24, 31, 62,
                93, 124, 186, 248, 372, 744));
    }
    
    private void testDivisors(final int n, final List<Integer> expected) {
        assertEquals(Algorithms.divisors(n), expected);
    }
    
    @Test
    public void testDivisorsFail() {
        try {
            Algorithms.divisors(0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testCommonDivisors() {
        testCommonDivisors(1, 1, Arrays.asList(1));
        testCommonDivisors(1, 100, Arrays.asList(1));
        testCommonDivisors(2, 3, Arrays.asList(1));
        testCommonDivisors(3, 9, Arrays.asList(1, 3));
        testCommonDivisors(4, 10, Arrays.asList(1, 2));
        testCommonDivisors(100, 10, Arrays.asList(1, 2, 5, 10));
    }
    
    private void testCommonDivisors(final int a, final int b,
            final List<Integer> expected) {
        assertEquals(Algorithms.commonDivisors(a, b), expected);
    }
    
    @Test
    public void testCommonDivisorsFail() {
        testCommonDivisorsFail(0, 1);
        testCommonDivisorsFail(1, 0);
        testCommonDivisorsFail(0, 0);
    }
    
    private void testCommonDivisorsFail(final int a, final int b) {
        try {
            Algorithms.commonDivisors(a, b);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testFactorize() {
        int n = 4864;
        ArrayList<Pair<Integer, Integer>> factors = new ArrayList<>();
        factors.add(new Pair<>(2, 8));
        factors.add(new Pair<>(19, 1));
        assertEquals(factors, Algorithms.factorize(n));
        
        n = 3458;
        factors = new ArrayList<>();
        factors.add(new Pair<>(2, 1));
        factors.add(new Pair<>(7, 1));
        factors.add(new Pair<>(13, 1));
        factors.add(new Pair<>(19, 1));
        assertEquals(factors, Algorithms.factorize(n));
        
        n = 1;
        factors = new ArrayList<>();
        factors.add(new Pair<>(1, 1));
        assertEquals(factors, Algorithms.factorize(n));
        
        n = 0;
        factors = new ArrayList<>();
        factors.add(new Pair<>(0, 1));
        assertEquals(factors, Algorithms.factorize(n));
    }
    
    @Test
    public void testFactorizeFail() {
        try {
            Algorithms.factorize(-15);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testEulerPhi() {
        assertTrue(Algorithms.eulerPhi(1) == 1);
        assertTrue(Algorithms.eulerPhi(25) == 20);
        assertTrue(Algorithms.eulerPhi(33) == 20);
        assertTrue(Algorithms.eulerPhi(44) == 20);
        assertTrue(Algorithms.eulerPhi(50) == 20);
        assertTrue(Algorithms.eulerPhi(66) == 20);
        assertTrue(Algorithms.eulerPhi(735) == 336);
        assertTrue(Algorithms.eulerPhi(1212) == 400);
        assertTrue(Algorithms.eulerPhi(2010) == 528);
        assertTrue(Algorithms.eulerPhi(3458) == 1296);
        assertTrue(Algorithms.eulerPhi(4864) == 2304);
    }
    
    @Test
    public void testEulerPhiFail() {
        testEulerPhiFail(-5);
        testEulerPhiFail(0);
    }
    
    private void testEulerPhiFail(final int n) {
        try {
            Algorithms.eulerPhi(n);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testNormalizeIntModulo() {
        assertTrue(Algorithms.normalizeIntModulo(8, 3) == 2);
        assertTrue(Algorithms.normalizeIntModulo(-8, 3) == 1);
        assertTrue(Algorithms.normalizeIntModulo(2, 5) == 2);
        assertTrue(Algorithms.normalizeIntModulo(-2, 5) == 3);
    }
    
    @Test
    public void testNormalizeIntModuloFail() {
        testNormalizeIntModuloFail(7, -4);
        testNormalizeIntModuloFail(7, 0);
    }
    
    private void testNormalizeIntModuloFail(final int a, final int b) {
        try {
            Algorithms.normalizeIntModulo(a, b);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testLinearCongruence() {
        testLinearCongruence(5, 7, 8, new Pair<>(3, 8));
        testLinearCongruence(4, 3, 7, new Pair<>(6, 7));
        testLinearCongruence(16, 4, 6, new Pair<>(1, 3));
        testLinearCongruence(21, 6, 9, new Pair<>(2, 3));
        testLinearCongruence(26, 4, 9, new Pair<>(5, 9));
        
        testLinearCongruence(3, 5, 17, new Pair<>(13, 17));
        testLinearCongruence(76, 8, 10, new Pair<>(3, 5));
        testLinearCongruence(12, 20, 28, new Pair<>(4, 7));
        testLinearCongruence(642, 1844, 1144, new Pair<>(10, 572));
        testLinearCongruence(11881376, 16, 11, new Pair<>(5, 11));
        
        testLinearCongruence(3, 1, 6, NO_SOLUTIONS);
        testLinearCongruence(5, 4, 5, NO_SOLUTIONS);
        
        testLinearCongruence(5, 0, 5, INF_SOLUTIONS);
        testLinearCongruence(0, 3, 3, INF_SOLUTIONS);
        testLinearCongruence(2, 4, 1, INF_SOLUTIONS);
    }
    
    private void testLinearCongruence(final int a, final int b, final int n,
            final Pair<Integer, Integer> expected) {
        assertEquals(Algorithms.linearCongruence(a, b, n), expected);
    }
    
    @Test
    public void testLinearCongruenceFail() {
        testLinearCongruenceFail(12, 20, 0);
        testLinearCongruenceFail(12, 5, -14);
    }
    
    private void testLinearCongruenceFail(final int a, final int b, final int n) {
        try {
            Algorithms.linearCongruence(a, b, n);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testLinearCongruenceSystem() {
        List<Integer> aList, bList, nList;
        Pair<Integer, Integer> expected;
        
        // 2 congruences
        aList = new ArrayList<>(Arrays.asList(1, 1));
        bList = new ArrayList<>(Arrays.asList(6, 2));
        nList = new ArrayList<>(Arrays.asList(8, 5));
        expected = new Pair<>(22, 40);
        testLinearCongruenceSystem(2, aList, bList, nList, expected);
        
        aList = new ArrayList<>(Arrays.asList(1, 1));
        bList = new ArrayList<>(Arrays.asList(-5, 2));
        nList = new ArrayList<>(Arrays.asList(20, 13));
        expected = new Pair<>(15, 260);
        testLinearCongruenceSystem(2, aList, bList, nList, expected);
        
        // 3 congruences
        aList = new ArrayList<>(Arrays.asList(1, 1, 1));
        bList = new ArrayList<>(Arrays.asList(8, 5, 1));
        nList = new ArrayList<>(Arrays.asList(11, 8, 3));
        expected = new Pair<>(85, 264);
        testLinearCongruenceSystem(3, aList, bList, nList, expected);
        
        /* 2x = 2 (mod 6)
           3x = 2 (mod 7)
           2x = 4 (mod 8) */
        aList = new ArrayList<>(Arrays.asList(2, 3, 2));
        bList = new ArrayList<>(Arrays.asList(2, 2, 4));
        nList = new ArrayList<>(Arrays.asList(6, 7, 8));
        expected = new Pair<>(10, 84);
        testLinearCongruenceSystem(3, aList, bList, nList, expected);
        
        aList = new ArrayList<>(Arrays.asList(1, 1, 1));
        bList = new ArrayList<>(Arrays.asList(10, 3, 0));
        nList = new ArrayList<>(Arrays.asList(13, 12, 11));
        expected = new Pair<>(231, 1716);
        testLinearCongruenceSystem(3, aList, bList, nList, expected);
        
        aList = new ArrayList<>(Arrays.asList(729, 169, 32));
        bList = new ArrayList<>(Arrays.asList(343, 27, 1331));
        nList = new ArrayList<>(Arrays.asList(5, 7, 9));
        expected = new Pair<>(97, 315);
        testLinearCongruenceSystem(3, aList, bList, nList, expected);
        
        aList = new ArrayList<>(Arrays.asList(1, 1, 1));
        bList = new ArrayList<>(Arrays.asList(2, 4, 6));
        nList = new ArrayList<>(Arrays.asList(4, 6, 10));
        expected = new Pair<>(46, 60);
        testLinearCongruenceSystem(3, aList, bList, nList, expected);
        
        aList = new ArrayList<>(Arrays.asList(2, 3, 5));
        bList = new ArrayList<>(Arrays.asList(3, 5, 7));
        nList = new ArrayList<>(Arrays.asList(5, 7, 9));
        expected = new Pair<>(284, 315);
        testLinearCongruenceSystem(3, aList, bList, nList, expected);
        
        aList = new ArrayList<>(Arrays.asList(1, 1, 1));
        bList = new ArrayList<>(Arrays.asList(3, 2, 5));
        nList = new ArrayList<>(Arrays.asList(7, 11, 13));
        expected = new Pair<>(486, 1001);
        testLinearCongruenceSystem(3, aList, bList, nList, expected);
        
        aList = new ArrayList<>(Arrays.asList(1, 1, 1));
        bList = new ArrayList<>(Arrays.asList(3, 1, -3));
        nList = new ArrayList<>(Arrays.asList(6, 5, 7));
        expected = new Pair<>(81, 210);
        testLinearCongruenceSystem(3, aList, bList, nList, expected);
        
        /* Unsolvable */
        aList = new ArrayList<>(Arrays.asList(5));
        bList = new ArrayList<>(Arrays.asList(4));
        nList = new ArrayList<>(Arrays.asList(5));
        testLinearCongruenceSystem(1, aList, bList, nList, NO_SOLUTIONS);
        
        aList = new ArrayList<>(Arrays.asList(1, 1));
        bList = new ArrayList<>(Arrays.asList(3, 7));
        nList = new ArrayList<>(Arrays.asList(9, 15));
        testLinearCongruenceSystem(2, aList, bList, nList, NO_SOLUTIONS);
        
        aList = new ArrayList<>(Arrays.asList(1, 1));
        bList = new ArrayList<>(Arrays.asList(1, -1));
        nList = new ArrayList<>(Arrays.asList(3, 9));
        testLinearCongruenceSystem(2, aList, bList, nList, NO_SOLUTIONS);
        
        /* Infinite solutions */
        aList = new ArrayList<>(Arrays.asList(5));
        bList = new ArrayList<>(Arrays.asList(0));
        nList = new ArrayList<>(Arrays.asList(5));
        testLinearCongruenceSystem(1, aList, bList, nList, INF_SOLUTIONS);
        
        aList = new ArrayList<>(Arrays.asList(0));
        bList = new ArrayList<>(Arrays.asList(3));
        nList = new ArrayList<>(Arrays.asList(3));
        testLinearCongruenceSystem(1, aList, bList, nList, INF_SOLUTIONS);
        
        aList = new ArrayList<>(Arrays.asList(0, 7, 158));
        bList = new ArrayList<>(Arrays.asList(3, 14, 0));
        nList = new ArrayList<>(Arrays.asList(3, 7, 158));
        testLinearCongruenceSystem(3, aList, bList, nList, INF_SOLUTIONS);
        
        /* Unsolvable, but containting equation with infinite solutions */
        aList = new ArrayList<>(Arrays.asList(7, 158, 16));
        bList = new ArrayList<>(Arrays.asList(14, 0, 5));
        nList = new ArrayList<>(Arrays.asList(7, 158, 8));
        testLinearCongruenceSystem(3, aList, bList, nList, NO_SOLUTIONS);
        
        /* Solvable, but containting equation with infinite solutions */
        aList = new ArrayList<>(Arrays.asList(2, 1, 40));
        bList = new ArrayList<>(Arrays.asList(3, 4, 40));
        nList = new ArrayList<>(Arrays.asList(5, 5, 20));
        expected = new Pair<>(4, 5);
        testLinearCongruenceSystem(3, aList, bList, nList, expected);
        
        aList = new ArrayList<>(Arrays.asList(40, 2, 1));
        bList = new ArrayList<>(Arrays.asList(40, 3, 4));
        nList = new ArrayList<>(Arrays.asList(20, 5, 5));
        expected = new Pair<>(4, 5);
        testLinearCongruenceSystem(3, aList, bList, nList, expected);
    }
    
    private void testLinearCongruenceSystem(final int count, final List<Integer> aList,
            final List<Integer> bList, final List<Integer> nList,
            final Pair<Integer, Integer> expected) {
        
        assertEquals(Algorithms.linearCongruenceSystem(count, aList, bList, nList), expected);
    }
    
    @Test
    public void testLinearCongruenceSystemFail() {
        List<Integer> aList = new ArrayList<>(Arrays.asList(2, 3, 2));
        List<Integer> bList = new ArrayList<>(Arrays.asList(2, 2, 4));
        List<Integer> nList = new ArrayList<>(Arrays.asList(6, 7, 0));
        try {
            Algorithms.linearCongruenceSystem(3, aList, bList, nList);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testElementsOfUnitGroup() {
        for (int i = 0; i < PRIMES.length; ++i) {
            final int n = PRIMES[i];
            final List<Integer> expected = new ArrayList<>();
            for (int j = 1; j < PRIMES[i]; ++j)
                expected.add(j);
            testElementsOfUnitGroup(n, expected);
        }
        
        testElementsOfUnitGroup(4, Arrays.asList(1, 3));
        testElementsOfUnitGroup(6, Arrays.asList(1, 5));
        testElementsOfUnitGroup(8, Arrays.asList(1, 3, 5, 7));
        testElementsOfUnitGroup(12, Arrays.asList(1, 5, 7, 11));
    }
    
    private void testElementsOfUnitGroup(final int n, final List<Integer> expected) {
        final List<Integer> result = Algorithms.elementsOfUnitGroup(n);
        assertTrue(result.size() == Algorithms.eulerPhi(n));
        assertEquals(result, expected);
    }
    
    @Test
    public void testElementsOfUnitGroupFail() {
        for (int i = -2; i < 2; ++i)
            testElementsOfUnitGroupFail(i);
    }
    
    private void testElementsOfUnitGroupFail(final int n) {
        try {
            Algorithms.elementsOfUnitGroup(n);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testUnitGroupElementOrder() {
        int n = 3; // group Z3×
        assertTrue(Algorithms.unitGroupElementOrder(1, n) == 1);
        assertTrue(Algorithms.unitGroupElementOrder(2, n) == 2);

        n = 4; // group Z4×
        assertTrue(Algorithms.unitGroupElementOrder(1, n) == 1);
        assertTrue(Algorithms.unitGroupElementOrder(3, n) == 2);
        
        n = 5; // group Z5×
        assertTrue(Algorithms.unitGroupElementOrder(2, n) == 4);
        assertTrue(Algorithms.unitGroupElementOrder(3, n) == 4);
        assertTrue(Algorithms.unitGroupElementOrder(4, n) == 2);
        
        n = 6; // group Z6×
        assertTrue(Algorithms.unitGroupElementOrder(1, n) == 1);
        assertTrue(Algorithms.unitGroupElementOrder(5, n) == 2);
        
        n = 7; // group Z7×
        assertTrue(Algorithms.unitGroupElementOrder(2, n) == 3);
        assertTrue(Algorithms.unitGroupElementOrder(3, n) == 6);
        assertTrue(Algorithms.unitGroupElementOrder(4, n) == 3);
        assertTrue(Algorithms.unitGroupElementOrder(5, n) == 6);
        assertTrue(Algorithms.unitGroupElementOrder(6, n) == 2);
        
        assertTrue(Algorithms.unitGroupElementOrder(86, 97) == 48);
    }
    
    @Test
    public void testUnitGroupElementOrderFail() {
        for (int i = -2; i < 2; ++i)
            testUnitGroupElementOrderFail(1, i);
        
        testUnitGroupElementOrderFail(-4, 5);
        testUnitGroupElementOrderFail(0, 5);
        testUnitGroupElementOrderFail(5, 5);
        testUnitGroupElementOrderFail(7, 5);
    }
    
    private void testUnitGroupElementOrderFail(final int element, final int n) {
        try {
            Algorithms.unitGroupElementOrder(element, n);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testModPow() {
        assertTrue(Algorithms.modPow(14, 15, 1) == 0);
        assertTrue(Algorithms.modPow(0, 0, 1574) == 1);
        assertTrue(Algorithms.modPow(0, 275, 1411) == 0);
        assertTrue(Algorithms.modPow(1, 557, 11889) == 1);
        assertTrue(Algorithms.modPow(-1, 557, 11889) == 11888);
        assertTrue(Algorithms.modPow(-1, 558, 11889) == 1);
        assertTrue(Algorithms.modPow(2, 6, 63) == 1);
        assertTrue(Algorithms.modPow(14, 5, 11) == 1);
        assertTrue(Algorithms.modPow(7, 144, 52) == 1);
        assertTrue(Algorithms.modPow(3, 351, 1000) == 747);
        assertTrue(Algorithms.modPow(157, 248, 369) == 160);
        assertTrue(Algorithms.modPow(-47, 25, 100) == 93);
    }
    
    @Test
    public void testModPowFail() {
        testModPowFail(14, -1, 15);
        testModPowFail(14, 1, -15);
        testModPowFail(14, -4, -815);
    }
    
    private void testModPowFail(final int b, final int e, final int m) {
        try {
            Algorithms.modPow(b, e, m);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testLegendreSymbol() {
        assertTrue(Algorithms.legendreSymbol(219, 383) == 1);
        assertTrue(Algorithms.legendreSymbol(79, 101) == 1);
        assertTrue(Algorithms.legendreSymbol(3, 59) == 1);
        assertTrue(Algorithms.legendreSymbol(49, 7) == 0);
        assertTrue(Algorithms.legendreSymbol(7, 43) == -1);
        assertTrue(Algorithms.legendreSymbol(-1, 3) == -1);
    }
    
    @Test
    public void testLegendreSymbolFail() {
        for (int i = -2; i < 3; ++i)
            testLegendreSymbolFail(14, i);
    }
    
    private void testLegendreSymbolFail(final int a, final int p) {
        try {
            Algorithms.legendreSymbol(a, p);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testIsPerfectSquare() {
        final int[] perfectSquares = new int[] {0, 1, 4, 9, 16, 25, 36, 49, 64,
            81, 100, 121, 144, 169};
        for (int i = 0; i < perfectSquares.length; ++i)
            assertTrue(Algorithms.isPerfectSquare(perfectSquares[i]));
        
        final int[] notSquares = new int[] {-2, -1, 2, 3, 5, 7, 8, 10, 11, 12,
            13, 14, 15, 17};
        for (int i = 0; i < notSquares.length; ++i)
            assertFalse(Algorithms.isPerfectSquare(notSquares[i]));
    }
    
    @Test
    public void testFindHighestPowerLessThan() {
        assertTrue(Algorithms.findHighestPowerLessThan(2, 2) == 1);
        assertTrue(Algorithms.findHighestPowerLessThan(2, 3) == 2);
        assertTrue(Algorithms.findHighestPowerLessThan(2, 5) == 4);
        
        assertTrue(Algorithms.findHighestPowerLessThan(3, 80) == 27);
        assertTrue(Algorithms.findHighestPowerLessThan(3, 81) == 27);
        assertTrue(Algorithms.findHighestPowerLessThan(3, 82) == 81);
        
        assertTrue(Algorithms.findHighestPowerLessThan(4, 2) == 1);
    }
    
    @Test
    public void testFindHighestPowerLessThanFail() {
        testFindHighestPowerLessThanFail(2, 1);
        testFindHighestPowerLessThanFail(2, 0);
        testFindHighestPowerLessThanFail(1, 5);
        testFindHighestPowerLessThanFail(0, 8);
    }
    
    private void testFindHighestPowerLessThanFail(final int base, final int limit) {
        try {
            Algorithms.findHighestPowerLessThan(base, limit);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testFindNextPowerOf4() {
        assertTrue(Algorithms.findNextHigherPowerOf4(-1) == 1);
        assertTrue(Algorithms.findNextHigherPowerOf4(0) == 1);
        assertTrue(Algorithms.findNextHigherPowerOf4(1) == 1);
        assertTrue(Algorithms.findNextHigherPowerOf4(2) == 4);
        assertTrue(Algorithms.findNextHigherPowerOf4(3) == 4);
        assertTrue(Algorithms.findNextHigherPowerOf4(4) == 4);
        assertTrue(Algorithms.findNextHigherPowerOf4(15) == 16);
        assertTrue(Algorithms.findNextHigherPowerOf4(37) == 64);
    }
    
    @Test
    public void testQuadraticCongruenceSimple() {
        int m = 1; // modulus
        testQuadraticCongruenceSimple(14, m, INF_SOLUTIONS_Q);

        m = 2;
        testQuadraticCongruenceSimple(14, m, Arrays.asList(0, m));  // 0
        testQuadraticCongruenceSimple(7, m, Arrays.asList(1, m));   // 1
        
        /* For mod > 2, first test all non-residues.
         * The parameter is a list of residues which the called function skips.
         * It then checks that the remaining numbers have no solution. */
        
        m = 3;
        testQuadraticCongruenceSimpleNonResidues(Arrays.asList(0, 1), m);
        testQuadraticCongruenceSimple(15, m, Arrays.asList(0, m));      // 0
        testQuadraticCongruenceSimple(7, m, Arrays.asList(1, 2, m));    // 1
        
        m = 4;
        testQuadraticCongruenceSimpleNonResidues(Arrays.asList(0, 1), m);
        testQuadraticCongruenceSimple(16, m, Arrays.asList(0, m/2));    // 0
        testQuadraticCongruenceSimple(13, m, Arrays.asList(1, m/2));    // 1
        
        m = 5;
        testQuadraticCongruenceSimpleNonResidues(Arrays.asList(0, 1, 4), m);
        testQuadraticCongruenceSimple(15, m, Arrays.asList(0, m));      // 0
        testQuadraticCongruenceSimple(16, m, Arrays.asList(1, 4, m));   // 1
        testQuadraticCongruenceSimple(19, m, Arrays.asList(2, 3, m));   // 4
        
        m = 6;
        testQuadraticCongruenceSimpleNonResidues(Arrays.asList(0, 1, 3, 4), m);
        testQuadraticCongruenceSimple(66, m, Arrays.asList(0, m));      // 0
        testQuadraticCongruenceSimple(37, m, Arrays.asList(1, 5, m));   // 1
        testQuadraticCongruenceSimple(39, m, Arrays.asList(3, m));      // 3
        testQuadraticCongruenceSimple(604, m, Arrays.asList(2, 4, m));  // 4
        
        m = 7;
        testQuadraticCongruenceSimpleNonResidues(Arrays.asList(0, 1, 2, 4), m);
        testQuadraticCongruenceSimple(0, m, Arrays.asList(0, m));
        testQuadraticCongruenceSimple(1, m, Arrays.asList(1, 6, m));
        testQuadraticCongruenceSimple(2, m, Arrays.asList(3, 4, m));
        testQuadraticCongruenceSimple(4, m, Arrays.asList(2, 5, m));
        
        m = 8;
        testQuadraticCongruenceSimpleNonResidues(Arrays.asList(0, 1, 4), m);
        testQuadraticCongruenceSimple(0, m, Arrays.asList(0, m/2));
        testQuadraticCongruenceSimple(1, m, Arrays.asList(1, m/4));
        testQuadraticCongruenceSimple(4, m, Arrays.asList(2, m/2));
        
        m = 9;
        testQuadraticCongruenceSimpleNonResidues(Arrays.asList(0, 1, 4, 7), m);
        testQuadraticCongruenceSimple(0, m, Arrays.asList(0, 3));
        testQuadraticCongruenceSimple(1, m, Arrays.asList(1, 8, m));
        testQuadraticCongruenceSimple(4, m, Arrays.asList(2, 7, m));
        testQuadraticCongruenceSimple(7, m, Arrays.asList(4, 5, m));
        
        m = 10;
        testQuadraticCongruenceSimpleNonResidues(Arrays.asList(0, 1, 4, 5, 6, 9), m);
        testQuadraticCongruenceSimple(0, m, Arrays.asList(0, m));
        testQuadraticCongruenceSimple(1, m, Arrays.asList(1, 9, m));
        testQuadraticCongruenceSimple(4, m, Arrays.asList(2, 8, m));
        testQuadraticCongruenceSimple(5, m, Arrays.asList(5, m));
        testQuadraticCongruenceSimple(6, m, Arrays.asList(4, 6, m));
        testQuadraticCongruenceSimple(9, m, Arrays.asList(3, 7, m));
        
        m = 11;
        testQuadraticCongruenceSimpleNonResidues(Arrays.asList(0, 1, 3, 4, 5, 9), m);
        testQuadraticCongruenceSimple(0, m, Arrays.asList(0, m));
        testQuadraticCongruenceSimple(1, m, Arrays.asList(1, 10, m));
        testQuadraticCongruenceSimple(3, m, Arrays.asList(5, 6, m));
        testQuadraticCongruenceSimple(4, m, Arrays.asList(2, 9, m));
        testQuadraticCongruenceSimple(5, m, Arrays.asList(4, 7, m));
        testQuadraticCongruenceSimple(9, m, Arrays.asList(3, 8, m));
        
        m = 12;
        testQuadraticCongruenceSimpleNonResidues(Arrays.asList(0, 1, 4, 9), m);
        testQuadraticCongruenceSimple(0, m, Arrays.asList(0, m/2));
        testQuadraticCongruenceSimple(1, m, Arrays.asList(1, 5, m/2));
        testQuadraticCongruenceSimple(4, m, Arrays.asList(2, 4, m/2));
        testQuadraticCongruenceSimple(9, m, Arrays.asList(3, m/2));
        
        m = 16;
        testQuadraticCongruenceSimpleNonResidues(Arrays.asList(0, 1, 4, 9), m);
        testQuadraticCongruenceSimple(0, m, Arrays.asList(0, 4));
        testQuadraticCongruenceSimple(1, m, Arrays.asList(1, 7, 8));
        testQuadraticCongruenceSimple(4, m, Arrays.asList(2, 4));
        testQuadraticCongruenceSimple(9, m, Arrays.asList(3, 5, 8));
        
        m = 32;
        testQuadraticCongruenceSimpleNonResidues(Arrays.asList(0, 1, 4, 9, 16, 17, 25), m);
        testQuadraticCongruenceSimple(0, m, Arrays.asList(0, 8));
        testQuadraticCongruenceSimple(1, m, Arrays.asList(1, 15, 16));
        testQuadraticCongruenceSimple(4, m, Arrays.asList(2, 4));
        testQuadraticCongruenceSimple(9, m, Arrays.asList(3, 13, 16));
        testQuadraticCongruenceSimple(16, m, Arrays.asList(4, 8));
        testQuadraticCongruenceSimple(17, m, Arrays.asList(7, 9, 16));
        testQuadraticCongruenceSimple(25, m, Arrays.asList(5, 11, 16));
        
        // Powers of 2, a = 0
        for (m = 2; m < 2048; m = m * 2) {
            final int closestPowerOf4 = Algorithms.findNextHigherPowerOf4(m);
            final int itsSqrt = (int)Math.sqrt(closestPowerOf4);
            testQuadraticCongruenceSimple(0, m, Arrays.asList(0, itsSqrt));
        }
        
        // Negative a
        testQuadraticCongruenceSimple(-55, 65, Arrays.asList(20, 45, 65));
        testQuadraticCongruenceSimple(-12, 52, Arrays.asList(12, 14, 26));
        testQuadraticCongruenceSimple(-12, 104, Arrays.asList(14, 38, 52));
        testQuadraticCongruenceSimple(-24, 55, Arrays.asList(14, 19, 36, 41, 55));
        testQuadraticCongruenceSimple(-363, 364, Arrays.asList(1, 27, 155, 181, 182));
        
        // Some other residues
        testQuadraticCongruenceSimpleResidues(13, Arrays.asList(0, 1, 3, 4, 9, 10, 12));
        testQuadraticCongruenceSimpleResidues(14, Arrays.asList(0, 1, 2, 4, 7, 8, 9, 11));
        testQuadraticCongruenceSimpleResidues(15, Arrays.asList(0, 1, 4, 6, 9, 10));
        testQuadraticCongruenceSimpleResidues(17, Arrays.asList(0, 1, 2, 4, 8, 9, 13, 15, 16));
        testQuadraticCongruenceSimpleResidues(18, Arrays.asList(0, 1, 4, 7, 9, 10, 13, 16));
        testQuadraticCongruenceSimpleResidues(19, Arrays.asList(0, 1, 4, 5, 6, 7, 9, 11, 16, 17));
        testQuadraticCongruenceSimpleResidues(20, Arrays.asList(0, 1, 4, 5, 9, 16));
    }
    
    private void testQuadraticCongruenceSimple(final int a, final int m, final List<Integer> expected) {
        assertEquals(Algorithms.quadraticCongruenceSimple(a, m), expected);
    }

    // Test non residues: numbers, for which x^2 ≡ i (mod m) does not have a solution
    private void testQuadraticCongruenceSimpleNonResidues(
            final List<Integer> residues, final int m) {
        for (int i = 2; i < m; ++i) {
            if (residues.contains(i))
                continue;
            assertEquals(Algorithms.quadraticCongruenceSimple(i, m), EMPTY);
        }
    }
    
    private void testQuadraticCongruenceSimpleResidues(final int m, final List<Integer> residues) {
        testQuadraticCongruenceSimpleNonResidues(residues, m);
        for (int i = 0; i < residues.size(); ++i) {
            List<Integer> result = Algorithms.quadraticCongruenceSimple(residues.get(i), m);
            assertTrue(!result.equals(EMPTY));
        }
    }
    
    @Test
    public void testQuadraticCongruenceSimpleFail() {
        testQuadraticCongruenceSimpleFail(14, -1);
        testQuadraticCongruenceSimpleFail(14, 0);
    }
    
    private void testQuadraticCongruenceSimpleFail(final int a, final int m) {
        try {
            Algorithms.quadraticCongruenceSimple(a, m);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testQuadraticCongruenceGeneral() {
        // b == 0
        int m = 11;
        testQuadraticCongruenceGeneral(7, 0, 5, m, Arrays.asList(2, 9, m));
        
        // c == 0
        m = 5;
        testQuadraticCongruenceGeneral(6, 2, 0, m, Arrays.asList(0, 3, m));
        m = 17;
        testQuadraticCongruenceGeneral(8, 4, 0, m, Arrays.asList(0, 8, m));
        
        // b == 0, c == 0
        m = 2;
        testQuadraticCongruenceGeneral(3, 0, 4, m, Arrays.asList(0, m));
        m = 16;
        testQuadraticCongruenceGeneral(7, 0, 0, m, Arrays.asList(0, 4, 8, 12, m));
        m = 17;
        testQuadraticCongruenceGeneral(5, 0, 0, m, Arrays.asList(0, m));
        
        // general
        m = 2;
        testQuadraticCongruenceGeneral(3, 5, 4, m, Arrays.asList(0, 1, m));
        m = 10;
        testQuadraticCongruenceGeneral(9, 2, 4, m, Arrays.asList(6, m));
        m = 11;
        testQuadraticCongruenceGeneral(5, 7, 9, m, Arrays.asList(6, 8, m));
        m = 26;
        testQuadraticCongruenceGeneral(33, 20, 11, m, Arrays.asList(5, 7, m));
        m = 77;
        testQuadraticCongruenceGeneral(25, 13, 21, m, Arrays.asList(21, 37, 65, 70, m));
        m = 91;
        testQuadraticCongruenceGeneral(17, 35, 18, m, Arrays.asList(15, 41, 64, 90, m));
        
        // no solution
        testQuadraticCongruenceGeneral(6, 8, 10, 13, EMPTY);
        testQuadraticCongruenceGeneral(8, 13, 17, 65, EMPTY);
    }
    
    private void testQuadraticCongruenceGeneral(final int a, final int b,
            final int c, final int m, final List<Integer> expected) {
        assertEquals(Algorithms.quadraticCongruenceGeneral(a, b, c, m), expected);
    }
    
    @Test
    public void testQuadraticCongruenceGeneralFail() {
        testQuadraticCongruenceGeneralFail(0, 0, 0, 0);
        testQuadraticCongruenceGeneralFail(0, 0, 0, 5);
        testQuadraticCongruenceGeneralFail(16, 0, 0, 32);
    }
    
    private void testQuadraticCongruenceGeneralFail(final int a, final int b,
            final int c, final int m) {
        try {
            Algorithms.quadraticCongruenceGeneral(a, b, c, m);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testPrimitiveRootsExist() {
        for (int i = 1; i < PRIMES.length; ++i) {
            final int prime = PRIMES[i];
            for (int j = 1; j < 5; ++j) {
                final int primePow = (int)Math.pow(prime, j);
                assertTrue(Algorithms.primitiveRootsExist(primePow));
                assertTrue(Algorithms.primitiveRootsExist(2 * primePow));
            }
        }
        assertTrue(Algorithms.primitiveRootsExist(2));
        assertTrue(Algorithms.primitiveRootsExist(4));
        
        final int[] noPrimitiveRoots = new int[] {8, 12, 15, 16, 20};
        for (int i = 0; i < noPrimitiveRoots.length; ++i)
            assertFalse(Algorithms.primitiveRootsExist(noPrimitiveRoots[i]));
    }
    
    @Test
    public void testPrimitiveRootsExistFail() {
        for (int i = -5; i < 2; ++i)
            testPrimitiveRootsExistFail(i);
    }
    
    private void testPrimitiveRootsExistFail(final int m) {
        try {
            Algorithms.primitiveRootsExist(m);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testBinomialCongruence() {
        int n = 3;
        
        int m = 1;
        testBinomialCongruence(n, 14, m, INF_SOLUTIONS_Q);
        
        m = 2;
        for (int a = 0; a < m; ++a)
            testBinomialCongruence(n, a, m, Arrays.asList(a, m));
        
        m = 3;
        for (int a = 0; a < m; ++a)
            testBinomialCongruence(n, a, m, Arrays.asList(a, m));
        
        m = 4;
        testBinomialCongruence(n, 0, m, Arrays.asList(0, m/2));
        testBinomialCongruence(n, 1, m, Arrays.asList(1, m));
        testBinomialCongruence(n, 2, m, EMPTY);
        testBinomialCongruence(n, 3, m, Arrays.asList(3, m));
        
        m = 5;
        testBinomialCongruence(n, 0, m, Arrays.asList(0, m));
        testBinomialCongruence(n, 1, m, Arrays.asList(1, m));
        testBinomialCongruence(n, 2, m, Arrays.asList(3, m));
        testBinomialCongruence(n, 3, m, Arrays.asList(2, m));
        testBinomialCongruence(n, 4, m, Arrays.asList(4, m));
        
        
        m = 529;
        testBinomialCongruence(5, 534, m, Arrays.asList(333, m));
        m = 91;
        testBinomialCongruence(8, 9, 91, Arrays.asList(3, 10, 11, 24, 67, 80, 81, 88, m));
    }
    
    private void testBinomialCongruence(final int n, final int a, final int m,
            final List<Integer> expected) {
        assertTrue(Algorithms.binomialCongruence(n, a, m).equals(expected));
    }
    
    @Test
    public void testBinomialCongruenceFail() {
        // Exponent is not positive
        testBinomialCongruenceFail(0, 4, 7);
        testBinomialCongruenceFail(-1, 4, 7);
        // Modulus is not positive
        testBinomialCongruenceFail(3, 4, 0);
        testBinomialCongruenceFail(3, 4, -1);
    }

    private void testBinomialCongruenceFail(final int n, final int a, final int m) {
        try {
            Algorithms.binomialCongruence(n, a, m);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testGenerateModulus() {
        for (int i = 0; i < Algorithms.FOR_LOOP_ATTEMPTS; ++i) {
            final int n = Algorithms.generateModulus(false);
            // Min value: 6 = 2*3, max value: 95 = 5*19
            assertTrue(n > 5);
            assertTrue(n < 96);
            testGenerateModulusHelper(n);
        }
        for (int i = 0; i < Algorithms.FOR_LOOP_ATTEMPTS; ++i) {
            final int n = Algorithms.generateModulus(true);
            // Min value: 15 = 3*5, max value: 95 = 5*19
            assertTrue(n > 14);
            assertTrue(n < 96);
            assertTrue(n % 2 == 1);
            testGenerateModulusHelper(n);
        }
    }
    
    private void testGenerateModulusHelper(final int n) {
        final List<Pair<Integer, Integer>> factorization = Algorithms.factorize(n);
        final int size = factorization.size();
        assertTrue(size == 2);

        final Pair<Integer, Integer> first = factorization.get(0);
        final Pair<Integer, Integer> second = factorization.get(1);
        assertTrue(Algorithms.isPrime(first.getFirst()));
        assertTrue(first.getSecond() == 1);
        assertTrue(Algorithms.isPrime(second.getFirst()));
        assertTrue(second.getSecond() == 1);
    }
    
    @Test
    public void testPermutationCyclesAndOrder() {
        testPermutationCyclesAndOrderEmpty();
        
        /* Length 2 */
        List<Integer> inputPerm = new ArrayList<>(Arrays.asList(2, 1));
        Set<List<Integer>> expected = newHashSet(Arrays.asList(1, 2));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        
        /* Length 3 */
        inputPerm = new ArrayList<>(Arrays.asList(1, 3, 2));
        expected = newHashSet(Arrays.asList(2, 3));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        
        inputPerm = new ArrayList<>(Arrays.asList(2, 1, 3));
        expected = newHashSet(Arrays.asList(1, 2));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        
        inputPerm = new ArrayList<>(Arrays.asList(2, 3, 1));
        expected = newHashSet(Arrays.asList(1, 2, 3));
        testPermutationCyclesAndOrder(inputPerm, expected, 3);
        
        inputPerm = new ArrayList<>(Arrays.asList(3, 1, 2));
        expected = newHashSet(Arrays.asList(1, 3, 2));
        testPermutationCyclesAndOrder(inputPerm, expected, 3);
        
        inputPerm = new ArrayList<>(Arrays.asList(3, 2, 1));
        expected = newHashSet(Arrays.asList(1, 3));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        
        /* Length 4 */
        inputPerm = new ArrayList<>(Arrays.asList(2, 3, 4, 1));
        expected = newHashSet(Arrays.asList(1, 2, 3, 4));
        testPermutationCyclesAndOrder(inputPerm, expected, 4);
        
        // Order of cycles does not matter
        inputPerm = new ArrayList<>(Arrays.asList(2, 1, 4, 3));
        expected = newHashSet((Arrays.asList(3, 4)), Arrays.asList(1, 2));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        expected = newHashSet((Arrays.asList(1, 2)), Arrays.asList(3, 4));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        
        /* Length 5 */
        inputPerm = new ArrayList<>(Arrays.asList(1, 4, 3, 2, 5));
        expected = newHashSet(Arrays.asList(2, 4));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        
        inputPerm = new ArrayList<>(Arrays.asList(2, 4, 1, 5, 3));
        expected = newHashSet(Arrays.asList(1, 2, 4, 5, 3));
        testPermutationCyclesAndOrder(inputPerm, expected, 5);
        
        inputPerm = new ArrayList<>(Arrays.asList(4, 2, 5, 1, 3));
        expected = newHashSet(Arrays.asList(1, 4), Arrays.asList(3, 5));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        expected = newHashSet(Arrays.asList(3, 5), Arrays.asList(1, 4));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        
        /* Length 6 */
        inputPerm = new ArrayList<>(Arrays.asList(2, 1, 4, 3, 6, 5));
        expected = newHashSet(Arrays.asList(1, 2), Arrays.asList(3, 4),
                Arrays.asList(5, 6));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        expected = newHashSet(Arrays.asList(3, 4), Arrays.asList(1, 2),
                Arrays.asList(5, 6));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        expected = newHashSet(Arrays.asList(5, 6), Arrays.asList(1, 2),
                Arrays.asList(3, 4));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        
        inputPerm = new ArrayList<>(Arrays.asList(1, 6, 5, 4, 3, 2));
        expected = newHashSet(Arrays.asList(2, 6), Arrays.asList(3, 5));
        testPermutationCyclesAndOrder(inputPerm, expected, 2);
        
        /* Length 7 */
        inputPerm = new ArrayList<>(Arrays.asList(4, 6, 5, 7, 3, 2, 1));
        expected = newHashSet(Arrays.asList(1, 4, 7), Arrays.asList(2, 6),
                Arrays.asList(3, 5));
        testPermutationCyclesAndOrder(inputPerm, expected, 6);
        
        /* Length 8 */
        inputPerm = new ArrayList<>(Arrays.asList(2, 6, 1, 7, 4, 8, 5, 3));
        expected = newHashSet(Arrays.asList(1, 2, 6, 8, 3), Arrays.asList(4, 7, 5));
        testPermutationCyclesAndOrder(inputPerm, expected, 15);
    }
    
    private static Set<List<Integer>> newHashSet(final List<Integer>... lists) {
        final Set<List<Integer>> set = new HashSet<>();
        set.addAll(Arrays.asList(lists));
        return set;
    }
    
    private void testPermutationCyclesAndOrder(final List<Integer> inputPerm,
            final Set<List<Integer>> expected, final int order) {
        
        final Set<List<Integer>> result = Algorithms.permutationCycles(inputPerm);
        assertEquals(result, expected);
        assertTrue(Algorithms.permutationOrder(inputPerm) == order);
        assertTrue(Algorithms.permutationOrder(expected) == order);
    }
    
    private void testPermutationCyclesAndOrderEmpty() {
        final Set<List<Integer>> empty = new HashSet<>();
        for (int n = 1; n < 101; ++n) {
            final List<Integer> inputPerm = new ArrayList<>(n);
            for (int i = 1; i <= n; ++i)
                inputPerm.add(i);
            testPermutationCyclesAndOrder(inputPerm, empty, 1);
        }
    }
    
    @Test
    public void testPermutationCyclesAndOrderFail() {
        testPermutationCyclesAndOrderFail(Arrays.asList(0));
        testPermutationCyclesAndOrderFail(Arrays.asList(2));
        testPermutationCyclesAndOrderFail(Arrays.asList(3));
        testPermutationCyclesAndOrderFail(Arrays.asList(-1));
        testPermutationCyclesAndOrderFail(Arrays.asList(-7));
        testPermutationCyclesAndOrderFail(Arrays.asList(1, 3));
        testPermutationCyclesAndOrderFail(Arrays.asList(2, 3));
        testPermutationCyclesAndOrderFail(Arrays.asList(1, 2, 3, 5));
    }
    
    private void testPermutationCyclesAndOrderFail(final List<Integer> inputPerm) {
        try {
            Algorithms.permutationCycles(inputPerm);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testRandPermutation() {
        for (int i = 1; i < Algorithms.FOR_LOOP_ATTEMPTS; ++i)
            Algorithms.permutationCheck(Algorithms.randPermutation(i));
    }
    
    @Test
    public void testRandPermutationFail() {
        testRandPermutationFail(0);
        testRandPermutationFail(-1);
        testRandPermutationFail(-2);
    }
    
    private void testRandPermutationFail(final int size) {
        try {
            Algorithms.randPermutation(size);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
}
