import re

bots = []
for line in file('../../../inputs/23.txt'):
    # pos=<0,0,0>, r=4
    x, y, z, r = map(lambda i: int(i), re.search(r'(-?\d+),(-?\d+),(-?\d+).+?(\d+)', line.strip()).groups())

    # print x, y, z, r
    bots.append((r, (x, y, z)))


def is_in_range(x, y, z, bot):
    br, (bx, by, bz) = bot
    return (abs(x - bx) + abs(y - by) + abs(z - bz)) <= br


tb = sorted(bots, reverse=True)[0]  # top bot
tr, (tx, ty, tz) = tb
in_range = [1 for (_, (x, y, z)) in bots if is_in_range(x, y, z, tb)]


print len(in_range)
exit()

def count_in_range(_x, _y, _z):
    return len([1 for bot in bots if is_in_range(_x, _z, _y, bot)])


X = [x for (_, (x, _, _)) in bots]
Y = [y for (_, (_, y, _)) in bots]
Z = [z for (_, (_, _, z)) in bots]
X = min(X), max(X)
Y = min(Y), max(Y)
Z = min(Z), max(Z)

print "X,Y,Z", X, Y, Z

RC = {}

for bot in bots:
    br, (bx, by, bz) = bot
    print bot
    for z in range(max(Z[0], bz - br), min(Z[1], bz + br) + 1):
        for y in xrange(max(Y[0], by - br), min(Y[1], by + br) + 1):
            for x in xrange(max(X[0], bx - br), min(X[1], bx + br) + 1):
                # print x, y, z
                rc, _ = RC.get((x, y, z), (0, (x, y, z)))
                RC[(x, y, z)] = (rc + 1, (x, y, z))
                # print "\r%s" % x,
        # break
    # break

bestRCs = sorted(RC.values(), reverse=True)

max_rc = bestRCs[0][0]

nearest = sorted([p for (rc, p) in bestRCs if rc == max_rc])
# for rc in nearest:
#     print rc
print '--'
print nearest[0]
