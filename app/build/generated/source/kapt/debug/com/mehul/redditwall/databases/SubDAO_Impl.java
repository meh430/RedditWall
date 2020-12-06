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
import com.mehul.redditwall.objects.Subreddit;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class SubDAO_Impl implements SubDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfSubreddit;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfSubreddit;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public SubDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSubreddit = new EntityInsertionAdapter<Subreddit>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `sub_table`(`sub_int_date`,`id`,`sub_name`,`sub_desc`,`sub_subs`,`sub_icon`,`sub_date`) VALUES (?,nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Subreddit value) {
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
        if (value.getSubDesc() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getSubDesc());
        }
        stmt.bindLong(5, value.getSubscribers());
        if (value.getSubIcon() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getSubIcon());
        }
        if (value.getSubDate() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getSubDate());
        }
      }
    };
    this.__deletionAdapterOfSubreddit = new EntityDeletionOrUpdateAdapter<Subreddit>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `sub_table` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Subreddit value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM sub_table";
        return _query;
      }
    };
  }

  @Override
  public void insert(Subreddit saved) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfSubreddit.insert(saved);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteSubreddit(Subreddit saved) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfSubreddit.handle(saved);
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
  public LiveData<List<Subreddit>> getAllSubreddit() {
    final String _sql = "SELECT * from sub_table ORDER BY sub_name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<Subreddit>>(__db.getQueryExecutor()) {
      private Observer _observer;

      @Override
      protected List<Subreddit> compute() {
        if (_observer == null) {
          _observer = new Observer("sub_table") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfInternalDate = _cursor.getColumnIndexOrThrow("sub_int_date");
          final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
          final int _cursorIndexOfSubName = _cursor.getColumnIndexOrThrow("sub_name");
          final int _cursorIndexOfSubDesc = _cursor.getColumnIndexOrThrow("sub_desc");
          final int _cursorIndexOfSubscribers = _cursor.getColumnIndexOrThrow("sub_subs");
          final int _cursorIndexOfSubIcon = _cursor.getColumnIndexOrThrow("sub_icon");
          final int _cursorIndexOfSubDate = _cursor.getColumnIndexOrThrow("sub_date");
          final List<Subreddit> _result = new ArrayList<Subreddit>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Subreddit _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpSubName;
            _tmpSubName = _cursor.getString(_cursorIndexOfSubName);
            final String _tmpSubDesc;
            _tmpSubDesc = _cursor.getString(_cursorIndexOfSubDesc);
            final int _tmpSubscribers;
            _tmpSubscribers = _cursor.getInt(_cursorIndexOfSubscribers);
            final String _tmpSubIcon;
            _tmpSubIcon = _cursor.getString(_cursorIndexOfSubIcon);
            final String _tmpSubDate;
            _tmpSubDate = _cursor.getString(_cursorIndexOfSubDate);
            _item = new Subreddit(_tmpId,_tmpSubName,_tmpSubDesc,_tmpSubscribers,_tmpSubIcon,_tmpSubDate);
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
  public Subreddit[] getAnySubreddit() {
    final String _sql = "SELECT * from sub_table LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfInternalDate = _cursor.getColumnIndexOrThrow("sub_int_date");
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfSubName = _cursor.getColumnIndexOrThrow("sub_name");
      final int _cursorIndexOfSubDesc = _cursor.getColumnIndexOrThrow("sub_desc");
      final int _cursorIndexOfSubscribers = _cursor.getColumnIndexOrThrow("sub_subs");
      final int _cursorIndexOfSubIcon = _cursor.getColumnIndexOrThrow("sub_icon");
      final int _cursorIndexOfSubDate = _cursor.getColumnIndexOrThrow("sub_date");
      final Subreddit[] _result = new Subreddit[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final Subreddit _item;
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpSubName;
        _tmpSubName = _cursor.getString(_cursorIndexOfSubName);
        final String _tmpSubDesc;
        _tmpSubDesc = _cursor.getString(_cursorIndexOfSubDesc);
        final int _tmpSubscribers;
        _tmpSubscribers = _cursor.getInt(_cursorIndexOfSubscribers);
        final String _tmpSubIcon;
        _tmpSubIcon = _cursor.getString(_cursorIndexOfSubIcon);
        final String _tmpSubDate;
        _tmpSubDate = _cursor.getString(_cursorIndexOfSubDate);
        _item = new Subreddit(_tmpId,_tmpSubName,_tmpSubDesc,_tmpSubscribers,_tmpSubIcon,_tmpSubDate);
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
