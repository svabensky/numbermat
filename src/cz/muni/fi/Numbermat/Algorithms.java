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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Static methods for computations and math problem solving.
 * 
 * @author Valdemar Svabensky <395868(at)mail(dot)muni(dot)cz>
 */
public final class Algorithms {
    
    private Algorithms() {
        throw new IllegalStateException(this.getClass().getName() +
                " class should not be instantiated.");
    }

    /**
     * FOR loops upper limit.
     */
    public static final int FOR_LOOP_ATTEMPTS = 1000;
    
    /**
     * @param n Integer
     * @return Is 'n' prime?
     */
    public static boolean isPrime(final int n) {
        if (n < 2)
            return false;
        if (n == 2)
            return true;
        if (n % 2 == 0)
            return false;
        
        for (int i = 3; i <= Math.sqrt(n) + 1; i += 2) {
            if (n % i == 0)
                return false;
        }
        return true;
    }
    
    /**
     * @param a Integer
     * @param b Integer
     * @return Are 'a', 'b' coprime integers?
     */
    public static boolean isCoprime(final int a, final int b) {
        return gcd(a, b) == 1;
    }
    
    /**
     * @param n Integer
     * @return Is 'n' a power of 2?
     */
    public static boolean isPowerOf2(final int n) {
        return (n != 0) && ((n & (n - 1)) == 0);
    }
    
    /**
     * @param n Integer
     * @return Is 'n' a power of 4?
     */
    public static boolean isPowerOf4(final int n) {
        return (isPowerOf2(n)) && ((getNumberOfBits(n) % 2) == 1);
    }
    
    /**
     * @param n Integer
     * @return Number of bits needed to express 'n' in binary
     */
    public static int getNumberOfBits(final int n) {
        if (n == 0)
            return 1;
        if (n < 0)
            return 1 + getNumberOfBits(-n); // sign bit
        
        return Integer.SIZE - Integer.numberOfLeadingZeros(n);
    }
    
    /**
     * Generates a pseudo-random integer between 'min' and 'max', inclusive.
     * The difference between min and max can be at most Integer.MAX_VALUE - 1.
     * @param min Minimum value
     * @param max Maximum value >= min
     * @param zero Is zero allowed?
     * @return Integer in range [min, max]
     */
    public static int randInt(final int min, final int max, final boolean zero) {
        notLessThanCheck(max, min);
        overflowCheck(min, max);
        if ((min == 0) && (max == 0) && !zero)
            throw new IllegalArgumentException("Bounds allow to generate only 0,"
                    + "but it was not allowed.");
         
        final Random rand = new Random();
        int n = rand.nextInt((max - min) + 1) + min;
        if (!zero) {
            while (n == 0)
                n = rand.nextInt((max - min) + 1) + min;
        }
        return n;
    }
    
    /**
     * Generates a pseudo-random integer between 'min' and 'max', inclusive.
     * The difference between min and max can be at most Integer.MAX_VALUE - 1.
     * @param min Minimum value
     * @param max Maximum value >= min
     * @return Integer in range [min, max]
     */
    public static int randInt(final int min, final int max) {
        return randInt(min, max, true);
    }
    
    /**
     * Generates a pseudo-random prime between 'min' and 'max', inclusive.
     * The difference between min and max can be at most Integer.MAX_VALUE - 1.
     * @param min Minimum value. Non-negative integer.
     * @param max Maximum value >= min. Cannot be smaller than 2.
     * @return Prime in range [min, max]
     * @throws RuntimeException If a prime cannot be generated in given range
     */
    public static int randPrime(final int min, final int max) {
        notNegativeCheck(min);
        notLessThanCheck(max, 2);
        notLessThanCheck(max, min);
        overflowCheck(min, max);

        int randomBitLength;
        do {
            randomBitLength = getNumberOfBits(randInt(min, max));
        } while (randomBitLength < 2);
        
        final Random rand = new SecureRandom();
        BigInteger n;
        for (int i = 0; i < FOR_LOOP_ATTEMPTS; ++i) {
            n = BigInteger.probablePrime(randomBitLength, rand);
            if ((n.intValue() >= min) && (n.intValue() <= max))
                return n.intValue();
        }
        throw new RuntimeException("Unable to generate a prime in range ["
                + min + ", " + max + "].");
    }
    
    /**
     * Generates a pseudo-random prime between 'min' and 'max', inclusive.
     * The difference between min and max can be at most Integer.MAX_VALUE - 1.
     * @param min Minimum value. Non-negative integer.
     * @param max Maximum value >= min. Cannot be smaller than 2.
     * @param odd Generate only odd primes
     * @return Prime in range [min, max]
     * @throws RuntimeException If a prime cannot be generated in given range
     */
    public static int randPrime(final int min, final int max, final boolean odd) {
        int n;
        for (int i = 0; i < FOR_LOOP_ATTEMPTS; ++i) {
            n = randPrime(min, max);
            if (!odd || n != 2)
                return n;
        }
        throw new RuntimeException("Unable to generate a prime in range ["
                + min + ", " + max + "].");
    }
    
