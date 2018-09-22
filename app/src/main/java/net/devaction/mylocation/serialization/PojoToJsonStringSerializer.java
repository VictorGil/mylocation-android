package net.devaction.mylocation.serialization;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Víctor Gil
 *
 * since Sat 2018-June-9
 */
public class PojoToJsonStringSerializer{

    public static String serialize(Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        String string = null;
        try {
            string = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            Log.e("mylocation.PojoToJsonStringSerializer",
                    "Unable to serialize Java object into JSON string: " +
                    "object class: " + object.getClass().getName() +
                    ", object: " + object);
        }

        return string;
    }
}

