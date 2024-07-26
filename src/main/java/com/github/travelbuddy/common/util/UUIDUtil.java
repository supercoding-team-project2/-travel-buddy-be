package com.github.travelbuddy.common.util;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class UUIDUtil {
    public static String generateUUIDFromUserId(Integer userId){
        UUID uuid = UUID.randomUUID();
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putInt(userId);
        byteBuffer.putInt(0);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(byteBuffer.array());
    }

    public static Integer extractUserIdFromUUID(String uuidStr) {
        byte[] bytes = Base64.getUrlDecoder().decode(uuidStr);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.getLong();
        return byteBuffer.getInt();
    }
}