    /**
     * Generates a pseudo-random pair of coprime integers between 'min' and 'max', inclusive.
     * The difference between min and max can be at most Integer.MAX_VALUE - 1.
     * @param min Minimum value. Non-negative integer.
     * @param max Maximum value >= min
     * @return Pair of coprime integers in range [min, max]
     * @throws RuntimeException If a pair of coprimes cannot be generated in given range
     */
    public static Pair<Integer, Integer> randCoprime(final int min, final int max) {
        notNegativeCheck(min);
        notLessThanCheck(max, min);
        overflowCheck(min, max);
        
        Pair<Integer, Integer> test = randCoprimeTry(randInt(min, max), min, max);
        if (test.isEmpty())
            test = randCoprimeTry(randPrime(min, max), min, max);
        if (!test.isEmpty())
            return test;
        throw new RuntimeException("Unable to generate a pair of coprimes in range ["
                + min + ", " + max + "].");
    }
    
    private static Pair<Integer, Integer> randCoprimeTry(final int a,
            final int min, final int max) {
        
        int b = randInt(min, max);
        for (int i = 0; i < FOR_LOOP_ATTEMPTS; ++i) {
            if (isCoprime(a, b))
                return new Pair<>(a, b);
            b = randInt(min, max);
        }
        return new Pair<>();
    }
     
    /**
     * Euclidean algorithm for finding the greatest common divisor of two integers.
     * See Handbook of Applied Cryptography, sec. 2.4, page 66 for details.
     * @param a Integer
     * @param b Integer
     * @return gcd(a, b)
     */
    public static int gcd(int a, int b) {
        if (a < 0)
            return gcd(-a, b);
        if (b < 0)
            return gcd(a, -b);
        if (a < b)
            return gcd(b, a);
        
        while (b > 0) {
            int r = a % b;
            a = b;
            b = r;
        }
        return a;
    }
    
    /**
     * Least common multiple of two positive integers.
     * @param a Positive integer
     * @param b Positive integer
     * @return lcm(a, b)
     */
    public static int lcm(int a, int b) {
        positiveCheck(a);
        positiveCheck(b);
        return a * b / gcd(a, b);
    }
    
    /**
     * Extended Euclidean algorithm for solving Bezout's identity.
     * This algorithm is also useful for finding multiplicative inverses mod n.
     * See Handbook of Applied Cryptography, sec. 2.4, pages 67 and 71 for details.
     * @param a Non-negative integer, a >= b
     * @param b Non-negative integer
     * @return List [d, x, y] where d = gcd(a, b) and x, y such that ax + by = d
     */
    public static List<Integer> bezout(int a, int b) {
        notNegativeCheck(a);
        notNegativeCheck(b);
        notLessThanCheck(a, b);
        if (b == 0)
            return new ArrayList(Arrays.asList(a, 1, 0));
        
        int x, y, q, r;
        int x1 = 0;
        int x2 = 1;
        int y1 = 1;
        int y2 = 0;
        while (b > 0) {
            q = a/b;
            r = a - q*b;
            x = x2 - q*x1;
            y = y2 - q*y1;
            a = b;
            b = r;
            x2 = x1;
            x1 = x;
            y2 = y1;
            y1 = y;
        }
        return new ArrayList(Arrays.asList(a, x2, y2));
    }
    
    /**
     * @param n Non-zero integer
     * @return List of all positive divisors of 'n' in ascending order
     */
    public static List<Integer> divisors(final int n) {
        notZeroCheck(n);
        if (n < 0)
            return divisors(-n);
        
        final List<Integer> divisors = new ArrayList<>();
        divisors.add(1);
        if (n == 1)
            return divisors;
        
        if (!isPrime(n)) {
            int i = 2;
            int increment = 1;
            if (n % 2 == 1) {
                i = 3;
                increment = 2; // test odd ones only
            }
            
            for (; i <= n/2; i += increment) {
                if (n % i == 0)
                    divisors.add(i);
            }
        }
        divisors.add(n);
        return divisors;
    }
    
    /**
     * @param a Non-zero integer
     * @param b Non-zero integer
     * @return List of all positive divisors of both a and b in ascending order
     */
    public static List<Integer> commonDivisors(final int a, final int b) {
        notZeroCheck(a);
        notZeroCheck(b);
        return listIntersection(divisors(a), divisors(b));
    }
    
