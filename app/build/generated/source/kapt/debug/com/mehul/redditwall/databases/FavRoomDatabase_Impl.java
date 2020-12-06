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
public final class FavRoomDatabase_Impl extends FavRoomDatabase {
  private volatile FavDAO _favDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(6) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `fav_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `fav_url` TEXT NOT NULL, `fav_gif` INTEGER NOT NULL, `fav_post_link` TEXT NOT NULL, `fav_name` TEXT NOT NULL, `preview_url` TEXT NOT NULL, `selected` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"27e046b4058d34b56068803748eb7cbb\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `fav_table`");
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
        final HashMap<String, TableInfo.Column> _columnsFavTable = new HashMap<String, TableInfo.Column>(7);
        _columnsFavTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsFavTable.put("fav_url", new TableInfo.Column("fav_url", "TEXT", true, 0));
        _columnsFavTable.put("fav_gif", new TableInfo.Column("fav_gif", "INTEGER", true, 0));
        _columnsFavTable.put("fav_post_link", new TableInfo.Column("fav_post_link", "TEXT", true, 0));
        _columnsFavTable.put("fav_name", new TableInfo.Column("fav_name", "TEXT", true, 0));
        _columnsFavTable.put("preview_url", new TableInfo.Column("preview_url", "TEXT", true, 0));
        _columnsFavTable.put("selected", new TableInfo.Column("selected", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavTable = new TableInfo("fav_table", _columnsFavTable, _foreignKeysFavTable, _indicesFavTable);
        final TableInfo _existingFavTable = TableInfo.read(_db, "fav_table");
        if (! _infoFavTable.equals(_existingFavTable)) {
          throw new IllegalStateException("Migration didn't properly handle fav_table(com.mehul.redditwall.objects.FavImage).\n"
                  + " Expected:\n" + _infoFavTable + "\n"
                  + " Found:\n" + _existingFavTable);
        }
      }
    }, "27e046b4058d34b56068803748eb7cbb", "0a54c511f7ccbf098bb97f80d57a9fcb");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "fav_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `fav_table`");
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
  public FavDAO favDAO() {
    if (_favDAO != null) {
      return _favDAO;
    } else {
      synchronized(this) {
        if(_favDAO == null) {
          _favDAO = new FavDAO_Impl(this);
        }
        return _favDAO;
      }
    }
  }
}
