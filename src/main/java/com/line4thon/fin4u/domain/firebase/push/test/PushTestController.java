package com.line4thon.fin4u.domain.firebase.push.test;



import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.line4thon.fin4u.domain.firebase.push.DeviceToken;
import com.line4thon.fin4u.domain.firebase.push.DeviceTokenRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class PushTestController {

    private final DeviceTokenRepository repo;
    private final FirebaseMessaging firebaseMessaging;

    @PostMapping("/test")
    public void sendTest(@RequestBody NotifyTestRequest req) throws FirebaseMessagingException {
        // 1. memberId 로 토큰 찾기
        List<DeviceToken> tokens = repo.findByMemberIdAndActiveTrue(req.getMemberId());

        // 2. 토큰마다 알림 전송
        for (DeviceToken dt : tokens) {
            Message msg = Message.builder()
                    .setToken(dt.getToken())
                    .setNotification(Notification.builder()
                            .setTitle("테스트 알림")
                            .setBody("type = " + req.getType())
                            .build())
                    .putData("type", req.getType()) // INSTALLMENT_DUE_TODAY 등
                    .build();

            firebaseMessaging.send(msg);
        }
    }

    //백엔드 확인용 멤버id없이 token으로만 FCM까지는 나간 거 확인하기
    @PostMapping("/direct-test")
    public String directTest(@RequestBody DirectPushRequest req) throws FirebaseMessagingException {
        System.out.println("▶ 받은 토큰: [" + req.getToken() + "]");
        Message msg = Message.builder()
                .setToken(req.getToken())
                .putData("type", "TEST")
                .putData("title", "테스트 알림")
                .putData("body", "direct-test 성공!")
                .build();

        return firebaseMessaging.send(msg);
    }
}

@Getter
@NoArgsConstructor
class DirectPushRequest {
    private String token;

}