    private static <T> List<T> listIntersection(final List<T> l1, final List<T> l2) {
        final List<T> list = new ArrayList<>();
        for (T t : l1) {
            if (l2.contains(t))
                list.add(t);
        }
        return list;
    }
    
    /**
     * Prime factorization.
     * @param n Non-negative integer
     * @return List of pairs (factor, exponent)
     */
    public static List<Pair<Integer, Integer>> factorize(int n) {
        notNegativeCheck(n);
        final List<Pair<Integer, Integer>> factors = new ArrayList<>();
        if (n < 4) {
            factors.add(new Pair<>(n, 1));
            return factors;
        }
        
        for (int i = 2; i <= n; ++i) {
            final Pair<Integer, Integer> factor = new Pair<>(i, 0);
            boolean factorFound = false;
            while (n % i == 0) {
                factorFound = true;
                factor.setSecond(factor.getSecond() + 1);
                n /= i;
            }
            if (factorFound)
                factors.add(factor);
        }
        return factors;
    }
    
    /**
     * Euler's totient (phi) function.
     * @param n Positive integer
     * @return Value of Euler's totient function for n
     */
    public static int eulerPhi(final int n) {
        positiveCheck(n);
        if (n == 1)
            return 1;
        
        int phi = 1;
        final List<Pair<Integer, Integer>> factors = factorize(n);
        for (int i = 0; i < factors.size(); ++i) {
            final int prime = factors.get(i).getFirst();
            final int exponent = factors.get(i).getSecond();
            phi *= prime - 1;
            phi *= (int)Math.pow(prime, exponent - 1);
        }
        return phi;
    }

    /**
     * Mods 'a' to closest positive integer modulo 'n'.
     * Example: -74 % 5 = -4 % 5 = 1
     * @param a Integer
     * @param n Positive integer
     * @return a % n, smallest possible positive solution
     */
    public static int normalizeIntModulo(int a, final int n) {
        positiveCheck(n);
        a = a % n;
        return (a < 0 ? a + n : a);
    }
    
    /**
     * Solving a congruence ax ≡ b (mod n).
     * @param a Integer
     * @param b Integer
     * @param n Positive integer
     * @return Pair (solution, modulus) OR empty pair if no solutions exist
     * OR pair (0, 1) if infinite solutions exist.
     */
    public static Pair<Integer, Integer> linearCongruence(int a, int b, final int n) {
        positiveCheck(n);
        a = normalizeIntModulo(a, n);
        b = normalizeIntModulo(b, n);
        if (((a == 0) && (b == 0)) || (n == 1))
            return new Pair<>(0, 1);
        
        final int numberOfSolutions = gcd(a, n);
        if (b % numberOfSolutions != 0)
            return new Pair<>();
        
        int bezoutCoefficientA; // coeffient r such that ra + sn = gcd(a, n)
        if (a > n)
            bezoutCoefficientA = bezout(a, n).get(1);
        else
            bezoutCoefficientA = bezout(n, a).get(2);

        final int shiftedModulus = n / numberOfSolutions;
        int x = (bezoutCoefficientA * b) / numberOfSolutions;
        x = normalizeIntModulo(x, shiftedModulus);
        return new Pair<>(x, shiftedModulus);
    }

    /**
     * Solving a system of 'i' congruences of type a_i x ≡ b_i (mod n_i).
     * See http://en.wikipedia.org/wiki/Linear_congruence_theorem#System_of_linear_congruences
     * @param count Number of equations, positive integer
     * @param aInList List of integers
     * @param bInList List of integers
     * @param nInList List of positive integers
     * @return Pair (solution, modulus) OR empty pair if no solutions exist
     * OR pair (0, 1) if infinite solutions exist.
     */
    public static Pair<Integer, Integer> linearCongruenceSystem(final int count,
            final List<Integer> aInList, final List<Integer> bInList, final List<Integer> nInList) {
        
        positiveCheck(count);
        listCheck(count, aInList);
        listCheck(count, bInList);
        listCheck(count, nInList);
        final List<Integer> aList = new ArrayList<>(aInList);
        final List<Integer> bList = new ArrayList<>(bInList);
        final List<Integer> nList = new ArrayList<>(nInList);
        for (int i = 0; i < count; ++i) {
            final int ai = aList.get(i);
            final int bi = bList.get(i);
            final int ni = nList.get(i);
            positiveCheck(ni);
            aList.set(i, normalizeIntModulo(ai, ni));
            bList.set(i, normalizeIntModulo(bi, ni));
        }
        
        final Pair<Integer, Integer> solution = new Pair<>();
        for (int i = 0; i < count; ++i) {
            final Pair<Integer, Integer> partial =
                    linearCongruence(aList.get(i), bList.get(i), nList.get(i));
            if (partial.isEmpty()) // no solutions for whole system
                return new Pair<>(); 
            if (partial.equals(new Pair<>(0, 1))) { // infinite solutions for congruence 'i'
                if ((solution.isEmpty()) && (Utils.lastForCycle(i, count)))
                    return new Pair<>(0, 1);
                
                continue;
            }
            
            // xi == partialX + partialM * k
            final int partialX = partial.getFirst();
            final int partialM = partial.getSecond();
            if (solution.isEmpty()) {
                solution.setFirst(partialX);
                solution.setSecond(partialM);
            } else {
                solution.setFirst(solution.getFirst() + solution.getSecond() * partialX);
                solution.setSecond(solution.getSecond() * partialM);
            }
            
            if (!Utils.lastForCycle(i, count)) {
                bList.set(i+1, bList.get(i+1) - (aList.get(i+1) * solution.getFirst()));
                aList.set(i+1, aList.get(i+1) * solution.getSecond());
            }
        }
        return solution;
    }

