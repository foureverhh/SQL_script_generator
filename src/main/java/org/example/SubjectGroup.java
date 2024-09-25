package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SubjectGroup {
    public static void main(String[] args) {
        String csvSubjectGroupInsertFile = "src/subjectGroup/insertSubjectGroup.csv";

        String insertSubjectGroup = "src/subjectGroup/insertSubjectGroup.txt";
        String insertSubjectGroupRollback = "src/subjectGroup/insertSubjectGroupRollback.txt";
        String subjectGroup = "SVEN,SVEA";

        try(FileReader frInsert = new FileReader(csvSubjectGroupInsertFile);


            FileWriter fw1 = new FileWriter(insertSubjectGroup);
            FileWriter fw2 = new FileWriter(insertSubjectGroupRollback);
        ) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("subject_code")
                    .setSkipHeaderRecord(true)
                    .build();
            Iterable<CSVRecord> records = csvFormat.parse(frInsert);
            String insertSQL,insertRollbackSQL;
            for (CSVRecord record : records) {

                    insertSQL = String.format("insert into SUBJECT_GROUPS (SUBJECT_CODE,SUBJECT_GROUP) values ('%s','%s');%n", record.get(0), subjectGroup);
                    insertRollbackSQL = String.format("delete from SUBJECT_GROUPS where SUBJECT_CODE='%s' AND SUBJECT_GROUP = '%s';%n",record.get(0), subjectGroup);


                fw1.write(insertSQL);
                fw2.write(insertRollbackSQL);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
