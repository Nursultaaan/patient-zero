import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */
    public static int[] prefixFunction(String p) {
        int m = p.length();
        int[] pi = new int[m];
        int j = 0;
        for (int i = 1; i < m; i++) {
            while (j > 0 && p.charAt(i) != p.charAt(j)) j = pi[j - 1];
            if (p.charAt(i) == p.charAt(j)) j++;
            pi[i] = j;
        }
        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */
    public static int[] findAll(String text, String p) {
        if (p == null || p.isEmpty() || text == null || text.length() < p.length()) return new int[0];
        int[] pi = prefixFunction(p);
        List<Integer> matches = new ArrayList<>();
        int n = text.length(), m = p.length(), j = 0;
        for (int i = 0; i < n; i++) {
            while (j > 0 && text.charAt(i) != p.charAt(j)) j = pi[j - 1];
            if (text.charAt(i) == p.charAt(j)) j++;
            if (j == m) {
                matches.add(i - m + 1);
                j = pi[j - 1];
            }
        }
        return matches.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */
    public static int sharedWords(String a, String b, int k) {
        if (a.length() < k || b.length() < k) return 0;
        long prime = 31, mod = 1_000_000_007L, h = 1;
        for (int i = 0; i < k - 1; i++) h = (h * prime) % mod;

        Map<Long, List<String>> bHashes = new HashMap<>();
        long currentHashB = 0;
        for (int i = 0; i < k; i++) currentHashB = (currentHashB * prime + b.charAt(i)) % mod;
        bHashes.computeIfAbsent(currentHashB, x -> new ArrayList<>()).add(b.substring(0, k));

        for (int i = 1; i <= b.length() - k; i++) {
            currentHashB = (currentHashB - (b.charAt(i - 1) * h) % mod + mod) % mod;
            currentHashB = (currentHashB * prime + b.charAt(i + k - 1)) % mod;
            bHashes.computeIfAbsent(currentHashB, x -> new ArrayList<>()).add(b.substring(i, i + k));
        }

        Set<Integer> countedPositions = new HashSet<>();
        long currentHashA = 0;
        for (int i = 0; i < k; i++) currentHashA = (currentHashA * prime + a.charAt(i)) % mod;

        if (bHashes.containsKey(currentHashA)) {
            String window = a.substring(0, k);
            for (String candidate : bHashes.get(currentHashA)) {
                if (candidate.equals(window)) { countedPositions.add(0); break; }
            }
        }

        for (int i = 1; i <= a.length() - k; i++) {
            currentHashA = (currentHashA - (a.charAt(i - 1) * h) % mod + mod) % mod;
            currentHashA = (currentHashA * prime + a.charAt(i + k - 1)) % mod;
            if (bHashes.containsKey(currentHashA)) {
                String window = a.substring(i, i + k);
                for (String candidate : bHashes.get(currentHashA)) {
                    if (candidate.equals(window)) { countedPositions.add(i); break; }
                }
            }
        }
        return countedPositions.size();
    }
}