    /**
     * @param n Integer > 1
     * @return List of elements of unit group Zn^× in ascending order
     */
    public static List<Integer> elementsOfUnitGroup(final int n) {
        notLessThanCheck(n, 2);
        final List<Integer> result = new ArrayList<>();
        if (isPrime(n)) {
            for (int i = 1; i < n; ++i)
                result.add(i);
        } else {
            for (int i = 1; i < n; ++i)
                if (isCoprime(i, n))
                    result.add(i);
        }
        return result;
    }
    
    /**
     * @param element Positive integer
     * @param n Integer > 1
     * @return Order of element 'element' in group Zn×
     */
    public static int unitGroupElementOrder(final int element, final int n) {
        positiveCheck(element);
        notLessThanCheck(n, 2);
        if (element == 1)
            return 1;
        
        final List<Integer> unitGroup = elementsOfUnitGroup(n);
        if (!unitGroup.contains(element))
            throw new IllegalArgumentException(element + " is not an element of Z" + n + "×");
        
        final List<Integer> possibleElementOrders = divisors(unitGroup.size());
        for (int i = 0; i < possibleElementOrders.size(); ++i) {
            final int currentOrder = possibleElementOrders.get(i);
            if (modPow(element, currentOrder, n) == 1)
                return currentOrder;
        }
        return Integer.MAX_VALUE;
    }
    
    /**
     * Modular exponentiation.
     * @param base Integer
     * @param exp Non-negative integer
     * @param mod Positive integer
     * @return (base^exp) mod (mod)
     */
    public static int modPow(int base, final int exp, final int mod) {
        notNegativeCheck(exp);
        positiveCheck(mod);
        base = normalizeIntModulo(base, mod);
        if (mod == 1)
            return 0;
        if (base == 0)
            return ((exp == 0) ? 1 : 0);
        if ((base == 1) || (exp == 0))
            return 1;
        if (base == -1)
            return ((exp % 2 == 0) ? 1 : -1 + mod);
        if (exp == 1)
            return base;
                    
        final BigInteger bigBase = intToBigInteger(base);
        final BigInteger bigExp = intToBigInteger(exp);
        final BigInteger bigMod = intToBigInteger(mod);
        return bigBase.modPow(bigExp, bigMod).intValue();
    }
    
    private static BigInteger intToBigInteger(final int n) {
        return new BigInteger(((Integer)n).toString());
    }
    
    /**
     * Computes the Legendre Symbol.
     * @param a Integer
     * @param p Odd prime
     * @return 1, 0 or -1
     */
    public static int legendreSymbol(final int a, final int p) {
        isPrimeCheck(p, true);
        int value = modPow(a, (p-1)/2, p);
        if (value == p - 1)
            value = -1;
        return value;
    }
    
    /**
     * @param n Integer
     * @return Is given integer a square of another integer?
     */
    public static boolean isPerfectSquare(final int n) {
        if (n < 0)
            return false;
        final int test = (int)(Math.sqrt(n));
        return (test * test) == n;
    }
    
    /**
     * @param base Integer > 1
     * @param limit Integer > 1
     * @return The highest power of 'base' smaller than 'limit'
     */
    public static int findHighestPowerLessThan(final int base, final int limit) {
        notLessThanCheck(base, 2);
        notLessThanCheck(limit, 2);
        int x = 1;
        while ((x * base) < limit)
            x *= base;
        return x;
    }
    
    /**
     * @param start Integer
     * @return The next power of 4 from 'start'
     * or start itself if it is a power of 4
     */
    public static int findNextHigherPowerOf4(final int start) {
        if (start < 1)
            return 1;
        if (isPowerOf4(start))
            return start;
        
        return 4 * findHighestPowerLessThan(4, start);
    }
    
