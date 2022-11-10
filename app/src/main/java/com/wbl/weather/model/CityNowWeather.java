package com.wbl.weather.model;



import java.util.Date;
import java.util.List;

public class CityNowWeather {
    private String code;
    private Date updateTime;
    private String fxLink;
    private Now now;
    private Refer refer;
    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public String getFxLink() {
        return fxLink;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
    }

    public void setNow(Now now) {
        this.now = now;
    }
    public Now getNow() {
        return now;
    }

    public void setRefer(Refer refer) {
        this.refer = refer;
    }
    public Refer getRefer() {
        return refer;
    }

    /**
     * Copyright 2022 json.cn
     */


    /**
     * Auto-generated: 2022-11-10 14:52:2
     *
     * @author json.cn (i@json.cn)
     * @website http://www.json.cn/java2pojo/
     */
    public class Now {

        private Date obsTime;
        private String temp;
        private String feelsLike;
        private String icon;
        private String text;
        private String wind360;
        private String windDir;
        private String windScale;
        private String windSpeed;
        private String humidity;
        private String precip;
        private String pressure;
        private String vis;
        private String cloud;
        private String dew;
        public void setObsTime(Date obsTime) {
            this.obsTime = obsTime;
        }
        public Date getObsTime() {
            return obsTime;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }
        public String getTemp() {
            return temp;
        }

        public void setFeelsLike(String feelsLike) {
            this.feelsLike = feelsLike;
        }
        public String getFeelsLike() {
            return feelsLike;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
        public String getIcon() {
            return icon;
        }

        public void setText(String text) {
            this.text = text;
        }
        public String getText() {
            return text;
        }

        public void setWind360(String wind360) {
            this.wind360 = wind360;
        }
        public String getWind360() {
            return wind360;
        }

        public void setWindDir(String windDir) {
            this.windDir = windDir;
        }
        public String getWindDir() {
            return windDir;
        }

        public void setWindScale(String windScale) {
            this.windScale = windScale;
        }
        public String getWindScale() {
            return windScale;
        }

        public void setWindSpeed(String windSpeed) {
            this.windSpeed = windSpeed;
        }
        public String getWindSpeed() {
            return windSpeed;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }
        public String getHumidity() {
            return humidity;
        }

        public void setPrecip(String precip) {
            this.precip = precip;
        }
        public String getPrecip() {
            return precip;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }
        public String getPressure() {
            return pressure;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }
        public String getVis() {
            return vis;
        }

        public void setCloud(String cloud) {
            this.cloud = cloud;
        }
        public String getCloud() {
            return cloud;
        }

        public void setDew(String dew) {
            this.dew = dew;
        }
        public String getDew() {
            return dew;
        }

    }

    public class Refer {

        private List<String> sources;
        private List<String> license;
        public void setSources(List<String> sources) {
            this.sources = sources;
        }
        public List<String> getSources() {
            return sources;
        }

        public void setLicense(List<String> license) {
            this.license = license;
        }
        public List<String> getLicense() {
            return license;
        }

    }
}
