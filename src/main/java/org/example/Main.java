package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String csvFileInsert = "src/insert.csv";
        String csvFileUpdate = "src/update.csv";
        String insert = "src/insert.txt";
        String update = "src/update.txt";
        String insertRollback = "src/insertRollback.txt";
        String updateRollback = "src/updateRollback.txt";
        try(FileReader frInsert = new FileReader(csvFileInsert);
            FileReader frUpdate = new FileReader(csvFileUpdate);
            FileWriter fw1 = new FileWriter(insert);
            FileWriter fw2 = new FileWriter(insertRollback);
            FileWriter fw3 = new FileWriter(update);
            FileWriter fw4 = new FileWriter(updateRollback);
        ) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("rule_id","parent_rule_id","type","value")
                    .setSkipHeaderRecord(true)
                    .build();
            Iterable<CSVRecord> records = csvFormat.parse(frInsert);
            String insertSQL,insertRollbackSQL;
            for (CSVRecord record : records) {
                if(record.get(1).equals("null") && record.get(3).equals("null")) {
                    insertSQL = String.format("insert into VUX_MANDATORY_SUBJECT_RULE25 (DEF_SUBJECT_KEY,DEF_SCOPE,RULE_ID,PARENT_RULE_ID,TYPE,VALUE,ACTIVE) values ('ENGE','VOCATIONAL_PROGRAM','%s',%s,'%s',%s,'1');%n", record.get(0), record.get(1), record.get(2), record.get(3));
                    insertRollbackSQL = String.format("delete from VUX_MANDATORY_SUBJECT_RULE25 where rule_id=%s AND DEF_SUBJECT_KEY = 'ENGE' AND def_scope = 'VOCATIONAL_PROGRAM' and parent_rule_id is %s;%n", record.get(0), record.get(1));
                }else if (record.get(3).equals("null")) {
                    insertSQL = String.format("insert into VUX_MANDATORY_SUBJECT_RULE25 (DEF_SUBJECT_KEY,DEF_SCOPE,RULE_ID,PARENT_RULE_ID,TYPE,VALUE,ACTIVE) values ('ENGE','VOCATIONAL_PROGRAM','%s','%s','%s',%s,'1');%n",record.get(0),record.get(1),record.get(2),record.get(3));
                    insertRollbackSQL = String.format("delete from VUX_MANDATORY_SUBJECT_RULE25 where rule_id=%s AND DEF_SUBJECT_KEY = 'ENGE' AND def_scope = 'VOCATIONAL_PROGRAM' and parent_rule_id = '%s';%n",record.get(0),record.get(1));
                } else {
                    insertSQL = String.format("insert into VUX_MANDATORY_SUBJECT_RULE25 (DEF_SUBJECT_KEY,DEF_SCOPE,RULE_ID,PARENT_RULE_ID,TYPE,VALUE,ACTIVE) values ('ENGE','VOCATIONAL_PROGRAM','%s','%s','%s','%s','1');%n",record.get(0),record.get(1),record.get(2),record.get(3));
                    insertRollbackSQL = String.format("delete from VUX_MANDATORY_SUBJECT_RULE25 where rule_id=%s AND DEF_SUBJECT_KEY = 'ENGE' AND def_scope = 'VOCATIONAL_PROGRAM' and parent_rule_id = '%s';%n",record.get(0),record.get(1));
                }

                fw1.write(insertSQL);
                fw2.write(insertRollbackSQL);
            }
            csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("rule_id","parent_rule_id","previous_rule_id")
                    .setSkipHeaderRecord(true)
                    .build();
            records = csvFormat.parse(frUpdate);
            for (CSVRecord record : records) {
                String updateSQL = String.format("update VUX_MANDATORY_SUBJECT_RULE25 set parent_rule_id=%s where rule_id=%s AND DEF_SUBJECT_KEY = 'ENG' AND def_scope = 'VOCATIONAL_PROGRAM' and parent_rule_id is %s;%n",record.get(1),record.get(0),record.get(2));
                String updateRollbackSQL = String.format("update VUX_MANDATORY_SUBJECT_RULE25 set parent_rule_id=%s where rule_id=%s AND DEF_SUBJECT_KEY = 'ENG' AND def_scope = 'VOCATIONAL_PROGRAM' and parent_rule_id=%s;%n",record.get(2),record.get(0),record.get(1));
                fw3.write(updateSQL);
                fw4.write(updateRollbackSQL);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
