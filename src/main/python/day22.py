import collections
import sys
from collections import defaultdict
from timeit import default_timer as timer
import heapq

t0 = timer()

# input
target = 12, 757  # x,y
mouth = (0, 0)
depth = 3198

# example
target = 10, 10
depth = 510

GI = {}
EL = {}
types = ['.', '=', '|']  # rocky wet narrow
rocky = 0
wet = 1
narrow = 2

tx, ty = target


def geologic_index(c):
    # The region at 0,0 (the mouth of the cave) has a geologic index of 0.
    if c == mouth:
        return 0
    # The region at the coordinates of the target has a geologic index of 0.
    if c == target:
        return 0

    x, y = c

    # If the region's Y coordinate is 0, the geologic index is its X coordinate times 16807.
    if y is 0:
        return x * 16807

    # If the region's X coordinate is 0, the geologic index is its Y coordinate times 48271.
    if x is 0:
        return y * 48271

    # Otherwise, the region's geologic index is the result of multiplying the erosion levels of the regions at X-1,Y and X,Y-1.
    return erosion_level((x - 1, y)) * erosion_level((x, y - 1))


def erosion_level(c):
    if c in EL:
        return EL[c]

    # A region's erosion level is its geologic index plus the cave system's depth, all modulo 20183. Then:
    EL[c] = (geologic_index(c) + depth) % 20183
    return EL[c]


def region_type(c):
    # If the erosion level modulo 3 is 0, the region's type is rocky.
    # If the erosion level modulo 3 is 1, the region's type is wet.
    # If the erosion level modulo 3 is 2, the region's type is narrow.
    return erosion_level(c) % 3


grid = collections.defaultdict(lambda: {})  # type: defaultdict[int, Dict[int, int]]


def g(x, y):
    try:
        return grid[y][x]
    except KeyError:
        t = region_type((x, y))
        grid[y][x] = t
        return t


for y in xrange(mouth[1], target[1] + 1):
    for x in xrange(mouth[0], target[0] + 1):
        t = region_type((x, y))
        grid[y][x] = t

torch = 0
climbing = 1
neither = 2

type_tools = {
    rocky: [climbing, torch],
    wet: [climbing, neither],
    narrow: [torch, neither]
}


class Node:
    def __init__(self, x, y, t, c):
        self.t = t
        self.y = y
        self.x = x
        self.c = c
        self.h = abs(x - tx) + abs(y - ty) - 1
        self.f = c + self.h

    def is_valid(self):
        if self.x < 0 or self.y < 0:
            return False
        return self.t in type_tools[g(self.x, self.y)]

    def is_goal(self):
        return self.x == tx and self.y == ty and self.t == torch

    def tuple(self):
        return self.x, self.y, self.t, self.c


cnode = None
nodes = [
    Node(mouth[0], mouth[1], torch, 0)
]

visited_nodes = set()


def expand(n):
    global nodes
    x, y, t, c = n.tuple()

    children = [
        Node(x + 1, y, t, c + 1),
        Node(x - 1, y, t, c + 1),
        Node(x, y + 1, t, c + 1),
        Node(x, y - 1, t, c + 1),
        Node(x, y, t, c + 1),
        Node(x, y, (t + 1) % 3, c + 7),
        Node(x, y, (t + 2) % 3, c + 7),
    ]
    new_children = [cn for cn in children if (cn.x, cn.y, cn.t) not in visited_nodes]

    for child in new_children:
        nodes.insert(0, child)
    # print len(nodes),'-',
    #
    # print '-', len(nodes)


def show():
    for y in xrange(mouth[1], target[1] + 1):
        for x in xrange(mouth[0], target[0] + 1):
            t = g(x, y)
            if (x, y) == mouth:
                sys.stdout.write('M')
            if cnode and (x, y) == (cnode.x, cnode.y):
                sys.stdout.write(['*', '^', '_'][cnode.t])
            elif (x, y) == target:
                sys.stdout.write('T')
            else:
                sys.stdout.write(types[t])
        print


show()

while len(nodes):
    T = min(len(nodes), 100)
    # nodes = sorted(nodes[:T], key=lambda _n: _n.f) + nodes[T:]

    nodes.sort(key=lambda _n: _n.f)
    n = nodes[0]
    cnode = n

    visited_nodes.add((n.x, n.y, n.t))

    if n.is_goal():
        print
        show()
        print {"goal": n.tuple()}
        break

    if n.is_valid():
        end = timer()
        rtime = end - t0
        print "\r#%s" % len(visited_nodes), (n.x, n.y, n.c, n.h), ('t=%d' % rtime), ('c/t=%02f' % (n.c / rtime)),
        expand(n)

    nodes.remove(n)

    # break
    # for node in nodes:
    #     print node.tuple()
