package pro.kdray.funniray.mixer.database;

import pro.kdray.funniray.mixer.main;

import java.util.UUID;

public class PlayerToken {

    private UUID uuid;
    private String accessToken;
    private String refreshToken;
    private Float expiresAt;

    public PlayerToken(UUID uuid, String accessToken, String refreshToken, Float expiresAt){
        this.uuid = uuid;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public PlayerToken setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public PlayerToken setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public Float getExpiresAt() {
        return expiresAt;
    }

    public PlayerToken setExpiresAt(Float expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public void update(){
        main.getStorageHandler().updateToken(this);
    }
}
