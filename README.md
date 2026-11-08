# Assignment 4. Patient Zero

Student ID: 250846
Name: Sabidoldanov Nursultan
Group: SE-2512

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment4.pdf`.

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

    Mutations: 99
    Shared core: 1026
    Marker hits: 6
    First hit: 6967
    Border length: 3
    Shared words: 365
    Tests needed: 40
    Cheapest testing: 187
    Longest chain: 32

My outbreak report (a screenshot of `output/outbreak.html`):

![outbreak](output/outbreak.png)

## Questions

Answer in your own words, two or three sentences each.

Mission 1. The cell $d[i][j]$ represents the minimum edit distance required to transform the prefix of strain A of length $i$ into the prefix of strain B of length $j$. It is computed by taking the minimum of three values: $d[i-1][j-1]$ plus 0 if the characters match or 1 if they differ (substitution), $d[i-1][j] + 1$ (deletion), and $d[i][j-1] + 1$ (insertion).

Mission 2. A longest common subsequence consists of characters that appear in both strains in the same relative order, whereas a longest common substring requires those matching characters to be strictly contiguous. The "Shared core" count is larger than the number of exact character matches at identical index positions because insertions and deletions offset character alignment, allowing non-contiguous matching sequences to be tracked across different positions.

Mission 3. The prefix function $\pi[i]$ indicates the length of the longest proper prefix of the pattern $P[0..i]$ that is also a suffix of $P[0..i]$. In KMP, the text pointer never backtracks because when a mismatch occurs, the prefix function determines how much of the matched prefix can be reused, shifting the pattern forward instead. The "Border length" is the value $\pi[m-1]$ of the marker, which represents the length of the longest proper prefix of the marker that matches its own suffix.

Mission 4. Character-by-character comparison is necessary after hash values match because different 12-letter sequence windows can produce identical hash values due to hash collisions. If many windows share the same hash value, Rabin-Karp spends excessive time performing full string comparisons for false alarms, causing its time complexity to degrade toward worst-case $O(N \cdot M)$.

Mission 5. The two DP states for a person $u$ are "TESTED" (person $u$ is tested) and "NOT_TESTED" (person $u$ is skipped). If $u$ is not tested, every child of $u$ must be tested to ensure the infection link is covered; if $u$ is tested, each child can independently be chosen as whichever state yields the smaller cost. A greedy strategy of testing the node with the most links fails because testing a high-degree node with an extremely high cost might be far more expensive than testing all of its lower-cost neighbors.

## AI use

The syllabus requires you to say which AI tools you used and for what. Write the tool (ChatGPT, Copilot, other) and what you asked it: explain an idea, find an error, review code. Write "none" if you did not use any. If you took code or text from a book or a website, give the source here.
Tool: Gemini
Usage: Used to explain dynamic programming recurrence relations

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test | size | time |
|---|---|---|
| naive search | 20 000 / 40 000 / 80 000 | 71.8 ms / 132.1 ms / 296.2 ms |
| KMP | 20 000 / 40 000 / 80 000 | 3.5 ms / 2.1 ms / 0.7 ms |
| edit distance | 500 / 1000 / 2000 / 4000 | 11.1 ms / 9.8 ms / 26.7 ms / 160.7 ms |

What do the numbers say about how the time grows? When you double the size, by how much does each time grow? Two or three sentences.
For naive search, quadrupling the text size from $20\,000$ to $80\,000$ increases the running time by approximately $4\times$ (from $71.8\text{ ms}$ to $296.2\text{ ms}$), reflecting $O(N \cdot M)$ scaling, whereas KMP maintains minimal execution times regardless of text growth due to its optimal $O(N + M)$ complexity. Edit distance demonstrates quadratic scaling ($O(N^2)$), where doubling the sequence length from $2000$ to $4000$ causes the execution time to jump significantly from $26.7\text{ ms}$ to $160.7\text{ ms}$.

## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people. Fill in every algorithm you wrote.

| algorithm | time | space | assumptions |
|---|---|---|---|
| Edit distance | $O(n_1 \cdot n_2)$ | $O(n_1 \cdot n_2)$ | $n_1, n_2$ are strain lengths |
| LCS | $O(n_1 \cdot n_2)$ | $O(n_1 \cdot n_2)$ | $n_1, n_2$ are strain lengths |
| Prefix function and KMP | $O(n + m)$ | $O(m)$ | $n$ is genome length, $m$ is marker length |
| Rabin-Karp (shared words) | $O(n_1 + n_2)$ average | $O(n_2)$ | Rolling hash with $O(1)$ lookup, low collisions |
| Tree DP | $O(v)$ | $O(v)$ | $v$ is number of people in the tree |

## Before you submit

- The four files in `data` and the screenshot `output/outbreak.png` are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
