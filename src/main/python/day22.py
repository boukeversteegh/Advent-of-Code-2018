target = 12, 757  # x,y
mouth = (0, 0)
depth = 3198
GI = {}
EL = {}
types = ['.', '=', '|']  # rocky wet narrow


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


import collections
import sys

grid = collections.defaultdict(lambda: {})

risklevel = 0
for y in xrange(mouth[1], target[1] + 1):
    for x in xrange(mouth[0], target[0] + 1):
        t = region_type((x, y))
        grid[y][x] = t
        risklevel += t

def show():
    for y in xrange(mouth[1], target[1] + 1):
        for x in xrange(mouth[0], target[0] + 1):
            t = grid[y][x]
            if (x, y) == mouth:
                sys.stdout.write('M')
            elif (x, y) == target:
                sys.stdout.write('T')
            else:
                sys.stdout.write(types[t])
        print


print risklevel
