import numpy as np
import random

def gen_pos():
    poslist = []
    index = 0
    while(True):
        x = random.randint(0, 34)
        y = random.randint(0, 34)
        app = True
        for each in poslist:
            distx = np.abs(x - each[0])
            disty = np.abs(y - each[1])
            if distx + disty < 5:
                app = False
        if app:
            index += 1
            poslist.append([x,y])
        else:
            continue
        if index >= 14:
            break
    return poslist

poslist = gen_pos()

def gen_matrix(poslist):
    matrix = np.zeros((36,36))
    for each in poslist:
        matrix[each[0], each[1]] = 1
        matrix[each[0]+1, each[1]] = 1
        matrix[each[0], each[1]+1] = 1
        matrix[each[0]+1, each[1]+1] = 1
    return matrix

def gen_txt(filename, matrix):
    with open(filename, 'w') as fin:
        for i in range(matrix.shape[0]):
            for j in range(matrix.shape[1]):
                if matrix[i, j] == 0:
                    fin.write(".")
                else:
                    fin.write("0")
                if j < 35:
                    fin.write(" ")
            fin.write('\n')

matrix = gen_matrix(poslist)
gen_txt('dangers3_big.txt', matrix)
