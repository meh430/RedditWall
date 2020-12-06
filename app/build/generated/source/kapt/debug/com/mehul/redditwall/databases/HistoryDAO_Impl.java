package com.mehul.redditwall.databases;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.lifecycle.ComputableLiveData;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.InvalidationTracker.Observer;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.mehul.redditwall.objects.HistoryItem;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class HistoryDAO_Impl implements HistoryDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfHistoryItem;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfHistoryItem;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public HistoryDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHistoryItem = new EntityInsertionAdapter<HistoryItem>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `hist_table`(`hist_int_date`,`id`,`hist_name`,`hist_date`,`hist_source`,`hist_url`,`hist_post_link`) VALUES (?,nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, HistoryItem value) {
        if (value.getInternalDate() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getInternalDate());
        }
        stmt.bindLong(2, value.getId());
        if (value.getSubName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSubName());
        }
        if (value.getSetDate() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getSetDate());
        }
        stmt.bindLong(5, value.getSource());
        if (value.getUrl() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getUrl());
        }
        if (value.getPostLink() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getPostLink());
        }
      }
    };
    this.__deletionAdapterOfHistoryItem = new EntityDeletionOrUpdateAdapter<HistoryItem>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `hist_table` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, HistoryItem value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM hist_table";
        return _query;
      }
    };
  }

  @Override
  public void insert(HistoryItem history) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfHistoryItem.insert(history);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteHistoryItem(HistoryItem history) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfHistoryItem.handle(history);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<HistoryItem>> getAllHistoryItems() {
    final String _sql = "SELECT * from hist_table ORDER BY hist_name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<HistoryItem>>(__db.getQueryExecutor()) {
      private Observer _observer;

      @Override
      protected List<HistoryItem> compute() {
        if (_observer == null) {
          _observer = new Observer("hist_table") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfInternalDate = _cursor.getColumnIndexOrThrow("hist_int_date");
          final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
          final int _cursorIndexOfSubName = _cursor.getColumnIndexOrThrow("hist_name");
          final int _cursorIndexOfSetDate = _cursor.getColumnIndexOrThrow("hist_date");
          final int _cursorIndexOfSource = _cursor.getColumnIndexOrThrow("hist_source");
          final int _cursorIndexOfUrl = _cursor.getColumnIndexOrThrow("hist_url");
          final int _cursorIndexOfPostLink = _cursor.getColumnIndexOrThrow("hist_post_link");
          final List<HistoryItem> _result = new ArrayList<HistoryItem>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final HistoryItem _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpSubName;
            _tmpSubName = _cursor.getString(_cursorIndexOfSubName);
            final String _tmpSetDate;
            _tmpSetDate = _cursor.getString(_cursorIndexOfSetDate);
            final int _tmpSource;
            _tmpSource = _cursor.getInt(_cursorIndexOfSource);
            final String _tmpUrl;
            _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            final String _tmpPostLink;
            _tmpPostLink = _cursor.getString(_cursorIndexOfPostLink);
            _item = new HistoryItem(_tmpId,_tmpSubName,_tmpSetDate,_tmpSource,_tmpUrl,_tmpPostLink);
            final String _tmpInternalDate;
            _tmpInternalDate = _cursor.getString(_cursorIndexOfInternalDate);
            _item.setInternalDate(_tmpInternalDate);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    }.getLiveData();
  }

  @Override
  public HistoryItem[] getAnyHistoryItem() {
    final String _sql = "SELECT * from hist_table LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfInternalDate = _cursor.getColumnIndexOrThrow("hist_int_date");
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfSubName = _cursor.getColumnIndexOrThrow("hist_name");
      final int _cursorIndexOfSetDate = _cursor.getColumnIndexOrThrow("hist_date");
      final int _cursorIndexOfSource = _cursor.getColumnIndexOrThrow("hist_source");
      final int _cursorIndexOfUrl = _cursor.getColumnIndexOrThrow("hist_url");
      final int _cursorIndexOfPostLink = _cursor.getColumnIndexOrThrow("hist_post_link");
      final HistoryItem[] _result = new HistoryItem[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final HistoryItem _item;
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpSubName;
        _tmpSubName = _cursor.getString(_cursorIndexOfSubName);
        final String _tmpSetDate;
        _tmpSetDate = _cursor.getString(_cursorIndexOfSetDate);
        final int _tmpSource;
        _tmpSource = _cursor.getInt(_cursorIndexOfSource);
        final String _tmpUrl;
        _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
        final String _tmpPostLink;
        _tmpPostLink = _cursor.getString(_cursorIndexOfPostLink);
        _item = new HistoryItem(_tmpId,_tmpSubName,_tmpSetDate,_tmpSource,_tmpUrl,_tmpPostLink);
        final String _tmpInternalDate;
        _tmpInternalDate = _cursor.getString(_cursorIndexOfInternalDate);
        _item.setInternalDate(_tmpInternalDate);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