    /**
     * Solves the quadratic congruence of form x^2 ≡ a (mod m).
     * @param a Integer
     * @param m Positive integer
     * @return List of solutions in ascending order ending with modulus,
     * e.g. [x1, x2, ..., xn, m]
     */
    public static List<Integer> quadraticCongruenceSimple(int a, int m) {
        positiveCheck(m);
        a = normalizeIntModulo(a, m);
        if (a == 0) {
            if (isPerfectSquare(m))
                m = (int)Math.sqrt(m);
            else if ((isPowerOf2(m)) && (m > 2))
                m = (int)Math.sqrt(findNextHigherPowerOf4(m));
            else if ((m % 2 == 0) && ((m/2 * m/2) % m == 0))
                m = m/2;
            return new ArrayList<>(Arrays.asList(0, m));
        }
        
        // Handle powers of 2 separately :(
        if (isPowerOf2(m)) {
            if (m < 16) {
                if (a < 2)
                    return new ArrayList<>(Arrays.asList(a, 2));
                if ((a == 4) && (m == 8))
                    return new ArrayList<>(Arrays.asList(2, 4));
                
                return new ArrayList<>();
            }
            
            // 0, 1, 4, 9, 16, 17, 25, 32, 33... are quadratic residues mod 2^k
            if (a % 8 == 1)
                return quadraticCongruenceSimpleSolve(a, m);
            if (!isPowerOf4(a))
                return new ArrayList<>();
            
            m = m/2;
            final int x1 = (int)Math.sqrt(a);
            final int limit = findHighestPowerLessThan(4, m);
            if (a == limit) {
                m = (int)Math.sqrt(findNextHigherPowerOf4(m));
                return new ArrayList<>(Arrays.asList(x1, m));
            }
            if ((a == limit/4) && (isPowerOf4(m))) {
                m = (int)Math.sqrt(m);
                return new ArrayList<>(Arrays.asList(x1, m));
            }
            
            m = m/2;
            final List<Integer> result = new ArrayList<>();
            quadraticCongruenceSimpleFillSolution(result, x1, m);
            result.add(m);
            return result;
        }
        
        /**
         * Let m = m1 * m2 * ... * mn = p1^e1 * p2^e2 * ... * pn^en.
         * The input congruence is equivalent to system of congruences:
         * x^2 ≡ a (mod p1), x^2 ≡ a (mod p2), ..., x^2 ≡ a (mod pn).
         * (The exponents are negligible, since p^e|a => p|a.)
         * Now each subcongruence must have a solution (=> the corresponding
         * Legendre symbol is 0 or 1).
         */
        final List<Pair<Integer,Integer>> factorization = factorize(m);
        for (int i = 0; i < factorization.size(); ++i) {
            final int pi = factorization.get(i).getFirst();
            if (pi == 2)
                continue;
            if (legendreSymbol(a, pi) == -1) // => no solutions to whole system
                return new ArrayList<>();
        }
        
        // Try to find solution with halved modulus
        if (m % 2 == 0) {
            boolean halfModSolution = true;
            final int halfMod = m/2;
            final List<Integer> test = quadraticCongruenceSimpleSolve(a, halfMod);
            for (int i = 0; i < test.size() - 1; ++i) {
                final int testXi = test.get(i) + halfMod;
                if (((testXi * testXi) % m) != a) {
                    halfModSolution = false;
                    break;
                }
            }
            if (halfModSolution) {
                if (a != halfMod)
                    return test;
                final List<Integer> result = new ArrayList<>();
                for (int i = 0; i < test.size() - 1; ++i) {
                    final int x1 = test.get(i) + halfMod;
                    quadraticCongruenceSimpleFillSolution(result, x1, m);
                    if (result.size() == 2)
                        break;
                }
                if (!result.isEmpty()) {
                    Collections.sort(result);
                    result.add(m);
                }
                return result;
            }
        }
        
        return quadraticCongruenceSimpleSolve(a, m);
    }
    
    /**
     * Helper method only for quadraticCongruenceSimple().
     * Tries to find solution by force.
     */
    private static List<Integer> quadraticCongruenceSimpleSolve(int a, final int m) {
        a = normalizeIntModulo(a, m);
        int testA = a;
        int testM = m;
        if (isPowerOf2(m))
            testM = m/2;
        
        final List<Integer> result = new ArrayList<>();
        for (int i = 0; i < testM; ++i) {
            if (isPerfectSquare(testA)) {
                final int x1 = (int)Math.sqrt(testA);
                if (modPow(x1, 2, m) == a)
                    quadraticCongruenceSimpleFillSolution(result, x1, testM);
                if (result.size() == 4)
                    break;
            }
            testA += testM;
        }
        
        if (!result.isEmpty()) {
            Collections.sort(result);
            result.add(testM);
            if ((result.size() == 5) && (m % 2 == 0))  // 5 = 4 + 1 for mod
                return binomialCongruenceRuleOut(result);
        }
        return result;
    }
    
