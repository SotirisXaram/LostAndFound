package com.charamidis.lostAndFound.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecordTest {

    @Test
    void testRecordConstructorAndGetters() {
        Record record = new Record(1, "UID123", "2026-02-23", "10:00:00", 12345, "Doe", "John", "AB123456", "2101234567", "Main St", "10", "Peter", "Athens", "2026-02-22", "09:00:00", "Airport", "Black Wallet", "Electronics", "Apple", "iPhone 13", "Black", "SN987654", "Scratched", "Vault A", "Stored", "/images/wallet.jpg");
        
        assertEquals(1, record.getId());
        assertEquals("UID123", record.getUid());
        assertEquals("2026-02-23", record.getRecord_date());
        assertEquals("10:00:00", record.getRecord_time());
        assertEquals(12345, record.getOfficer_id());
        assertEquals("Doe", record.getFounder_last_name());
        assertEquals("John", record.getFounder_first_name());
        assertEquals("Black Wallet", record.getItem_description());
        assertEquals("Stored", record.getStatus());
    }

    @Test
    void testRecordSetters() {
        Record record = new Record();
        record.setId(2);
        record.setUid("UID456");
        record.setRecord_date("2026-02-24");
        record.setItem_description("Samsung Phone");
        record.setStatus("Returned");
        
        assertEquals(2, record.getId());
        assertEquals("UID456", record.getUid());
        assertEquals("2026-02-24", record.getRecord_date());
        assertEquals("Samsung Phone", record.getItem_description());
        assertEquals("Returned", record.getStatus());
    }

    @Test
    void testToString() {
        Record record = new Record();
        record.setItem_description("Key Chain");
        assertEquals("Key Chain", record.toString());
    }
}
