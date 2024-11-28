package org.example.ids;

public class CassandraIds {
    public static final String KEYSPACE="buildings_app";
    public static final String LOKAL_TABLE="Lokal";
    public static final String BUDYNEK_TABLE="Budynek";
    public static final String WRITE_CONSISTENCY_LEVEL="ONE";
    public static final String READ_CONSISTENCY_LEVEL="ONE";
    public enum selectType{
        BIURO,
        MIESZKANIE,
        LOKAL,
        BUDYNEK
    }
}
