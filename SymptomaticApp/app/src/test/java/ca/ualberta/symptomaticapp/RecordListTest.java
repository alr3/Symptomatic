package ca.ualberta.symptomaticapp;

import junit.framework.TestCase;


import java.util.Collection;
import java.util.Date;

public class RecordListTest extends TestCase {
    public void testEmptyRecordList() {
        RecordList recordList = new RecordList();
        Collection<Record> records = recordList.getRecords();
        assertTrue("Empty record list", records.size() == 0 );
    }

    public void testAddRecord() {
        RecordList recordList = new RecordList();
        Date date = new Date();
        Record testRecord = new Record(date);
        recordList.addRecord(testRecord);
        Collection<Record> records = recordList.getRecords();
        assertTrue("Record list size == 1", records.size() == 1);
        assertTrue("", records.contains(testRecord));
    }

    public void testDeleteRecord() {
        RecordList recordList = new RecordList();
        Date date = new Date();
        Record testRecord = new Record(date);
        recordList.addRecord(testRecord);
        recordList.deleteRecord(testRecord);
        Collection<Record> records = recordList.getRecords();
        assertTrue("Record list size == 0", records.size() == 0);
        assertFalse("", records.contains(testRecord));
    }
}


