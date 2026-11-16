# Chocolate-Chilli-Game-Machine-Learning-Program
Chocolate Chilli is a two-player game where a human competes against a computer, this project uses machine learning so that the computer improves it's strategy and it wins most of the time


Run the learning version with `javac chocolateChilli.java` followed by
`java chocolateChilli`.

Each chocolate count has a `GameState` record-style class instance with three
Boolean move flags. The computer randomly chooses an enabled legal move. After
a loss, its final decision is disabled at that state. If all moves at a state
are disabled, the computer resigns; its previous decision leading there is then
disabled. Earlier moves in a losing round are kept available.

`learning[game]` keeps the actual moves, alternating computer and human, without
adding a move for resignation. `numOfWins[game]` is 1 for a computer win and 0 for
a human win (including resignation). Counts and win percentages are shown after
each round. History and learned flags last for the current program session.
