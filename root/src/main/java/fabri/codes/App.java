package fabri.codes;

import java.io.*;
import com.linuxense.javadbf.*;

public class App {
    public static void main(String[] args) {
        String dbfFilePath = "path/to/your/dbf/file.dbf";
        String csvFilePath = "path/to/save/csv/file.csv";

        try {
            // Leia o arquivo DBF
            InputStream inputStream = new FileInputStream(dbfFilePath);
            DBFReader reader = new DBFReader(inputStream);

            // Pega os nomes das colunas
            int numColumns = reader.getFieldCount();
            String[] columnNames = new String[numColumns];
            for (int i = 0; i < numColumns; i++) {
                com.linuxense.javadbf.DBFField field = reader.getField(i);
                columnNames[i] = field.getName();
            }

            // Captura os tipos de cada coluna
            char[] columnTypes = new char[numColumns];
            for (int i = 0; i < numColumns; i++) {
                com.linuxense.javadbf.DBFField field = reader.getField(i);
                columnTypes[i] = (char) field.getDataType();
            }

            // Inicializa a soma de cada coluna
            double[] columnSums = new double[numColumns];

            // Le os registros e calcula a soma de cada coluna
            Object[] record;
            while ((record = reader.nextRecord()) != null) {
                for (int i = 0; i < numColumns; i++) {
                    if (columnTypes[i] == 'N') { // Coluna numérica
                        double value = ((Number) record[i]).doubleValue();
                        columnSums[i] += value;
                    }
                }
            }

            // Fecha o leitor do DBF
            ((Closeable) reader).close();

            // Grava o resultado em um arquivo CSV
            FileWriter writer = new FileWriter(csvFilePath);
            writer.append(String.join(",", columnNames));
            writer.append("\n");
            for (int i = 0; i < numColumns; i++) {
                writer.append(Double.toString(columnSums[i]));
                if (i < numColumns - 1) {
                    writer.append(",");
                }
            }
            writer.flush();
            writer.close();

            System.out.println("Arquivo CSV com a soma das colunas numéricas foi criado.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
