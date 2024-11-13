
package aproximacionvogelprueba;


public class AproximacionVogelPrueba {
    
    
    public static void main(String[] args) {
        Avoguel avoguel = new Avoguel();
        double[][] costos = {
            {1250,1380,1000},
            {950,1230,840},
            {1520,1420,1360}
        };
        double[] oferta = {12,25,16};
        double[] demanda = {15,20,18};

        avoguel.CalcularAproximacion(costos, oferta, demanda);
        avoguel.imprimirMatriz(avoguel.getAsignaciones());
        String resultado = avoguel.calcularCostosAsignaciones();
System.out.println(resultado);
        //String resultado = avoguel.calcularCostosYGenerarString();
    }
    
     public static void Penalizacion(double[][] costos) {
    int numFilas = costos.length;
    int numColumnas = costos[0].length;
    
    double[] penalizacionesFilas = new double[numFilas];
    double[] penalizacionesColumnas = new double[numColumnas];
    
    // Calcular penalizaciones para las filas
    for (int i = 0; i < numFilas; i++) {
        double min1 = Double.MAX_VALUE;
        double min2 = Double.MAX_VALUE;

        for (int j = 0; j < numColumnas; j++) {
            double costoActual = costos[i][j];

            if (costoActual < min1) {
                min2 = min1;
                min1 = costoActual;
            } else if (costoActual < min2) {
                min2 = costoActual;
            }
        }

        // Almacenar la penalización para la fila
        penalizacionesFilas[i] = min2 - min1;
    }

    // Calcular penalizaciones para las columnas
    for (int j = 0; j < numColumnas; j++) {
        double min1 = Double.MAX_VALUE;
        double min2 = Double.MAX_VALUE;

        for (int i = 0; i < numFilas; i++) {
            double costoActual = costos[i][j];

            if (costoActual < min1) {
                min2 = min1;
                min1 = costoActual;
            } else if (costoActual < min2) {
                min2 = costoActual;
            }
        }

        // Almacenar la penalización para la columna
        penalizacionesColumnas[j] = min2 - min1;
    }

    // Imprimir o devolver las penalizaciones
    System.out.println("Penalizaciones de las filas:");
    for (double penalizacion : penalizacionesFilas) {
        System.out.println(penalizacion);
    }

    System.out.println("Penalizaciones de las columnas:");
    for (double penalizacion : penalizacionesColumnas) {
        System.out.println(penalizacion);
    }
    double max=obtenerMayor(penalizacionesFilas,penalizacionesColumnas);
         System.out.println(max);
}
     
     public static double obtenerMayor(double[] penalizacionesFilas, double[] penalizacionesColumnas) {
        double max = Double.NEGATIVE_INFINITY; // Iniciar con el menor valor posible

        // Comparar valores de penalizacionesFilas
        for (double valor : penalizacionesFilas) {
            if (valor > max) {
                max = valor;
            }
        }

        // Comparar valores de penalizacionesColumnas
        for (double valor : penalizacionesColumnas) {
            if (valor > max) {
                max = valor;
            }
        }

        return max; // Retornar el valor máximo encontrado
    }
    
}
