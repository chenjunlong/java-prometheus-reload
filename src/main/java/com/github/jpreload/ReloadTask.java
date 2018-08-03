package com.github.jpreload;

import com.github.jpreload.config.Config;
import com.github.jpreload.model.Params;
import com.github.jpreload.utils.HttpClient;
import com.github.jpreload.utils.NetUtils;

import java.io.IOException;

/**
 * Created by chenjunlong on 2018/8/3.
 */
public class ReloadTask implements Runnable {

    private Reloader reloader = new Reloader();

    @Override
    public void run() {
        while (true) {
            try {
                Params params = BlockingQueueStore.getBlockingQueue().take();
                reloader.register(params);
                String url = "http://" + NetUtils.getLocalHost() + ":" + Config.PROMETHEUS_PORT + "/-/reload";
                HttpClient.doPost(url, null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
