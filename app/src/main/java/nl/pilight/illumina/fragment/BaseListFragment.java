/*
 * illumina, a pilight remote
 *
 * Copyright (c) 2014 Peter Heisig <http://google.com/+PeterHeisig>
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

package nl.pilight.illumina.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ListFragment;

import org.slf4j.Logger;

import java.util.ArrayList;

import nl.pilight.illumina.pilight.devices.Device;
import nl.pilight.illumina.pilight.Group;
import nl.pilight.illumina.service.PilightBinder;

public abstract class BaseListFragment extends ListFragment implements
        PilightBinder.ServiceListener {

    private PilightBinder mBinder;

    @Override
    public void onPilightError(int cause) {
        getLogger().info("onPilightError(" + cause + ")");
    }

    @Override
    public void onPilightConnected() {
        getLogger().info("onPilightConnected");
    }

    @Override
    public void onPilightDisconnected() {
        getLogger().info("onPilightDisconnected");
    }

    @Override
    public void onPilightDeviceChange(Device device) {
        getLogger().info("onPilightDeviceChange(" + device.getId() + ")");
    }

    @Override
    public void onServiceConnected() {
        getLogger().info("onServiceConnected");
    }

    @Override
    public void onServiceDisconnected() {
        getLogger().info("onServiceDisconnected");
    }

    @Override
    public void onGroupListResponse(ArrayList<Group> groups) {
        getLogger().info("onGroupListResponse, #groups = " + groups.size());
    }

    @Override
    public void onGroupResponse(Group group) {
        getLogger().info("onGroupResponse(" + group.getId() + ")");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = new PilightBinder(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinder.bindService(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinder.unbindService(getActivity());
    }

    protected void dispatch(Message message) {
        getLogger().info("dispatch(" + message.what + ")");
        mBinder.send(message);
    }

    abstract protected Logger getLogger();

}
