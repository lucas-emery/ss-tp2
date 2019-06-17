import numpy as np
import matplotlib.pyplot as plt

PATH = '../data/{}_10.0_0.4_0.03_1.0_2D_order.csv'


orders = []
densities = []
for n in ([k * 10 for k in range(1, 21)] + [200 + k * 20 for k in range(1, 16)] + [500 + k * 50 for k in range(1, 11)]):
    file_orders = np.genfromtxt(PATH.format(n))
    orders.append(file_orders[-1])
    densities.append(n / 100)

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
plt.plot(densities, ma, label='Noise = 0.8 PI')
plt.scatter(densities, ma)

plt.xlabel('Densidad')
plt.ylabel('Orden [Va]')
plt.legend()
plt.show()
