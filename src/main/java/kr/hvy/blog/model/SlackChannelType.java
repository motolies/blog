package kr.hvy.blog.model;

public enum SlackChannelType {
    HVY_NOTIFY("#hvy-notify"),
    HVY_ERROR("#hvy-error");

    private String channel;

    SlackChannelType(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
}
