package nl.pilight.illumina.pilight.devices;

import android.os.Parcel;
import android.os.Parcelable;

import org.junit.Assert;

public class ParcelCheckHelper {
	public static void compareParceledVersion(final Device aDevice, final Parcelable.Creator<?extends Device> aCreator) {
		final Parcel parcel = Parcel.obtain();
		aDevice.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);
		final Device extracted = aCreator.createFromParcel(parcel);
		Assert.assertTrue(aDevice.identical(extracted));
	}
}
