# ParallelBFS

### First launch
#### without parallel scan
```cpp
Block size: 1000
Cube size: 250
Attempts count: 5
```
```cpp
Parallel BFS times: 8703 ms, 8840 ms, 8702 ms, 7898 ms, 7500 ms.
Sequence BFS times: 10191 ms, 11902 ms, 12017 ms, 11014 ms, 8203 ms.

Average parallel BFS time: 8328 ms.
Average sequence BFS time: 10665 ms.

Parallel BFS 1.2806195965417868 times faster than Sequence BFS.
```

#### with parallel scan
```cpp
Block size: 1000
Cube size: 250
Attempts count: 5
```
```cpp
Parallel BFS times: 9993 ms, 7479 ms, 7243 ms, 8765 ms, 7130 ms.
Sequence BFS times: 10742 ms, 8867 ms, 9463 ms, 9229 ms, 7546 ms.

Average parallel BFS time: 8122 ms.
Average sequence BFS time: 9169 ms.

Parallel BFS 1.1289091356808667 times faster than Sequence BFS.
```

### Second launch
#### without parallel scan
```cpp
Block size: 100
Cube size: 250
Attempts count: 5
```
```cpp
Parallel BFS times: 7667 ms, 7482 ms, 7348 ms, 7310 ms, 7370 ms.
Sequence BFS times: 9042 ms, 7743 ms, 7401 ms, 7484 ms, 7434 ms.

Average parallel BFS time: 7435 ms.
Average sequence BFS time: 7820 ms.

Parallel BFS 1.0517821116341628 times faster than Sequence BFS.
```

#### with parallel scan
```cpp
Block size: 100
Cube size: 250
Attempts count: 5
```
```cpp
Parallel BFS times: 10254 ms, 8017 ms, 9338 ms, 7848 ms, 7686 ms.
Sequence BFS times: 9290 ms, 8044 ms, 9662 ms, 7927 ms, 10105 ms.

Average parallel BFS time: 8628 ms.
Average sequence BFS time: 9005 ms.

Parallel BFS 1.043694946685211 times faster than Sequence BFS.
```