    /**
     * Helper method only for quadraticCongruenceSimple().
     * Fills the input collection with results.
     */
    private static void quadraticCongruenceSimpleFillSolution(
            final List<Integer> result, final int x1, final int m) {
        final int x2 = normalizeIntModulo(-x1, m);
        if (!result.contains(x1))
            result.add(x1);
        if (!result.contains(x2))
            result.add(x2);
    }
    
    /**
     * Helper method for quadraticCongruenceSimpleSolve() and binomialCongruence().
     * Rules out equivalent solutions for even modulus.
     */
    private static List<Integer> binomialCongruenceRuleOut(final List<Integer> result) {
        if (result.isEmpty())
            return result;
        
        final int resultSize = result.size();
        final int m = result.get(resultSize - 1); // last item is modulus
        if (m % 2 == 1)
            return result;
        
        final int testM = m/2;         
        final int x1 = normalizeIntModulo(result.get(0), testM); // first solution
        final List<Integer> testResult = new ArrayList<>(Arrays.asList(x1));
        for (int i = 1; i < resultSize - 1; ++i) {
            final int xi = normalizeIntModulo(result.get(i), testM);
            final int terminator = testResult.size();
            for (int j = 0; j < terminator; ++j) {
                if (xi == testResult.get(j))
                    break;
                if (Utils.lastForCycle(j, terminator))
                    testResult.add(xi);
            }
        }
        
        if (testResult.size() < resultSize - 1) {
            testResult.add(testM);
            return testResult;
        }
        return result;
    }
 
    /**
     * Solves the quadratic congruence of form ax^2 + bx + c ≡ 0 (mod m).
     * @param a Integer
     * @param b Integer
     * @param c Integer
     * @param m Positive integer coprime with a
     * @return List of solutions in ascending order ending with modulus, 
     * e.g. [x1, x2, ..., xn, m]
     */
    public static List<Integer> quadraticCongruenceGeneral(int a, int b, int c, int m) {
        positiveCheck(m);
        isCoprimeCheck(a, m);
        if (m == 1)
            return new ArrayList<>(Arrays.asList(0, m));
        
        a = normalizeIntModulo(a, m);
        b = normalizeIntModulo(b, m);
        c = normalizeIntModulo(c, m);
        if (a == 0) {
            final Pair<Integer, Integer> linear = linearCongruence(b, -c, m);
            return new ArrayList<>(Arrays.asList(linear.getFirst(), linear.getSecond()));
        }
        
        final List<Integer> subResult = quadraticCongruenceSimple(b*b - 4*a*c, 4*m);
        if (subResult.isEmpty())
            return new ArrayList<>();
        
        final List<Integer> generated = quadraticCongruenceGeneralGenerate(subResult, m);
        if (generated.isEmpty())
            return new ArrayList<>();
        
        final int terminator = generated.size() - 1;
        final List<Integer> result = new ArrayList<>();
        for (int i = 0; i < terminator; ++i) {
            final int t = generated.get(i);
            if (((t - b) % 2) != 0)
                throw new RuntimeException("Computational error.");
            
            final Pair<Integer, Integer> partial = linearCongruence(a, (t - b)/2, m);
            if (partial.isEmpty())
                return new ArrayList<>();
            if ((partial.equals(new Pair<>(0, 1))) && (!Utils.lastForCycle(i, terminator)))
                continue;
            
            final int xi = partial.getFirst();
            if (!result.contains(xi))
                result.add(xi);
            m = partial.getSecond();
        }
        
        if (!result.isEmpty()) {
            Collections.sort(result);
            result.add(m);
        }
        return result;
    }
    
    /**
     * Generates a solution for quadratic congruence of form ax^2 + bx + c ≡ 0
     * (mod m) from its sub-result. Called only from quadraticCongruenceGeneral()
     * and quadraticCongruenceGeneralSteps().
     * @param subResult Solution of simple quadratic congruence of form
     * t^2 ≡ b*b - 4*a*c (mod 4*m)
     * @param m Input modulus m
     * @return Solution for input quadratic congruence ax^2 + bx + c ≡ 0 (mod m)
     */
    public static List<Integer> quadraticCongruenceGeneralGenerate(
            final List<Integer> subResult, final int m) {
        
        final int currentMod = subResult.get(subResult.size() - 1);
        final int requiredMod = 2*m;
        if (currentMod >= requiredMod)
            return subResult;
        
        int generator = subResult.get(0);
        final List<Integer> generated = new ArrayList<>();
        final int generatedSize = requiredMod / currentMod;
        for (int i = 0; i < generatedSize; ++i) {
            generated.add(generator);
            generator += currentMod;
        }
        generated.add(requiredMod);
        return generated;
    }
    
