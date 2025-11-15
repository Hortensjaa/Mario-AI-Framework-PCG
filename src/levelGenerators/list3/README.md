# List 3 - offline search-based generator
My goal will be to implement Mario level offline search-based generator that will pass some tests 
(goal is to be passable by very smart agent + have the highest possible number of specials and obstacles). 
## Steps:
- Define structures (pipes, blocks, etc.) 
- [-] Implement simulated annealing
  - [+] Define structures (pipes, blocks, etc.) and random generator of them
  - [+] Define mutations (small - change parameters of the structure, big - add/remove structure)
  - [-] Define cooling schedule
  - [-] Merge it all together
- [-] Implement fitness function
  - [-] bonus points for number of obstacles and specials
  - [-] penalty for too long jumps and too much density of enemies in 5 tiles fragment
  - `fitness =
      + 3 * number_of_enemies
      + 1 * number_of_coins
      + 1 * number_of_jumps
      + 1 * special structures
      - 10 * impassable gaps
      - 10 * impassable jumps
      - 5 * too_dense_fragments`
  - [-] later: simulate the level with an agent and highly penalize if impassable
