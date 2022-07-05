package com.farid.newsapp.mvvmnewsapp.dp

import androidx.room.TypeConverter
import com.farid.newsapp.mvvmnewsapp.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String): Source {
        return Source(name,name)
    }
}