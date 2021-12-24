# ParallelBFS

### First launch
#### without parallel scan
```cpp
Block size: 100
Cube size: 250
Attempts count: 5
```
```cpp
Parallel BFS times: 18281 ms, 18725 ms, 19368 ms, 18090 ms, 16367 ms.
Sequence BFS times: 8624 ms, 8899 ms, 10199 ms, 7576 ms, 7627 ms.

Average parallel BFS time: 18166 ms.
Average sequence BFS time: 8585 ms.

Parallel BFS 0.4725861499504569 times faster than Sequence BFS.
```

#### with parallel scan
```cpp
Block size: 1000
Cube size: 250
Attempts count: 5
```
```cpp
Parallel BFS times: 20412 ms, 20195 ms, 18188 ms, 18625 ms, 18887 ms.
Sequence BFS times: 8702 ms, 7638 ms, 7675 ms, 9492 ms, 9390 ms.

Average parallel BFS time: 19261 ms.
Average sequence BFS time: 8579 ms.

Parallel BFS 0.44540781890867553 times faster than Sequence BFS.
```

### Second launch
#### without parallel scan
```cpp
Block size: 1000
Cube size: 100
Attempts count: 5
```
```cpp
Parallel BFS times: 460 ms, 374 ms, 357 ms, 452 ms, 336 ms.
Sequence BFS times: 363 ms, 378 ms, 269 ms, 250 ms, 252 ms.

Average parallel BFS time: 395 ms.
Average sequence BFS time: 302 ms.

Parallel BFS 0.7645569620253164 times faster than Sequence BFS.
```

#### with parallel scan
```cpp
Block size: 1000
Cube size: 100
Attempts count: 5
```
```cpp
Parallel BFS times: 673 ms, 387 ms, 379 ms, 464 ms, 386 ms.
Sequence BFS times: 366 ms, 298 ms, 265 ms, 254 ms, 249 ms.

Average parallel BFS time: 457 ms.
Average sequence BFS time: 286 ms.

Parallel BFS 0.6258205689277899 times faster than Sequence BFS.
```