# List 1

## Task 1
Create a level that is always passed by sergeyKarakovskiy agent and contains at least 10 enemies and 10 pits
(tiles where Mario can fall out of the level).

**Solution:** This agent always jumps, so just adjust placement of pits and enemies for this jump pattern.

## Task 2
Create a level that is always passed by spencerSchumann agent but not by the trondEllingsen agent.

**Solution:** spencerSchumann is another "greedy-jumper", and trondEllingsen is a "lazy-jumper"
(jumps only if needed on exact height), so we can adjust space between bullet shooters to punish him.

## Task 3
Create a level where any of the agents collects at least 50 coins, and all these coins need to be placed in the air

**Solution:** Just place coins in the air and add obstacles to force "lazy-jumping" agents to reach them.

## Task 4
Create a level where any of the agents collects three upgrade mushrooms.

**Solution (almost):** 
Trigger the supersizing mushroom dispenser -> force Mario to wait for it by blocking movement by bricks ->
destroy bricks after eating mushroom -> meet enemy to be smaller again -> repeat two more times.

## Task 5, 6
Bonus point for creating a level that is impossible to pass by the robinBaumgarten agent – but is possible to pass
overall (e.g. by a human).
Bonus point for creating a level that is impossible to pass by the robinBaumgarten, but some other agent can pass it.

**Solution:** robinBaumgarten is not that cautious, and doesn't cosider state change after eating a mushroom.
So, instead of waiting for mushroom, it will fall to pit full of spikies. On the other hand, very cautious andySloane
will get mushroom and then use other way by breaking bricks.