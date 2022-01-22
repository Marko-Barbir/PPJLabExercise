import os
import subprocess


def main():
    # root directory of tests folder
    rootdir = 'D:/5_SEMESTAR/PPJ/ppjlab/4-t/1-t'

    # root directory of simulator program
    simulatordir = 'D:/5_SEMESTAR/PPJ/ppjlab/frisc_simulator'

    # root directory of compiled classes
    outdir = 'D:/5_SEMESTAR/PPJ/ppjlab/out/production/ppjlab'

    # fully qualified class name
    classname = 'hr.fer.ppj.maniacs414.lab4.GeneratorKoda'

    subdirectories = [x[0] for x in os.walk(rootdir)]
    # remove root
    subdirectories = subdirectories[1:]

    success_counter = 0
    for i in range(len(subdirectories)):
        failed = False
        subdirectory = subdirectories[i]

        with open(subdirectory + '/test.in', 'r') as reader:
            inp = reader.read()

        with open(subdirectory + '/test.out', 'r') as reader:
            out = reader.read()

        try:
            generation = subprocess.run(['java', '-cp', outdir, classname],
                                        input=inp,
                                        stdout=subprocess.PIPE,
                                        stderr=subprocess.DEVNULL,
                                        timeout=2,
                                        text=True)
        except subprocess.TimeoutExpired:
            failed = True

        with open(subdirectory + '/a.frisc', 'w') as writer:
            writer.write(generation.stdout)

        try:
            simulation = subprocess.run(['node', simulatordir + '/main.js', subdirectory + '/a.frisc'],
                                        stdout=subprocess.PIPE,
                                        stderr=subprocess.DEVNULL,
                                        timeout=3,
                                        text=True)
        except subprocess.TimeoutExpired:
            #print('Simulation timeout')
            failed = True

        if failed or not simulation.stdout.strip() == out.strip():
            print(f'Test {i + 1}: FAIL')
        else:
            print(f'Test {i + 1}: PASS')
            success_counter += 1

    print('=' * 40)
    print(f'{success_counter}/{len(subdirectories)} tests have passed.')
    print(f'Success rate: {round(success_counter * 100/len(subdirectories), 2)}%')
    print('=' * 40)


if __name__ == '__main__':
    main()
