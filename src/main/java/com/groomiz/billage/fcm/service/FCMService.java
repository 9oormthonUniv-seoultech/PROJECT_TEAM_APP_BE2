package com.groomiz.billage.fcm.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.groomiz.billage.auth.document.LoginExceptionDocs;
import com.groomiz.billage.auth.service.RedisService;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class FCMService {

	private final RedisService redisService;
	private final LoginExceptionDocs loginExceptionDocs;

	public void sendMessage(String studentNumber, String title, String body) throws FirebaseMessagingException {
		String key = "FCM_" + studentNumber;
		String token = redisService.getValues(key);

		if (Objects.equals(token, "false")) {
			throw new MemberException(MemberErrorCode.FCM_TOKEN_NOT_FOUND);
		}

		Message message = Message.builder()
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.setToken(token)
			.build();

		FirebaseMessaging.getInstance().send(message);
	}
}
