package com.kreeda.ankana.db;

import android.database.Cursor;
import android.os.CancellationSignal;
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
import com.kreeda.ankana.model.Team;
import java.lang.Class;
import java.lang.Exception;
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
public final class TeamDao_Impl implements TeamDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Team> __insertionAdapterOfTeam;

  private final EntityDeletionOrUpdateAdapter<Team> __updateAdapterOfTeam;

  private final SharedSQLiteStatement __preparedStmtOfGrantAdmin;

  public TeamDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTeam = new EntityInsertionAdapter<Team>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `teams` (`id`,`name`,`sport`,`motto`,`wins`,`losses`,`matchesPlayed`,`email`,`uid`,`role`,`status`,`village`,`playerCount`,`phone`,`firebaseUid`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Team entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getSport());
        statement.bindString(4, entity.getMotto());
        statement.bindLong(5, entity.getWins());
        statement.bindLong(6, entity.getLosses());
        statement.bindLong(7, entity.getMatchesPlayed());
        statement.bindString(8, entity.getEmail());
        statement.bindString(9, entity.getUid());
        statement.bindString(10, entity.getRole());
        statement.bindString(11, entity.getStatus());
        statement.bindString(12, entity.getVillage());
        statement.bindLong(13, entity.getPlayerCount());
        statement.bindString(14, entity.getPhone());
        statement.bindString(15, entity.getFirebaseUid());
      }
    };
    this.__updateAdapterOfTeam = new EntityDeletionOrUpdateAdapter<Team>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `teams` SET `id` = ?,`name` = ?,`sport` = ?,`motto` = ?,`wins` = ?,`losses` = ?,`matchesPlayed` = ?,`email` = ?,`uid` = ?,`role` = ?,`status` = ?,`village` = ?,`playerCount` = ?,`phone` = ?,`firebaseUid` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Team entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getSport());
        statement.bindString(4, entity.getMotto());
        statement.bindLong(5, entity.getWins());
        statement.bindLong(6, entity.getLosses());
        statement.bindLong(7, entity.getMatchesPlayed());
        statement.bindString(8, entity.getEmail());
        statement.bindString(9, entity.getUid());
        statement.bindString(10, entity.getRole());
        statement.bindString(11, entity.getStatus());
        statement.bindString(12, entity.getVillage());
        statement.bindLong(13, entity.getPlayerCount());
        statement.bindString(14, entity.getPhone());
        statement.bindString(15, entity.getFirebaseUid());
        statement.bindString(16, entity.getId());
      }
    };
    this.__preparedStmtOfGrantAdmin = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE teams SET role = 'admin' WHERE email = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTeam(final Team team, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTeam.insert(team);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTeam(final Team team, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTeam.handle(team);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object grantAdmin(final String email, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfGrantAdmin.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, email);
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
          __preparedStmtOfGrantAdmin.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Team>> getTeamsRanked() {
    final String _sql = "SELECT * FROM teams ORDER BY wins DESC LIMIT 50";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"teams"}, new Callable<List<Team>>() {
      @Override
      @NonNull
      public List<Team> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfMotto = CursorUtil.getColumnIndexOrThrow(_cursor, "motto");
          final int _cursorIndexOfWins = CursorUtil.getColumnIndexOrThrow(_cursor, "wins");
          final int _cursorIndexOfLosses = CursorUtil.getColumnIndexOrThrow(_cursor, "losses");
          final int _cursorIndexOfMatchesPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "matchesPlayed");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfVillage = CursorUtil.getColumnIndexOrThrow(_cursor, "village");
          final int _cursorIndexOfPlayerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playerCount");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfFirebaseUid = CursorUtil.getColumnIndexOrThrow(_cursor, "firebaseUid");
          final List<Team> _result = new ArrayList<Team>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Team _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSport;
            _tmpSport = _cursor.getString(_cursorIndexOfSport);
            final String _tmpMotto;
            _tmpMotto = _cursor.getString(_cursorIndexOfMotto);
            final int _tmpWins;
            _tmpWins = _cursor.getInt(_cursorIndexOfWins);
            final int _tmpLosses;
            _tmpLosses = _cursor.getInt(_cursorIndexOfLosses);
            final int _tmpMatchesPlayed;
            _tmpMatchesPlayed = _cursor.getInt(_cursorIndexOfMatchesPlayed);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpUid;
            _tmpUid = _cursor.getString(_cursorIndexOfUid);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpVillage;
            _tmpVillage = _cursor.getString(_cursorIndexOfVillage);
            final int _tmpPlayerCount;
            _tmpPlayerCount = _cursor.getInt(_cursorIndexOfPlayerCount);
            final String _tmpPhone;
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            final String _tmpFirebaseUid;
            _tmpFirebaseUid = _cursor.getString(_cursorIndexOfFirebaseUid);
            _item = new Team(_tmpId,_tmpName,_tmpSport,_tmpMotto,_tmpWins,_tmpLosses,_tmpMatchesPlayed,_tmpEmail,_tmpUid,_tmpRole,_tmpStatus,_tmpVillage,_tmpPlayerCount,_tmpPhone,_tmpFirebaseUid);
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
  public Flow<Team> getTeamByFirebaseUid(final String fuid) {
    final String _sql = "SELECT * FROM teams WHERE firebaseUid = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, fuid);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"teams"}, new Callable<Team>() {
      @Override
      @Nullable
      public Team call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfMotto = CursorUtil.getColumnIndexOrThrow(_cursor, "motto");
          final int _cursorIndexOfWins = CursorUtil.getColumnIndexOrThrow(_cursor, "wins");
          final int _cursorIndexOfLosses = CursorUtil.getColumnIndexOrThrow(_cursor, "losses");
          final int _cursorIndexOfMatchesPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "matchesPlayed");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfVillage = CursorUtil.getColumnIndexOrThrow(_cursor, "village");
          final int _cursorIndexOfPlayerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playerCount");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfFirebaseUid = CursorUtil.getColumnIndexOrThrow(_cursor, "firebaseUid");
          final Team _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSport;
            _tmpSport = _cursor.getString(_cursorIndexOfSport);
            final String _tmpMotto;
            _tmpMotto = _cursor.getString(_cursorIndexOfMotto);
            final int _tmpWins;
            _tmpWins = _cursor.getInt(_cursorIndexOfWins);
            final int _tmpLosses;
            _tmpLosses = _cursor.getInt(_cursorIndexOfLosses);
            final int _tmpMatchesPlayed;
            _tmpMatchesPlayed = _cursor.getInt(_cursorIndexOfMatchesPlayed);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpUid;
            _tmpUid = _cursor.getString(_cursorIndexOfUid);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpVillage;
            _tmpVillage = _cursor.getString(_cursorIndexOfVillage);
            final int _tmpPlayerCount;
            _tmpPlayerCount = _cursor.getInt(_cursorIndexOfPlayerCount);
            final String _tmpPhone;
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            final String _tmpFirebaseUid;
            _tmpFirebaseUid = _cursor.getString(_cursorIndexOfFirebaseUid);
            _result = new Team(_tmpId,_tmpName,_tmpSport,_tmpMotto,_tmpWins,_tmpLosses,_tmpMatchesPlayed,_tmpEmail,_tmpUid,_tmpRole,_tmpStatus,_tmpVillage,_tmpPlayerCount,_tmpPhone,_tmpFirebaseUid);
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
  public Flow<Team> getTeamByUid(final String uid) {
    final String _sql = "SELECT * FROM teams WHERE uid = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, uid);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"teams"}, new Callable<Team>() {
      @Override
      @Nullable
      public Team call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfMotto = CursorUtil.getColumnIndexOrThrow(_cursor, "motto");
          final int _cursorIndexOfWins = CursorUtil.getColumnIndexOrThrow(_cursor, "wins");
          final int _cursorIndexOfLosses = CursorUtil.getColumnIndexOrThrow(_cursor, "losses");
          final int _cursorIndexOfMatchesPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "matchesPlayed");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfVillage = CursorUtil.getColumnIndexOrThrow(_cursor, "village");
          final int _cursorIndexOfPlayerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playerCount");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfFirebaseUid = CursorUtil.getColumnIndexOrThrow(_cursor, "firebaseUid");
          final Team _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSport;
            _tmpSport = _cursor.getString(_cursorIndexOfSport);
            final String _tmpMotto;
            _tmpMotto = _cursor.getString(_cursorIndexOfMotto);
            final int _tmpWins;
            _tmpWins = _cursor.getInt(_cursorIndexOfWins);
            final int _tmpLosses;
            _tmpLosses = _cursor.getInt(_cursorIndexOfLosses);
            final int _tmpMatchesPlayed;
            _tmpMatchesPlayed = _cursor.getInt(_cursorIndexOfMatchesPlayed);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpUid;
            _tmpUid = _cursor.getString(_cursorIndexOfUid);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpVillage;
            _tmpVillage = _cursor.getString(_cursorIndexOfVillage);
            final int _tmpPlayerCount;
            _tmpPlayerCount = _cursor.getInt(_cursorIndexOfPlayerCount);
            final String _tmpPhone;
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            final String _tmpFirebaseUid;
            _tmpFirebaseUid = _cursor.getString(_cursorIndexOfFirebaseUid);
            _result = new Team(_tmpId,_tmpName,_tmpSport,_tmpMotto,_tmpWins,_tmpLosses,_tmpMatchesPlayed,_tmpEmail,_tmpUid,_tmpRole,_tmpStatus,_tmpVillage,_tmpPlayerCount,_tmpPhone,_tmpFirebaseUid);
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
  public Object getTeamByEmail(final String email, final Continuation<? super Team> $completion) {
    final String _sql = "SELECT * FROM teams WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, email);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Team>() {
      @Override
      @Nullable
      public Team call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfMotto = CursorUtil.getColumnIndexOrThrow(_cursor, "motto");
          final int _cursorIndexOfWins = CursorUtil.getColumnIndexOrThrow(_cursor, "wins");
          final int _cursorIndexOfLosses = CursorUtil.getColumnIndexOrThrow(_cursor, "losses");
          final int _cursorIndexOfMatchesPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "matchesPlayed");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfVillage = CursorUtil.getColumnIndexOrThrow(_cursor, "village");
          final int _cursorIndexOfPlayerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playerCount");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfFirebaseUid = CursorUtil.getColumnIndexOrThrow(_cursor, "firebaseUid");
          final Team _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSport;
            _tmpSport = _cursor.getString(_cursorIndexOfSport);
            final String _tmpMotto;
            _tmpMotto = _cursor.getString(_cursorIndexOfMotto);
            final int _tmpWins;
            _tmpWins = _cursor.getInt(_cursorIndexOfWins);
            final int _tmpLosses;
            _tmpLosses = _cursor.getInt(_cursorIndexOfLosses);
            final int _tmpMatchesPlayed;
            _tmpMatchesPlayed = _cursor.getInt(_cursorIndexOfMatchesPlayed);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpUid;
            _tmpUid = _cursor.getString(_cursorIndexOfUid);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpVillage;
            _tmpVillage = _cursor.getString(_cursorIndexOfVillage);
            final int _tmpPlayerCount;
            _tmpPlayerCount = _cursor.getInt(_cursorIndexOfPlayerCount);
            final String _tmpPhone;
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            final String _tmpFirebaseUid;
            _tmpFirebaseUid = _cursor.getString(_cursorIndexOfFirebaseUid);
            _result = new Team(_tmpId,_tmpName,_tmpSport,_tmpMotto,_tmpWins,_tmpLosses,_tmpMatchesPlayed,_tmpEmail,_tmpUid,_tmpRole,_tmpStatus,_tmpVillage,_tmpPlayerCount,_tmpPhone,_tmpFirebaseUid);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
