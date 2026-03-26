package com.afterlight.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.afterlight.data.local.converter.InstantConverter;
import com.afterlight.data.local.model.MediaEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kotlinx.datetime.Instant;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MediaDao_Impl implements MediaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MediaEntity> __insertionAdapterOfMediaEntity;

  private final InstantConverter __instantConverter = new InstantConverter();

  private final SharedSQLiteStatement __preparedStmtOfUpdateFlagged;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByPartyId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public MediaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMediaEntity = new EntityInsertionAdapter<MediaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `media` (`id`,`partyId`,`encryptedFilePath`,`createdAt`,`flagged`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MediaEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getPartyId());
        statement.bindString(3, entity.getEncryptedFilePath());
        final Long _tmp = __instantConverter.toTimestamp(entity.getCreatedAt());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp);
        }
        final int _tmp_1 = entity.getFlagged() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
      }
    };
    this.__preparedStmtOfUpdateFlagged = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media SET flagged = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM media WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByPartyId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM media WHERE partyId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM media";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final MediaEntity media, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMediaEntity.insert(media);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<MediaEntity> media,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMediaEntity.insert(media);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFlagged(final String mediaId, final boolean flagged,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFlagged.acquire();
        int _argIndex = 1;
        final int _tmp = flagged ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, mediaId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateFlagged.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String mediaId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, mediaId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByPartyId(final String partyId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByPartyId.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, partyId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteByPartyId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<MediaEntity> getMediaById(final String mediaId) {
    final String _sql = "SELECT * FROM media WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, mediaId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media"}, new Callable<MediaEntity>() {
      @Override
      @Nullable
      public MediaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPartyId = CursorUtil.getColumnIndexOrThrow(_cursor, "partyId");
          final int _cursorIndexOfEncryptedFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedFilePath");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfFlagged = CursorUtil.getColumnIndexOrThrow(_cursor, "flagged");
          final MediaEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpPartyId;
            _tmpPartyId = _cursor.getString(_cursorIndexOfPartyId);
            final String _tmpEncryptedFilePath;
            _tmpEncryptedFilePath = _cursor.getString(_cursorIndexOfEncryptedFilePath);
            final Instant _tmpCreatedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final Instant _tmp_1 = __instantConverter.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'kotlinx.datetime.Instant', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_1;
            }
            final boolean _tmpFlagged;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfFlagged);
            _tmpFlagged = _tmp_2 != 0;
            _result = new MediaEntity(_tmpId,_tmpPartyId,_tmpEncryptedFilePath,_tmpCreatedAt,_tmpFlagged);
          } else {
            _result = null;
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
    });
  }

  @Override
  public Flow<List<MediaEntity>> getMediaForParty(final String partyId) {
    final String _sql = "SELECT * FROM media WHERE partyId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, partyId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media"}, new Callable<List<MediaEntity>>() {
      @Override
      @NonNull
      public List<MediaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPartyId = CursorUtil.getColumnIndexOrThrow(_cursor, "partyId");
          final int _cursorIndexOfEncryptedFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedFilePath");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfFlagged = CursorUtil.getColumnIndexOrThrow(_cursor, "flagged");
          final List<MediaEntity> _result = new ArrayList<MediaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpPartyId;
            _tmpPartyId = _cursor.getString(_cursorIndexOfPartyId);
            final String _tmpEncryptedFilePath;
            _tmpEncryptedFilePath = _cursor.getString(_cursorIndexOfEncryptedFilePath);
            final Instant _tmpCreatedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final Instant _tmp_1 = __instantConverter.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'kotlinx.datetime.Instant', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_1;
            }
            final boolean _tmpFlagged;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfFlagged);
            _tmpFlagged = _tmp_2 != 0;
            _item = new MediaEntity(_tmpId,_tmpPartyId,_tmpEncryptedFilePath,_tmpCreatedAt,_tmpFlagged);
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
    });
  }

  @Override
  public Flow<List<MediaEntity>> getFlaggedMedia() {
    final String _sql = "SELECT * FROM media WHERE flagged = 1 ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media"}, new Callable<List<MediaEntity>>() {
      @Override
      @NonNull
      public List<MediaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPartyId = CursorUtil.getColumnIndexOrThrow(_cursor, "partyId");
          final int _cursorIndexOfEncryptedFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedFilePath");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfFlagged = CursorUtil.getColumnIndexOrThrow(_cursor, "flagged");
          final List<MediaEntity> _result = new ArrayList<MediaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpPartyId;
            _tmpPartyId = _cursor.getString(_cursorIndexOfPartyId);
            final String _tmpEncryptedFilePath;
            _tmpEncryptedFilePath = _cursor.getString(_cursorIndexOfEncryptedFilePath);
            final Instant _tmpCreatedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final Instant _tmp_1 = __instantConverter.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'kotlinx.datetime.Instant', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_1;
            }
            final boolean _tmpFlagged;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfFlagged);
            _tmpFlagged = _tmp_2 != 0;
            _item = new MediaEntity(_tmpId,_tmpPartyId,_tmpEncryptedFilePath,_tmpCreatedAt,_tmpFlagged);
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
    });
  }

  @Override
  public Flow<Integer> getMediaCountForParty(final String partyId) {
    final String _sql = "SELECT COUNT(*) FROM media WHERE partyId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, partyId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
