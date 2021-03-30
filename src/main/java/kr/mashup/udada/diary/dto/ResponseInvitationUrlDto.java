package kr.mashup.udada.diary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseInvitationUrlDto {

    private String invitationUrl;

    @Builder
    public ResponseInvitationUrlDto(String invitationUrl) {
        this.invitationUrl = invitationUrl;
    }
}
