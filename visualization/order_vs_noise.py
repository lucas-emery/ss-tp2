import numpy as np
import matplotlib.pyplot as plt

PATHS = [
    '../data/40_3.16_{}_0.03_1.0_2D_order.csv',
    '../data/100_5.0_{}_0.03_1.0_2D_order.csv',
    '../data/400_10.0_{}_0.03_1.0_2D_order.csv',
    '../data/1000_15.81_{}_0.03_1.0_2D_order.csv'
]
LABELS = [
    'N = 40',
    'N = 100',
    'N = 400',
    'N = 1000'
]

for i in range(len(PATHS)):
    orders = []
    n_rs = []
    for n_r in [k / 50 for k in range(0, 51)]:
        file_orders = np.genfromtxt(PATHS[i].format(n_r))
        orders.append(file_orders[-1])
        n_rs.append(n_r * 6.28)

    orders = np.array(orders)
    ma = (orders[:-4] + orders[1:-3] + orders[2:-2] + orders[3:-1] + orders[4:]) / 5
    plt.plot(n_rs, np.concatenate([orders[:4], ma]), label=LABELS[i])
    plt.scatter(n_rs, np.concatenate([orders[:4], ma]))

plt.xlabel('Ruido en radianes')
plt.ylabel('Orden [Va]')
plt.legend()
plt.show()
