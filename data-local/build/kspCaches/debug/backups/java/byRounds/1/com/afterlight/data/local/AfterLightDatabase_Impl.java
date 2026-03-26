package com.afterlight.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.afterlight.data.local.dao.FaceDao;
import com.afterlight.data.local.dao.FaceDao_Impl;
import com.afterlight.data.local.dao.MediaDao;
import com.afterlight.data.local.dao.MediaDao_Impl;
import com.afterlight.data.local.dao.PartyDao;
import com.afterlight.data.local.dao.PartyDao_Impl;
import com.afterlight.data.local.dao.SyncStateDao;
import com.afterlight.data.local.dao.SyncStateDao_Impl;
import com.afterlight.data.local.dao.UserDao;
import com.afterlight.data.local.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AfterLightDatabase_Impl extends AfterLightDatabase {
  private volatile UserDao _userDao;

  private volatile PartyDao _partyDao;

  private volatile MediaDao _mediaDao;

  private volatile FaceDao _faceDao;

  private volatile SyncStateDao _syncStateDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` TEXT NOT NULL, `email` TEXT NOT NULL, `displayName` TEXT, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `parties` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `hostUserId` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `expiresAt` INTEGER NOT NULL, `isDeleted` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `media` (`id` TEXT NOT NULL, `partyId` TEXT NOT NULL, `encryptedFilePath` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `flagged` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`partyId`) REFERENCES `parties`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_partyId` ON `media` (`partyId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `faces` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `mediaId` TEXT NOT NULL, `boundingBox` TEXT NOT NULL, `isBlurred` INTEGER NOT NULL, FOREIGN KEY(`mediaId`) REFERENCES `media`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_faces_mediaId` ON `faces` (`mediaId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sync_state` (`id` TEXT NOT NULL, `mediaId` TEXT NOT NULL, `syncStatus` TEXT NOT NULL, `lastAttemptAt` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`mediaId`) REFERENCES `media`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_sync_state_mediaId` ON `sync_state` (`mediaId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_sync_state_syncStatus` ON `sync_state` (`syncStatus`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3cb6ed826788e2e5cd1dacb6e40ab780')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `parties`");
        db.execSQL("DROP TABLE IF EXISTS `media`");
        db.execSQL("DROP TABLE IF EXISTS `faces`");
        db.execSQL("DROP TABLE IF EXISTS `sync_state`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(4);
        _columnsUsers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("displayName", new TableInfo.Column("displayName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.afterlight.data.local.model.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsParties = new HashMap<String, TableInfo.Column>(6);
        _columnsParties.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParties.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParties.put("hostUserId", new TableInfo.Column("hostUserId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParties.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParties.put("expiresAt", new TableInfo.Column("expiresAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParties.put("isDeleted", new TableInfo.Column("isDeleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysParties = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesParties = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoParties = new TableInfo("parties", _columnsParties, _foreignKeysParties, _indicesParties);
        final TableInfo _existingParties = TableInfo.read(db, "parties");
        if (!_infoParties.equals(_existingParties)) {
          return new RoomOpenHelper.ValidationResult(false, "parties(com.afterlight.data.local.model.PartyEntity).\n"
                  + " Expected:\n" + _infoParties + "\n"
                  + " Found:\n" + _existingParties);
        }
        final HashMap<String, TableInfo.Column> _columnsMedia = new HashMap<String, TableInfo.Column>(5);
        _columnsMedia.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("partyId", new TableInfo.Column("partyId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("encryptedFilePath", new TableInfo.Column("encryptedFilePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedia.put("flagged", new TableInfo.Column("flagged", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMedia = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMedia.add(new TableInfo.ForeignKey("parties", "CASCADE", "NO ACTION", Arrays.asList("partyId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMedia = new HashSet<TableInfo.Index>(1);
        _indicesMedia.add(new TableInfo.Index("index_media_partyId", false, Arrays.asList("partyId"), Arrays.asList("ASC")));
        final TableInfo _infoMedia = new TableInfo("media", _columnsMedia, _foreignKeysMedia, _indicesMedia);
        final TableInfo _existingMedia = TableInfo.read(db, "media");
        if (!_infoMedia.equals(_existingMedia)) {
          return new RoomOpenHelper.ValidationResult(false, "media(com.afterlight.data.local.model.MediaEntity).\n"
                  + " Expected:\n" + _infoMedia + "\n"
                  + " Found:\n" + _existingMedia);
        }
        final HashMap<String, TableInfo.Column> _columnsFaces = new HashMap<String, TableInfo.Column>(4);
        _columnsFaces.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFaces.put("mediaId", new TableInfo.Column("mediaId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFaces.put("boundingBox", new TableInfo.Column("boundingBox", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFaces.put("isBlurred", new TableInfo.Column("isBlurred", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFaces = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysFaces.add(new TableInfo.ForeignKey("media", "CASCADE", "NO ACTION", Arrays.asList("mediaId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesFaces = new HashSet<TableInfo.Index>(1);
        _indicesFaces.add(new TableInfo.Index("index_faces_mediaId", false, Arrays.asList("mediaId"), Arrays.asList("ASC")));
        final TableInfo _infoFaces = new TableInfo("faces", _columnsFaces, _foreignKeysFaces, _indicesFaces);
        final TableInfo _existingFaces = TableInfo.read(db, "faces");
        if (!_infoFaces.equals(_existingFaces)) {
          return new RoomOpenHelper.ValidationResult(false, "faces(com.afterlight.data.local.model.FaceEntity).\n"
                  + " Expected:\n" + _infoFaces + "\n"
                  + " Found:\n" + _existingFaces);
        }
        final HashMap<String, TableInfo.Column> _columnsSyncState = new HashMap<String, TableInfo.Column>(4);
        _columnsSyncState.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("mediaId", new TableInfo.Column("mediaId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("syncStatus", new TableInfo.Column("syncStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("lastAttemptAt", new TableInfo.Column("lastAttemptAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSyncState = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSyncState.add(new TableInfo.ForeignKey("media", "CASCADE", "NO ACTION", Arrays.asList("mediaId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSyncState = new HashSet<TableInfo.Index>(2);
        _indicesSyncState.add(new TableInfo.Index("index_sync_state_mediaId", false, Arrays.asList("mediaId"), Arrays.asList("ASC")));
        _indicesSyncState.add(new TableInfo.Index("index_sync_state_syncStatus", false, Arrays.asList("syncStatus"), Arrays.asList("ASC")));
        final TableInfo _infoSyncState = new TableInfo("sync_state", _columnsSyncState, _foreignKeysSyncState, _indicesSyncState);
        final TableInfo _existingSyncState = TableInfo.read(db, "sync_state");
        if (!_infoSyncState.equals(_existingSyncState)) {
          return new RoomOpenHelper.ValidationResult(false, "sync_state(com.afterlight.data.local.model.SyncStateEntity).\n"
                  + " Expected:\n" + _infoSyncState + "\n"
                  + " Found:\n" + _existingSyncState);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3cb6ed826788e2e5cd1dacb6e40ab780", "4f1b59ad1010acfe1aaa584131e6660c");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","parties","media","faces","sync_state");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `parties`");
      _db.execSQL("DELETE FROM `media`");
      _db.execSQL("DELETE FROM `faces`");
      _db.execSQL("DELETE FROM `sync_state`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PartyDao.class, PartyDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MediaDao.class, MediaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FaceDao.class, FaceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SyncStateDao.class, SyncStateDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public PartyDao partyDao() {
    if (_partyDao != null) {
      return _partyDao;
    } else {
      synchronized(this) {
        if(_partyDao == null) {
          _partyDao = new PartyDao_Impl(this);
        }
        return _partyDao;
      }
    }
  }

  @Override
  public MediaDao mediaDao() {
    if (_mediaDao != null) {
      return _mediaDao;
    } else {
      synchronized(this) {
        if(_mediaDao == null) {
          _mediaDao = new MediaDao_Impl(this);
        }
        return _mediaDao;
      }
    }
  }

  @Override
  public FaceDao faceDao() {
    if (_faceDao != null) {
      return _faceDao;
    } else {
      synchronized(this) {
        if(_faceDao == null) {
          _faceDao = new FaceDao_Impl(this);
        }
        return _faceDao;
      }
    }
  }

  @Override
  public SyncStateDao syncStateDao() {
    if (_syncStateDao != null) {
      return _syncStateDao;
    } else {
      synchronized(this) {
        if(_syncStateDao == null) {
          _syncStateDao = new SyncStateDao_Impl(this);
        }
        return _syncStateDao;
      }
    }
  }
}
