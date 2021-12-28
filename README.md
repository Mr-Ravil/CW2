# ParallelBFS

### First launch
```cpp
Parallel for block size: 20000
Scan block size: 50000
Cube size: 200
Attempts count: 5
```
```cpp
Parallel BFS times: 1047 ms, 893 ms, 1055 ms, 985 ms, 1287 ms.
Sequence BFS times: 3534 ms, 3364 ms, 2757 ms, 3141 ms, 3093 ms.

Average parallel BFS time: 1053 ms.
Average sequence BFS time: 3177 ms.

Parallel BFS 3.017094017094017 times faster than Sequence BFS.
```

### Second launch
```cpp
Parallel for block size: 2000
Scan block size: 5000
Cube size: 200
Attempts count: 5
```
```cpp
Parallel BFS times: 871 ms, 728 ms, 968 ms, 886 ms, 799 ms.
Sequence BFS times: 3234 ms, 2838 ms, 3068 ms, 2848 ms, 2650 ms.

Average parallel BFS time: 850 ms.
Average sequence BFS time: 2927 ms.

Parallel BFS 3.4435294117647057 times faster than Sequence BFS.
```

### Third launch
```cpp
Parallel for block size: 2000
Scan block size: 5000
Cube size: 300
Attempts count: 5
```
```cpp
Parallel BFS times: 3238 ms, 3128 ms, 2937 ms, 2975 ms, 2758 ms.
Sequence BFS times: 11755 ms, 10571 ms, 11208 ms, 10955 ms, 10794 ms.

Average parallel BFS time: 3007 ms.
Average sequence BFS time: 11056 ms.

Parallel BFS 3.676754240106418 times faster than Sequence BFS.
```
