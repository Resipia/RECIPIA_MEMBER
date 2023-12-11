package com.recipia.member.hexagonal.config;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class P6SpySqlFormatter implements MessageFormattingStrategy {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        // 배치 테이블 관련 쿼리 제외
        if (isBatchTableQuery(sql)) {
            return ""; // 배치 테이블 관련 쿼리는 로그에서 제외
        }
        return String.format("[%s] | %d ms | %s", category, elapsed, formatSql(category, sql));
    }

    private boolean isBatchTableQuery(String sql) {
        if (sql != null) {
            String lowerCaseSql = sql.toLowerCase(Locale.ROOT);
            return lowerCaseSql.contains("batch_job_instance") ||
                    lowerCaseSql.contains("batch_job_execution") ||
                    lowerCaseSql.contains("batch_step_execution") ||
                    lowerCaseSql.contains("batch_step_execution_context");
        }
        return false;
    }

    private String formatSql(String category, String sql) {
        if (sql != null && !sql.trim().isEmpty() && Category.STATEMENT.getName().equals(category)) {
            String trimmedSQL = sql.trim().toLowerCase(Locale.ROOT);
            if (trimmedSQL.startsWith("create") || trimmedSQL.startsWith("alter") || trimmedSQL.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            } else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
            return sql;
        }
        return sql;
    }
}
