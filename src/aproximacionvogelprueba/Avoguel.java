package aproximacionvogelprueba;

public class Avoguel {
    private double[][] costos;
    private double[] OFERTA;
    private double[] DEMANDA;
    private double[] penalizacionesFilas;
    private double[] penalizacionesColumnas;
    private double[][] asignaciones; // Matriz para almacenar las asignaciones
    private double[][] matrizPenalizaciones; // Matriz para cálculos de penalizaciones

    public void CalcularAproximacion(double[][] costos, double[] OFERTA, double[] DEMANDA) {
        this.costos = costos;
        this.OFERTA = OFERTA;
        this.DEMANDA = DEMANDA;
        this.asignaciones = new double[costos.length][costos[0].length]; // Inicializar la matriz de asignaciones
        this.matrizPenalizaciones = new double[costos.length][costos[0].length]; // Inicializar matriz de penalizaciones

        // Inicializar la matriz de penalizaciones con la matriz de costos
        for (int i = 0; i < costos.length; i++) {
            System.arraycopy(costos[i], 0, matrizPenalizaciones[i], 0, costos[i].length);
        }

        while (hayOfertaYDemanda(OFERTA, DEMANDA)) {
            Penalizacion(matrizPenalizaciones);
            realizarAsignacion();
        }
    }

    public double[][] getAsignaciones() {
        return asignaciones;
    }

    private boolean hayOfertaYDemanda(double[] OFERTA, double[] DEMANDA) {
        for (double oferta : OFERTA) {
            if (oferta > 0) {
                return true;
            }
        }
        for (double demanda : DEMANDA) {
            if (demanda > 0) {
                return true;
            }
        }
        return false;
    }

    public void Penalizacion(double[][] costos) {
        int numFilas = costos.length;
        int numColumnas = costos[0].length;

        this.penalizacionesFilas = new double[numFilas];
        this.penalizacionesColumnas = new double[numColumnas];

        for (int i = 0; i < numFilas; i++) {
            double min1 = Double.MAX_VALUE;
            double min2 = Double.MAX_VALUE;

            if (OFERTA[i] > 0) {
                for (int j = 0; j < numColumnas; j++) {
                    double costoActual = costos[i][j];

                    if (costoActual < min1) {
                        min2 = min1;
                        min1 = costoActual;
                    } else if (costoActual < min2 && costoActual != min1) {
                        min2 = costoActual;
                    }
                }

                // Almacenar la penalización para la fila
                penalizacionesFilas[i] = min2 - min1;
            } else {
                penalizacionesFilas[i] = Double.NEGATIVE_INFINITY;
            }
        }

        // Similar para columnas
        for (int j = 0; j < numColumnas; j++) {
            double min1 = Double.MAX_VALUE;
            double min2 = Double.MAX_VALUE;

            if (DEMANDA[j] > 0) {
                for (int i = 0; i < numFilas; i++) {
                    double costoActual = costos[i][j];

                    if (costoActual < min1) {
                        min2 = min1;
                        min1 = costoActual;
                    } else if (costoActual < min2 && costoActual != min1) {
                        min2 = costoActual;
                    }
                }

                penalizacionesColumnas[j] = min2 - min1;
            } else {
                penalizacionesColumnas[j] = Double.NEGATIVE_INFINITY;
            }
        }
    }

