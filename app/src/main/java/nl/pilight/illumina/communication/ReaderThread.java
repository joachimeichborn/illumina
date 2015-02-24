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

package nl.pilight.illumina.communication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ReaderThread extends Thread {

    public static final Logger log = LoggerFactory.getLogger(ReaderThread.class);
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_INTERRUPTED = "interrupted";
    private Handler mHandler;
    private BufferedReader mBufferedReader;

    public ReaderThread(InputStream inputStream, Handler handler) {
        super("SOCKET READER");

        mHandler = handler;

        try {
            mBufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        } catch (UnsupportedEncodingException exception) {
            log.error("cannot create socket reader", exception);
        }
    }

    @Override
    public void run() {
        String message;

        while (!isInterrupted()) {
            final Message msg = mHandler.obtainMessage();
            final Bundle bundle = new Bundle();

            msg.setData(bundle);

            try {
                while (!mBufferedReader.ready()) {
                    sleep(10);
                }

                message = mBufferedReader.readLine().trim();
            } catch (InterruptedException | IOException exception) {
                // happens even when disconnected on purpose
                log.info("reading was interrupted");
                bundle.putBoolean(EXTRA_INTERRUPTED, true);
                mHandler.sendMessage(msg);
                break;
            }

            if (TextUtils.isEmpty(message)) {
                continue;
            }

            if (TextUtils.equals("BEAT", message) == false) {
                log.info("RAW read: " + message);
            }

            bundle.putString(EXTRA_MESSAGE, message);
            mHandler.sendMessage(msg);
        }
    }

}
