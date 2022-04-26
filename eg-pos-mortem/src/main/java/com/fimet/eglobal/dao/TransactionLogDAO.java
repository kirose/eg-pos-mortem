package com.fimet.eglobal.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.fimet.eglobal.model.TransactionLog;
import com.fimet.eglobal.model.TransactionLogId;

public interface TransactionLogDAO extends CrudRepository<TransactionLog, TransactionLogId> {

    List<TransactionLog> findByTimestamp(Date timestamp);

    @Query(nativeQuery = true, value = "SELECT TRL_ID, TRL_RRN, TRL_SYSTEM_TIMESTAMP, TRL_ORIGIN_IAP_NAME  FROM TRANSACTION_LOG t WHERE t.TRL_RRN = :rrn AND t.TRL_SYSTEM_TIMESTAMP >= to_timestamp(:start, 'YYYY-MM-DD HH24:MI:SS.FF3') AND t.TRL_SYSTEM_TIMESTAMP < to_timestamp(:end, 'YYYY-MM-DD HH24:MI:SS.FF3') AND ROWNUM <= 1 ORDER BY t.TRL_ID DESC")
    TransactionLog findByRrnAndRangeTime(@Param("rrn") String rrn, @Param("start") String start, @Param("end") String end);

}