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
import com.mehul.redditwall.objects.FavImage;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class FavDAO_Impl implements FavDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfFavImage;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfFavImage;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public FavDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFavImage = new EntityInsertionAdapter<FavImage>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `fav_table`(`id`,`fav_url`,`fav_gif`,`fav_post_link`,`fav_name`,`preview_url`,`selected`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FavImage value) {
        stmt.bindLong(1, value.getId());
        if (value.getFavUrl() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getFavUrl());
        }
        final int _tmp;
        _tmp = value.isGif() ? 1 : 0;
        stmt.bindLong(3, _tmp);
        if (value.getPostLink() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPostLink());
        }
        if (value.getFavName() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getFavName());
        }
        if (value.getPreviewUrl() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPreviewUrl());
        }
        final int _tmp_1;
        _tmp_1 = value.getSelected() ? 1 : 0;
        stmt.bindLong(7, _tmp_1);
      }
    };
    this.__deletionAdapterOfFavImage = new EntityDeletionOrUpdateAdapter<FavImage>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `fav_table` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FavImage value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM fav_table";
        return _query;
      }
    };
  }

  @Override
  public void insert(FavImage saved) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfFavImage.insert(saved);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteFavImage(FavImage saved) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfFavImage.handle(saved);
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
  public LiveData<List<FavImage>> getAllFavImages() {
    final String _sql = "SELECT * from fav_table ORDER BY fav_url ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<FavImage>>(__db.getQueryExecutor()) {
      private Observer _observer;

      @Override
      protected List<FavImage> compute() {
        if (_observer == null) {
          _observer = new Observer("fav_table") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
          final int _cursorIndexOfFavUrl = _cursor.getColumnIndexOrThrow("fav_url");
          final int _cursorIndexOfIsGif = _cursor.getColumnIndexOrThrow("fav_gif");
          final int _cursorIndexOfPostLink = _cursor.getColumnIndexOrThrow("fav_post_link");
          final int _cursorIndexOfFavName = _cursor.getColumnIndexOrThrow("fav_name");
          final int _cursorIndexOfPreviewUrl = _cursor.getColumnIndexOrThrow("preview_url");
          final int _cursorIndexOfSelected = _cursor.getColumnIndexOrThrow("selected");
          final List<FavImage> _result = new ArrayList<FavImage>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final FavImage _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpFavUrl;
            _tmpFavUrl = _cursor.getString(_cursorIndexOfFavUrl);
            final boolean _tmpIsGif;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsGif);
            _tmpIsGif = _tmp != 0;
            final String _tmpPostLink;
            _tmpPostLink = _cursor.getString(_cursorIndexOfPostLink);
            final String _tmpFavName;
            _tmpFavName = _cursor.getString(_cursorIndexOfFavName);
            final String _tmpPreviewUrl;
            _tmpPreviewUrl = _cursor.getString(_cursorIndexOfPreviewUrl);
            final boolean _tmpSelected;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSelected);
            _tmpSelected = _tmp_1 != 0;
            _item = new FavImage(_tmpId,_tmpFavUrl,_tmpIsGif,_tmpPostLink,_tmpFavName,_tmpPreviewUrl,_tmpSelected);
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
  public List<FavImage> getFavAsList() {
    final String _sql = "SELECT * from fav_table ORDER BY fav_url ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfFavUrl = _cursor.getColumnIndexOrThrow("fav_url");
      final int _cursorIndexOfIsGif = _cursor.getColumnIndexOrThrow("fav_gif");
      final int _cursorIndexOfPostLink = _cursor.getColumnIndexOrThrow("fav_post_link");
      final int _cursorIndexOfFavName = _cursor.getColumnIndexOrThrow("fav_name");
      final int _cursorIndexOfPreviewUrl = _cursor.getColumnIndexOrThrow("preview_url");
      final int _cursorIndexOfSelected = _cursor.getColumnIndexOrThrow("selected");
      final List<FavImage> _result = new ArrayList<FavImage>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FavImage _item;
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpFavUrl;
        _tmpFavUrl = _cursor.getString(_cursorIndexOfFavUrl);
        final boolean _tmpIsGif;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsGif);
        _tmpIsGif = _tmp != 0;
        final String _tmpPostLink;
        _tmpPostLink = _cursor.getString(_cursorIndexOfPostLink);
        final String _tmpFavName;
        _tmpFavName = _cursor.getString(_cursorIndexOfFavName);
        final String _tmpPreviewUrl;
        _tmpPreviewUrl = _cursor.getString(_cursorIndexOfPreviewUrl);
        final boolean _tmpSelected;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfSelected);
        _tmpSelected = _tmp_1 != 0;
        _item = new FavImage(_tmpId,_tmpFavUrl,_tmpIsGif,_tmpPostLink,_tmpFavName,_tmpPreviewUrl,_tmpSelected);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
