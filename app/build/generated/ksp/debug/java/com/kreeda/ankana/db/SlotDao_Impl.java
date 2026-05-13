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
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.kreeda.ankana.model.Slot;
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
public final class SlotDao_Impl implements SlotDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Slot> __insertionAdapterOfSlot;

  private final EntityDeletionOrUpdateAdapter<Slot> __deletionAdapterOfSlot;

  private final EntityDeletionOrUpdateAdapter<Slot> __updateAdapterOfSlot;

  public SlotDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSlot = new EntityInsertionAdapter<Slot>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `slots` (`id`,`time`,`date`,`status`,`bookedByEmail`,`bookedByTeam`,`bookedByUid`,`sport`,`opponentTeam`,`challengeId`,`rescheduleRequestedTo`,`rescheduleStatus`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Slot entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTime());
        statement.bindString(3, entity.getDate());
        statement.bindString(4, entity.getStatus());
        statement.bindString(5, entity.getBookedByEmail());
        statement.bindString(6, entity.getBookedByTeam());
        statement.bindString(7, entity.getBookedByUid());
        statement.bindString(8, entity.getSport());
        statement.bindString(9, entity.getOpponentTeam());
        statement.bindString(10, entity.getChallengeId());
        statement.bindString(11, entity.getRescheduleRequestedTo());
        statement.bindString(12, entity.getRescheduleStatus());
      }
    };
    this.__deletionAdapterOfSlot = new EntityDeletionOrUpdateAdapter<Slot>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `slots` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Slot entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfSlot = new EntityDeletionOrUpdateAdapter<Slot>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `slots` SET `id` = ?,`time` = ?,`date` = ?,`status` = ?,`bookedByEmail` = ?,`bookedByTeam` = ?,`bookedByUid` = ?,`sport` = ?,`opponentTeam` = ?,`challengeId` = ?,`rescheduleRequestedTo` = ?,`rescheduleStatus` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Slot entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTime());
        statement.bindString(3, entity.getDate());
        statement.bindString(4, entity.getStatus());
        statement.bindString(5, entity.getBookedByEmail());
        statement.bindString(6, entity.getBookedByTeam());
        statement.bindString(7, entity.getBookedByUid());
        statement.bindString(8, entity.getSport());
        statement.bindString(9, entity.getOpponentTeam());
        statement.bindString(10, entity.getChallengeId());
        statement.bindString(11, entity.getRescheduleRequestedTo());
        statement.bindString(12, entity.getRescheduleStatus());
        statement.bindString(13, entity.getId());
      }
    };
  }

  @Override
  public Object insertSlot(final Slot slot, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSlot.insert(slot);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSlot(final Slot slot, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSlot.handle(slot);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSlot(final Slot slot, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSlot.handle(slot);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Slot>> getSlotsByDate(final String date) {
    final String _sql = "SELECT * FROM slots WHERE date = ? ORDER BY time";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"slots"}, new Callable<List<Slot>>() {
      @Override
      @NonNull
      public List<Slot> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBookedByEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "bookedByEmail");
          final int _cursorIndexOfBookedByTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "bookedByTeam");
          final int _cursorIndexOfBookedByUid = CursorUtil.getColumnIndexOrThrow(_cursor, "bookedByUid");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfOpponentTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "opponentTeam");
          final int _cursorIndexOfChallengeId = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeId");
          final int _cursorIndexOfRescheduleRequestedTo = CursorUtil.getColumnIndexOrThrow(_cursor, "rescheduleRequestedTo");
          final int _cursorIndexOfRescheduleStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "rescheduleStatus");
          final List<Slot> _result = new ArrayList<Slot>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Slot _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTime;
            _tmpTime = _cursor.getString(_cursorIndexOfTime);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpBookedByEmail;
            _tmpBookedByEmail = _cursor.getString(_cursorIndexOfBookedByEmail);
            final String _tmpBookedByTeam;
            _tmpBookedByTeam = _cursor.getString(_cursorIndexOfBookedByTeam);
            final String _tmpBookedByUid;
            _tmpBookedByUid = _cursor.getString(_cursorIndexOfBookedByUid);
            final String _tmpSport;
            _tmpSport = _cursor.getString(_cursorIndexOfSport);
            final String _tmpOpponentTeam;
            _tmpOpponentTeam = _cursor.getString(_cursorIndexOfOpponentTeam);
            final String _tmpChallengeId;
            _tmpChallengeId = _cursor.getString(_cursorIndexOfChallengeId);
            final String _tmpRescheduleRequestedTo;
            _tmpRescheduleRequestedTo = _cursor.getString(_cursorIndexOfRescheduleRequestedTo);
            final String _tmpRescheduleStatus;
            _tmpRescheduleStatus = _cursor.getString(_cursorIndexOfRescheduleStatus);
            _item = new Slot(_tmpId,_tmpTime,_tmpDate,_tmpStatus,_tmpBookedByEmail,_tmpBookedByTeam,_tmpBookedByUid,_tmpSport,_tmpOpponentTeam,_tmpChallengeId,_tmpRescheduleRequestedTo,_tmpRescheduleStatus);
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
  public Flow<List<Slot>> getSlotsByUser(final String uid) {
    final String _sql = "SELECT * FROM slots WHERE bookedByUid = ? ORDER BY date, time";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, uid);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"slots"}, new Callable<List<Slot>>() {
      @Override
      @NonNull
      public List<Slot> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBookedByEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "bookedByEmail");
          final int _cursorIndexOfBookedByTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "bookedByTeam");
          final int _cursorIndexOfBookedByUid = CursorUtil.getColumnIndexOrThrow(_cursor, "bookedByUid");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfOpponentTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "opponentTeam");
          final int _cursorIndexOfChallengeId = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeId");
          final int _cursorIndexOfRescheduleRequestedTo = CursorUtil.getColumnIndexOrThrow(_cursor, "rescheduleRequestedTo");
          final int _cursorIndexOfRescheduleStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "rescheduleStatus");
          final List<Slot> _result = new ArrayList<Slot>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Slot _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTime;
            _tmpTime = _cursor.getString(_cursorIndexOfTime);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpBookedByEmail;
            _tmpBookedByEmail = _cursor.getString(_cursorIndexOfBookedByEmail);
            final String _tmpBookedByTeam;
            _tmpBookedByTeam = _cursor.getString(_cursorIndexOfBookedByTeam);
            final String _tmpBookedByUid;
            _tmpBookedByUid = _cursor.getString(_cursorIndexOfBookedByUid);
            final String _tmpSport;
            _tmpSport = _cursor.getString(_cursorIndexOfSport);
            final String _tmpOpponentTeam;
            _tmpOpponentTeam = _cursor.getString(_cursorIndexOfOpponentTeam);
            final String _tmpChallengeId;
            _tmpChallengeId = _cursor.getString(_cursorIndexOfChallengeId);
            final String _tmpRescheduleRequestedTo;
            _tmpRescheduleRequestedTo = _cursor.getString(_cursorIndexOfRescheduleRequestedTo);
            final String _tmpRescheduleStatus;
            _tmpRescheduleStatus = _cursor.getString(_cursorIndexOfRescheduleStatus);
            _item = new Slot(_tmpId,_tmpTime,_tmpDate,_tmpStatus,_tmpBookedByEmail,_tmpBookedByTeam,_tmpBookedByUid,_tmpSport,_tmpOpponentTeam,_tmpChallengeId,_tmpRescheduleRequestedTo,_tmpRescheduleStatus);
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
  public Flow<List<Slot>> getAllActiveSlots() {
    final String _sql = "SELECT * FROM slots WHERE status IN ('Booked','Playing') ORDER BY date, time";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"slots"}, new Callable<List<Slot>>() {
      @Override
      @NonNull
      public List<Slot> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBookedByEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "bookedByEmail");
          final int _cursorIndexOfBookedByTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "bookedByTeam");
          final int _cursorIndexOfBookedByUid = CursorUtil.getColumnIndexOrThrow(_cursor, "bookedByUid");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfOpponentTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "opponentTeam");
          final int _cursorIndexOfChallengeId = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeId");
          final int _cursorIndexOfRescheduleRequestedTo = CursorUtil.getColumnIndexOrThrow(_cursor, "rescheduleRequestedTo");
          final int _cursorIndexOfRescheduleStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "rescheduleStatus");
          final List<Slot> _result = new ArrayList<Slot>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Slot _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTime;
            _tmpTime = _cursor.getString(_cursorIndexOfTime);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpBookedByEmail;
            _tmpBookedByEmail = _cursor.getString(_cursorIndexOfBookedByEmail);
            final String _tmpBookedByTeam;
            _tmpBookedByTeam = _cursor.getString(_cursorIndexOfBookedByTeam);
            final String _tmpBookedByUid;
            _tmpBookedByUid = _cursor.getString(_cursorIndexOfBookedByUid);
            final String _tmpSport;
            _tmpSport = _cursor.getString(_cursorIndexOfSport);
            final String _tmpOpponentTeam;
            _tmpOpponentTeam = _cursor.getString(_cursorIndexOfOpponentTeam);
            final String _tmpChallengeId;
            _tmpChallengeId = _cursor.getString(_cursorIndexOfChallengeId);
            final String _tmpRescheduleRequestedTo;
            _tmpRescheduleRequestedTo = _cursor.getString(_cursorIndexOfRescheduleRequestedTo);
            final String _tmpRescheduleStatus;
            _tmpRescheduleStatus = _cursor.getString(_cursorIndexOfRescheduleStatus);
            _item = new Slot(_tmpId,_tmpTime,_tmpDate,_tmpStatus,_tmpBookedByEmail,_tmpBookedByTeam,_tmpBookedByUid,_tmpSport,_tmpOpponentTeam,_tmpChallengeId,_tmpRescheduleRequestedTo,_tmpRescheduleStatus);
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
  public Object countUserBookingsOnDate(final String date, final String uid,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM slots WHERE date = ? AND bookedByUid = ? AND status != 'Completed'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    _argIndex = 2;
    _statement.bindString(_argIndex, uid);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
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
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSlotById(final String id, final Continuation<? super Slot> $completion) {
    final String _sql = "SELECT * FROM slots WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Slot>() {
      @Override
      @Nullable
      public Slot call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBookedByEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "bookedByEmail");
          final int _cursorIndexOfBookedByTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "bookedByTeam");
          final int _cursorIndexOfBookedByUid = CursorUtil.getColumnIndexOrThrow(_cursor, "bookedByUid");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfOpponentTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "opponentTeam");
          final int _cursorIndexOfChallengeId = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeId");
          final int _cursorIndexOfRescheduleRequestedTo = CursorUtil.getColumnIndexOrThrow(_cursor, "rescheduleRequestedTo");
          final int _cursorIndexOfRescheduleStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "rescheduleStatus");
          final Slot _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTime;
            _tmpTime = _cursor.getString(_cursorIndexOfTime);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpBookedByEmail;
            _tmpBookedByEmail = _cursor.getString(_cursorIndexOfBookedByEmail);
            final String _tmpBookedByTeam;
            _tmpBookedByTeam = _cursor.getString(_cursorIndexOfBookedByTeam);
            final String _tmpBookedByUid;
            _tmpBookedByUid = _cursor.getString(_cursorIndexOfBookedByUid);
            final String _tmpSport;
            _tmpSport = _cursor.getString(_cursorIndexOfSport);
            final String _tmpOpponentTeam;
            _tmpOpponentTeam = _cursor.getString(_cursorIndexOfOpponentTeam);
            final String _tmpChallengeId;
            _tmpChallengeId = _cursor.getString(_cursorIndexOfChallengeId);
            final String _tmpRescheduleRequestedTo;
            _tmpRescheduleRequestedTo = _cursor.getString(_cursorIndexOfRescheduleRequestedTo);
            final String _tmpRescheduleStatus;
            _tmpRescheduleStatus = _cursor.getString(_cursorIndexOfRescheduleStatus);
            _result = new Slot(_tmpId,_tmpTime,_tmpDate,_tmpStatus,_tmpBookedByEmail,_tmpBookedByTeam,_tmpBookedByUid,_tmpSport,_tmpOpponentTeam,_tmpChallengeId,_tmpRescheduleRequestedTo,_tmpRescheduleStatus);
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
