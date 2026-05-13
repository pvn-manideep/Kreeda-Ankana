package com.kreeda.ankana.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.kreeda.ankana.model.MatchScore;
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
public final class MatchScoreDao_Impl implements MatchScoreDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MatchScore> __insertionAdapterOfMatchScore;

  public MatchScoreDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMatchScore = new EntityInsertionAdapter<MatchScore>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `match_scores` (`id`,`slotId`,`date`,`team1`,`team2`,`sport`,`score1`,`score2`,`winner`,`notes`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MatchScore entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getSlotId());
        statement.bindString(3, entity.getDate());
        statement.bindString(4, entity.getTeam1());
        statement.bindString(5, entity.getTeam2());
        statement.bindString(6, entity.getSport());
        statement.bindLong(7, entity.getScore1());
        statement.bindLong(8, entity.getScore2());
        statement.bindString(9, entity.getWinner());
        statement.bindString(10, entity.getNotes());
      }
    };
  }

  @Override
  public Object insertScore(final MatchScore score, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMatchScore.insert(score);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MatchScore>> getAllScores() {
    final String _sql = "SELECT * FROM match_scores ORDER BY rowid DESC LIMIT 100";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"match_scores"}, new Callable<List<MatchScore>>() {
      @Override
      @NonNull
      public List<MatchScore> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSlotId = CursorUtil.getColumnIndexOrThrow(_cursor, "slotId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTeam1 = CursorUtil.getColumnIndexOrThrow(_cursor, "team1");
          final int _cursorIndexOfTeam2 = CursorUtil.getColumnIndexOrThrow(_cursor, "team2");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfScore1 = CursorUtil.getColumnIndexOrThrow(_cursor, "score1");
          final int _cursorIndexOfScore2 = CursorUtil.getColumnIndexOrThrow(_cursor, "score2");
          final int _cursorIndexOfWinner = CursorUtil.getColumnIndexOrThrow(_cursor, "winner");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<MatchScore> _result = new ArrayList<MatchScore>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchScore _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSlotId;
            _tmpSlotId = _cursor.getString(_cursorIndexOfSlotId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpTeam1;
            _tmpTeam1 = _cursor.getString(_cursorIndexOfTeam1);
            final String _tmpTeam2;
            _tmpTeam2 = _cursor.getString(_cursorIndexOfTeam2);
            final String _tmpSport;
            _tmpSport = _cursor.getString(_cursorIndexOfSport);
            final int _tmpScore1;
            _tmpScore1 = _cursor.getInt(_cursorIndexOfScore1);
            final int _tmpScore2;
            _tmpScore2 = _cursor.getInt(_cursorIndexOfScore2);
            final String _tmpWinner;
            _tmpWinner = _cursor.getString(_cursorIndexOfWinner);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new MatchScore(_tmpId,_tmpSlotId,_tmpDate,_tmpTeam1,_tmpTeam2,_tmpSport,_tmpScore1,_tmpScore2,_tmpWinner,_tmpNotes);
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
