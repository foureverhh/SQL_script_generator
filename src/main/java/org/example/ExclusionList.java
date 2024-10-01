package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ExclusionList {
    public static void main(String[] args) {
        String csvExclusionSourceFile = "src/exclusionCSV/source.csv";

        String insertExclusion = "src/exclusionCSV/insertExclusion.txt";
        String insertExclusionRollback = "src/exclusionCSV/insertExclusionRollback.txt";
        try(FileReader frInsert = new FileReader(csvExclusionSourceFile);
            FileWriter fw1 = new FileWriter(insertExclusion);
            FileWriter fw2 = new FileWriter(insertExclusionRollback);
        ) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("reference_code", "excluded_code")
                    .setSkipHeaderRecord(true)
                    .build();
            Iterable<CSVRecord> records = csvFormat.parse(frInsert);
            String insertSQL,insertRollbackSQL;
            for (CSVRecord r : records) {

                insertSQL = String.format("insert into EXCLUSIONS_LIST (REFERENCE_CODE,EXCLUDED_CODE) values ('%s','%s');%n", r.get(0).trim(), r.get(1).trim());
                insertRollbackSQL = String.format("delete from EXCLUSIONS_LIST where REFERENCE_CODE='%s' AND EXCLUDED_CODE = '%s';%n",r.get(0).trim(), r.get(1).trim());


                fw1.write(insertSQL);
                fw2.write(insertRollbackSQL);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