    public void realizarAsignacion() {
        double max = obtenerMayor();
        int filaMayor = -1;
        int columnaMayor = -1;

        // Determinar la fila o columna correspondiente al mayor
        for (int i = 0; i < penalizacionesFilas.length; i++) {
            if (penalizacionesFilas[i] == max) {
                filaMayor = i;
                break;
            }
        }

        if (filaMayor == -1) { // Si no se encontró en filas, buscar en columnas
            for (int j = 0; j < penalizacionesColumnas.length; j++) {
                if (penalizacionesColumnas[j] == max) {
                    columnaMayor = j;
                    break;
                }
            }
        }

        // Asignar la mayor cantidad en el menor costo
        if (filaMayor != -1) {
            double minCosto = Double.MAX_VALUE;
            int indexMinCosto = -1;

            // Encontrar el índice del menor costo
            for (int j = 0; j < costos[filaMayor].length; j++) {
                if (DEMANDA[j] > 0) { // Solo considerar columnas con demanda
                    if (costos[filaMayor][j] < minCosto) {
                        minCosto = costos[filaMayor][j];
                        indexMinCosto = j;
                    }
                }
            }

            // Asignar la oferta máxima en el menor costo de la fila
            if (indexMinCosto != -1) {
                double cantidadASignar = Math.min(OFERTA[filaMayor], DEMANDA[indexMinCosto]); // Asignar lo que se puede
                asignaciones[filaMayor][indexMinCosto] = cantidadASignar; // Almacenar la asignación
                matrizPenalizaciones[filaMayor][indexMinCosto] = 0; // Marcar la posición como asignada

                // Actualizar oferta y demanda
                OFERTA[filaMayor] -= cantidadASignar; // Restar la cantidad asignada a la oferta
                DEMANDA[indexMinCosto] -= cantidadASignar; // Restar la cantidad asignada a la demanda

                // Verificar si se asignó toda la oferta o demanda
                if (OFERTA[filaMayor] == 0) {
                    for (int j = 0; j < matrizPenalizaciones[filaMayor].length; j++) {
                        matrizPenalizaciones[filaMayor][j] = Double.MAX_VALUE; // Marcar fila como no asignable
                    }
                }
                if (DEMANDA[indexMinCosto] == 0) {
                    for (int i = 0; i < matrizPenalizaciones.length; i++) {
                        matrizPenalizaciones[i][indexMinCosto] = Double.MAX_VALUE; // Marcar columna como no asignable
                    }
                }
            }
        } else if (columnaMayor != -1) {
            double minCosto = Double.MAX_VALUE;
            int indexMinCosto = -1;

            // Encontrar el índice del menor costo
            for (int i = 0; i < costos.length; i++) {
                if (OFERTA[i] > 0) { // Solo considerar filas con oferta
                    if (costos[i][columnaMayor] < minCosto) {
                        minCosto = costos[i][columnaMayor];
                        indexMinCosto = i;
                    }
                }
            }

            // Asignar la demanda máxima en el menor costo de la columna
            if (indexMinCosto != -1) {
                double cantidadASignar = Math.min(OFERTA[indexMinCosto], DEMANDA[columnaMayor]); // Asignar lo que se puede
                asignaciones[indexMinCosto][columnaMayor] = cantidadASignar; // Almacenar la asignación
                matrizPenalizaciones[indexMinCosto][columnaMayor] = 0; // Marcar la posición como asignada

                // Actualizar oferta y demanda
                DEMANDA[columnaMayor] -= cantidadASignar; // Restar la cantidad asignada a la demanda
                OFERTA[indexMinCosto] -= cantidadASignar; // Restar la cantidad asignada a la oferta

                // Verificar si se asignó toda la oferta o demanda
                if (DEMANDA[columnaMayor] == 0) {
                    for (int i = 0; i < matrizPenalizaciones.length; i++) {
                        matrizPenalizaciones[i][columnaMayor] = Double.MAX_VALUE; // Marcar columna como no asignable
                    }
                }
                if (OFERTA[indexMinCosto] == 0) {
                    for (int j = 0; j < matrizPenalizaciones[indexMinCosto].length; j++) {
                        matrizPenalizaciones[indexMinCosto][j] = Double.MAX_VALUE; // Marcar fila como no asignable
                    }
                }
            }
        }
    }

    public double obtenerMayor() {
        double max = Double.NEGATIVE_INFINITY;

        for (double valor : penalizacionesFilas) {
            if (valor > max) {
                max = valor;
            }
        }

        for (double valor : penalizacionesColumnas) {
            if (valor > max) {
                max = valor;
            }
        }

        return max;
    }

    public void imprimirMatriz(double[][] matriz) {
        for (double[] fila : matriz) {
            for (double valor : fila) {
                System.out.printf("%10.2f", valor); // Formato con 2 decimales
            }
            System.out.println(); // Nueva línea al final de cada fila
        }
    }
    public String calcularCostosAsignaciones() {
    StringBuilder expresion = new StringBuilder();
    double total = 0.0;
    
    int numFilas = costos.length;
    int numColumnas = costos[0].length;

    for (int i = 0; i < numFilas; i++) {
        for (int j = 0; j < numColumnas; j++) {
            double costo = costos[i][j];
            double asignacion = asignaciones[i][j];

            if (asignacion > 0) {
                if (expresion.length() > 0) {
                    expresion.append("+");
                }
                expresion.append(costo).append("*").append(asignacion);
                total += costo * asignacion; // Sumar el resultado de la multiplicación
            }
        }
    }

    // Devolver la expresión y el total
    return expresion + " = " + total;
}

}

