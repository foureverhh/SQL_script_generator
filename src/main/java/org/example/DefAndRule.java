package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DefAndRule {
    public static void main(String[] args) {
        String csvRuleInsertFile = "src/DefAndRule/insertRule.csv";
        String csvRuleUpdateFile = "src/DefAndRule/updateRule.csv";
        String csvDefInsertFile = "src/DefAndRule/insertDef.csv";

        String insertRule = "src/DefAndRule/insertRule.txt";
        String updateRule = "src/DefAndRule/updateRule.txt";
        String insertDef = "src/DefAndRule/insertDef.txt";

        String insertRuleRollback = "src/DefAndRule/insertRuleRollback.txt";
        String updateRuleRollback = "src/DefAndRule/updateRuleRollback.txt";
        String insertDefRollback = "src/DefAndRule/insertDefRollback.txt";

        String gy25SubjectCode = "SVEN,SVEA";
        String gy11SubjectCode = "SVE,SVA";

        try(FileReader frInsertRule = new FileReader(csvRuleInsertFile);
            FileReader frUpdateRule = new FileReader(csvRuleUpdateFile);
            FileReader frInsertDef = new FileReader(csvDefInsertFile);

            FileWriter fw1 = new FileWriter(insertRule);
            FileWriter fw2 = new FileWriter(insertRuleRollback);
            FileWriter fw3 = new FileWriter(updateRule);
            FileWriter fw4 = new FileWriter(updateRuleRollback);
            FileWriter fw5 = new FileWriter(insertDef);
            FileWriter fw6 = new FileWriter(insertDefRollback);
        ) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("rule_id","parent_rule_id","type","value")
                    .setSkipHeaderRecord(true)
                    .build();
            Iterable<CSVRecord> records = csvFormat.parse(frInsertRule);
            String insertSQL,insertRollbackSQL;
            for (CSVRecord record : records) {
                if(record.get(1).equals("null") && record.get(3).equals("null")) {
                    insertSQL = String.format("insert into VUX_MANDATORY_SUBJECT_RULE25 (DEF_SUBJECT_KEY,DEF_SCOPE,RULE_ID,PARENT_RULE_ID,TYPE,VALUE,ACTIVE) values ('%s','VOCATIONAL_PROGRAM','%s',%s,'%s',%s,'1');%n", gy25SubjectCode, record.get(0), record.get(1), record.get(2), record.get(3));
                    insertRollbackSQL = String.format("delete from VUX_MANDATORY_SUBJECT_RULE25 where rule_id='%s' AND DEF_SUBJECT_KEY = '%s' AND def_scope = 'VOCATIONAL_PROGRAM' and parent_rule_id is %s;%n", gy25SubjectCode,record.get(0), record.get(1));
                }else if (record.get(3).equals("null")) {
                    insertSQL = String.format("insert into VUX_MANDATORY_SUBJECT_RULE25 (DEF_SUBJECT_KEY,DEF_SCOPE,RULE_ID,PARENT_RULE_ID,TYPE,VALUE,ACTIVE) values ('%s','VOCATIONAL_PROGRAM','%s','%s','%s',%s,'1');%n",gy25SubjectCode,record.get(0),record.get(1),record.get(2),record.get(3));
                    insertRollbackSQL = String.format("delete from VUX_MANDATORY_SUBJECT_RULE25 where rule_id='%s' AND DEF_SUBJECT_KEY = '%s' AND def_scope = 'VOCATIONAL_PROGRAM' and parent_rule_id = '%s';%n",record.get(0),gy25SubjectCode,record.get(1));
                } else {
                    insertSQL = String.format("insert into VUX_MANDATORY_SUBJECT_RULE25 (DEF_SUBJECT_KEY,DEF_SCOPE,RULE_ID,PARENT_RULE_ID,TYPE,VALUE,ACTIVE) values ('%s','VOCATIONAL_PROGRAM','%s','%s','%s','%s','1');%n",gy25SubjectCode,record.get(0),record.get(1),record.get(2),record.get(3));
                    insertRollbackSQL = String.format("delete from VUX_MANDATORY_SUBJECT_RULE25 where rule_id='%s' AND DEF_SUBJECT_KEY = '%s' AND def_scope = 'VOCATIONAL_PROGRAM' and parent_rule_id = '%s';%n",record.get(0),gy25SubjectCode,record.get(1));
                }

                fw1.write(insertSQL);
                fw2.write(insertRollbackSQL);
            }
            csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("rule_id","parent_rule_id","previous_rule_id")
                    .setSkipHeaderRecord(true)
                    .build();
            records = csvFormat.parse(frUpdateRule);
            for (CSVRecord record : records) {
                String updateSQL = String.format("update VUX_MANDATORY_SUBJECT_RULE25 set parent_rule_id='%s' where rule_id='%s' AND DEF_SUBJECT_KEY = '%s' AND def_scope = 'VOCATIONAL_PROGRAM' and parent_rule_id is %s;%n", record.get(1),record.get(0),gy11SubjectCode,record.get(2));
                String updateRollbackSQL = String.format("update VUX_MANDATORY_SUBJECT_RULE25 set parent_rule_id=%s where rule_id='%s' AND DEF_SUBJECT_KEY = '%s' AND def_scope = 'VOCATIONAL_PROGRAM' and parent_rule_id='%s';%n",record.get(2),record.get(0), gy11SubjectCode, record.get(1));
                fw3.write(updateSQL);
                fw4.write(updateRollbackSQL);
            }

            csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("scope","name","active","groups")
                    .setSkipHeaderRecord(true)
                    .build();
            records = csvFormat.parse(frInsertDef);
            for (CSVRecord record : records) {
                insertSQL = String.format("insert into VUX_MANDATORY_SUBJECT_DEF25 (SUBJECT_KEY,SCOPE,NAME,ACTIVE,GROUPS) values ('%s','%s','%s','%s','%s');%n",gy25SubjectCode,record.get(0),record.get(1),record.get(2),record.get(3));
                insertRollbackSQL = String.format("delete from VUX_MANDATORY_SUBJECT_DEF25 where SUBJECT_KEY='%s' AND SCOPE='%s';%n",gy25SubjectCode,record.get(0));
                fw5.write(insertSQL);
                fw6.write(insertRollbackSQL);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
