package com.amaze.filemanager.database.daos;

import android.database.Cursor;
import androidx.room.EmptyResultSetException;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.RxRoom;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.amaze.filemanager.database.models.utilities.SftpEntry;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SftpEntryDao_Impl implements SftpEntryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SftpEntry> __insertionAdapterOfSftpEntry;

  private final EntityDeletionOrUpdateAdapter<SftpEntry> __updateAdapterOfSftpEntry;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByName;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByNameAndPath;

  public SftpEntryDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSftpEntry = new EntityInsertionAdapter<SftpEntry>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `sftp` (`_id`,`path`,`name`,`pub_key`,`ssh_key_name`,`ssh_key`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SftpEntry value) {
        stmt.bindLong(1, value._id);
        if (value.path == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.path);
        }
        if (value.name == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.name);
        }
        if (value.hostKey == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.hostKey);
        }
        if (value.sshKeyName == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.sshKeyName);
        }
        if (value.sshKey == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.sshKey);
        }
      }
    };
    this.__updateAdapterOfSftpEntry = new EntityDeletionOrUpdateAdapter<SftpEntry>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `sftp` SET `_id` = ?,`path` = ?,`name` = ?,`pub_key` = ?,`ssh_key_name` = ?,`ssh_key` = ? WHERE `_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SftpEntry value) {
        stmt.bindLong(1, value._id);
        if (value.path == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.path);
        }
        if (value.name == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.name);
        }
        if (value.hostKey == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.hostKey);
        }
        if (value.sshKeyName == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.sshKeyName);
        }
        if (value.sshKey == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.sshKey);
        }
        stmt.bindLong(7, value._id);
      }
    };
    this.__preparedStmtOfDeleteByName = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM sftp WHERE name = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByNameAndPath = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM sftp WHERE name = ? AND path = ?";
        return _query;
      }
    };
  }

  @Override
  public Completable insert(final SftpEntry instance) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSftpEntry.insert(instance);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable update(final SftpEntry instance) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSftpEntry.handle(instance);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable deleteByName(final String name) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByName.acquire();
        int _argIndex = 1;
        if (name == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, name);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteByName.release(_stmt);
        }
      }
    });
  }

  @Override
  public Completable deleteByNameAndPath(final