import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     * <p>
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        int[] parent = new int[n];
        List<Integer> order = new ArrayList<>();
        Arrays.fill(parent, -1);
        parent[0] = 0;
        order.add(0);

        for (int i = 0; i < order.size(); i++) {
            int v = order.get(i);
            for (int u : nb[v]) {
                if (parent[u] < 0) {
                    parent[u] = v;
                    order.add(u);
                }
            }
        }

        long[] takeCost = new long[n], skipCost = new long[n];
        for (int h = order.size() - 1; h >= 0; h--) {
            int v = order.get(h);
            takeCost[v] = cost[v];
            skipCost[v] = 0;
            for (int u : nb[v]) {
                if (u == parent[v] && v != 0) continue;
                takeCost[v] += Math.min(takeCost[u], skipCost[u]);
                skipCost[v] += takeCost[u];
            }
        }

        boolean[] tested = new boolean[n];
        tested[0] = takeCost[0] <= skipCost[0];
        for (int h = 1; h < order.size(); h++) {
            int v = order.get(h), p = parent[v];
            tested[v] = !tested[p] || takeCost[v] <= skipCost[v];
        }
        return tested;
    }

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        if (n <= 1) return 0;
        int[] parent = new int[n];
        List<Integer> order = new ArrayList<>();
        Arrays.fill(parent, -1);
        parent[0] = 0;
        order.add(0);

        for (int i = 0; i < order.size(); i++) {
            int v = order.get(i);
            for (int u : nb[v]) {
                if (parent[u] < 0) {
                    parent[u] = v;
                    order.add(u);
                }
            }
        }

        int maxChainValue = 0;
        int[] top1 = new int[n], top2 = new int[n];
        for (int h = order.size() - 1; h >= 0; h--) {
            int v = order.get(h);
            maxChainValue = Math.max(maxChainValue, top1[v] + top2[v]);
            if (v != 0) {
                int p = parent[v], len = top1[v] + 1;
                if (len > top1[p]) {
                    top2[p] = top1[p];
                    top1[p] = len;
                } else if (len > top2[p]) {
                    top2[p] = len;
                }
            }
        }
        return maxChainValue;
    }
}