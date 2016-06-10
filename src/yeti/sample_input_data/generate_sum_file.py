# coding=utf8
from random import randint


def generate(lines):
    sum = 0
    with open('sum', 'w') as f:
        f.write('SUM\n')
        f.write(str(lines) + '\n')
        for _ in xrange(lines):
            random_number = randint(0, 10**9)
            sum += random_number
            f.write(str(random_number) + '\n')
    print 'Sum  should be:', sum


generate(500000)
