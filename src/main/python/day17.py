import re
import collections
import sys

grid = collections.defaultdict(lambda: {})


class Stream:
    def __init__(self, x, y, d):
        self.d = d
        self.x = x
        self.y = y


streams = set()


def load_file_coordinates(file_name):
    for line in file(file_name):
        minX, maxX = re.search(r'x=(\d+)(?=\.\.(\d+))?', line.strip()).groups()
        minY, maxY = re.search(r'y=(\d+)(?=\.\.(\d+))?', line.strip()).groups()

        minX, minY = int(minX), int(minY)
        maxX, maxY = (int(maxX) if maxX is not None else minX), \
                     (int(maxY) if maxY is not None else minY)

        for y in xrange(minY, maxY + 1):
            for x in xrange(minX, maxX + 1):
                grid[y][x] = '#'
    streams.add(Stream(500, 0, '+'))


def load_file_ascii(file_name):
    y = 0
    for line in file(file_name):
        x = 0
        for c in line:
            if c in ['#', '.', ' ']:
                grid[y][x] = '#' if c == '#' else '.'
            if c == '+':
                streams.add(Stream(x, y, '+'))
            x += 1
        y += 1


load_file_ascii('../../../inputs/17ascii.txt')

minY = min(grid.keys())
maxY = max(grid.keys())
maxX = max([max(row.keys()) for row in grid.values()])
minX = min([min(row.keys()) for row in grid.values()])

blue = u'\u001b[34;1m'
bg_blue = u'\u001b[44;1m'
red = u'\u001b[32;1m'
cyan = u'\u001b[34;1m'
reset = '\033[0m'

water = u'\u001b[34;44;1m'
flow = u'\u001b[36;46;1m'
stream = u'\u001b[37m'
sand = u'\u001b[38;5;131m'
clay = u'\u001b[48;5;59;1m\u001b[38;5;59m'


def print_color(d, clr):
    sys.stdout.write("%s%s%s" % (clr, d, reset))


last_vMaxY = 0
last_vMinY = 0


def show():
    global last_vMaxY, last_vMinY
    smap = {}
    for s in streams:
        smap[(s.x, s.y)] = s
    if streams:
        vMinY = min(min([s.y for s in smap.values()]) - 10, last_vMinY)
        vMaxY = max([s.y for s in smap.values()]) + 10
    else:
        vMinY = minY
        vMaxY = maxY

    last_vMinY = vMinY
    last_vMaxY = vMaxY
    print
    for y in xrange(0, vMaxY + 1):
        print_color(str(y).rjust(5, ' '), red)
        for x in xrange(minX - 1, maxX + 1):
            if (x, y) in smap:
                print_color(smap[(x, y)].d, stream)
            else:
                c = get(x, y)
                if c is '#':
                    print_color(c, clay)
                elif c is '.':
                    print_color(' ', sand)
                elif c is '|':
                    print_color(c, flow)
                else:
                    print_color(c, water)
        print
    print 'streams=', len(smap), 'water=', count_water(), 'still water=', count_water(
        ['~']), 'vMaxY/maxY=', vMaxY, maxY, 'minY-maxY', minY, maxY


def count_water(types=['|', '~']):
    count = 0
    for y, row in grid.items():
        for x, c in row.items():
            if c in types and minY <= y <= maxY:
                count += 1
    return count


iteration = 0


def debug(s):
    global iteration
    sys.stdout.write("\r%s: %s" % (iteration, str(s)))
    sys.stdout.flush()
    pass


def get(x, y):
    try:
        return grid[y][x]
    except KeyError:
        return '.'


def run():
    global streams
    nstreams = []
    for s in list(streams):
        x, y, d = s.x, s.y, s.d
        # debug("stream: %s, %s" % (x, y))
        if d is '+':
            grid[y][x] = '+'
            s.d = '|'
            s.y += 1
            continue

        try:
            below = grid[y + 1][x]
        except KeyError:
            below = '.'
        if below == '|':
            # debug("Remove stream %s %s" % (x, y))
            streams.remove(s)
            # debug("Done")
            continue

        if below in ['#', '~']:
            lx, rx = x, x
            while get(lx, y + 1) in ['~', '#'] and get(lx, y) in ['.', '|', '~']:
                lx -= 1
            while get(rx, y + 1) in ['~', '#'] and get(rx, y) in ['.', '|', '~']:
                rx += 1

            edges = (get(lx, y), get(rx, y))
            surface = '~' if edges == ('#', '#') else '|'
            for _x in xrange(lx + 1, rx):
                if grid[y - 1].get(_x, None) == '|':
                    streams.add(Stream(_x, y - 1, '|'))
                grid[y][_x] = surface

            if edges[1] != '#':
                nstreams.append(Stream(rx, y, '|'))
            if edges[0] != '#':
                nstreams.append(Stream(lx, y, '|'))

            # if surface is '|':
            streams.remove(s)

            # s.y -= 1

        _y = y
        while get(x, _y + 1) == '.' and _y < maxY:
            grid[_y][x] = '|'
            grid[_y + 1][x] = '|'
            _y += 1
            s.y += 1

    # debug("adding new streams %s" % len(nstreams))
    for nstream in nstreams:
        streams.add(nstream)

    global iteration
    iteration += 1
    debug("...")


# 3791
# for n in range(0, 410):
#     run()

print_color('', reset)

skip = 0
last_skip = 1
while True:
    run()
    if skip == 0:
        show()
        print_color("" + str(iteration) + "\n", red)

        skip_input = raw_input()
        if skip_input:
            skip = int(skip_input)
            last_skip = skip
        else:
            skip = last_skip
        print "skipping %s" % skip

    skip -= 1

# 30492 too low
# 30493 too low
# 30502 too high
