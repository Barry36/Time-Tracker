package com.cs446.group18.timetracker.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

// This associative entity will create a cross-reference table in SQLite database
@Entity(tableName = "event_tag_table", primaryKeys = {"event_id", "tag_id"})
public class EventTagCrossRef {
    @ColumnInfo(name = "event_id")
    private long eventId;

    @ColumnInfo(name = "tag_id")
    private long tagId;
}
