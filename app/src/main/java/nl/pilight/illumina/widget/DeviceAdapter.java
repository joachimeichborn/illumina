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

package nl.pilight.illumina.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import nl.pilight.illumina.R;
import nl.pilight.illumina.pilight.devices.AbstractDevice;
import nl.pilight.illumina.pilight.devices.Device;
import nl.pilight.illumina.pilight.DeviceType;
import nl.pilight.illumina.widget.viewHolder.AbstractDeviceViewHolder;
import nl.pilight.illumina.widget.viewHolder.ContactViewHolder;
import nl.pilight.illumina.widget.viewHolder.DateTimeViewHolder;
import nl.pilight.illumina.widget.viewHolder.DimmerViewHolder;
import nl.pilight.illumina.widget.viewHolder.LabelViewHolder;
import nl.pilight.illumina.widget.viewHolder.PendingSwitchViewHolder;
import nl.pilight.illumina.widget.viewHolder.ScreenViewHolder;
import nl.pilight.illumina.widget.viewHolder.SwitchViewHolder;
import nl.pilight.illumina.widget.viewHolder.UnknownViewHolder;
import nl.pilight.illumina.widget.viewHolder.WeatherViewHolder;
import nl.pilight.illumina.widget.viewHolder.XbmcViewHolder;

public class DeviceAdapter extends ArrayAdapter<Device> {

    private DeviceChangeListener mDeviceChangeListener;

    private List<Device> mOriginalDeviceList;

    private Filter mFilter = new Filter() {
        @Override
		      protected FilterResults performFiltering(CharSequence charSequence) {
            final FilterResults result = new FilterResults();
            final List<Device> filteredDeviceList = new ArrayList<>();

            for (Device device : mOriginalDeviceList) {
                if (device.isReadonly() && device.getType() == DeviceType.SCREEN) {
                    continue;
                }

                filteredDeviceList.add(device);
            }

            result.values = filteredDeviceList;
            result.count = filteredDeviceList.size();

            return result;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();

            addAll((ArrayList<Device>) filterResults.values);

            if (filterResults.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

    public interface DeviceChangeListener {
        void onDeviceChange(Device device, AbstractDevice.Property property);
    }

    public DeviceAdapter(Context context, List<Device> objects,
                DeviceChangeListener deviceChangeListener) {
        super(context, 0, objects);

        mDeviceChangeListener = deviceChangeListener;
        mOriginalDeviceList = objects;

        final TypedArray typedArray = context.obtainStyledAttributes(
                new int[]{R.attr.battery_full, R.attr.battery_empty});

        assert typedArray != null;

        typedArray.recycle();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final Device device = getItem(position);
        final DeviceType type = device.getType();

        View view = convertView;
        AbstractDeviceViewHolder viewHolder = null;

        if (view == null) {

            switch (type) {
                case SWITCH:
                    view = inflater.inflate(R.layout.device_list_item_switch, parent, false);
                    viewHolder = new SwitchViewHolder(view);
                    break;

                case PENDING_SW:
                    view = inflater.inflate(R.layout.device_list_item_switch, parent, false);
                    viewHolder = new PendingSwitchViewHolder(view);
                    break;

                case CONTACT:
                    view = inflater.inflate(R.layout.device_list_item_contact, parent, false);
                    viewHolder = new ContactViewHolder(view);
                    break;

                case DIMMER:
                    view = inflater.inflate(R.layout.device_list_item_dimmer, parent, false);
                    viewHolder = new DimmerViewHolder(view);
                    break;

                case SCREEN:
                    view = inflater.inflate(R.layout.device_list_item_screen, parent, false);
                    viewHolder = new ScreenViewHolder(view);
                    break;

                case WEATHER:
                    view = inflater.inflate(R.layout.device_list_item_weather, parent, false);
                    viewHolder = new WeatherViewHolder(view);
                    break;

                case DATE_TIME:
                    view = inflater.inflate(R.layout.device_list_item_datetime, parent, false);
                    viewHolder = new DateTimeViewHolder(view);
                    break;

                case XBMC:
                    view = inflater.inflate(R.layout.device_list_item_xbmc, parent, false);
                    viewHolder = new XbmcViewHolder(view);
                    break;

                case LABEL:
                    view = inflater.inflate(R.layout.device_list_item_label, parent, false);
                    viewHolder = new LabelViewHolder(view);
                    break;

                case UNKNOWN:
                    view = inflater.inflate(R.layout.device_list_item_unknown, parent, false);
                    viewHolder = new UnknownViewHolder(view);
                    break;
            }

            assert view != null;
            view.setTag(viewHolder);

        } else {
            viewHolder = (AbstractDeviceViewHolder) view.getTag();
        }

        assert viewHolder != null;

        viewHolder.setDevice(device);
        viewHolder.setDeviceChangeListener(mDeviceChangeListener);

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType().ordinal();
    }

    @Override
    public int getViewTypeCount() {
        return DeviceType.values().length;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId().hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        final Device device = getItem(position);
        return device != null && !device.isReadonly();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

}
