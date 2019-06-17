import numpy as np
import matplotlib.pyplot as plt

PATHS = [
    '../data/{}_20.0_0.0_0.03_1.0_2D_order.csv',
    '../data/{}_20.0_0.1_0.03_1.0_2D_order.csv',
    '../data/{}_20.0_0.2_0.03_1.0_2D_order.csv',
    '../data/{}_20.0_0.4_0.03_1.0_2D_order.csv',
    '../data/{}_20.0_0.8_0.03_1.0_2D_order.csv',
    '../data/{}_20.0_1.0_0.03_1.0_2D_order.csv'
]
LABELS = [
    'Noise = 0',
    'Noise = 0.2 PI',
    'Noise = 0.4 PI',
    'Noise = 0.8 PI',
    'Noise = 1.6 PI',
    'Noise = 1 PI'
]

for i in range(len(PATHS)):
    orders = []
    densities = []
    for n in [k * 50 for k in range(1, 21)]:
        file_orders = np.genfromtxt(PATHS[i].format(n))
        orders.append(file_orders[-1])
        densities.append(n / 400)

    orders = np.array(orders)
    ma = orders.copy()
    ma[0] *= 5
    ma[1] *= 4
    ma[2] *= 3
    ma[3] *= 2
    ma[1:] += orders[:-1]
    ma[2:] += orders[:-2]
    ma[3:] += orders[:-3]
    ma[4:] += orders[:-4]
    ma /= 5
    plt.plot(densities, ma, label=LABELS[i])
    plt.scatter(densities, ma)

plt.xlabel('Densidad')
plt.ylabel('Orden [Va]')
plt.legend()
plt.show()
