# coding=utf8
from random import randint


def generate(lines):
    with open('knapsack', 'w') as f:
        f.write('KNAPSACK\n')
        backpack = randint(0, 10 ** 9)
        f.write(str(backpack) + '\n')
        f.write(str(lines) + '\n')
        for _ in xrange(lines):
            capacity = randint(0, 10 ** 9)
            value = randint(0, 10 ** 9)
            f.write(str(capacity) + ' ' + str(value) + '\n')


generate(10)
