package ru.mgusev.eldritchhorror.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.mgusev.eldritchhorror.model.Expansion;

@Dao
public interface ExpansionDAO {

    @Query("SELECT * FROM expansions")
    List<Expansion> getAll();

    @Query("SELECT * FROM expansions WHERE _id IS :id")
    Expansion getExpansionByID(int id);

    @Update
    void updateExpansion(Expansion expansion);

    /**
     * Возвращает список id дополнений
     * @return список id дополнений, которые включены
     */
    @Query("SELECT _id FROM expansions WHERE is_enable IS 1")
    List<Integer> getEnableExpansionList();
}
