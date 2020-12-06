package com.mehul.redditwall.databases;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public final class SubRoomDatabase_Impl extends SubRoomDatabase {
  private volatile SubDAO _subDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `sub_table` (`sub_int_date` TEXT, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sub_name` TEXT NOT NULL, `sub_desc` TEXT NOT NULL, `sub_subs` INTEGER NOT NULL, `sub_icon` TEXT NOT NULL, `sub_date` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"210947ec6234a97461d71ec8d277754c\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `sub_table`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsSubTable = new HashMap<String, TableInfo.Column>(7);
        _columnsSubTable.put("sub_int_date", new TableInfo.Column("sub_int_date", "TEXT", false, 0));
        _columnsSubTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsSubTable.put("sub_name", new TableInfo.Column("sub_name", "TEXT", true, 0));
        _columnsSubTable.put("sub_desc", new TableInfo.Column("sub_desc", "TEXT", true, 0));
        _columnsSubTable.put("sub_subs", new TableInfo.Column("sub_subs", "INTEGER", true, 0));
        _columnsSubTable.put("sub_icon", new TableInfo.Column("sub_icon", "TEXT", true, 0));
        _columnsSubTable.put("sub_date", new TableInfo.Column("sub_date", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSubTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSubTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSubTable = new TableInfo("sub_table", _columnsSubTable, _foreignKeysSubTable, _indicesSubTable);
        final TableInfo _existingSubTable = TableInfo.read(_db, "sub_table");
        if (! _infoSubTable.equals(_existingSubTable)) {
          throw new IllegalStateException("Migration didn't properly handle sub_table(com.mehul.redditwall.objects.Subreddit).\n"
                  + " Expected:\n" + _infoSubTable + "\n"
                  + " Found:\n" + _existingSubTable);
        }
      }
    }, "210947ec6234a97461d71ec8d277754c", "ce9acec48f9b4e5698780b9d9551c390");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "sub_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `sub_table`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public SubDAO subDao() {
    if (_subDAO != null) {
      return _subDAO;
    } else {
      synchronized(this) {
        if(_subDAO == null) {
          _subDAO = new SubDAO_Impl(this);
        }
        return _subDAO;
      }
    }
  }
}
