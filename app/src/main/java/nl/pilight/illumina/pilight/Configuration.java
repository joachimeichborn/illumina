/*
 * illumina, a pilight remote
 *
 * Copyright (c) 2014 Peter Heisig <http://google.com/+PeterHeisig>
 *                    CurlyMo <http://www.pilight.org>
 *
 * illumina is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * illumina is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with illumina. If not, see <http://www.gnu.org/licenses/>.
 */

package nl.pilight.illumina.pilight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Configuration extends LinkedHashMap<String, Location> {

    public static final Logger log = LoggerFactory.getLogger(Configuration.class);

    private final RemoteChangeHandler mRemoteChangeHandler;

    public interface RemoteChangeHandler {

        void onRemoteChange(Device device);

    }

    private Configuration(RemoteChangeHandler handler,
                          JSONObject devicesJson) throws JSONException {

        mRemoteChangeHandler = handler;

        Iterator devicesJsonIterator = devicesJson.keys();
        Map<String, Location> unsortedLocations = new HashMap<>();
        Location location = null;

        while (devicesJsonIterator.hasNext()) {
            final String deviceID = (String) devicesJsonIterator.next();
            final JSONObject jdevice = devicesJson.getJSONObject(deviceID);
            final String currentLocation = jdevice.getJSONArray("group").get(0).toString();
//            final String media = jdevice.getJSONArray("media").toString();
//            if(media.contains("mobile") || media.contains("all")) {
                if (!unsortedLocations.containsKey(currentLocation)) {
                    location = new Location();
                    location.setId(currentLocation);
                    location.setName(currentLocation);
                    location.setOrder(jdevice.getInt("order"));
                    Map<String, Device> devices = new HashMap<>();
                    location.addSorted(devices);
                    unsortedLocations.put(currentLocation, location);
                }
//            }
        }

        devicesJsonIterator = devicesJson.keys();
        while (devicesJsonIterator.hasNext()) {
            final String deviceID = (String) devicesJsonIterator.next();
            final JSONObject jdevice = devicesJson.getJSONObject(deviceID);
            final String currentLocation = jdevice.getJSONArray("group").get(0).toString();
//            final String media = jdevice.getJSONArray("media").toString();
//            if(media.contains("mobile") || media.contains("all")) {
                location = unsortedLocations.get(currentLocation);

                final Device device = parseDevice(jdevice);
                device.setId(deviceID);
                device.setLocationId(location.getId());
                location.getDevices().put(deviceID, device);
//            }
        }

        addSorted(unsortedLocations);
    }

    private Device parseDevice(JSONObject jsonDevice) throws JSONException {
        final Device device = new Device();

        final Iterator deviceJsonIterator = jsonDevice.keys();

        while (deviceJsonIterator.hasNext()) {
            final String currentDeviceAttribute = (String) deviceJsonIterator.next();

            switch (currentDeviceAttribute) {
                case "name":
                    device.setName(jsonDevice.optString(currentDeviceAttribute).trim());
                    break;

                case "order":
                    device.setOrder(jsonDevice.optInt(currentDeviceAttribute));
                    break;

                case "type":
                    switch(jsonDevice.optInt(currentDeviceAttribute)){
                        case 1:
                        case 4:
                            device.setType(Device.DeviceTypes.SWITCH);
                            break;
                        case 2:
                            device.setType(Device.DeviceTypes.DIMMER);
                            break;
                        case 3:
                            device.setType(Device.DeviceTypes.WEATHER);
                            break;
                        case 5:
                            device.setType(Device.DeviceTypes.SCREEN);
                            break;
                        case 6:
                            device.setType(Device.DeviceTypes.CONTACT);
                            break;
                        case 7:
                            device.setType(Device.DeviceTypes.PENDINGSW);
                            break;
                        case 8:
                            device.setType(Device.DeviceTypes.DATETIME);
                            break;
                        case 9:
                            device.setType(Device.DeviceTypes.XBMC);
                            break;
                        default:
                            device.setType(Device.DeviceTypes.UNKNOWN);
                            break;

                    }

                /* Device GUI settings */
                case "show-battery":
                    device.setShowBattery(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "show-temperature":
                    device.setShowTemperature(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "show-humidity":
                    device.setShowHumidity(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "show-pressure":
                    device.setShowPressure(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "show-wind":
                    device.setShowWindgust(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    device.setShowWindavg(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    device.setShowWinddir(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "show-sunriseset":
                    device.setShowSunriseset(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "show-media":
                    device.setShowMedia(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "show-action":
                    device.setShowAction(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "show-update":
                    device.setShowUpdate(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "decimals":
                    device.setGUIDecimals(jsonDevice.optInt(currentDeviceAttribute));
                    break;

                case "readonly":
                    device.setReadOnly(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "datetime-format":
                    device.setDateTimeFormat(jsonDevice.optString(currentDeviceAttribute));
                    break;

                default:
                    log.debug("unhandled setting " + currentDeviceAttribute
                            + ":" + jsonDevice.optString(currentDeviceAttribute));
                    break;
            }
        }

        return device;
    }

    private void updateDevices(JSONArray deviceIds, JSONObject jsonValues) {
        final int deviceCount = deviceIds.length();
        Location location = null;
        boolean match = false;

        for (int i = 0; i < deviceCount; i++) {
            try {
                match = false;
                final String deviceId = deviceIds.getString(i);
                Iterator<Location> it = this.values().iterator();
                while(it.hasNext()) {
                    location = it.next();
                    if(location.getDevices().containsKey(deviceId)) {
                        match = true;
                        break;
                    }
                }
                if(match) {
                    updateDevice(location.get(deviceId), jsonValues);
                }
            } catch (JSONException exception) {
                log.warn("- updating values failed", exception);
            }
        }
    }

    private void updateDevice(Device device, JSONObject jsonValues) throws JSONException {
        final Iterator jsonValuesIterator = jsonValues.keys();

        while (jsonValuesIterator.hasNext()) {
            final String valueKey = (String) jsonValuesIterator.next();

            switch (valueKey) {
                case "all":
                    device.setAll(jsonValues.optInt(valueKey) == 1);
                    break;

                case "timestamp":
                    device.setTimestamp(jsonValues.optInt(valueKey));
                    break;

                case "state":
                    device.setState(jsonValues.getString(valueKey));
                    break;

                case "dimlevel":
                    device.setDimLevel(jsonValues.getInt(valueKey));
                    break;

                case "temperature":
                    device.setTemperature(jsonValues.optInt(valueKey));
                    break;

                case "humidity":
                    device.setHumidity(jsonValues.optInt(valueKey));
                    break;

                case "pressure":
                    device.setPressure(jsonValues.optInt(valueKey));
                    break;

                case "windavg":
                    device.setWindavg(jsonValues.optInt(valueKey));
                    break;

                case "windgust":
                    device.setWindgust(jsonValues.optInt(valueKey));
                    break;

                case "winddir":
                    device.setWinddir(jsonValues.optInt(valueKey));
                    break;

                case "battery":
                    device.setHealthyBattery(jsonValues.optInt(valueKey) == 1);
                    break;

                case "year":
                    device.setYear(jsonValues.optInt(valueKey));
                    break;

                case "month":
                    device.setMonth(jsonValues.optInt(valueKey));
                    break;

                case "day":
                    device.setDay(jsonValues.optInt(valueKey));
                    break;

                case "hour":
                    device.setHour(jsonValues.optInt(valueKey));
                    break;

                case "minute":
                    device.setMinute(jsonValues.optInt(valueKey));
                    break;

                case "second":
                    device.setSecond(jsonValues.optInt(valueKey));
                    break;

                case "action":
                    device.setAction(jsonValues.optString(valueKey));
                    break;

                case "media":
                    device.setMedia(jsonValues.optString(valueKey));
                    break;

                case "update":
                    device.setUpdate(jsonValues.optInt(valueKey) == 1);
                    break;

                case "sunrise":
                    device.setSunrise(jsonValues.optDouble(valueKey));
                    break;

                case "sunset":
                    device.setSunset(jsonValues.optDouble(valueKey));
                    break;

                default:
                    log.info("device value ignored: " + valueKey);
                    break;
            }
        }

        mRemoteChangeHandler.onRemoteChange(device);
    }

    private void addSorted(Map<String, Location> locations) {
        final List<Entry<String, Location>> entries = new LinkedList<>(locations.entrySet());

        Collections.sort(entries, new Comparator<Entry<String, Location>>() {
            @Override
            public int compare(Entry<String, Location> e1,
                               Entry<String, Location> e2) {

                final int o1 = e1.getValue().getOrder();
                final int o2 = e2.getValue().getOrder();

                if (o1 > o2) {
                    return 1;
                } else if (o1 < o2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        for(Map.Entry<String, Location> entry: entries){
            put(entry.getKey(), entry.getValue());
        }
    }

    public static Configuration create(RemoteChangeHandler handler, JSONObject json) throws JSONException {
        return new Configuration(handler, json);
    }

    public void update(JSONObject json) {
        log.info("update setting");
        if(json.has("devices")) {
            final JSONArray jsonDevices = json.optJSONArray("devices");
            final JSONObject jsonValues = json.optJSONObject("values");

            updateDevices(jsonDevices, jsonValues);
        }
    }
}
