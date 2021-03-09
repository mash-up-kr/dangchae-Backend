package kr.mashup.udada.diary.dao;

import kr.mashup.udada.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM diary " +
                    "WHERE diary_id = (SELECT diary_id FROM diary_user WHERE user_id = ?1)")
    List<Diary> findByUserId(Long userId);
}
