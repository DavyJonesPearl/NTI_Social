package com.afterlight.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.afterlight.data.local.model.FaceEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FaceDao_Impl implements FaceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FaceEntity> __insertionAdapterOfFaceEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBlurred;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByMediaId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public FaceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFaceEntity = new EntityInsertionAdapter<FaceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `faces` (`id`,`mediaId`,`boundingBox`,`isBlurred`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FaceEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getMediaId());
        statement.bindString(3, entity.getBoundingBox());
        final int _tmp = entity.isBlurred() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
    this.__preparedStmtOfUpdateBlurred = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE faces SET isBlurred = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM faces WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByMediaId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM faces WHERE mediaId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM faces";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final FaceEntity face, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFaceEntity.insert(face);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<FaceEntity> faces,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFaceEntity.insert(faces);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBlurred(final long faceId, final boolean isBlurred,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBlurred.acquire();
        int _argIndex = 1;
        final int _tmp = isBlurred ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, faceId);
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
          __preparedStmtOfUpdateBlurred.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long faceId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, faceId);
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
  public Object deleteByMediaId(final String mediaId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByMediaId.acquire();
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
          __preparedStmtOfDeleteByMediaId.release(_stmt);
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
  public Flow<List<FaceEntity>> getFacesForMedia(final String mediaId) {
    final String _sql = "SELECT * FROM faces WHERE mediaId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, mediaId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"faces"}, new Callable<List<FaceEntity>>() {
      @Override
      @NonNull
      public List<FaceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMediaId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaId");
          final int _cursorIndexOfBoundingBox = CursorUtil.getColumnIndexOrThrow(_cursor, "boundingBox");
          final int _cursorIndexOfIsBlurred = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlurred");
          final List<FaceEntity> _result = new ArrayList<FaceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FaceEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMediaId;
            _tmpMediaId = _cursor.getString(_cursorIndexOfMediaId);
            final String _tmpBoundingBox;
            _tmpBoundingBox = _cursor.getString(_cursorIndexOfBoundingBox);
            final boolean _tmpIsBlurred;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBlurred);
            _tmpIsBlurred = _tmp != 0;
            _item = new FaceEntity(_tmpId,_tmpMediaId,_tmpBoundingBox,_tmpIsBlurred);
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
  public Flow<List<FaceEntity>> getUnblurredFacesForMedia(final String mediaId) {
    final String _sql = "SELECT * FROM faces WHERE mediaId = ? AND isBlurred = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, mediaId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"faces"}, new Callable<List<FaceEntity>>() {
      @Override
      @NonNull
      public List<FaceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMediaId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaId");
          final int _cursorIndexOfBoundingBox = CursorUtil.getColumnIndexOrThrow(_cursor, "boundingBox");
          final int _cursorIndexOfIsBlurred = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlurred");
          final List<FaceEntity> _result = new ArrayList<FaceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FaceEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMediaId;
            _tmpMediaId = _cursor.getString(_cursorIndexOfMediaId);
            final String _tmpBoundingBox;
            _tmpBoundingBox = _cursor.getString(_cursorIndexOfBoundingBox);
            final boolean _tmpIsBlurred;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBlurred);
            _tmpIsBlurred = _tmp != 0;
            _item = new FaceEntity(_tmpId,_tmpMediaId,_tmpBoundingBox,_tmpIsBlurred);
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
  public Flow<Integer> getFaceCountForMedia(final String mediaId) {
    final String _sql = "SELECT COUNT(*) FROM faces WHERE mediaId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, mediaId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"faces"}, new Callable<Integer>() {
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
