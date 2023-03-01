package kr.co.pulmuone.batch.esl.domain.model.system;

import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SlackMessage {

    @NotEmpty
    private String channel;

    @NotEmpty
    private String username = "HGRM Batch";

    @NotEmpty
    private String text;

    private String icon_emoji = ":alert:";

    public SlackMessage(@NotEmpty String channel, @NotEmpty String text) {
        this.channel = channel;
        this.text = text;
    }

    @Builder
    public SlackMessage(@NotEmpty String channel, String username, @NotEmpty String text, String iconEmoji) {
        this.channel = channel.startsWith("#") ? channel : "#"+channel;
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
