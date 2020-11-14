import matplotlib.pyplot as plt

if __name__ == '__main__':
    actual = []
    estimated = []
    with open('output/output.txt') as file:
        for line in file:
            splitted = line.split(',')
            actual.append(int(splitted[0]))
            estimated.append(int(splitted[1]))

    plt.plot(actual, estimated, 'o')
    plt.ylabel("Estimated")
    plt.xlabel("Actual")
    plt.title("Estimated vs Actual")
    plt.grid()
    # plt.show()
    plt.savefig('output/result.png')