    /**
     * @param m Integer > 1
     * @return Do primitive roots mod m exist?
     */
    public static boolean primitiveRootsExist(final int m) {
        notLessThanCheck(m, 2);
        if ((m == 2) || (m == 4) || (isPrime(m)))
            return true;
        
        final List<Pair<Integer, Integer>> factorization = factorize(m);
        final int factSize = factorization.size();
        if (factSize > 2)
            return false;
        
        final Pair<Integer, Integer> a = factorization.get(0);
        final int aFirst = a.getFirst();
        if (factSize == 1) // m must be a power of odd prime
            return (aFirst > 2) && (isPrime(aFirst));
        
        // factSize == 2, m must be double of a power of odd prime
        final Pair<Integer, Integer> b = factorization.get(1);
        final int bFirst = b.getFirst();
        return (aFirst == 2) && (a.getSecond() == 1) &&
                (bFirst > 2) && (isPrime(bFirst));
    }

    /**
     * Solves the binomial congruence of form x^n ≡ a (mod m).
     * @param n Positive integer
     * @param a Integer
     * @param m Positive integer
     * @return List of solutions modulo m in ascending order ending with modulus,
     * e.g. [x1, x2, ..., xn, m]
     */
    public static List<Integer> binomialCongruence(final int n, int a, final int m) {
        positiveCheck(n);
        positiveCheck(m);
        a = normalizeIntModulo(a, m);
        if (m == 1)
            return new ArrayList<>(Arrays.asList(0, 1));
        if (n == 1) {
            final Pair<Integer, Integer> linear = linearCongruence(1, a, m);
            return new ArrayList<>(Arrays.asList(linear.getFirst(), linear.getSecond()));
        }
        if (n == 2)
            return quadraticCongruenceSimple(a, m);
        
        int maxNumberOfSolutions = m;
        if ((isCoprime(a, m)) && (primitiveRootsExist(m))) {
            final int phiM = eulerPhi(m);
            final int d = gcd(n, phiM);
            if (modPow(a, phiM / d, m) != 1)
                return new ArrayList<>();
            maxNumberOfSolutions = d;
        }
        return binomialCongruenceSolve(n, a, m, maxNumberOfSolutions);
    }
    
    /**
     * Helper method only for binomialCongruence().
     * Tries to find solution by force.
     */
    private static List<Integer> binomialCongruenceSolve(final int n, final int a,
            final int m, final int maxSolutions) {
        
        final List<Integer> result = new ArrayList<>();
        for (int i = 0; i < m; ++i) {
            if (modPow(i, n, m) == a) {
                result.add(i);
                if (result.size() == maxSolutions)
                    break;
            }
        }
        if (!result.isEmpty())
            result.add(m);
        return binomialCongruenceRuleOut(result);
    }
    
    /**
     * Generates modulus for quadratic (simple and general) and binomial congruences.
     * @param odd Allow only odd primes (do not allow even modulus 2*p)
     * @return m which is a product of 2 different odd primes, min. 6, max. 95
     */
    public static int generateModulus(final boolean odd) {
        final int[] primes = new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29,
            31, 37, 41, 43, 47};

        // start on index 0, 1, 2, 3
        final int startingIndex = randInt(odd ? 1 : 0, 3);  
        // end max. on index 14, 10, 7, 5
        int maxFinishingIndex = 13 - startingIndex * 3;     
        if (startingIndex == 0 || startingIndex == 3)
            ++maxFinishingIndex;
        final int finishingIndex = randInt(startingIndex + 1, maxFinishingIndex);

