package kr.co.pulmuone.v1.send.slack.dto;

import lombok.*;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SlackMessage {

    @NotEmpty
    private String channel;

    @NotEmpty
    private String username = "HGRM Common";

    @NotEmpty
    private String text;

    private String icon_emoji = ":alert:";

    public SlackMessage(@NotEmpty String channel, @NotEmpty String text) {
        this.channel = channel;
        this.text = text;
    }

    @Builder
    public SlackMessage(@NotEmpty String channel, String username, @NotEmpty String text, String iconEmoji) {
        this.channel = channel.startsWith("#") ? channel : "#" + channel;
        if (!StringUtils.isEmpty(username)) {
            this.username = username;
        }
        this.text = text;
        if (!StringUtils.isEmpty(iconEmoji)) {
            this.icon_emoji = iconEmoji;
        }
    }

    public void setActiveProfile(String activeProfile) {
        if (activeProfile.equalsIgnoreCase("prod")) {
            return;
        }
        this.username = new StringBuilder("[")
                .append(activeProfile.toUpperCase())
                .append("] ")
                .append(this.username)
                .toString();
    }
}
