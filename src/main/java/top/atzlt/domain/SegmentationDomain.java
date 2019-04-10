package top.atzlt.domain;

import java.sql.ResultSet;
import java.util.Map;

public class SegmentationDomain extends BaseDomain<SegmentationDomain>{

    private String rawText;//原来的句子
    private String parsedText;//分词后的结果
    private Results results;//从分词的结果得到的动作命令

    public SegmentationDomain(){
        results = new Results();
    }


    //处理结果类
    public static class Results{


        private String domain;
        private Map<String, String> intent;
        private Map<String, String> object;
        private Map<String, String> replenish;

        private String extCode;//扩展使用，补充信息

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getDomain() {
            return domain;
        }

        public Map<String, String> getReplenish() {
            return replenish;
        }

        public void setReplenish(Map<String, String> replenish) {
            this.replenish = replenish;
        }

        public Map<String, String> getIntent() {
            return intent;
        }

        public void setIntent(Map<String, String> intent) {
            this.intent = intent;
        }

        public Map<String, String> getObject() {
            return object;
        }

        public void setObject(Map<String, String> object) {
            this.object = object;
        }

        public String getExtCode() {
            return extCode;
        }

        public void setExtCode(String extCode) {
            this.extCode = extCode;
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            str.append("results:\n");
            if (domain != null) {
                str.append("domain:").append(domain).append("\n");
            }
            if (intent != null && intent.size() > 0) {
                str.append("intent:").append(intent.toString()).append("\n");
            }
            if (object != null && object.size() > 0) {
                str.append("object:").append(object.toString()).append("\n");
            }
            if (replenish != null && replenish.size() > 0) {
                str.append("replenish:").append(replenish.toString()).append("\n");
            }
            if (extCode != null) {
                str.append("extCode:").append(extCode);
            }
            return str.toString();
        }
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getParsedText() {
        return parsedText;
    }

    public void setParsedText(String parsedText) {
        this.parsedText = parsedText;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }
}
