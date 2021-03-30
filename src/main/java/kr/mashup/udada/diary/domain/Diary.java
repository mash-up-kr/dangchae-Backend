package kr.mashup.udada.diary.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.mashup.udada.diary.dto.RequestDiaryDto;
import kr.mashup.udada.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Diary {

    @Id
    @Column(name = "DIARY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private String coverImgUrl;

    @ManyToMany
    @JoinTable(name = "DIARY_USER",
            joinColumns = @JoinColumn(name = "DIARY_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
            )
    private List<User> user = new ArrayList<>();

    @Builder
    public Diary(String title, String coverImgUrl, User user, LocalDate date) {
        this.title = title;
        this.date = date;
        this.coverImgUrl = coverImgUrl;
        this.user.add(user);
    }

    public void update(RequestDiaryDto requestdto, String coverImgUrl) {
        if(!requestdto.getTitle().isEmpty()) {
            this.title = requestdto.getTitle();
        }
        this.coverImgUrl = coverImgUrl;
    }

    public void inviteMember(User user) {
        this.user.add(user);
    }
}
