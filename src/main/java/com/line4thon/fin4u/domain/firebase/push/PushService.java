package com.line4thon.fin4u.domain.firebase.push;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PushService {

    private final DeviceTokenRepository tokenRepo;

    @Value("${fcm.deep-link-base}")
    private String deepLinkBase;

    public void sendToMember(Long memberId, PushType type, Map<String,String> data) {
        List<DeviceToken> tokens = tokenRepo.findByMemberIdAndActiveTrue(memberId);
        for (DeviceToken dt : tokens) {
            sendToToken(dt.getToken(), type, data);
        }
    }

    public String sendToToken(String token, PushType type, Map<String,String> data) {
        try {
            String link = deepLinkBase + "/" + type.getDeeplink();

            // lang 파라미터(KO/EN). 없으면 EN
            String lang = data.getOrDefault("lang", "EN");
            String title = "KO".equalsIgnoreCase(lang) ? type.getTitleKo() : type.getTitleEn();

            Message msg = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(data.getOrDefault("body",""))
                            .build())
                    .putAllData(data)
                    .putData("type", type.name())
                    .putData("link", link)
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .setNotification(AndroidNotification.builder()
                                    .setSound("default")
                                    .setClickAction("FLUTTER_NOTIFICATION_CLICK")
                                    .build())
                            .build())
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setSound("default")
                                    .setBadge(1)
                                    .build())
                            .build())
                    .build();

            return FirebaseMessaging.getInstance().send(msg);

        } catch (FirebaseMessagingException e) {
            if ("registration-token-not-registered".equals(e.getErrorCode())) {
                tokenRepo.findByToken(token).forEach(d -> d.setActive(false));
            }
            throw new RuntimeException("FCM send failed: " + e.getErrorCode(), e);
        }
    }
}
