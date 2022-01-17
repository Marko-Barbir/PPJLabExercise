import filecmp
import os
import re
import subprocess


def main():
    rootdir = 'D:/5_SEMESTAR/PPJ/ppjlab/4-t/1-t'
    simulatordir = 'D:/5_SEMESTAR/PPJ/ppjlab/frisc_simulator'

    subdirectories = [x[0] for x in os.walk(rootdir)]
    # remove root
    subdirectories = subdirectories[1:]

    for i in range(len(subdirectories)):
        subdirectory = subdirectories[i]
        simulation = subprocess.run(['node', simulatordir + '/main.js', subdirectory + '/a.frisc'],
                                 stdout=subprocess.PIPE,
                                 stderr=subprocess.DEVNULL,
                                 universal_newlines=True)

        print(f'Result {subdirectory}: {simulation.stdout.strip()}')
        equal = filecmp.cmp(subdirectory + '/test.out', subdirectory + '/a.out', shallow=False)
        print(f'Test {i + 1}: {equal}')


if __name__ == '__main__':
    main()
