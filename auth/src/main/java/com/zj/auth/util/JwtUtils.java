package com.zj.auth.util;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import net.minidev.json.JSONObject;

import java.text.ParseException;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/16 10:34
 */
public class JwtUtils {

    public static JSONObject decodeJwt(String jwt)
    {
        JWSObject jwsObject;
        JSONObject jsonObject = null;
        try {
            jwsObject = JWSObject.parse(jwt);
            Payload payload = jwsObject.getPayload();
            jsonObject = (JSONObject) payload.toJSONObject();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
