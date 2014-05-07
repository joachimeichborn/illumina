/*
 * Copyright (c) 2014 Peter Heisig.
 *
 * This work is licensed under a Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 */

package de.medienDresden.illumina.pilight;

import android.text.TextUtils;

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

public class Setting extends LinkedHashMap<String, Location> {

    public static final Logger log = LoggerFactory.getLogger(Setting.class);

    private final RemoteChangeHandler mRemoteChangeHandler;

    public interface RemoteChangeHandler {

        void onRemoteChange(Device device);

    }

    private Setting(RemoteChangeHandler handler,
                    JSONObject locationsJson) throws JSONException {

        mRemoteChangeHandler = handler;

        final Iterator locationsJsonIterator = locationsJson.keys();
        final Map<String, Location> unsortedLocations = new HashMap<>();

        while (locationsJsonIterator.hasNext()) {
            final String currentLocation = (String) locationsJsonIterator.next();
            final Location location = parseLocation(currentLocation,
                    locationsJson.getJSONObject(currentLocation));

            unsortedLocations.put(currentLocation, location);
        }

        addSorted(unsortedLocations);
    }

    private Location parseLocation(String locationId,
                                   JSONObject jsonLocation) throws JSONException {
        final Location location = new Location();
        final Iterator locationJsonIterator = jsonLocation.keys();
        final Map<String, Device> devices = new HashMap<>();

        location.setId(locationId);

        while (locationJsonIterator.hasNext()) {
            final String currentLocationAttribute = (String) locationJsonIterator.next();

            switch (currentLocationAttribute) {
                case "name":
                    location.setName(jsonLocation.optString(currentLocationAttribute).trim());
                    break;

                case "order":
                    location.setOrder(jsonLocation.optInt(currentLocationAttribute));
                    break;

                default:
                    final JSONObject jsonDevice = jsonLocation.optJSONObject(currentLocationAttribute);

                    if (jsonDevice != null) {
                        final Device device = parseDevice(jsonDevice);

                        device.setId(currentLocationAttribute);
                        device.setLocationId(location.getId());
                        devices.put(currentLocationAttribute, device);
                    } else {
                        log.debug("unhandled device parameter " + currentLocationAttribute
                                + ":" + jsonLocation.optString(currentLocationAttribute));
                    }

                    break;
            }
        }

        location.addSorted(devices);
        return location;
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

                case "state":
                    device.setValue(jsonDevice.optString(currentDeviceAttribute));
                    break;

                case "dimlevel":
                    device.setDimLevel(jsonDevice.optInt(currentDeviceAttribute));
                    break;

                case "temperature":
                    device.setTemperature(jsonDevice.optInt(currentDeviceAttribute));
                    break;

                case "humidity":
                    device.setHumidity(jsonDevice.optInt(currentDeviceAttribute));
                    break;

                case "battery":
                    device.setHealthyBattery(jsonDevice.optInt(currentDeviceAttribute) == 1);
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
                        default:
                            device.setType(Device.DeviceTypes.UNKNOWN);
                            break;

                    }

                case "sunrise":
                    device.setSunrise(jsonDevice.optInt(currentDeviceAttribute));
                    break;

                case "sunset":
                    device.setSunset(jsonDevice.optInt(currentDeviceAttribute));
                    break;

                /* Device GUI settings */
                case "gui-show-battery":
                    device.setShowBattery(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "gui-show-temperature":
                    device.setShowTemperature(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "gui-show-humidity":
                    device.setShowHumidity(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "gui-show-sunriseset":
                    device.setShowSunriseset(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                case "gui-decimals":
                    device.setGUIDecimals(jsonDevice.optInt(currentDeviceAttribute));
                    break;

                case "device-decimals":
                    device.setDeviceDecimals(jsonDevice.optInt(currentDeviceAttribute));
                    break;

                case "gui-readonly":
                    device.setReadOnly(jsonDevice.optInt(currentDeviceAttribute) == 1);
                    break;

                default:
                    log.debug("unhandled setting " + currentDeviceAttribute
                            + ":" + jsonDevice.optString(currentDeviceAttribute));
                    break;
            }
        }

        return device;
    }

    private void updateDevices(String locationId, JSONArray deviceIds, JSONObject jsonValues) {
        final int deviceCount = deviceIds.length();

        for (int i = 0; i < deviceCount; i++) {
            try {
                final String deviceId = deviceIds.getString(i);
                updateDevice(get(locationId).get(deviceId), jsonValues);
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
                case "state":
                    device.setValue(jsonValues.getString(valueKey));
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

                case "battery":
                    device.setHealthyBattery(jsonValues.optInt(valueKey) == 1);
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

    public static Setting create(RemoteChangeHandler handler, JSONObject json) throws JSONException {
        return new Setting(handler, json);
    }

    public void update(JSONObject json) {
        log.info("update setting");

        final JSONObject jsonDevices = json.optJSONObject("devices");
        final JSONObject jsonValues = json.optJSONObject("values");
        final Iterator locationIterator = jsonDevices.keys();

        while (locationIterator.hasNext()) {
            final String locationId = (String) locationIterator.next();
            final JSONArray deviceIds = jsonDevices.optJSONArray(locationId);

            updateDevices(locationId, deviceIds, jsonValues);
        }
    }

}