        return primes[startingIndex] * primes[finishingIndex];
    }
    
    /**
     * Decomposes a permutation into a set of independent cycles.
     * @param inputPerm Permutation (a subgroup of Sn for n >= 1)
     * @return Independent cycles into which the input decomposes
     */
    public static Set<List<Integer>> permutationCycles(final List<Integer> inputPerm) {
        permutationCheck(inputPerm);
        final List<Integer> indicesToVisit = new ArrayList<>();
        for (int i = 0; i < inputPerm.size(); ++i)
            indicesToVisit.add(i);
        
        final Set<List<Integer>> result = new HashSet<>();
        List<Integer> currentPerm = new ArrayList<>();
        int i = 0;
        while (!indicesToVisit.isEmpty()) {
            if (!indicesToVisit.contains(i))    // Already visited this index
                i = indicesToVisit.get(0);      // Move to next index
            
            indicesToVisit.remove((Integer)i);  // Object i, not object on index i
            final int element = inputPerm.get(i);
            if (currentPerm.isEmpty())
                currentPerm.add(i + 1);
            if (currentPerm.contains(element)) {// End of cycle
                if (currentPerm.size() > 1)     // Do not include id's in result
                    result.add(currentPerm);
                currentPerm = new ArrayList<>();
                continue;
            }
            currentPerm.add(element);
            i = element - 1;
        }
        return result;
    }
    
    /**
     * @param cycles Permutation given by its independent cycles
     * @return Order of a permutation
     */
    public static int permutationOrder(final Set<List<Integer>> cycles) {
        int order = 1;
        for (List<Integer> cycle : cycles)
            order = lcm(order, cycle.size());
        return order;
    }
    
    /**
     * @param inputPerm Permutation (a subgroup of Sn for n >= 1)
     * @return Order of a permutation given by its cycles
     */
    public static int permutationOrder(final List<Integer> inputPerm) {
        final Set<List<Integer>> cycles = permutationCycles(inputPerm);
        return permutationOrder(cycles);
    }
    
    /**
     * Generates a random permutation of given size
     * @param size Positive integer
     * @return Permutation with 'size' elements
     */
    public static List<Integer> randPermutation(final int size) {
        positiveCheck(size);
        final List<Integer> permutation = new ArrayList<>();
        for (int i = 1; i < size + 1; ++i)
            permutation.add(i);
        Collections.shuffle(permutation);
        return permutation;
    }
    
    /*** CHECKS ***/
    
    /**
     * Throws exception if a < lowerBound.
     * @param a Integer
     * @param lowerBound Integer
     */
    public static void notLessThanCheck(final int a, final int lowerBound) {
        if (a < lowerBound)
            throw new IllegalArgumentException("Input argument " + a +
                    " should not be smaller than " + lowerBound);
    }
    
    /**
     * Throws exception if a is negative.
     * @param a Integer
     */
    public static void notNegativeCheck(final int a) {
        notLessThanCheck(a, 0);
    }
    
    /**
     * Throws exception if a < 1.
     * @param a Integer
     */
    public static void positiveCheck(final int a) {
        notLessThanCheck(a, 1);
    }
    
    /**
     * Throws exception if a > upperBound.
     * @param a Integer
     * @param upperBound Integer
     */
    public static void notGreaterThanCheck(final int a, final int upperBound) {
        if (a > upperBound)
            throw new IllegalArgumentException("Input argument " + a +
                    " should not be greater than " + upperBound);
    }
    
    /**
     * Throws exception if a == 0.
     * @param a Integer
     */
    public static void notZeroCheck(final int a) {
        if (a == 0)
            throw new IllegalArgumentException("Expecting non-zero argument.");
    }
    
    /**
     * Throws exception if a is not prime.
     * @param a Integer
     * @param odd Should 'a' be odd?
     */
    public static void isPrimeCheck(final int a, final boolean odd) {
        if ((odd) && (a == 2))
            throw new IllegalArgumentException(a + " is not an odd prime.");
        if (!isPrime(a))
            throw new IllegalArgumentException(a + " is not a prime.");
    }
    
    /**
     * Throws exception if a, b are not coprime.
     * @param a Integer
     * @param b Integer
     */
    public static void isCoprimeCheck(final int a, final int b) {
        if (!isCoprime(a, b))
            throw new IllegalArgumentException(a + ", " + b + " are not coprime.");
    }
    
    /**
     * Throws exception if size of inputList is different than count
     * @param count Non-negative integer
     * @param inputList List of integers
     */
    public static void listCheck(final int count, final List<Integer> inputList) {
        notNegativeCheck(count);
        final int inputListSize = inputList.size();
        if (inputListSize != count)
            throw new IllegalArgumentException("inputList size: " + inputListSize
                    + ", expected: " + count);
    }
    
    /**
     * Throws exception if the difference between max and min is
     * Integer.MAX_VALUE or more.
     * @param min Integer
     * @param max Integer >= min
     */
    public static void overflowCheck(final int min, final int max) {
        notLessThanCheck(max, min);
        long result = ((long) max) - min;
        if (result >= Integer.MAX_VALUE) {
             throw new RuntimeException("Overflow occured");
        } else if (result <= Integer.MIN_VALUE) {
             throw new RuntimeException("Underflow occured");
        }
    }
    
    /**
     * Throws exception if the input list is not a permutation.
     * @param perm List of integers
     */
    public static void permutationCheck(final List<Integer> perm) {
        final List<Integer> permCopy = new ArrayList<>(perm);
        Collections.sort(permCopy);
        for (int i = 0; i < permCopy.size(); ++i) {
            if (permCopy.get(i) != i + 1)
                throw new IllegalArgumentException(perm + " is not a permutation.");
        }
    }
}
