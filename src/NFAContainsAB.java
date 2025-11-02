import java.util.*;

public class NFAContainsAB {
    private static Map<Integer, Map<Character, Set<Integer>>> transitions = new HashMap<>();
    private static Map<String, Boolean> memo = new HashMap<>();

    public static void main(String[] args) {
        buildNFA();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Enter string of 'a' and 'b' (or 'exit' to quit):");
            System.out.print("Input: ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) break;

            if (!input.matches("[ab]*")) {
                System.out.println("Invalid input. Only 'a' and 'b' allowed.\n");
                continue;
            }

            memo.clear();
            boolean accepted = simulate(0, 0, input);
            System.out.print("Output: ");
            System.out.println(accepted ? "Accepted\n" : "Rejected\n");
        }

        sc.close();
    }

    private static boolean simulate(int pos, int state, String s) {
        if (pos == s.length()) {
            return state == 2; // accept if in state 2 at end of input
        }

        String key = pos + "#" + state;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        char ch = s.charAt(pos);
        Map<Character, Set<Integer>> map = transitions.get(state);
        if (map == null) {
            memo.put(key, false);
            return false;
        }

        Set<Integer> targets = map.get(ch);
        if (targets == null || targets.isEmpty()) {
            memo.put(key, false);
            return false;
        }

        for (int t : targets) {
            if (simulate(pos + 1, t, s)) {
                memo.put(key, true);
                return true;
            }
        }

        memo.put(key, false);
        return false;
    }

    private static void buildNFA() {
        addTransition(0, 'a', 1);
        addTransition(0, 'b', 0);

        addTransition(1, 'a', 1);
        addTransition(1, 'b', 2);

        addTransition(2, 'a', 2);
        addTransition(2, 'b', 2);
    }

    private static void addTransition(int from, char symbol, int to) {
        transitions.computeIfAbsent(from, k -> new HashMap<>())
                   .computeIfAbsent(symbol, k -> new HashSet<>())
                   .add(to);
    }
}
