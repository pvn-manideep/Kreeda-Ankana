package com.kreeda.ankana.db;

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
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TeamDao _teamDao;

  private volatile SlotDao _slotDao;

  private volatile ChallengeDao _challengeDao;

  private volatile MatchScoreDao _matchScoreDao;

  private volatile NotificationDao _notificationDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(6) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `teams` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `sport` TEXT NOT NULL, `motto` TEXT NOT NULL, `wins` INTEGER NOT NULL, `losses` INTEGER NOT NULL, `matchesPlayed` INTEGER NOT NULL, `email` TEXT NOT NULL, `uid` TEXT NOT NULL, `role` TEXT NOT NULL, `status` TEXT NOT NULL, `village` TEXT NOT NULL, `playerCount` INTEGER NOT NULL, `phone` TEXT NOT NULL, `firebaseUid` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `slots` (`id` TEXT NOT NULL, `time` TEXT NOT NULL, `date` TEXT NOT NULL, `status` TEXT NOT NULL, `bookedByEmail` TEXT NOT NULL, `bookedByTeam` TEXT NOT NULL, `bookedByUid` TEXT NOT NULL, `sport` TEXT NOT NULL, `opponentTeam` TEXT NOT NULL, `challengeId` TEXT NOT NULL, `rescheduleRequestedTo` TEXT NOT NULL, `rescheduleStatus` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `challenges` (`id` TEXT NOT NULL, `fromTeam` TEXT NOT NULL, `fromTeamUid` TEXT NOT NULL, `toSlotId` TEXT NOT NULL, `toTeam` TEXT NOT NULL, `sport` TEXT NOT NULL, `time` TEXT NOT NULL, `date` TEXT NOT NULL, `message` TEXT NOT NULL, `status` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `match_scores` (`id` TEXT NOT NULL, `slotId` TEXT NOT NULL, `date` TEXT NOT NULL, `team1` TEXT NOT NULL, `team2` TEXT NOT NULL, `sport` TEXT NOT NULL, `score1` INTEGER NOT NULL, `score2` INTEGER NOT NULL, `winner` TEXT NOT NULL, `notes` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `notifications` (`id` TEXT NOT NULL, `toUid` TEXT NOT NULL, `title` TEXT NOT NULL, `body` TEXT NOT NULL, `type` TEXT NOT NULL, `isRead` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, `refId` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'fa26d18b39fef99c55a342081e4cf325')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `teams`");
        db.execSQL("DROP TABLE IF EXISTS `slots`");
        db.execSQL("DROP TABLE IF EXISTS `challenges`");
        db.execSQL("DROP TABLE IF EXISTS `match_scores`");
        db.execSQL("DROP TABLE IF EXISTS `notifications`");
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
        final HashMap<String, TableInfo.Column> _columnsTeams = new HashMap<String, TableInfo.Column>(15);
        _columnsTeams.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("sport", new TableInfo.Column("sport", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("motto", new TableInfo.Column("motto", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("wins", new TableInfo.Column("wins", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("losses", new TableInfo.Column("losses", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("matchesPlayed", new TableInfo.Column("matchesPlayed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("uid", new TableInfo.Column("uid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("village", new TableInfo.Column("village", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("playerCount", new TableInfo.Column("playerCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("firebaseUid", new TableInfo.Column("firebaseUid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTeams = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTeams = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTeams = new TableInfo("teams", _columnsTeams, _foreignKeysTeams, _indicesTeams);
        final TableInfo _existingTeams = TableInfo.read(db, "teams");
        if (!_infoTeams.equals(_existingTeams)) {
          return new RoomOpenHelper.ValidationResult(false, "teams(com.kreeda.ankana.model.Team).\n"
                  + " Expected:\n" + _infoTeams + "\n"
                  + " Found:\n" + _existingTeams);
        }
        final HashMap<String, TableInfo.Column> _columnsSlots = new HashMap<String, TableInfo.Column>(12);
        _columnsSlots.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlots.put("time", new TableInfo.Column("time", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlots.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlots.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlots.put("bookedByEmail", new TableInfo.Column("bookedByEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlots.put("bookedByTeam", new TableInfo.Column("bookedByTeam", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlots.put("bookedByUid", new TableInfo.Column("bookedByUid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlots.put("sport", new TableInfo.Column("sport", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlots.put("opponentTeam", new TableInfo.Column("opponentTeam", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlots.put("challengeId", new TableInfo.Column("challengeId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlots.put("rescheduleRequestedTo", new TableInfo.Column("rescheduleRequestedTo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlots.put("rescheduleStatus", new TableInfo.Column("rescheduleStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSlots = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSlots = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSlots = new TableInfo("slots", _columnsSlots, _foreignKeysSlots, _indicesSlots);
        final TableInfo _existingSlots = TableInfo.read(db, "slots");
        if (!_infoSlots.equals(_existingSlots)) {
          return new RoomOpenHelper.ValidationResult(false, "slots(com.kreeda.ankana.model.Slot).\n"
                  + " Expected:\n" + _infoSlots + "\n"
                  + " Found:\n" + _existingSlots);
        }
        final HashMap<String, TableInfo.Column> _columnsChallenges = new HashMap<String, TableInfo.Column>(10);
        _columnsChallenges.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallenges.put("fromTeam", new TableInfo.Column("fromTeam", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallenges.put("fromTeamUid", new TableInfo.Column("fromTeamUid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallenges.put("toSlotId", new TableInfo.Column("toSlotId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallenges.put("toTeam", new TableInfo.Column("toTeam", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallenges.put("sport", new TableInfo.Column("sport", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallenges.put("time", new TableInfo.Column("time", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallenges.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallenges.put("message", new TableInfo.Column("message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallenges.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChallenges = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChallenges = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChallenges = new TableInfo("challenges", _columnsChallenges, _foreignKeysChallenges, _indicesChallenges);
        final TableInfo _existingChallenges = TableInfo.read(db, "challenges");
        if (!_infoChallenges.equals(_existingChallenges)) {
          return new RoomOpenHelper.ValidationResult(false, "challenges(com.kreeda.ankana.model.Challenge).\n"
                  + " Expected:\n" + _infoChallenges + "\n"
                  + " Found:\n" + _existingChallenges);
        }
        final HashMap<String, TableInfo.Column> _columnsMatchScores = new HashMap<String, TableInfo.Column>(10);
        _columnsMatchScores.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchScores.put("slotId", new TableInfo.Column("slotId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchScores.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchScores.put("team1", new TableInfo.Column("team1", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchScores.put("team2", new TableInfo.Column("team2", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchScores.put("sport", new TableInfo.Column("sport", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchScores.put("score1", new TableInfo.Column("score1", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchScores.put("score2", new TableInfo.Column("score2", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchScores.put("winner", new TableInfo.Column("winner", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchScores.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMatchScores = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMatchScores = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMatchScores = new TableInfo("match_scores", _columnsMatchScores, _foreignKeysMatchScores, _indicesMatchScores);
        final TableInfo _existingMatchScores = TableInfo.read(db, "match_scores");
        if (!_infoMatchScores.equals(_existingMatchScores)) {
          return new RoomOpenHelper.ValidationResult(false, "match_scores(com.kreeda.ankana.model.MatchScore).\n"
                  + " Expected:\n" + _infoMatchScores + "\n"
                  + " Found:\n" + _existingMatchScores);
        }
        final HashMap<String, TableInfo.Column> _columnsNotifications = new HashMap<String, TableInfo.Column>(8);
        _columnsNotifications.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("toUid", new TableInfo.Column("toUid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("body", new TableInfo.Column("body", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("isRead", new TableInfo.Column("isRead", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("refId", new TableInfo.Column("refId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotifications = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotifications = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotifications = new TableInfo("notifications", _columnsNotifications, _foreignKeysNotifications, _indicesNotifications);
        final TableInfo _existingNotifications = TableInfo.read(db, "notifications");
        if (!_infoNotifications.equals(_existingNotifications)) {
          return new RoomOpenHelper.ValidationResult(false, "notifications(com.kreeda.ankana.model.AppNotification).\n"
                  + " Expected:\n" + _infoNotifications + "\n"
                  + " Found:\n" + _existingNotifications);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "fa26d18b39fef99c55a342081e4cf325", "b06ff808626f69a2f4fe6be68d4bf449");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "teams","slots","challenges","match_scores","notifications");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `teams`");
      _db.execSQL("DELETE FROM `slots`");
      _db.execSQL("DELETE FROM `challenges`");
      _db.execSQL("DELETE FROM `match_scores`");
      _db.execSQL("DELETE FROM `notifications`");
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
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TeamDao.class, TeamDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SlotDao.class, SlotDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChallengeDao.class, ChallengeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MatchScoreDao.class, MatchScoreDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(NotificationDao.class, NotificationDao_Impl.getRequiredConverters());
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
  public TeamDao teamDao() {
    if (_teamDao != null) {
      return _teamDao;
    } else {
      synchronized(this) {
        if(_teamDao == null) {
          _teamDao = new TeamDao_Impl(this);
        }
        return _teamDao;
      }
    }
  }

  @Override
  public SlotDao slotDao() {
    if (_slotDao != null) {
      return _slotDao;
    } else {
      synchronized(this) {
        if(_slotDao == null) {
          _slotDao = new SlotDao_Impl(this);
        }
        return _slotDao;
      }
    }
  }

  @Override
  public ChallengeDao challengeDao() {
    if (_challengeDao != null) {
      return _challengeDao;
    } else {
      synchronized(this) {
        if(_challengeDao == null) {
          _challengeDao = new ChallengeDao_Impl(this);
        }
        return _challengeDao;
      }
    }
  }

  @Override
  public MatchScoreDao matchScoreDao() {
    if (_matchScoreDao != null) {
      return _matchScoreDao;
    } else {
      synchronized(this) {
        if(_matchScoreDao == null) {
          _matchScoreDao = new MatchScoreDao_Impl(this);
        }
        return _matchScoreDao;
      }
    }
  }

  @Override
  public NotificationDao notificationDao() {
    if (_notificationDao != null) {
      return _notificationDao;
    } else {
      synchronized(this) {
        if(_notificationDao == null) {
          _notificationDao = new NotificationDao_Impl(this);
        }
        return _notificationDao;
      }
    }
  }
}
