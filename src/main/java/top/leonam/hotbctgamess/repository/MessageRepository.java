package top.leonam.hotbctgamess.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import top.leonam.hotbctgamess.model.entity.Message;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    BigInteger countByTimestampBetween(Instant start, Instant end);

    @Query("""
                SELECT COUNT(DISTINCT m.user.id)
                FROM Message m
                WHERE m.timestamp BETWEEN :start AND :end
            """)
    BigInteger countDistinctUsers(@Param("start") Instant start,
                                  @Param("end") Instant end);

    @Query("""
                SELECT COALESCE(SUM(m.length), 0)
                FROM Message m
                WHERE m.timestamp BETWEEN :start AND :end
            """)
    BigInteger sumCharacters(@Param("start") Instant start,
                             @Param("end") Instant end);

    @Query("""
                SELECT COALESCE(SUM(m.mentionCount), 0)
                FROM Message m
                WHERE m.timestamp BETWEEN :start AND :end
            """)
    BigInteger sumMentions(@Param("start") Instant start,
                           @Param("end") Instant end);

    @Query("""
                SELECT COALESCE(SUM(m.linkCount), 0)
                FROM Message m
                WHERE m.timestamp BETWEEN :start AND :end
            """)
    BigInteger sumLinks(@Param("start") Instant start,
                        @Param("end") Instant end);

    // 🔥 CANAL MAIS ATIVO
    @Query("""
                SELECT m.channel.id
                FROM Message m
                WHERE m.timestamp BETWEEN :start AND :end
                GROUP BY m.channel.id
                ORDER BY COUNT(m) DESC
            """)
    List<Long> findMostActiveChannel(@Param("start") Instant start,
                                     @Param("end") Instant end,
                                     Pageable pageable);

    @Query(value = """
                SELECT EXTRACT(HOUR FROM timestamp)
                FROM message
                WHERE timestamp BETWEEN :start AND :end
                GROUP BY EXTRACT(HOUR FROM timestamp)
                ORDER BY COUNT(*) DESC
                LIMIT 1
            """, nativeQuery = true)
    Integer findPeakHour(@Param("start") Instant start,
                         @Param("end") Instant end);

}


