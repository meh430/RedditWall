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
public final class HistRoomDatabase_Impl extends HistRoomDatabase {
  private volatile HistoryDAO _historyDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `hist_table` (`hist_int_date` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `hist_name` TEXT NOT NULL, `hist_date` TEXT NOT NULL, `hist_source` INTEGER NOT NULL, `hist_url` TEXT NOT NULL, `hist_post_link` TEXT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"6173c6c65832e4af01b43810696d5176\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `hist_table`");
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
        final HashMap<String, TableInfo.Column> _columnsHistTable = new HashMap<String, TableInfo.Column>(7);
        _columnsHistTable.put("hist_int_date", new TableInfo.Column("hist_int_date", "TEXT", true, 0));
        _columnsHistTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsHistTable.put("hist_name", new TableInfo.Column("hist_name", "TEXT", true, 0));
        _columnsHistTable.put("hist_date", new TableInfo.Column("hist_date", "TEXT", true, 0));
        _columnsHistTable.put("hist_source", new TableInfo.Column("hist_source", "INTEGER", true, 0));
        _columnsHistTable.put("hist_url", new TableInfo.Column("hist_url", "TEXT", true, 0));
        _columnsHistTable.put("hist_post_link", new TableInfo.Column("hist_post_link", "TEXT", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHistTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHistTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHistTable = new TableInfo("hist_table", _columnsHistTable, _foreignKeysHistTable, _indicesHistTable);
        final TableInfo _existingHistTable = TableInfo.read(_db, "hist_table");
        if (! _infoHistTable.equals(_existingHistTable)) {
          throw new IllegalStateException("Migration didn't properly handle hist_table(com.mehul.redditwall.objects.HistoryItem).\n"
                  + " Expected:\n" + _infoHistTable + "\n"
                  + " Found:\n" + _existingHistTable);
        }
      }
    }, "6173c6c65832e4af01b43810696d5176", "b83e96d0798a30efa2520c73e1828552");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "hist_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `hist_table`");
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
  public HistoryDAO historyDAO() {
    if (_historyDAO != null) {
      return _historyDAO;
    } else {
      synchronized(this) {
        if(_historyDAO == null) {
          _historyDAO = new HistoryDAO_Impl(this);
        }
        return _historyDAO;
      }
    }
  }
}
