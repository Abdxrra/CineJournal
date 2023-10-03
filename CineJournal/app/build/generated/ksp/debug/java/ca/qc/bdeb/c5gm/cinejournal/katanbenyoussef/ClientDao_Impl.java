package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ClientDao_Impl implements ClientDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Client> __insertionAdapterOfClient;

  private final EntityDeletionOrUpdateAdapter<Client> __deletionAdapterOfClient;

  private final EntityDeletionOrUpdateAdapter<Client> __updateAdapterOfClient;

  public ClientDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfClient = new EntityInsertionAdapter<Client>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Client` (`uid`,`prenom`,`nom`,`telephone`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Client value) {
        if (value.getUid() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getUid());
        }
        if (value.getPrenom() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getPrenom());
        }
        if (value.getNom() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getNom());
        }
        if (value.getTelephone() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTelephone());
        }
      }
    };
    this.__deletionAdapterOfClient = new EntityDeletionOrUpdateAdapter<Client>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Client` WHERE `uid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Client value) {
        if (value.getUid() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getUid());
        }
      }
    };
    this.__updateAdapterOfClient = new EntityDeletionOrUpdateAdapter<Client>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Client` SET `uid` = ?,`prenom` = ?,`nom` = ?,`telephone` = ? WHERE `uid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Client value) {
        if (value.getUid() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getUid());
        }
        if (value.getPrenom() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getPrenom());
        }
        if (value.getNom() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getNom());
        }
        if (value.getTelephone() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTelephone());
        }
        if (value.getUid() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getUid());
        }
      }
    };
  }

  @Override
  public Object insertAll(final Client[] clients, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfClient.insert(clients);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final Client client, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfClient.handle(client);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateAll(final Client[] clients, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfClient.handleMultiple(clients);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object getAll(final Continuation<? super List<Client>> continuation) {
    final String _sql = "SELECT * FROM Client";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Client>>() {
      @Override
      public List<Client> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfPrenom = CursorUtil.getColumnIndexOrThrow(_cursor, "prenom");
          final int _cursorIndexOfNom = CursorUtil.getColumnIndexOrThrow(_cursor, "nom");
          final int _cursorIndexOfTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "telephone");
          final List<Client> _result = new ArrayList<Client>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Client _item;
            final Integer _tmpUid;
            if (_cursor.isNull(_cursorIndexOfUid)) {
              _tmpUid = null;
            } else {
              _tmpUid = _cursor.getInt(_cursorIndexOfUid);
            }
            final String _tmpPrenom;
            if (_cursor.isNull(_cursorIndexOfPrenom)) {
              _tmpPrenom = null;
            } else {
              _tmpPrenom = _cursor.getString(_cursorIndexOfPrenom);
            }
            final String _tmpNom;
            if (_cursor.isNull(_cursorIndexOfNom)) {
              _tmpNom = null;
            } else {
              _tmpNom = _cursor.getString(_cursorIndexOfNom);
            }
            final String _tmpTelephone;
            if (_cursor.isNull(_cursorIndexOfTelephone)) {
              _tmpTelephone = null;
            } else {
              _tmpTelephone = _cursor.getString(_cursorIndexOfTelephone);
            }
            _item = new Client(_tmpUid,_tmpPrenom,_tmpNom,_tmpTelephone);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object loadAllByIds(final int[] userIds,
      final Continuation<? super List<Client>> continuation) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM Client WHERE uid IN (");
    final int _inputSize = userIds.length;
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int _item : userIds) {
      _statement.bindLong(_argIndex, _item);
      _argIndex ++;
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Client>>() {
      @Override
      public List<Client> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfPrenom = CursorUtil.getColumnIndexOrThrow(_cursor, "prenom");
          final int _cursorIndexOfNom = CursorUtil.getColumnIndexOrThrow(_cursor, "nom");
          final int _cursorIndexOfTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "telephone");
          final List<Client> _result = new ArrayList<Client>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Client _item_1;
            final Integer _tmpUid;
            if (_cursor.isNull(_cursorIndexOfUid)) {
              _tmpUid = null;
            } else {
              _tmpUid = _cursor.getInt(_cursorIndexOfUid);
            }
            final String _tmpPrenom;
            if (_cursor.isNull(_cursorIndexOfPrenom)) {
              _tmpPrenom = null;
            } else {
              _tmpPrenom = _cursor.getString(_cursorIndexOfPrenom);
            }
            final String _tmpNom;
            if (_cursor.isNull(_cursorIndexOfNom)) {
              _tmpNom = null;
            } else {
              _tmpNom = _cursor.getString(_cursorIndexOfNom);
            }
            final String _tmpTelephone;
            if (_cursor.isNull(_cursorIndexOfTelephone)) {
              _tmpTelephone = null;
            } else {
              _tmpTelephone = _cursor.getString(_cursorIndexOfTelephone);
            }
            _item_1 = new Client(_tmpUid,_tmpPrenom,_tmpNom,_tmpTelephone);
            _result.add(_item_1);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object findByName(final String prenom, final String nom,
      final Continuation<? super Client> continuation) {
    final String _sql = "SELECT * FROM Client WHERE prenom LIKE ? AND nom LIKE ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (prenom == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, prenom);
    }
    _argIndex = 2;
    if (nom == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nom);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Client>() {
      @Override
      public Client call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfPrenom = CursorUtil.getColumnIndexOrThrow(_cursor, "prenom");
          final int _cursorIndexOfNom = CursorUtil.getColumnIndexOrThrow(_cursor, "nom");
          final int _cursorIndexOfTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "telephone");
          final Client _result;
          if(_cursor.moveToFirst()) {
            final Integer _tmpUid;
            if (_cursor.isNull(_cursorIndexOfUid)) {
              _tmpUid = null;
            } else {
              _tmpUid = _cursor.getInt(_cursorIndexOfUid);
            }
            final String _tmpPrenom;
            if (_cursor.isNull(_cursorIndexOfPrenom)) {
              _tmpPrenom = null;
            } else {
              _tmpPrenom = _cursor.getString(_cursorIndexOfPrenom);
            }
            final String _tmpNom;
            if (_cursor.isNull(_cursorIndexOfNom)) {
              _tmpNom = null;
            } else {
              _tmpNom = _cursor.getString(_cursorIndexOfNom);
            }
            final String _tmpTelephone;
            if (_cursor.isNull(_cursorIndexOfTelephone)) {
              _tmpTelephone = null;
            } else {
              _tmpTelephone = _cursor.getString(_cursorIndexOfTelephone);
            }
            _result = new Client(_tmpUid,_tmpPrenom,_tmpNom,_tmpTelephone);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
