package com.afterlight.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.afterlight.data.local.converter.InstantConverter;
import com.afterlight.data.local.converter.SyncStatusConverter;
import com.afterlight.data.local.model.SyncStateEntity;
import com.afterlight.data.local.model.SyncStatus;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
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
public final class SyncStateDao_Impl implements SyncStateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SyncStateEntity> __insertionAdapterOfSyncStateEntity;

  private final SyncStatusConverter __syncStatusConverter = new SyncStatusConverter();

  private final InstantConverter __instantConverter = new InstantConverter();

  private final EntityDeletionOrUpdateAdapter<SyncStateEntity> __updateAdapterOfSyncStateEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByMediaId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public SyncStateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSyncStateEntity = new EntityInsertionAdapter<SyncStateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sync_state` (`id`,`mediaId`,`syncStatus`,`lastAttemptAt`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SyncStateEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getMediaId());
        final String _tmp = __syncStatusConverter.fromSyncStatus(entity.getSyncStatus());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final Long _tmp_1 = __instantConverter.toTimestamp(entity.getLastAttemptAt());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp_1);
        }
      }
    };
    this.__updateAdapterOfSyncStateEntity = new EntityDeletionOrUpdateAdapter<SyncStateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `sync_state` SET `id` = ?,`mediaId` = ?,`syncStatus` = ?,`lastAttemptAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SyncStateEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getMediaId());
        final String _tmp = __syncStatusConverter.fromSyncStatus(entity.getSyncStatus());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final Long _tmp_1 = __instantConverter.toTimestamp(entity.getLastAttemptAt());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp_1);
        }
        statement.bindString(5, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sync_state WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByMediaId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sync_state WHERE mediaId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sync_state";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final SyncStateEntity syncState,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSyncStateEntity.insert(syncState);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<SyncStateEntity> syncStates,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSyncStateEntity.insert(syncStates);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final SyncStateEntity syncState,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSyncStateEntity.handle(syncState);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
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
  public Flow<SyncStateEntity> getSyncStateForMedia(final String mediaId) {
    final String _sql = "SELECT * FROM sync_state WHERE mediaId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, mediaId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sync_state"}, new Callable<SyncStateEntity>() {
      @Override
      @Nullable
      public SyncStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMediaId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaId");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfLastAttemptAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAttemptAt");
          final SyncStateEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMediaId;
            _tmpMediaId = _cursor.getString(_cursorIndexOfMediaId);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            final SyncStatus _tmp_1 = __syncStatusConverter.toSyncStatus(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.afterlight.data.local.model.SyncStatus', but it was NULL.");
            } else {
              _tmpSyncStatus = _tmp_1;
            }
            final Instant _tmpLastAttemptAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfLastAttemptAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfLastAttemptAt);
            }
            _tmpLastAttemptAt = __instantConverter.fromTimestamp(_tmp_2);
            _result = new SyncStateEntity(_tmpId,_tmpMediaId,_tmpSyncStatus,_tmpLastAttemptAt);
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
  public Flow<List<SyncStateEntity>> getSyncStateByStatus(final SyncStatus status) {
    final String _sql = "SELECT * FROM sync_state WHERE syncStatus = ? ORDER BY lastAttemptAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __syncStatusConverter.fromSyncStatus(status);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sync_state"}, new Callable<List<SyncStateEntity>>() {
      @Override
      @NonNull
      public List<SyncStateEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMediaId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaId");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfLastAttemptAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAttemptAt");
          final List<SyncStateEntity> _result = new ArrayList<SyncStateEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SyncStateEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMediaId;
            _tmpMediaId = _cursor.getString(_cursorIndexOfMediaId);
            final SyncStatus _tmpSyncStatus;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            final SyncStatus _tmp_2 = __syncStatusConverter.toSyncStatus(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.afterlight.data.local.model.SyncStatus', but it was NULL.");
            } else {
              _tmpSyncStatus = _tmp_2;
            }
            final Instant _tmpLastAttemptAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfLastAttemptAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfLastAttemptAt);
            }
            _tmpLastAttemptAt = __instantConverter.fromTimestamp(_tmp_3);
            _item = new SyncStateEntity(_tmpId,_tmpMediaId,_tmpSyncStatus,_tmpLastAttemptAt);
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
  public Flow<List<SyncStateEntity>> getPendingSync() {
    final String _sql = "SELECT * FROM sync_state WHERE syncStatus IN ('PENDING', 'FAILED') ORDER BY lastAttemptAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sync_state"}, new Callable<List<SyncStateEntity>>() {
      @Override
      @NonNull
      public List<SyncStateEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMediaId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaId");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfLastAttemptAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAttemptAt");
          final List<SyncStateEntity> _result = new ArrayList<SyncStateEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SyncStateEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMediaId;
            _tmpMediaId = _cursor.getString(_cursorIndexOfMediaId);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            final SyncStatus _tmp_1 = __syncStatusConverter.toSyncStatus(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.afterlight.data.local.model.SyncStatus', but it was NULL.");
            } else {
              _tmpSyncStatus = _tmp_1;
            }
            final Instant _tmpLastAttemptAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfLastAttemptAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfLastAttemptAt);
            }
            _tmpLastAttemptAt = __instantConverter.fromTimestamp(_tmp_2);
            _item = new SyncStateEntity(_tmpId,_tmpMediaId,_tmpSyncStatus,_tmpLastAttemptAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
