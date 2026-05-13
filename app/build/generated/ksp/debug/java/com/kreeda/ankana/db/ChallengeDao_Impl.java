package com.kreeda.ankana.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.kreeda.ankana.model.Challenge;
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
public final class ChallengeDao_Impl implements ChallengeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Challenge> __insertionAdapterOfChallenge;

  private final EntityDeletionOrUpdateAdapter<Challenge> __deletionAdapterOfChallenge;

  private final EntityDeletionOrUpdateAdapter<Challenge> __updateAdapterOfChallenge;

  public ChallengeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChallenge = new EntityInsertionAdapter<Challenge>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `challenges` (`id`,`fromTeam`,`fromTeamUid`,`toSlotId`,`toTeam`,`sport`,`time`,`date`,`message`,`status`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Challenge entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getFromTeam());
        statement.bindString(3, entity.getFromTeamUid());
        statement.bindString(4, entity.getToSlotId());
        statement.bindString(5, entity.getToTeam());
        statement.bindString(6, entity.getSport());
        statement.bindString(7, entity.getTime());
        statement.bindString(8, entity.getDate());
        statement.bindString(9, entity.getMessage());
        statement.bindString(10, entity.getStatus());
      }
    };
    this.__deletionAdapterOfChallenge = new EntityDeletionOrUpdateAdapter<Challenge>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `challenges` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Challenge entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfChallenge = new EntityDeletionOrUpdateAdapter<Challenge>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `challenges` SET `id` = ?,`fromTeam` = ?,`fromTeamUid` = ?,`toSlotId` = ?,`toTeam` = ?,`sport` = ?,`time` = ?,`date` = ?,`message` = ?,`status` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Challenge entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getFromTeam());
        statement.bindString(3, entity.getFromTeamUid());
        statement.bindString(4, entity.getToSlotId());
        statement.bindString(5, entity.getToTeam());
        statement.bindString(6, entity.getSport());
        statement.bindString(7, entity.getTime());
        statement.bindString(8, entity.getDate());
        statement.bindString(9, entity.getMessage());
        statement.bindString(10, entity.getStatus());
        statement.bindString(11, entity.getId());
      }
    };
  }

  @Override
  public Object insertChallenge(final Challenge challenge,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChallenge.insert(challenge);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteChallenge(final Challenge challenge,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfChallenge.handle(challenge);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateChallenge(final Challenge challenge,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfChallenge.handle(challenge);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Challenge>> getPendingChallenges() {
    final String _sql = "SELECT * FROM challenges WHERE status = 'pending' ORDER BY rowid DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"challenges"}, new Callable<List<Challenge>>() {
      @Override
      @NonNull
      public List<Challenge> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFromTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "fromTeam");
          final int _cursorIndexOfFromTeamUid = CursorUtil.getColumnIndexOrThrow(_cursor, "fromTeamUid");
          final int _cursorIndexOfToSlotId = CursorUtil.getColumnIndexOrThrow(_cursor, "toSlotId");
          final int _cursorIndexOfToTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "toTeam");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<Challenge> _result = new ArrayList<Challenge>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Challenge _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpFromTeam;
            _tmpFromTeam = _cursor.getString(_cursorIndexOfFromTeam);
            final String _tmpFromTeamUid;
            _tmpFromTeamUid = _cursor.getString(_cursorIndexOfFromTeamUid);
            final String _tmpToSlotId;
            _tmpToSlotId = _cursor.getString(_cursorIndexOfToSlotId);
            final String _tmpToTeam;
            _tmpToTeam = _cursor.getString(_cursorIndexOfToTeam);
            final String _tmpSport;
            _tmpSport = _cursor.getString(_cursorIndexOfSport);
            final String _tmpTime;
            _tmpTime = _cursor.getString(_cursorIndexOfTime);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _item = new Challenge(_tmpId,_tmpFromTeam,_tmpFromTeamUid,_tmpToSlotId,_tmpToTeam,_tmpSport,_tmpTime,_tmpDate,_tmpMessage,_tmpStatus);
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
  public Flow<List<Challenge>> getChallengesForSlot(final String slotId) {
    final String _sql = "SELECT * FROM challenges WHERE toSlotId = ? AND status = 'pending'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, slotId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"challenges"}, new Callable<List<Challenge>>() {
      @Override
      @NonNull
      public List<Challenge> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFromTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "fromTeam");
          final int _cursorIndexOfFromTeamUid = CursorUtil.getColumnIndexOrThrow(_cursor, "fromTeamUid");
          final int _cursorIndexOfToSlotId = CursorUtil.getColumnIndexOrThrow(_cursor, "toSlotId");
          final int _cursorIndexOfToTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "toTeam");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<Challenge> _result = new ArrayList<Challenge>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Challenge _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpFromTeam;
            _tmpFromTeam = _cursor.getString(_cursorIndexOfFromTeam);
            final String _tmpFromTeamUid;
            _tmpFromTeamUid = _cursor.getString(_cursorIndexOfFromTeamUid);
            final String _tmpToSlotId;
            _tmpToSlotId = _cursor.getString(_cursorIndexOfToSlotId);
            final String _tmpToTeam;
            _tmpToTeam = _cursor.getString(_cursorIndexOfToTeam);
            final String _tmpSport;
            _tmpSport = _cursor.getString(_cursorIndexOfSport);
            final String _tmpTime;
            _tmpTime = _cursor.getString(_cursorIndexOfTime);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _item = new Challenge(_tmpId,_tmpFromTeam,_tmpFromTeamUid,_tmpToSlotId,_tmpToTeam,_tmpSport,_tmpTime,_tmpDate,_tmpMessage,_tmpStatus);
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
  public Flow<List<Challenge>> getMyChallenges(final String uid) {
    final String _sql = "SELECT * FROM challenges WHERE fromTeamUid = ? ORDER BY rowid DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, uid);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"challenges"}, new Callable<List<Challenge>>() {
      @Override
      @NonNull
      public List<Challenge> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFromTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "fromTeam");
          final int _cursorIndexOfFromTeamUid = CursorUtil.getColumnIndexOrThrow(_cursor, "fromTeamUid");
          final int _cursorIndexOfToSlotId = CursorUtil.getColumnIndexOrThrow(_cursor, "toSlotId");
          final int _cursorIndexOfToTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "toTeam");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<Challenge> _result = new ArrayList<Challenge>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Challenge _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpFromTeam;
            _tmpFromTeam = _cursor.getString(_cursorIndexOfFromTeam);
            final String _tmpFromTeamUid;
            _tmpFromTeamUid = _cursor.getString(_cursorIndexOfFromTeamUid);
            final String _tmpToSlotId;
            _tmpToSlotId = _cursor.getString(_cursorIndexOfToSlotId);
            final String _tmpToTeam;
            _tmpToTeam = _cursor.getString(_cursorIndexOfToTeam);
            final String _tmpSport;
            _tmpSport = _cursor.getString(_cursorIndexOfSport);
            final String _tmpTime;
            _tmpTime = _cursor.getString(_cursorIndexOfTime);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _item = new Challenge(_tmpId,_tmpFromTeam,_tmpFromTeamUid,_tmpToSlotId,_tmpToTeam,_tmpSport,_tmpTime,_tmpDate,_tmpMessage,_tmpStatus);
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
