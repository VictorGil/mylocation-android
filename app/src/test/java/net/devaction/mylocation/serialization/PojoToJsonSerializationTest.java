package net.devaction.mylocation.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.devaction.mylocation.api.data.LocationData;

import org.junit.Test;

/**
 * @author Víctor Gil
 *
 * since June 2018
 */
public class PojoToJsonSerializationTest {

    @Test
    public void test01(){
        System.out.println("Started test");

        LocationData data = constructLocationData();
        System.out.println("LocationData: " + data);

        ObjectMapper objectMapper = new ObjectMapper();
        String string = null;
        try {
            string = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }

        System.out.println("JSON string: " + string);

        PojoToJsonStringSerializer.serialize(data);
        System.out.println();
        System.out.println("JSON string: " + string);

        //JSON string:
        // {"latitude":"41.05397","longitude":"44.82093","horizontalAccuracy":"12.929","altitude":"0.0",
        // "verticalAccuracy":"N/A","timeChecked":1528547887,"timeMeasured":1528547887,
        // "timeCheckedString":"Saturday 09-Jun-2018 14:38:07+0200",
        // "timeMeasuredString":"Saturday 09-Jun-2018 14:38:07+0200"}
    }

    static LocationData constructLocationData(){
        //LocationData: LocationData [latitude=41.05397, longitude=44.82093, horizontalAccuracy=12.929,
        // altitude=0.0, verticalAccuracy=N/A, timeChecked=Saturday 09-Jun-2018 14:38:07+0200 (1528547887),
        // timeMeasured=Saturday 09-Jun-2018 14:38:07+0200 (1528547887)]

        LocationData data = new LocationData();

        data.setLatitude("41.05397");
        data.setLongitude("44.82093");
        data.setHorizontalAccuracy("12.929");
        data.setAltitude("0.0");
        data.setVerticalAccuracy("N/A");
        data.setTimeChecked(1528547887);
        data.setTimeMeasured(1528547887);

        return data;

        //For exmaple: 41.05397, 44.82093 works in Google maps Web UI
    }
}

