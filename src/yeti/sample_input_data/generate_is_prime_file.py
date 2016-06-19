# coding=utf8
import math
from random import randint


def is_prime(n):
    if n % 2 == 0 and n > 2:
        return False
    return all(n % i for i in range(3, int(math.sqrt(n)) + 1, 2))


def generate():
    random_number = randint(0, 2147483647)
    with open('is_prime', 'w') as f:
        f.write('ISPRIME\n')
        f.write('1\n')
        f.write(str(random_number) + '\n')
    if is_prime(random_number):
        print random_numer, 'is prime number'
    else:
        print random_number, 'is not prime numebr'


generate()
