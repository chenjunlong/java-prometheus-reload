package com.github.jpreload;

import com.github.jpreload.config.Config;
import com.github.jpreload.model.Params;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by chenjunlong on 2018/8/1.
 */
public class Reloader {

    private File ymlFile = new File(Config.YML_PATH);
    private Yaml yaml = new Yaml();

    public synchronized void register(Params configParams) throws IOException {
        String jobName = configParams.getJobName();
        String target = configParams.getHost() + ":" + configParams.getPort();

        InputStream ymlFileInputStream = FileUtils.openInputStream(ymlFile);
        Map<String, Object> scrapeConfigs = (Map<String, Object>) yaml.load(ymlFileInputStream);
        if (null == scrapeConfigs || scrapeConfigs.isEmpty()) {
            scrapeConfigs = new LinkedHashMap<>();
            scrapeConfigs.put("scrape_configs", new ArrayList<Map<String, Object>>());
        }

        List<Map<String, Object>> list = (List<Map<String, Object>>) scrapeConfigs.get("scrape_configs");
        if (null == list || list.size() == 0) {
            list = new ArrayList<>();
            Map<String, Object> jobItem = new LinkedHashMap<>();
            jobItem.put("job_name", jobName);
            jobItem.put("scrape_interval", "5s");
            jobItem.put("metrics_path", "/metrics");
            Map<String, Object> staticConfigs = new LinkedHashMap();
            List<String> targets = new ArrayList<>();
            targets.add(target);
            staticConfigs.put("targets", targets);
            List<Map<String, Object>> staticConfigsList = new ArrayList<>();
            staticConfigsList.add(staticConfigs);
            jobItem.put("static_configs", staticConfigsList);
            list.add(jobItem);
        } else {
            Map<String, Object> jobItemMap = null;
            for (Object o : list) {
                Map<String, Object> tempJobItemMap = (Map<String, Object>) o;
                String itemJobName = (String) tempJobItemMap.get("job_name");
                if (itemJobName.equals(jobName)) {
                    jobItemMap = tempJobItemMap;
                    break;
                }
            }

            // 新注册任务
            if (null == jobItemMap) {
                jobItemMap = new LinkedHashMap<>();
                jobItemMap.put("job_name", jobName);
                jobItemMap.put("scrape_interval", "5s");
                jobItemMap.put("metrics_path", "/metrics");
                List<Map<String, Object>> staticConfigsList = new ArrayList<>();
                jobItemMap.put("static_configs", staticConfigsList);
                Map<String, Object> staticConfigsMap = new LinkedHashMap();
                staticConfigsList.add(staticConfigsMap);
                List<String> targets = new ArrayList<>();
                staticConfigsMap.put("targets", targets);
                targets.add(target);
                list.add(jobItemMap);
            } else {
                // 增加节点
                List<Map<String, Object>> staticConfigsList = (ArrayList<Map<String, Object>>) jobItemMap.get("static_configs");
                jobItemMap.put("static_configs", staticConfigsList);
                if (null == staticConfigsList || staticConfigsList.isEmpty() || staticConfigsList.size() == 0) {
                    staticConfigsList = new ArrayList();
                    Map<String, Object> staticConfigsMap = new LinkedHashMap();
                    staticConfigsList.add(staticConfigsMap);
                    List<String> targets = new ArrayList<>();
                    staticConfigsMap.put("targets", targets);
                    targets.add(target);
                    list.add(jobItemMap);
                } else {
                    for (Map<String, Object> staticConfigsMap : staticConfigsList) {
                        List<String> targets = (List<String>) staticConfigsMap.get("targets");
                        if (!targets.contains(target)) {
                            targets.add(target);
                        }
                    }
                }
            }
        }
        FileWriter writer = new FileWriter(ymlFile);
        yaml.dump(scrapeConfigs, writer);
        writer.flush();
        writer.close();
        ymlFileInputStream.close();
    }
}
