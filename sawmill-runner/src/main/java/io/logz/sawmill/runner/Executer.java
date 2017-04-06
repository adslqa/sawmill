package io.logz.sawmill.runner;

import io.logz.sawmill.Doc;
import io.logz.sawmill.Pipeline;
import io.logz.sawmill.PipelineExecutionMetricsMBean;
import io.logz.sawmill.PipelineExecutionMetricsTracker;
import io.logz.sawmill.PipelineExecutionTimeWatchdog;
import io.logz.sawmill.PipelineExecutor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.logz.sawmill.utilities.JsonUtils.fromJsonString;

/**
 * Created by Eran Shahar on 06/04/2017.
 */
public class Executer {

    private final String logsFilePath;
    private final String pipelineConfigFilePath;
    private PipelineExecutor pipelineExecutor;

    public Executer(String logsFilePath, String pipelineConfigFilePath) {
        this.logsFilePath = logsFilePath;
        this.pipelineConfigFilePath = pipelineConfigFilePath;
    }

    public void execute() {
        pipelineExecutor = parsePipelineConfiguration();
        runLogsThroughPipeline();
    }

    private void runLogsThroughPipeline() {
        List<Doc> logs = parseLogsFile();
    }

    private List<Doc> parseLogsFile() {
        List<Doc> logs = new ArrayList<>();
        try {
            Pipeline pipeline = createPipeline(pipelineConfigFilePath);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(logsFilePath));
            String log;
            while((log = bufferedReader.readLine()) != null) {
                Doc doc = createDoc(log);
                pipelineExecutor.execute(pipeline, doc);
                logs.add(doc);
            }
            bufferedReader.close();
        }
        catch (Exception e) {

        }
        return null;
    }

    private Pipeline createPipeline(String pipelineConfig) {
        try {
            Pipeline.Factory factory = new Pipeline.Factory();
            return factory.create(pipelineConfig);
        } catch (Exception e) {
            throw new RuntimeException("Invalid pipeline config", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Doc createDoc(String logMessage) {
        try {
            return new Doc(fromJsonString(Map.class, logMessage));
        } catch (Exception e) {
            Map<String, Object> map = new HashMap();
            map.put("message", logMessage);
            map.put("reason", e.getStackTrace());
            return new Doc(map);
        }
    }

    private PipelineExecutor parsePipelineConfiguration() {
        PipelineExecutionMetricsTracker sawmillPipelineMetrics = new PipelineExecutionMetricsMBean();
        PipelineExecutionTimeWatchdog watchdog = new PipelineExecutionTimeWatchdog(1000, sawmillPipelineMetrics, context -> {});
        return new PipelineExecutor(watchdog, sawmillPipelineMetrics);
    }

}
