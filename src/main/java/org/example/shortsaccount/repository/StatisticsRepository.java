package org.example.shortsaccount.repository;

import org.example.shortsaccount.domain.Statistics;
import org.example.shortsaccount.dto.TopFiveVideoInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    Optional<List<Statistics>> findAllByVideoId(int id);

    @Query(value = "select s from Statistics s where s.statsDate = :statsDate order by s.dailyViews desc limit 5")
    List<Statistics> findDailyTop5Views(@Param("statsDate") LocalDate statsDate);

    @Query("select s from Statistics s where s.statsDate = :statsDate order by s.dailyPlaytime desc limit 5")
    List<Statistics> findDailyTop5Playtime(@Param("statsDate") LocalDate statsDate);

    @Query(value = "select s.video_id as videoId, sum(s.daily_views) as views, sum(s.daily_playtime) as playtime from statistics s where s.stats_date >= :froms and s.stats_date <= :tos group by s.video_id order by sum(s.daily_views) desc limit 5",nativeQuery = true)
    List<TopFiveVideoInterface> findTop5ViewsBetween(@Param("froms") LocalDate froms, @Param("tos") LocalDate tos);

    @Query(value = "select s.video_id as videoId, sum(s.daily_views) as views, sum(s.daily_playtime) as playtime from statistics s where s.stats_date >= :froms and s.stats_date <= :tos group by s.video_id order by sum(s.daily_playtime) desc limit 5",nativeQuery = true)
    List<TopFiveVideoInterface> findTop5PlaytimeBetween(@Param("froms") LocalDate froms, @Param("tos") LocalDate tos);
}
