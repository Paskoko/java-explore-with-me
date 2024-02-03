package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Statistics;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Statistics, Integer> {

    @Query("select new ru.practicum.ViewStatsDto(s.app, s.uri, count(distinct s.ip)) " +
            "from Statistics as s " +
            "where s.timeStamp between ?1 and ?2 and s.uri in ?3 " +
            "group by s.app, s.uri")
    List<ViewStatsDto> getAllWithUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.ViewStatsDto(s.app, s.uri, count(s.ip)) " +
            "from Statistics as s " +
            "where s.timeStamp between ?1 and ?2 and s.uri in ?3 " +
            "group by s.app, s.uri")
    List<ViewStatsDto> getAllWithAllIp(LocalDateTime start, LocalDateTime end, List<String> uris);


    @Query("select new ru.practicum.ViewStatsDto(s.app, s.uri, count(distinct s.ip)) " +
            "from Statistics as s " +
            "where s.timeStamp between ?1 and ?2 " +
            "group by s.app, s.uri")
    List<ViewStatsDto> getAllWithUniqueIpAndWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ViewStatsDto(s.app, s.uri, count(s.ip)) " +
            "from Statistics as s " +
            "where s.timeStamp between ?1 and ?2 " +
            "group by s.app, s.uri")
    List<ViewStatsDto> getAllWithAllIpAndWithoutUris(LocalDateTime start, LocalDateTime end);


}
