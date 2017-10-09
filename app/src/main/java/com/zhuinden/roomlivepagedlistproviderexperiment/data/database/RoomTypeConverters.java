package com.zhuinden.roomlivepagedlistproviderexperiment.data.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Owner on 2017. 10. 09..
 */

public class RoomTypeConverters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
